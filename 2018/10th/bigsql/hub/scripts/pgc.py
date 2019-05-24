from __future__ import print_function, division

####################################################################
########       Copyright (c) 2015-2017 OpenSCG          ############
####################################################################

import sys
if sys.version_info <= (2, 5):
  print("Currently we run best on Python 2.7")
  sys.exit(1)

IS_64BITS = sys.maxsize > 2**32
if not IS_64BITS:
  print("This is a 32bit machine and BigSQL packages are 64bit.\n"
        "Cannot continue")
  sys.exit(1)

import os
import socket
import subprocess
import time
import datetime
import hashlib
import platform
import tarfile
import sqlite3
import time
import json
import glob
from shutil import copy2, copytree
import re
import io
import errno
import traceback
import argparse

## Our own library files ##########################################
sys.path.append(os.path.join(os.path.dirname(__file__), 'lib'))

this_platform_system = str(platform.system())
platform_lib_path = os.path.join(os.path.dirname(__file__), 'lib', this_platform_system)
if os.path.exists(platform_lib_path):
  if platform_lib_path not in sys.path:
    sys.path.append(platform_lib_path)

import util, api, update_hub, startup, meta, repo, tune, lab, dbaas, component
import logging
import logging.handlers
from PgInstance import PgInstance
from semantic_version import Version
import mistune

if not util.is_writable(os.path.join(os.getenv('PGC_HOME'), 'conf')):
  print("You must run as administrator/root.")
  exit()

## Verify that the SQLite MetaData is up to date
update_hub.verify_metadata()

if util.get_value("GLOBAL", "PLATFORM", "") in ("", "posix", "windoze"):
  util.set_value("GLOBAL", "PLATFORM", util.get_default_pf())

try:
    ## Globals and other Initializations ##############################
    LOG_FILENAME = os.getenv('PGC_LOGS')
    LOG_DIRECTORY = os.path.split(LOG_FILENAME)[0]

    if not os.path.isdir(LOG_DIRECTORY):
      os.mkdir(LOG_DIRECTORY)

    # Set up a specific logger with our desired output level
    my_logger = logging.getLogger('pgcli_logger')
    COMMAND = 9
    logging.addLevelName(COMMAND, "COMMAND")
    my_logger.setLevel(logging.DEBUG)


    # Add the log message handler to the logger
    handler = logging.handlers.RotatingFileHandler(
                  LOG_FILENAME, maxBytes=10*1024*1024, backupCount=5)

    formatter = logging.Formatter('%(asctime)s [%(levelname)s] : %(message)s',
                                  datefmt='%Y-%m-%d %H:%M:%S')

    handler.setFormatter(formatter)
    my_logger.addHandler(handler)
except (IOError, OSError) as err:
    print(str(err))
    if err.errno in (errno.EACCES, errno.EPERM):
      print("You must run as administrator/root.")
    exit()

if not util.is_admin() and util.get_platform() == "Windows":
  if meta.is_any_autostart():
    print("You must run as administrator/root when there are any AUTOSTART components.")
    exit()

ansi_escape = re.compile(r'\x1b[^m]*m')

dep9 = util.get_depend()
mode_list = ["start", "stop", "restart", "status", "list", "info", "update",
             "upgrade", "enable", "disable", "install", "groupinstall",
             "remove", "reload", "activity", "help", "get", "set", "unset",
             "repolist", "repo-pkgs", "discover", "verify-ami",
             "lablist", "dirlist", "metalist", "dbconfig", "default",
             "dbstat", "dbtune", "create", "instances", "dbdump", "dbrestore",
             "credentials", "cloudlist", "cloud", "ssh", "pgha",
             "register", "unregister", "top", "--autostart", "--relnotes",
             "--help", "--json", "--test", "--extra", "--extensions",
             "--host", "--list", "--old", "--showduplicates", "-y", "-t",
             "--verbose"]

mode_list_advanced = ['kill', 'config', 'deplist', 'download', 'cancel',
                      'verify', 'init', 'clean', 'useradd', 'provision']

ignore_comp_list = [
  "get", "set", "unset", "register", "unregister", "repolist", "repo-pkgs", "discover", "useradd",
  "dbtune", "lablist", "default", "create", "instances", "metalist", "dbconfig",
  "dirlist", "dbdump", "dbrestore", "verify-ami", "groupinstall", "credentials", 
  "cloud", "ssh", "pgha"]

no_log_commands = ['status', 'info', 'list', 'activity', 'top', 'register', "credentials",
                   'cancel', 'lablist', 'dirlist', 'get', 'verify-ami', "cloudlist"]

lock_commands = ["install", "remove", "update", "upgrade"]

available_dbstats = ['get_aggregate_tps']

my_depend = []
installed_comp_list = []
global check_sum_match
check_sum_match = True

backup_dir = os.path.join(os.getenv('PGC_HOME'), 'conf', 'backup')
backup_target_dir = os.path.join(backup_dir, time.strftime("%Y%m%d%H%M"))

pid_file = os.path.join(os.getenv('PGC_HOME'), 'conf', 'pgc.pid')

PGC_ISJSON = os.environ.get("PGC_ISJSON", "False")
IS_DEVOPS = os.environ.get("IS_DEVOPS", "False")


###################################################################
## Subroutines ####################################################
###################################################################

def group_install(p_install_group):
  install_group_list = meta.get_groupinstall_list(p_install_group)
  if install_group_list == []:
    util.exit_message("Invalid InstallGroup", 1, isJSON)

  install_parms = ""
  for gl in install_group_list:
    install_parms = install_parms + " " + str(gl[1])

  cmd = os.path.join(os.getenv('PGC_HOME'), 'pgc install') + install_parms

  return(os.system(cmd))


## is there a dependency violation if component where removed ####
def is_depend_violation(p_comp, p_remove_list):
  data = meta.get_dependent_components(p_comp)

  kount = 0
  vv = []
  for i in data:
    if str(i[0]) in p_remove_list:
      continue
    kount = kount + 1
    vv.append(str(i[0]))

  if kount == 0:
    return False

  errMsg = "Failed to remove " + p_comp + "(" + str(vv) + " is depending on this)."
  if isJSON:
    dict_error = {}
    dict_error['status'] = "error"
    dict_error["msg"] = errMsg
    msg = json.dumps([dict_error])
  else:
    msg = "ERROR-DEPENDENCY-LIST: " + str(vv)

  my_logger.error(errMsg)

  print(msg)
  return True


## run external scripts #########################
def run_script(componentName, scriptName, scriptParm):
  if componentName not in installed_comp_list:
    return

  componentDir = componentName

  cmd=""
  scriptFile = os.path.join(PGC_HOME, componentDir, scriptName)

  if (os.path.isfile(scriptFile)):
    cmd = "bash"
  else:
    cmd = sys.executable + " -u"
    scriptFile = scriptFile + ".py"

  rc = 0
  compState = util.get_comp_state(componentName)
  if compState == "Enabled" and os.path.isfile(scriptFile):
    run_cmd = cmd + ' ' + scriptFile + ' ' + scriptParm
    if str(platform.system()) == "Windows" and ' ' in scriptFile:
      run_cmd = '%s "%s" %s' % (cmd, scriptFile, scriptParm)
      rc = subprocess.Popen(run_cmd).wait()
    else:
      rc = os.system(run_cmd)

  if rc != 0:
    print('Error running ' + scriptName)
    exit_cleanly(1)

  return;


## Get Dependency List #########################################
def get_depend_list(p_list, p_display=True):
  if p_list == ['all']:
     if p_mode=="install":
       pp_list = available_comp_list
     else:
       pp_list = installed_comp_list
  else:
     pp_list = p_list
  ndx = 0
  deplist = []
  for c in pp_list:
    ndx = ndx + 1
    new_list = list_depend_recur(c)
    deplist.append(c)
    for c1 in new_list:
      deplist.append(c1)

  deplist = set(deplist)

  num_deplist = []
  ndx = 0
  for ndx, comp in enumerate(deplist):
    num_deplist.append(get_comp_num(comp) + ':' + str(comp))

  sorted_depend_list = []
  for c in sorted(num_deplist):
    comp = str(c[4:])
    if comp != "hub":
      sorted_depend_list.append(c[4:])

  msg = '  ' + str(sorted_depend_list)
  my_logger.info(msg)
  if isJSON:
    dictDeplist = {}
    dictDeplist["state"] = "deplist"
    dictDeplist["component"] = p_list
    dictDeplist["deps"] = sorted_depend_list
    msg = json.dumps([dictDeplist])
  if p_display:
    if not isSILENT:
      print(msg)
  return sorted_depend_list


# Check if component is already downloaded
def is_downloaded(p_comp, component_name=None):
  conf_cache = "conf" + os.sep + "cache"
  bz2_file = p_comp + ".tar.bz2"
  checksum_file = bz2_file + ".sha512"

  if os.path.isfile(conf_cache + os.sep + checksum_file):
    if validate_checksum(conf_cache + os.sep + bz2_file, conf_cache + os.sep + checksum_file):
      return (True)

  msg = ""
  if not util.http_get_file(isJSON, checksum_file, REPO, conf_cache, False, msg, component_name):
    return (False)

  return validate_checksum(conf_cache + os.sep + bz2_file, conf_cache + os.sep + checksum_file)


## Get Component Number ####################################################
def get_comp_num(p_app):
  ndx = 0
  for comp in dep9:
    ndx = ndx + 1
    if comp[0] == p_app:
      if ndx < 10:
        return "00" + str(ndx)
      elif ndx < 100:
        return "0" + str(ndx)

      return str(ndx)
  return "000"


# get percentage of unpack progress in json format
class ProgressTarExtract(io.FileIO):
  component_name = ""
  file_name = ""

  def __init__(self, path, *args, **kwargs):
    self._total_size = os.path.getsize(path)
    io.FileIO.__init__(self, path, *args, **kwargs)

  def read(self, size):
    if not os.path.isfile(pid_file):
      raise KeyboardInterrupt("No lock file exists.")
    percentage = self.tell()*100/self._total_size
    if isJSON:
      json_dict = {}
      json_dict['state'] = "unpack"
      json_dict['status'] = "wip"
      json_dict['pct'] = int(percentage)
      json_dict['file'] = self.file_name
      json_dict['component'] = self.component_name
      print(json.dumps([json_dict]))
    return io.FileIO.read(self, size)


## Install Component ######################################################
def install_comp(p_app, p_ver=0, p_rver=None, p_re_install=False):
  if p_ver is None:
    p_ver = 0
  if p_rver:
    parent = util.get_parent_component(p_app, p_rver)
  else:
    parent = util.get_parent_component(p_app, p_ver)
  if parent !="":
    parent_state = util.get_comp_state(parent)
    if parent_state == "NotInstalled":
      errmsg = "{0} has to be installed before installing {1}".format(parent, p_app)
      if isJSON:
        json_dict = {}
        json_dict['state'] = "error"
        json_dict['msg'] = errmsg
        errmsg = json.dumps([json_dict])
      print(errmsg)
      exit_cleanly(1)

  state = util.get_comp_state(p_app)
  if state == "NotInstalled" or p_re_install:
    if p_ver ==  0:
      ver = meta.get_latest_ver_plat(p_app)
    else:
      ver = p_ver
    base_name = p_app + "-" + ver
    conf_cache = "conf" + os.sep + "cache"
    file = base_name + ".tar.bz2"
    bz2_file = conf_cache + os.sep + file
    json_dict = {}
    json_dict['component'] = p_app
    json_dict['file'] = file
    if isJSON:
      json_dict['state'] = "download"
      json_dict['status'] = "start"
      print(json.dumps([json_dict]))

    if os.path.exists(bz2_file) and is_downloaded(base_name, p_app):
      msg = "File is already downloaded."
      my_logger.info(msg)
      if isJSON:
        json_dict['status'] = "complete"
        msg = json.dumps([json_dict])
      if not isSILENT:
        print(msg)
    elif not retrieve_comp(base_name, p_app):
      exit_cleanly(1)

    msg = " Unpacking " + file
    my_logger.info(msg)
    if isJSON:
      json_dict['state'] = "unpack"
      json_dict['status'] = "start"
      json_dict['msg'] = msg
      msg = json.dumps([json_dict])
    if not isSILENT:
      print(msg)
    tarFileObj = ProgressTarExtract("conf" + os.sep + "cache" + os.sep + file)
    tarFileObj.component_name = p_app
    tarFileObj.file_name = file
    tar = tarfile.open(fileobj=tarFileObj, mode="r:bz2")
    try:
      tar.extractall(path=".")
    except KeyboardInterrupt as e:
      temp_tar_dir = os.path.join(PGC_HOME, p_app)
      util.delete_dir(temp_tar_dir)
      msg = "Unpacking cancelled for file %s" % file
      my_logger.error(msg)
      return_code = 1
      if isJSON:
        json_dict = {}
        json_dict['state'] = "unpack"
        json_dict['status'] = "cancelled"
        json_dict['component'] = p_app
        json_dict['msg'] = msg
        msg = json.dumps([json_dict])
        return_code = 0
      util.exit_message(msg, return_code)
    except Exception as e:
      temp_tar_dir = os.path.join(PGC_HOME, p_app)
      util.delete_dir(temp_tar_dir)
      msg = "Unpacking failed for file %s" % str(e)
      my_logger.error(msg)
      my_logger.error(traceback.format_exc())
      return_code = 1
      if isJSON:
        json_dict = {}
        json_dict['state'] = "error"
        json_dict['component'] = p_app
        json_dict['msg'] = str(e)
        msg = json.dumps([json_dict])
        return_code = 0
      util.exit_message(msg, return_code)

    tar.close
    if isJSON:
      json_dict['msg'] = "Unpack complete."
      json_dict['status'] = "complete"
      print(json.dumps([json_dict]))
  else:
    msg = p_app + " is already installed."
    my_logger.info(msg)
    if isJSON:
      json_dict = {}
      json_dict['state'] = "install"
      json_dict['component'] = p_app
      json_dict['status'] = "complete"
      json_dict['msg'] = msg
      msg = json.dumps([json_dict])
    print(msg)
    return 1


## Upgrade Component ######################################################
def upgrade_component(p_comp):
  present_version = meta.get_version(p_comp)
  if not present_version:
    return
  present_state   = util.get_comp_state(p_comp)
  server_port     = util.get_comp_port(p_comp)
  try:
    c = connL.cursor()

    sql = "SELECT version, platform FROM versions " + \
          " WHERE component = '" + p_comp + "' \n" + \
          "   AND " + util.like_pf("platform") + " \n" + \
          "   AND is_current = 1"
    c.execute(sql)
    row = c.fetchone()
    c.close()
  except Exception as e:
    fatal_sql_error(e, sql, "upgrade_component()")

  if str(row) == 'None':
    return

  update_version = str(row[0])
  platform = str(row[1])
  if platform > "":
    platform = util.get_pf()

  is_update_available=0
  cv = Version.coerce(update_version)
  iv = Version.coerce(present_version)
  if cv>iv:
    is_update_available=1

  if is_update_available==0:
    return 1

  if present_state == "NotInstalled":
    meta.update_component_version(p_comp, update_version)
    return 0

  server_running = False
  if server_port > "1":
    server_running = util.is_socket_busy(int(server_port), p_comp)

  if server_running:
    run_script(p_comp, "stop-" + p_comp, "stop")

  msg = "upgrading " + p_comp + " from (" + present_version + ") to (" + update_version + ")"
  my_logger.info(msg)
  if isJSON:
    print('[{"state":"update","status":"start","component":"' + p_comp + '","msg":"'+msg+'"}]')
  else:
    if not isSILENT:
      print(msg)

  components_stopped = []
  dependent_components = meta.get_dependent_components(p_comp)
  isExt = meta.is_extension(p_comp)
  if isExt:
    parent = util.get_parent_component(p_comp, 0)
    dependent_components.append([parent])
  if not p_comp == 'hub':
    for dc in dependent_components:
      d_comp = str(dc[0])
      d_comp_present_state   = util.get_comp_state(d_comp)
      d_comp_server_port     = util.get_comp_port(d_comp)
      d_comp_server_running = False
      if d_comp_server_port > "1":
        d_comp_server_running = util.is_socket_busy(int(d_comp_server_port), p_comp)
      if d_comp_server_running:
        my_logger.info("Stopping the " + d_comp + " to upgrade the " + p_comp)
        run_script(d_comp, "stop-" + d_comp, "stop")
        components_stopped.append(d_comp)

  rc = unpack_comp(p_comp, present_version, update_version)
  if rc == 0:
    meta.update_component_version(p_comp, update_version)
    run_script(p_comp, "update-" + p_comp, "update")
    if isJSON:
      msg = "updated " + p_comp + " from (" + present_version + ") to (" + update_version + ")"
      print('[{"status": "complete", "state": "update", "component": "' + p_comp + '","msg":"' + msg + '"}]')

  if server_running:
    run_script(p_comp, "start-" + p_comp, "start")

  for dc in components_stopped:
    my_logger.info("Starting the " + dc + " after upgrading the " + p_comp)
    run_script(dc, "start-" + dc, "start")

  return 0


def unpack_comp(p_app, p_old_ver, p_new_ver):
  state = util.get_comp_state(p_app)

  base_name = p_app + "-" + meta.get_latest_ver_plat(p_app, p_new_ver)

  file = base_name + ".tar.bz2"
  bz2_file = os.path.join(PGC_HOME, 'conf', 'cache', file)

  if os.path.exists(bz2_file) and is_downloaded(base_name, p_app):
    msg = "File is already downloaded."
    my_logger.info(msg)
    if isJSON:
      json_dict = {}
      json_dict['state'] = "download"
      json_dict['component'] = p_app
      json_dict['status'] = "complete"
      json_dict['file'] = file
      msg = json.dumps([json_dict])
    print(msg)
  elif not retrieve_comp(base_name, p_app):
    return 1

  msg = " Unpacking " + p_app + "(" + p_new_ver + ") over (" + p_old_ver + ")"
  my_logger.info(msg)

  file = base_name + ".tar.bz2"

  if isJSON:
    print('[{"state":"unpack","status":"start","component":"' + p_app + '","msg":"'+msg+'","file":"' + file + '"}]')
  else:
    if not isSILENT:
      print(msg)

  return_value = 0

  tarFileObj = ProgressTarExtract("conf" + os.sep + "cache" + os.sep + file)
  tarFileObj.component_name = p_app
  tarFileObj.file_name = file
  tar = tarfile.open(fileobj=tarFileObj, mode="r:bz2")


  new_comp_dir = p_app + "_new"
  old_comp_dir = p_app + "_old"
  if p_app in ('hub'):
      new_comp_dir = p_app + "_update"
  try:
    tar.extractall(path=new_comp_dir)
  except KeyboardInterrupt as e:
    util.delete_dir(new_comp_dir)
    msg = "Unpacking cancelled for file %s" % file
    if isJSON:
      json_dict = {}
      json_dict['state'] = "unpack"
      json_dict['status'] = "cancelled"
      json_dict['component'] = p_app
      json_dict['msg'] = msg
      msg = json.dumps([json_dict])
    if not isSILENT:
      print(msg)
    my_logger.error(msg)
    return 1
  except Exception as e:
    util.delete_dir(new_comp_dir)
    msg = "Unpacking failed for file %s" % str(e)
    if isJSON:
      json_dict = {}
      json_dict['state'] = "error"
      json_dict['component'] = p_app
      json_dict['msg'] = str(e)
      msg = json.dumps([json_dict])
    if not isSILENT:
      print(msg)
    my_logger.error(msg)
    my_logger.error(traceback.format_exc())
    return 1

  tar.close

  isExt = meta.is_extension(p_app)
  if isExt:
    try:
      parent = util.get_parent_component(p_app,0)
      if not os.path.exists(os.path.join(backup_target_dir, parent)):
        my_logger.info("backing up the parent component %s " % parent)
        copytree(os.path.join(PGC_HOME, parent), os.path.join(backup_target_dir, parent))
      manifest_file_name = p_app + ".manifest"
      manifest_file_path = os.path.join(PGC_HOME, "conf", manifest_file_name)
      my_logger.info("backing up current manifest file " + manifest_file_path)
      copy2(manifest_file_path, backup_target_dir)
      my_logger.info("deleting existing extension files from " + parent)
      util.delete_extension_files(manifest_file_path,upgrade=True)
      my_logger.info("deleting existing manifest file : " + manifest_file_name )
      os.remove(manifest_file_path)
      my_logger.info("creating new manifest file : " + manifest_file_name)
      util.create_manifest(p_app, parent, upgrade=True)
      my_logger.info("copying new extension filess : " + manifest_file_name)
      util.copy_extension_files(p_app, parent, upgrade=True)
    except Exception as e:
      error_msg = "Error while upgrading the " + p_app + " : " + str(e)
      my_logger.error(error_msg)
      my_logger.error(traceback.format_exc())
      if isJSON:
        json_dict = {}
        json_dict['state'] = "error"
        json_dict['component'] = p_app
        json_dict['msg'] = str(e)
        error_msg = json.dumps([json_dict])
      if not isSILENT:
        print(error_msg)
      return_value = 1
  else:
    try:
      if not os.path.exists(backup_dir):
        os.mkdir(backup_dir)
      if not os.path.exists(backup_target_dir):
        os.mkdir(backup_target_dir)
      if not os.path.exists(os.path.join(backup_target_dir, p_app)):
        my_logger.info("backing up the old version of %s " % p_app)
        copytree(os.path.join(PGC_HOME, p_app), os.path.join(backup_target_dir, p_app))
      msg = p_app + " upgrade staged for completion."
      my_logger.info(msg)
      if p_app in ('hub'):
        if util.get_platform() == "Windows":
          copy2(os.path.join(PGC_HOME, "pgc.bat"), backup_target_dir)
        else:
          copy2(os.path.join(PGC_HOME, "pgc"), backup_target_dir)
        os.rename(new_comp_dir, "hub_new")
        ## run the update_hub script in the _new directory
        upd_hub_cmd = sys.executable + " hub_new" + os.sep + "hub" + os.sep + "scripts" + os.sep + "update_hub.py "
        os.system(upd_hub_cmd + p_old_ver + " " + p_new_ver)
      else:
        my_logger.info("renaming the existing folder %s" % p_app)
        os.rename(p_app, p_app+"_old")
        my_logger.info("copying the new files to folder %s" % p_app)
        copytree(os.path.join(PGC_HOME, new_comp_dir, p_app), os.path.join(PGC_HOME, p_app))
        my_logger.info("Restoring the conf and extension files if any")
        util.restore_conf_ext_files(os.path.join(PGC_HOME, p_app+"_old"), os.path.join(PGC_HOME, p_app))
        my_logger.info(p_app + " upgrade completed.")
    except Exception as upgrade_exception:
      error_msg = "Error while upgrading the " + p_app + " : " + str(upgrade_exception)
      my_logger.error(error_msg)
      my_logger.error(traceback.format_exc())
      if isJSON:
        json_dict = {}
        json_dict['state'] = "error"
        json_dict['component'] = p_app
        json_dict['msg'] = str(upgrade_exception)
        error_msg = json.dumps([json_dict])
      if not isSILENT:
        print(error_msg)
      return_value = 1

  if os.path.exists(os.path.join(PGC_HOME, new_comp_dir)):
    util.delete_dir(os.path.join(PGC_HOME, new_comp_dir))
  if os.path.exists(os.path.join(PGC_HOME, old_comp_dir)):
    util.delete_dir(os.path.join(PGC_HOME, old_comp_dir))

  return return_value


## Delete Component #######################################################
def remove_comp(p_comp):
  msg = p_comp + " removing"
  my_logger.info(msg)
  if isJSON:
    msg = '[{"status":"wip","msg":"'+ msg + '","component":"' + p_comp + '"}]'
  print(msg)
  if os.path.isdir(p_comp):
    script_name = "remove-" + p_comp
    run_script(c, script_name, "")
    util.delete_dir(p_comp)
  if meta.is_extension(p_comp):
    manifest_file_name = p_comp + ".manifest"
    manifest_file_path = os.path.join(PGC_HOME, "conf", manifest_file_name)
    util.delete_extension_files(manifest_file_path)
    my_logger.info("deleted manifest file : " + manifest_file_name )
    os.remove(manifest_file_path)
  return 0


## List component dependencies recursively ###############################
def list_depend_recur(p_app):
  for i in dep9:
    if i[0] == p_app:
       if i[1] not in my_depend and meta.is_dependent_platform(i[1]):
         my_depend.append(i[1])
         list_depend_recur(i[1])
  return my_depend


## Update component state ###############################################
def update_component_state(p_app, p_mode, p_ver=None):
  new_state = "Disabled"
  if p_mode  == "enable":
    new_state = "Enabled"
  elif p_mode == "install":
    new_state = "Enabled"
  elif p_mode == "remove":
    new_state = "NotInstalled"

  current_state = util.get_comp_state(p_app)
  ver = ""

  if current_state == new_state:
    return

  if p_mode == "disable" or p_mode == "remove":
    run_script(p_app, "stop-"  + p_app, "kill")

  try:
    c = connL.cursor()

    if p_mode in ('enable', 'disable'):
      ver = meta.get_version(p_app)
      sql = "UPDATE components SET status = ? WHERE component = ?"
      c.execute(sql, [new_state, p_app])

    if p_mode == 'remove':
      ver = meta.get_version(p_app)
      sql = "DELETE FROM components WHERE component = ?"
      c.execute(sql, [p_app])

    if p_mode == 'install':
      sql = "INSERT INTO components (component, project, version, platform, port, status) " + \
            "SELECT v.component, r.project, v.version, " + \
            " CASE WHEN v.platform='' THEN '' ELSE '" + util.get_pf() + "' END, p.port, 'Enabled' " + \
            "  FROM versions v, releases r, projects p " + \
            " WHERE v.component = ? " + \
            "   AND v.component = r.component " + \
            "   AND r.project = p.project " + \
            "   AND v.version = ? "
      if p_ver:
        ver = p_ver
      else:
        ver = meta.get_current_version(p_app)
      c.execute(sql, [p_app, ver])

    connL.commit()
    c.close()
  except Exception as e:
    fatal_sql_error(e,sql,"update_component_state()")

  msg = p_app + ' ' + new_state
  my_logger.info(msg)
  if isJSON:
    msg = '[{"status":"wip","msg":"'+ msg + '"}]'

  return


## Check component state & status ##########################################
def check_comp(p_comp, p_port, p_kount, check_status=False):
  ver = meta.get_ver_plat(p_comp)
  app_state = util.get_comp_state(p_comp)

  if (app_state in ("Disabled", "NotInstalled")):
    api.status(isJSON, p_comp, ver, app_state, p_port, p_kount)
    return

  if ((p_port == "0") or (p_port == "1")) and util.get_column("autostart",p_comp)!="on":
    api.status(isJSON, p_comp, ver, "Installed", "", p_kount)
    return

  is_pg = util.is_postgres(p_comp)
  if is_pg or p_comp in ("pgdevops"):
    if is_pg:
      util.read_env_file(p_comp)
    app_datadir = util.get_comp_datadir(p_comp)
    if app_datadir == "":
      if check_status:
        return "NotInitialized"
      api.status(isJSON, p_comp, ver, "Not Initialized", "", p_kount)
      return

  status = "Stopped"
  comp_svcname = util.get_column("svcname", p_comp)
  if util.get_platform()!="Darwin" and comp_svcname != "" and util.get_column("autostart", p_comp) == "on":
    status = util.get_service_status(comp_svcname)
  elif util.is_socket_busy(int(p_port), p_comp):
    status = "Running"
  else:
    status = "Stopped"
  if check_status:
      return status
  api.status(isJSON, p_comp, ver, status, p_port, p_kount)

  return;


## Check component state #################################################
def check_status(p_comp, p_mode):
  if p_comp in ['all', '*']:
    try:
      c = connL.cursor()
      sql = "SELECT component, port, autostart, pidfile FROM components"
      c.execute(sql)
      data = c.fetchall()
      kount = 0
      if isJSON:
        print("[")
      for row in data:
        comp = row[0]
        port = row[1]
        autostart = row[2]
        pidfile = row[3]
        if pidfile != 'None' and pidfile > "":
          component.check_pid_status(comp, pidfile)
          continue
        if (port > 1) or (p_mode == 'list') or (autostart=="on"):
          kount = kount + 1
          check_comp(comp, str(port), kount)
      if isJSON:
        print("]")
    except Exception as e:
      fatal_sql_error(e,sql,"check_status()")
  else:
    pidfile = util.get_comp_pidfile(p_comp)
    if pidfile != "None" and pidfile > "":
      component.check_pid_status(p_comp, pidfile)
    else:
      port = util.get_comp_port(p_comp)
      check_comp(p_comp, port, 0)
  return;


def retrieve_remote():
  if not os.path.exists(backup_dir):
    os.mkdir(backup_dir)
  if not os.path.exists(backup_target_dir):
    os.mkdir(backup_target_dir)
  recent_version_sql = os.path.join(PGC_HOME, 'conf', 'versions.sql')
  recent_pgc_local_db = os.path.join(PGC_HOME, 'conf', 'pgc_local.db')
  if os.path.exists(recent_pgc_local_db):
    copy2(recent_pgc_local_db, backup_target_dir)
  if os.path.exists(recent_version_sql):
    copy2(recent_version_sql, backup_target_dir)
  remote_file = util.get_versions_sql()
  msg = "Retrieving the remote list of latest component versions (" + remote_file + ") ..."
  my_logger.info(msg)
  if isJSON:
    print('[{"status":"wip","msg":"'+msg+'"}]')
    msg=""
  else:
    if not isSILENT:
      print(msg)
  if not util.http_get_file(isJSON, remote_file, REPO, "conf", False, msg):
    exit_cleanly(1)
  msg=""

  sql_file = "conf" + os.sep + remote_file
  if remote_file == "versions.sql":
    if not util.http_get_file(isJSON, remote_file + ".sha512", REPO, "conf", False, msg):
      exit_cleanly(1)
    msg = "Validating checksum file..."
    my_logger.info(msg)
    if isJSON:
      print('[{"status":"wip","msg":"'+msg+'"}]')
    else:
      if not isSILENT:
        print(msg)
    if not validate_checksum(sql_file, sql_file + ".sha512"):
      exit_cleanly(1)
  msg = "Updating local repository with remote entries..."
  my_logger.info(msg)
  if isJSON:
    print('[{"status":"wip","msg":"'+msg+'"}]')
  else:
    if not isSILENT:
      print(msg)
  if not util.process_sql_file(sql_file, isJSON):
    exit_cleanly(1)



## Download tarball component and verify against checksum ###############
def retrieve_comp(p_base_name, component_name=None):
  conf_cache = "conf" + os.sep + "cache"
  bz2_file = p_base_name + ".tar.bz2"
  checksum_file = bz2_file + ".sha512"
  global download_count
  download_count += 1

  msg = "Get:" + str(download_count) + " " + REPO + " " + p_base_name
  my_logger.info(msg)
  display_status = True
  if isSILENT:
    display_status = False
  if not util.http_get_file(isJSON, bz2_file, REPO, conf_cache, display_status, msg, component_name):
    return (False)

  msg = "Preparing to unpack " + p_base_name
  if not util.http_get_file(isJSON, checksum_file, REPO, conf_cache, False, msg, component_name):
    return (False)

  return validate_checksum(conf_cache + os.sep + bz2_file, conf_cache + os.sep + checksum_file)


def validate_checksum(p_file_name, p_checksum_file_name):
  checksum_from_file = util.get_file_checksum(p_file_name)
  checksum_from_remote_file = util.read_file_string(p_checksum_file_name).rstrip()
  checksum_from_remote = checksum_from_remote_file.split()[0]
  global check_sum_match
  check_sum_match = False
  if checksum_from_remote == checksum_from_file:
      check_sum_match = True
      return check_sum_match
  util.print_error("SHA512 CheckSum Mismatch" )
  return check_sum_match


def is_component(p_comp):
  if p_comp in ['all', '*']:
    return True
  for comp in dep9:
    if comp[0] == p_comp:
      return True
  return False;


#############################################################################
# expand the prefix for a component's version number into the full version
#  number for the most recent version that matches
#############################################################################
def wildcard_version(p_comp, p_ver):
  try:
    ## for an exact match then don't try the wildcard
    sql = "SELECT count(*) FROM versions WHERE component = ? AND version = ?"
    c = connL.cursor()
    c.execute(sql,[p_comp, p_ver])
    data = c.fetchone()
    if data[0] == 1:
      ## return the parm that was passed into this function
      return p_ver

    sql = "SELECT release_date, version FROM versions \n" + \
          " WHERE component = ? AND version LIKE ? \n" +\
          "ORDER BY 1 DESC"
    c = connL.cursor()
    c.execute(sql,[p_comp, p_ver + "%"])
    data = c.fetchone()
    if data is None:
      ## return the parm that was passed into this function
      return p_ver

  except Exception as e:
    fatal_sql_error(e,sql,"wildcard_version")

  ## return the full version number from the sql statement
  return (str(data[1]))


#############################################################################
# If the prefix for a component uniquely matches only one component, then
#  expand the prefix into the full component name
#############################################################################
def wildcard_component(p_component):
  ## Trim slashes for dweeb convenience
  p_comp = p_component.replace("/", "")

  comp = check_release("%" + p_comp + "%")
  if comp > "":
    return comp

  ## check if only a single version of PG is installed ###
  pg_ver = ""
  data = []
  sql = "SELECT component FROM components" + \
        " WHERE component in ('pg92', 'pg93', 'pg94', 'pg95', 'pg96', 'pg10')"
  try:
    c = connL.cursor()
    c.execute(sql)
    data = c.fetchall()
  except Exception as e:
    fatal_sql_error(e,sql,"wildcard_component")
  if len(data) == 1:
    for comp in data:
      pg_ver = str(comp[0])
  else:
    return p_comp

  ## if only single version of PG installed, see if we match with that suffix
  comp = check_release("%" + p_comp + "%-" + pg_ver)
  if comp > "":
    return comp

  return p_comp


def check_release(p_wild):
  data = []
  sql = "SELECT component FROM releases WHERE component LIKE '" + p_wild + "'"
  try:
    c = connL.cursor()
    c.execute(sql)
    data = c.fetchall()
  except Exception as e:
    fatal_sql_error(e,sql,"check-release")
  ret = ""
  if len(data) == 1:
    for comp in data:
      ret = str(comp[0])
  return (ret)


def is_dbstat(p_stat):
  if p_stat in available_dbstats:
    return True
  return False


def get_comp_display():
  comp_display = "("
  for comp in installed_comp_list:
    if not comp_display == "(":
      comp_display += ", "
    comp_display += comp
  comp_display += ")"
  return(comp_display)


def get_mode_display():
  mode_display = "("
  for mode in mode_list:
    if not mode_display == "(":
      mode_display += ", "
    mode_display += mode
  mode_display += ")"
  return(mode_display)


def get_help_text():
  helpf = "README.md"
  helpfile = os.path.dirname(os.path.realpath(__file__)) + "/../doc/" + helpf
  s  = util.read_file_string(helpfile)

  ## filter out the awkward markdown lines
  lines = s.split('\n')
  new_s = ""
  for line in lines:
    if line not in ["```", "#!"]:
      new_s = new_s + line + '\n'
  return(new_s)


def is_valid_mode(p_mode):
  if p_mode in mode_list:
    return True
  if p_mode in mode_list_advanced:
    return True
  return False


def fatal_sql_error(err,sql,func):
  print("################################################")
  print("# FATAL SQL Error in " + str(func))
  print("#    SQL Message =  " + str(err.args[0]))
  print("#  SQL Statement = " + sql)
  print("################################################")
  exit_cleanly(1)


def exit_cleanly(p_rc):
  try:
    connL.close()
  except Exception as e:
    pass
  sys.exit(p_rc)


def pgc_lock():
  try:
    fd = os.open(pid_file, os.O_RDONLY)
    pgc_pid = os.read(fd,12)
    os.close(fd)
  except IOError as e:
    return False
  except OSError as e:
    return False
  if not pgc_pid:
    return False
  if util.get_platform() == "Windows":
    try:
      ps = subprocess.Popen('tasklist.exe /NH /FI "PID eq %s"' % pgc_pid, shell=True, stdout=subprocess.PIPE)
      output = ps.stdout.read()
      ps.stdout.close()
      ps.wait()
      if pgc_pid in output:
        return True
    except Exception as e:
      my_logger.error("Error while checking for lock with command " + p_mode + " : " + str(e))
      my_logger.error(traceback.format_exc())
      return False
  else:
    try:
      os.kill(int(pgc_pid), 0)
    except OSError as e:
      return False
    else:
      return True
  return False


####################################################################
########                    MAINLINE                      ##########
####################################################################

## Initialize Globals ##############################################
REPO=util.get_value('GLOBAL', 'REPO')
PGC_HOME=os.getenv('PGC_HOME')

os.chdir(PGC_HOME)

db_local = "conf" + os.sep + "pgc_local.db"
connL = sqlite3.connect(db_local)

## eliminate empty parameters #################
args = sys.argv
while True:
  try:
     args.remove("")
  except:
     break
full_cmd_line = " ".join(args[1:])

## validate inputs ###########################################
if len(args) == 1:
  api.info(False, PGC_HOME, REPO)
  print(" ")
  print(get_help_text())
  exit_cleanly(0)

if ((args[1] == "--version") or (args[1] == "-v")):
  api.info(False, PGC_HOME, REPO)
  exit_cleanly(0)

p_mode = ""
p_comp = "all"
installed_comp_list = meta.get_component_list()
available_comp_list = meta.get_available_component_list()
download_count = 0

if ((args[1] == "help") or (args[1] == "--help")):
  print(get_help_text())
  exit_cleanly(0)


## process global parameters #################
isJSON = False
if "--json" in args:
  isJSON = True
  args.remove("--json")
  os.environ['isJson'] = "True"

isTTY=True
if "--no-tty" in args:
  args.remove("--no-tty")
  isTTY=False

p_host=""
p_home=""
p_user=""
p_passwd=""
p_host_name=""
p_ssh_key=""


## HOST ######################################################################################
for i in range(0, len(args) - 1):
  if args[i] == "--host":
    p_host = args[i+1]
    args.remove("--host")
    args.remove(p_host)
    get_host_info = util.get_pgc_host(p_host)
    p_host_name = get_host_info.get("host_name")
    p_home = get_host_info.get("pgc_home")
    if p_home == "":
      msg="host server not defined"
      util.exit_message(msg, 1, isJSON)
    if get_host_info.get("ssh_cred_id") is None or get_host_info.get("ssh_cred_id")=="":
      msg = "Update the host with the credentials to connect."
      util.exit_message(msg, 1, isJSON)

if p_host > "":
  cmd = ""
  if len(args[1:]) == 1:
    cmd = args[1]
  if len(args[1:]) > 1:
    cmd = args[1] + ' "' + '" "'.join(args[2:]) + '"'
  exit_cleanly(util.run_pgc_ssh(p_host_name, cmd, isJSON,tty=isTTY))


isVERBOSE = False
if "--verbose" in args:
  isVERBOSE = True
  args.remove("--verbose")

isYES = False
if "-y" in args:
  isYES = True
  args.remove("-y")
  os.environ['isYes'] = "True"

isTIME = False
if "-t" in args:
  isTIME = True
  args.remove("-t")
  os.environ['isTime'] = "True"

isTEST = False
if "--test" in args:
  isTEST = True
  args.remove("--test")
if "--extra" in args:
  # '--extra' is a synonym for '--test'
  isTEST = True
  args.remove("--extra")

if util.get_stage() == "test":
  isTEST = True

isSHOWDUPS = False
if "--old" in args:
  isSHOWDUPS = True
  args.remove("--old")
if "--showduplicates" in args:
  isSHOWDUPS = True
  args.remove("--showduplicates")

isAUTOSTART = False
if "--autostart" in args and 'install' in args:
  isAUTOSTART = True
  args.remove("--autostart")

isRELNOTES = False
if "--relnotes" in args and ('info' in args or 'list' in args):
  isRELNOTES = True
  args.remove("--relnotes")

isSILENT = False
if "--silent" in args:
  isSILENT = True
  os.environ['isSilent'] = "True"
  args.remove("--silent")

isEXTENSIONS = False
if "--extensions" in args:
  isEXTENSIONS = True
  args.remove("--extensions")

isLIST = False
if "--list" in args:
  isLIST = True
  args.remove("--list")

if len(args) == 1:
  util.exit_message("Nothing to do", 1, isJSON)

arg = 1
p_mode = args[1]

cmd_alias = {"dblist" : "instances"}
if p_mode in cmd_alias:
  p_mode = cmd_alias.get(p_mode)

if (p_mode in no_log_commands) and (isJSON == True):
  pass
else:
  my_logger.setLevel(COMMAND)
  my_logger.log(COMMAND,"pgc %s ", full_cmd_line)

if not is_valid_mode(p_mode):
  util.exit_message("Invalid option or command", 1, isJSON)

if p_mode in lock_commands:
  if pgc_lock():
    msg = "Unable to execute '{0}', another pgc instance may be running. \n" \
          "HINT: Delete the lock file: '{1}' if no other instance is running.".format(p_mode, pid_file)
    if isJSON:
      msg = '[{"state":"locked","msg":"' + msg + '"}]'
    util.exit_message(msg, 0)
  pgc_pid_fd = open(pid_file, 'w')
  pgc_pid_fd.write(str(os.getpid()))
  pgc_pid_fd.close()

p_comp_list=[]
p_dbstat_list=[]
extra_args=""
p_version=""
requested_p_version=""
info_arg=0
try:
  for i in range((arg + 1), len(args)):
    if p_host > "":
      break
    if p_mode in ("update", "cancel"):
      util.print_error("No additional parameters allowed.")
      exit_cleanly(1)
    comp1 = wildcard_component(args[i])
    if is_component(comp1):
      p_comp_list.append(comp1)
      if( p_mode == "info" and args[i]== "all"):
        info_arg=1
        p_version = "all"
    elif is_dbstat(args[i]):
      p_dbstat_list.append(args[i])
      if ( p_mode == 'dbstat' and len(p_dbstat_list) != 1):
        util.exit_message("Only one dbstat allowed per run", 1, isJSON)
    else:
      if p_mode in ("config" , "init", "provision") and len(p_comp_list) == 1:
        if str(args[i]) > '':
          extra_args = extra_args + '"' + str(args[i]) + '" '
      elif( p_mode in ("info", "download", "install", "update") and len(p_comp_list) == 1 and info_arg == 0 ):
        if p_mode == "info":
          p_version = args[i]
        else:
          ver1 = wildcard_version(p_comp_list[0], args[i])
          p_version = meta.get_platform_specific_version(p_comp_list[0], ver1)
          if p_version == "-1":
            util.print_error("Invalid component version parameter  (" + ver1 + ")")
            exit_cleanly(1)
          requested_p_version=ver1
        info_arg = 1
      elif p_mode in ignore_comp_list:
        pass
      else:
        util.exit_message("Invalid component parameter (" + args[i] + ")", 1, isJSON)

  if len(p_comp_list) == 0:
    if p_mode == "download":
      util.print_error("No component parameter specified.")
      exit_cleanly(1)
    p_comp_list.append('all')

  if len(p_comp_list) >= 1:
    p_comp = p_comp_list[0]

  ## validate api commands #################
  no_pgc_commands = ["stop", "restart", "start"]
  no_devops_commands = ["remove", "stop", "restart", "upgrade", "start"]
  for comp_node in p_comp_list:
    if (IS_DEVOPS == "True" and (p_mode in no_devops_commands) and (comp_node in "pgdevops")) or (
              IS_DEVOPS == "True" and (p_mode in no_pgc_commands) and (comp_node == 'all')):
      msg = "You can't perform {0} on {1}.".format(p_mode, comp_node)
      return_code = 0
      msg = '[{"status":"error","msg":"' + msg + '"}]'
      util.exit_message(msg, return_code)

  ## DISCOVER ####################################################################
  if p_mode == "discover":
    if len(args) == 2:
      verList =  ["9.6", "9.5", "9.4", "9.3", "9.2" ]
    elif len(args) == 3:
      verList = [args[2]]
    else:
      util.exit_message("try:  pgc discover [ 9.6 thru 9.2 ]", 1, isJSON)

    pgdg_comps = []
    for v in verList:
      if not isJSON:
        isSILENT=True
        if isVERBOSE:
          isSILENT=False
      rc = repo.discover(v, isSILENT, isJSON, isYES)
      comp = "pgdg" + v.replace(".", "")
      if rc in (0,4):
        pgdg_comps.append({'version': v, 'comp': comp})
        if rc == 0:
          pgc_pid_fd = open(pid_file, 'w')
          pgc_pid_fd.write(str(os.getpid()))
          pgc_pid_fd.close()
          msg = "Installing pgc controller for existing {0} instance.".format(comp)
          if not isJSON:
            print (msg)
          install_comp(comp, p_re_install=True)


    if isJSON:
      if len(pgdg_comps)==0:
        isYES=False
      print (json.dumps([{'dicovered_pgdgs' : pgdg_comps, 'state' : 'complete', 'installed' : isYES}]))
    else:
      print ("")

    exit_cleanly(0)


  ## REPO-PKGS ####################################################################
  if p_mode == "repo-pkgs":
    repo.validate_os(isJSON)
    if len(args) == 4:
      if args[3] == "list":
        exit_cleanly(repo.list_packages(args[2], isSHOWDUPS, isJSON, isTEST))

    if len(args) >= 5:
      if args[3] == "install":
        exit_cleanly(repo.install_packages(args[2], args[4:], isJSON))

      if args[3] == "remove":
        exit_cleanly(repo.remove_packages(args[2], args[4:], isJSON))

    error_msg = "try: ./pgc repo-pkgs <repo-id> list\n" + \
                "            ./pgc repo-pkgs <repo-id> [install | remove] <pkg-list>"
    util.exit_message(error_msg, 1, isJSON)


  ## DBTUNE #################################################################### 
  if p_mode == "dbtune":
    tune_good = False
    msg1 = ""
    if len(args) == 5:
      if args[3] == "--email":
        if util.is_postgres(args[2]) and util.get_column('datadir', args[2]) != '-1':
          tune_good = True
          tune.do_pgc_tune(args[2], args[4], isJSON)
        else:
          msg1 = "Must provide installed & initialized Postgres component (i.e. pg96) to tune"

    if not tune_good:
      msg2 = "try:  ./pgc dbtune pgXX --email you@email.com"
      if msg1 != "":
        msg2 = msg1 + "\n  " + msg2
      util.exit_message(msg2, 1, isJSON)


  ## DBCREATE #################################################################
  if p_mode == "create":
      parser = argparse.ArgumentParser()
      parser.add_argument('--cloud', help="Cloud type where you want to create the instance")
      parser.add_argument('--params', help="json object required to create the instance")
      known_args, unknown_args = parser.parse_known_args(args)
      if len(unknown_args) != 3:
          msg1 = p_mode + " needs exactly 1 arguments: type"
          util.exit_message(msg1, 1, isJSON)
      if known_args.params and known_args.cloud:

          response = dbaas.create(isJSON, unknown_args[2], known_args.params, known_args.cloud)
          if isJSON:
              print (json.dumps([response]))
          else:
              print (response['msg'])
      else:
        msg1 = p_mode + " requires --params and --cloud parameter."
        util.exit_message(msg1, 1, isJSON)
      exit_cleanly(0)


  # DBCONFIG #######################################################
  if p_mode in ["dbconfig"]:
    parser = argparse.ArgumentParser()
    parser.add_argument('--pwd')
    parser.add_argument('--set')
    parser.add_argument('--reset')
    known_args, unknown_args = parser.parse_known_args(args)
    if known_args.pwd in ["", None]:
      p_passwd = ""
    else:
      p_passwd = known_args.pwd
    rc = 0
    params = "host user port database [--pwd passwd] {[--set '{\"name\":\"value\", ...}'] || [--reset 'name1, name2, ....']}"
    if len(unknown_args) == 6:
      if known_args.set or known_args.reset:
        set_params = {}
        reset_params = []
        if known_args.set:
          try:
            set_params = json.loads(known_args.set)
          except ValueError as e:
            util.exit_message("Invalid JSON object parameter", 1, isJSON)
        if known_args.reset:
          reset_params = known_args.reset.split(",")
        rc = dbaas.dbconfig(isJSON, unknown_args[2], unknown_args[3], p_passwd, unknown_args[4], unknown_args[5],
                            set_params, reset_params)
      else:
        rc = 1
        msg1 = "Missing some parameters. \n try : " + params
        util.exit_message(msg1, rc, isJSON)
    else:
      rc = 1
      msg1 = "Invalid number of arguments. \n try : " + params
      util.exit_message(msg1, rc, isJSON)

    exit_cleanly(rc)

  # DBDUMP & DBRESTORE #######################################################
  if p_mode in ["dbdump", "dbrestore"]:
    parser = argparse.ArgumentParser()
    parser.add_argument('--pwd')
    known_args, unknown_args = parser.parse_known_args(args)
    args = unknown_args
    if known_args.pwd in ["", None]:
      p_passwd = ""
    else:
      p_passwd = known_args.pwd
    rc=0
    if len(args) > 7:
      rc = dbaas.dbdumprest(isJSON, p_mode, args[2], args[3], args[4], args[5],
                       args[6], args[7], args[8:], p_passwd=p_passwd)
    else:
      msg1 = p_mode + " needs minimum seven arguments: "
      msg2 = "  dbname host port user --pwd passwd filename format options"
      util.exit_message(msg1 + "\n" + msg2, 1, isJSON)
    exit_cleanly(rc)


  ## DBLIST ###################################################################
  if p_mode == "instances":
    bad_msg = "Invalid parameters." + "\ntry:  instances {db || pg || vm} " + \
              "[--email you@email.com  --region name  --instance name  --verbose]"

    if len(args) < 3:
      util.exit_message(bad_msg, 1, isJSON)

    list_type = args[2]

    parser = argparse.ArgumentParser()
    parser.add_argument('--region')
    parser.add_argument('--instance')
    parser.add_argument('--email')
    parser.add_argument('--cloud')
    known_args, unknown_args = parser.parse_known_args(args)
    args = unknown_args

    if known_args.email in ["", None]:
      email = "%"
    else:
      email =  known_args.email

    if len(args) == 3:
      rc = dbaas.instancelist(isJSON, isVERBOSE, list_type, known_args.region, known_args.instance,
                              email,p_cloud=known_args.cloud)
      exit_cleanly(rc)
    else:
      util.exit_message(bad_msg, 1, isJSON)


  ## VERIFY-AMI ##################################################################
  if p_mode == "verify-ami":
    if len(args) == 3:
      exit_cleanly(dbaas.verify_ami(isJSON, args[2]))
    else:
      util.exit_message("VERIFY-AMI takes one argument", 1, isJSON)


  ## DIRLIST #####################################################################
  if p_mode == "dirlist":
    if len(args) == 3:
      exit_cleanly(util.dirlist(isJSON, args[2]))
    else:
      util.exit_message("DIRLIST takes one argument", 1, isJSON)


  ## LABLIST #####################################################################
  if p_mode == "lablist":
    if len(args) == 2:
      lab.list(isJSON)
      exit_cleanly(0)
    else:
      util.exit_message("LABLIST takes no arguments", 1, isJSON)


  ## REPOLIST ####################################################################
  if p_mode == "repolist":
    if len(args) == 2:
      repo.list(isJSON)
      exit_cleanly(0)
    else:
      util.exit_message("REPOLIST takes no arguments", 1, isJSON)


  ## DEFAULT ####################################################################
  if p_mode == "default":
    if len(args) < 3:
       util.exit_message("DEFAULT takes at least one parm {get || set || list}", 1, isJSON)

    action = args[2]
    actions = ['get', 'set', 'list']
    if action not in actions:
      util.exit_message("Action must be in " + str(actions), 1, isJSON)

    if action == "list":
      exit_cleanly(meta.default(isJSON, action, "*", "*", "*", "*", ""))

    if len(args) != 8:
       util.exit_message("'default " + action + "' command must have six parms", 1, isJSON)
     
    exit_cleanly(meta.default(isJSON, action, args[3], args[4], args[5], args[6], args[7]))


  ## SSH ########################################################################
  if p_mode == "ssh":
    import nacl
    exit_cleanly(nacl.run_ssh(isJSON, subprocess.list2cmdline(sys.argv[2:])))


  ## CLOUD ######################################################################
  if p_mode == "cloud":
    import nacl
    exit_cleanly(nacl.run_cloud(isJSON, subprocess.list2cmdline(sys.argv[2:])))

 
  ## CLOUDLIST ##################################################################
  if p_mode == "cloudlist":
    exit_cleanly(util.get_cloud_list(isJSON))


  ## METALIST ###################################################################
  if p_mode == "metalist":
    parser = argparse.ArgumentParser()
    parser.add_argument('--instance')
    parser.add_argument('--region')
    parser.add_argument('--version')
    parser.add_argument('--cloud')
    parser.add_argument('--type')
    parser.add_argument('--group')
    known_args, unknown_args = parser.parse_known_args(args)
    args = unknown_args

    if len(args) == 3:
      if args[2] in ("rds-versions", "vpc-list", "instance-class", "images", "res-group", "instance-type","storage-accounts"):
        if known_args.cloud not in ['aws', 'azure']:
          util.exit_message("Missing or invalid cloud parameter. Possible values are aws,azure", 1, isJSON)
        if known_args.type and known_args.type not in ['db', 'vm', 'pg']:
          util.exit_message("Missing or invalid type parameter. Possible values are db,vm and pg", 1, isJSON)
        elif not known_args.type:
          known_args.type = 'db'
        if not known_args.region and known_args.cloud in ['aws']:
          util.exit_message(args[2] + " need region parameter.", 1, isJSON)
        if args[2] in ("instance-class") and not known_args.version:
          util.exit_message(args[2] + " need engine version parameter.", 1, isJSON)
        if args[2] in ("instance-type") and not known_args.region:
          util.exit_message(args[2] + " need region parameter.", 1, isJSON)
        if args[2] in ("storage-accounts","vpc-list") and known_args.cloud in ("azure") and not known_args.group:
          util.exit_message(args[2] + " need group parameter.", 1, isJSON)
        exit_cleanly(dbaas.cloud_metalist(isJSON, isVERBOSE, args[2], known_args.region,
                                          known_args.version, cloud=known_args.cloud,
                                          instance_type=known_args.type,group=known_args.group))
      else:
        exit_cleanly(dbaas.meta_list(isJSON, isVERBOSE, args[2], known_args.instance))
    else:
      util.exit_message("METALIST takes one argument", 1, isJSON)

  ## pgha ##################################################################
  if p_mode == "pgha":
    parser = argparse.ArgumentParser()
    parser.add_argument('--provider', help="Provider cloud name")
    parser.add_argument('--cluster', help="Cluster name")
    parser.add_argument('--status', action='store_true', help="Status of the cluster")
    known_args, unknown_args = parser.parse_known_args(args)
    config_dir = os.path.join(PGC_HOME, "pgha3", "data", "config")
    msg = "No clusters are provisioned."
    json_dict = {}
    clusters=[]
    if os.path.exists(config_dir):
      clusters = util.get_pgha_clusters(config_dir, known_args.provider, known_args.cluster, known_args.status)
    if len(clusters) > 0:
      if isJSON:
        json_dict['state'] = 'completed'
        json_dict['data'] = clusters
        print(json.dumps([json_dict]))
      else:
        if known_args.status:
          keys = ['cluster_name', 'provider', 'endpoint', 'pg_nodes', 'haproxy_nodes']
          headers = ['Cluster', 'Provider', 'Endpoint', 'pg_nodes', 'haproxy_nodes']
          if known_args.cluster:
            keys = ['name', 'provider', 'type', 'ip_address', 'role', 'status']
            headers = ['Node', 'Provider', 'Type', 'Ip Address', 'Role', 'status']
          print(api.format_data_to_table(clusters, keys=keys, header=headers))
        else:
          keys = ['cluster_name', 'provider',  'size', 'endpoint']
          headers = ['Cluster', 'Provider', 'Size', 'Endpoint']
          print(api.format_data_to_table(clusters, keys=keys, header=headers))
    else:
      if isJSON:
        json_dict['state'] = 'complete'
        json_dict['msg'] = msg
        msg = json.dumps([json_dict])
      print(msg)
    sys.exit(0)

  ## CREDENTIALS ##################################################################
  if p_mode == "credentials":
    enc_secret = util.get_value("GLOBAL", "SECRET", "")
    if enc_secret == "":
      import uuid
      util.set_value("GLOBAL", "SECRET", str(uuid.uuid4()))
    parser = argparse.ArgumentParser()
    parser.add_argument('--name')
    parser.add_argument('--type', choices=['pwd', 'ssh-key', 'cloud'])
    parser.add_argument('--user')
    parser.add_argument('--pwd')
    parser.add_argument('--key')  # Either ssh_key
    parser.add_argument('--sudo_pwd')
    parser.add_argument('--cloud', type=str.lower)
    parser.add_argument('--region')
    parser.add_argument('--cred_uuid')
    parser.add_argument('--credentials_json', help = "json object required to add credentials")
    known_args, unknown_args = parser.parse_known_args(args)
    args = unknown_args
    rc = 0
    try:
      action = str(args[2]).upper()
    except IndexError as e:
      action = ""
      pass
    msg = ""
    mode_types = "ADD | UPDATE | DELETE"
    bad_reg_msg = "try: pgc " + p_mode + " [" + mode_types + "] [--list] "
    bad_reg_msg = bad_reg_msg + \
      "[--name credential_name] [--type credential_type] [--user ssh_user] " + \
      "[--pwd ssh_pwd] [--key ssh_key | cloud_key] " + \
      "[--sudo_pwd ssh_sudo_pwd] [--cloud cloud_name] " + \
      "[--region region] [--cred_uuid credential_uuid] [--credentials_json json]"
    if action not in ("ADD", "UPDATE", "DELETE") and not isLIST:
      print("ERROR: first parm must be ADD or UPDATE or DELETE")
      util.exit_message(bad_reg_msg, 1, isJSON)
    else:
      if isLIST:
        util.list_credentials(isJSON)
      elif action == "DELETE":
        msg = "Credential has been deleted successfully."
        if not known_args.cred_uuid:
          bad_reg_msg = bad_reg_msg + "\n to DELETE cred_uuid is required."
          util.exit_message(bad_reg_msg, 1, isJSON)
        else:
          if util.get_credentials_by_uuid(known_args.cred_uuid):
            hosts_by_creds = util.get_hosts_by_credential_uuid(known_args.cred_uuid)
            if len(hosts_by_creds)==0:
              rc = util.save_credentials(action, known_args, isJSON)
            else:
              msg = "Credential(s) cannot be deleted as they are currently in use by hosts({0}).".format(",".join(hosts_by_creds))
              rc=1
          else:
            msg = "Invalid credential id"
            rc = 1
          util.exit_message(msg, rc, isJSON)
      elif known_args.type:
        if known_args.credentials_json:
          try:
            t_cred = json.loads(known_args.credentials_json)
          except Exception as ex:
            util.exit_message("Invalid credential_json, please provide valid json.", 1, isJSON)
        if action == "ADD":
          msg = "Credential has been created successfully."
          if known_args.type == "pwd":
            if known_args.user and known_args.pwd:
              pass
            else:
              bad_reg_msg = bad_reg_msg + "\n pwd type requires ssh user and pwd."
              util.exit_message(bad_reg_msg, 1, isJSON)
          elif known_args.type == "ssh-key":
            if not known_args.key:
              bad_reg_msg = bad_reg_msg + "\n ssh-key type requires ssh-key."
              util.exit_message(bad_reg_msg, 1, isJSON)
          elif known_args.type == "cloud":
            if not known_args.credentials_json:
              bad_reg_msg = bad_reg_msg + "\n cloud type requires --credentials_json."
              util.exit_message(bad_reg_msg, 1, isJSON)
            if util.is_credentials_by_cloud_exists(known_args.cloud):
              util.exit_message("Credentials already exists for %s, Please update or delete existing one." % known_args.cloud, 1, isJSON)
          rc = util.save_credentials(action, known_args, isJSON)
        elif action == "UPDATE":
          msg = "Credential has been update successfully."
          if not known_args.cred_uuid:
            bad_reg_msg = bad_reg_msg + "\n to UPDATE cred_uuid is required."
            util.exit_message(bad_reg_msg, 1, isJSON)
          check_cred = util.get_credentials_by_uuid(known_args.cred_uuid)
          if check_cred.get("cred_uuid"):
            rc = util.save_credentials(action, known_args, isJSON)
          else:
            msg = "Invalid credential id"
            rc = 1
        util.exit_message(msg, rc, isJSON)
      else:
        util.exit_message(bad_reg_msg, 1, isJSON)


  ## REGISTER & UNREGISTER ######################################################
  if (p_mode in ('register', 'unregister')):
    parser = argparse.ArgumentParser()
    parser.add_argument('--cred_name')
    parser.add_argument('--update')
    known_args, unknown_args = parser.parse_known_args(args)
    args = unknown_args

    mode_types = "HOST | GROUP | REPO"
    bad_reg_msg = "try: pgc " + p_mode + " [" + mode_types + \
      "] [id pgc_home [name] [group]] [--list] [--cred_name credential_name] [--update host_id]"

    try:
      reg = str(args[2]).upper()
      if reg not in ('HOST', 'GROUP', 'REPO'):
        print("ERROR: first parm must be HOST, GROUP or REPO")
        util.exit_message(bad_reg_msg, 1, isJSON)
    except IndexError as e:
      print("ERROR: first parm must be HOST, GROUP or REPO")
      util.exit_message(bad_reg_msg, 1, isJSON)

    args_len = len(args)

    if reg == "REPO":
      if args_len != 4:
        print("ERROR: The '" + p_mode + " REPO' command takes one parameter")
        exit_cleanly(1)
      else:
        exit_cleanly(repo.process_cmd(p_mode, args[3], isJSON))

    if isLIST:
      if reg == "HOST":
        ## ignore the other parms (for now)
        util.list_registrations(isJSON)
      elif reg == "GROUP":
        util.list_groups(isJSON)
      exit_cleanly(0)

    if reg == "HOST":
      if p_mode == 'register':
        if args_len < 5:
          print("ERROR: must be minimum 2 parms")
          util.exit_message(bad_reg_msg, 1, isJSON)

        if args_len > 6:
          print("ERROR: Register command accepts max 3 parms")
          util.exit_message(bad_reg_msg, 1, isJSON)
      else:
        if args_len != 4:
          print("ERROR: must be exactly 4 parms")
          util.exit_message(bad_reg_msg, 1, isJSON)
    elif reg == "GROUP":
      if args_len != 4:
        print("ERROR: must be exactly 4 parms")
        util.exit_message(bad_reg_msg, 1, isJSON)

    host_array = args[2:]

    if p_mode == 'register':
      rc = 0
      if reg == "HOST":
        update_host_id = known_args.update
        if update_host_id and not util.get_host_with_id(update_host_id):
          msg = "ERROR: host_id is not available to update"
          util.exit_message(msg, 1)
        if not util.get_credentials_by_name(known_args.cred_name):
          msg = "ERROR: Credential provided is not available"
          util.exit_message(msg, 1, isJSON)
        cred_info = util.get_credentials_by_name(known_args.cred_name)
        enc_secret = util.get_value("GLOBAL", "SECRET", "")
        enc_key = "{0}{1}".format(enc_secret, cred_info.get("cred_uuid"))
        cred_ssh_user = cred_info.get("ssh_user", "")
        ssh_user = ""
        if cred_ssh_user:
          ssh_user = cred_ssh_user
        enc_ssh_key = cred_info.get("ssh_key", "")
        ssh_key = ""
        if enc_ssh_key:
          ssh_key = util.decrypt(enc_ssh_key, enc_key)
        enc_pwd = cred_info.get("ssh_passwd", "")
        pwd = ""
        if enc_pwd:
          pwd = util.decrypt(enc_pwd, enc_key)
        enc_sudo_pwd = cred_info.get("ssh_sudo_pwd", "")
        sudo_pwd = ""
        if enc_sudo_pwd:
          sudo_pwd = util.decrypt(enc_sudo_pwd, enc_key)
        check_host_name=host_array[1]
        if len(host_array)>3:
          check_host_name=host_array[3]
        if not update_host_id and util.get_host_with_name(check_host_name):
          msg = "ERROR: Host already exists with the name {0}.".format(check_host_name)
          util.exit_message(msg, 1, isJSON)

        remote_pgc_home, pgc_info, p_is_sudo = util.check_remote_host(host_array, ssh_user=ssh_user, isJson=isJSON,
                                                                      ssh_key=ssh_key,
                                                                      pwd=pwd, sudo_pwd=sudo_pwd)
        update_info = False
        if not pgc_info:
          update_info = True
        if remote_pgc_home:
          host_array[2] = remote_pgc_home
          rc = util.register(host_array,
                             isJson=isJSON,
                             update_info=update_info,
                             pgc_info=pgc_info,
                             host_id=update_host_id,
                             is_sudo=p_is_sudo,
                             cred_id=cred_info.get("cred_uuid"))
      elif reg == "GROUP":
        rc = util.register_group(host_array[1], isJson=isJSON)
    else:
      if reg == "HOST":
        rc = util.unregister(host_array, isJson=isJSON)
      elif reg == "GROUP":
        rc = util.unregister_group(host_array[1], isJson=isJSON)

    exit_cleanly(rc)


  ## CANCEL (delete the pid file) #############################################################
  if p_mode == 'cancel':
    try:
      util.delete_file(pid_file)
    except Exception as e:
      pass
    exit_cleanly(0)


  ## TOP ######################################################################################
  if p_mode == 'top':
    try:
      api.top(display=False)
      if isJSON:
        time.sleep(0.5)
        api.top(display=True, isJson=isJSON)
        exit_cleanly(0)
      while True:
        api.top(display=True)
        time.sleep(1)
    except KeyboardInterrupt as e:
      pass
    exit_cleanly(0)


  ## INFO ######################################################################################
  if (p_mode == 'info'):
    if(p_comp=="all" and info_arg==0):
      api.info(isJSON, PGC_HOME, REPO)
    else:
      try:
        c = connL.cursor()
        datadir = ""
        logdir = ""
        svcname = ""
        svcuser = ""
        port = 0
        is_installed = 0
        autostart = ""
        version = ""
        install_dt = ""
        if p_comp != "all":
          sql = "SELECT coalesce(c.datadir,''), coalesce(c.logdir,''), \n" \
                "       coalesce(c.svcname,''), coalesce(c.svcuser,''), \n" \
                "       c.port, c.autostart, c.version, c.install_dt, \n" +\
                "       coalesce((select release_date from versions where c.component = component and c.version = version),'20160101') " + \
                "  FROM components c WHERE c.component = '" + p_comp + "'"
          c.execute(sql)
          data = c.fetchone()
          if not data is None:
            is_installed = 1
            datadir = str(data[0])
            logdir = str(data[1])
            svcname = str(data[2])
            svcuser = str(data[3])
            port = int(data[4])
            autostart = str(data[5])
            version = str(data[6])
            install_dt = str(data[7])
            ins_release_dt=str(data[8])

        sql = "SELECT c.category, c.description, p.project, r.component, v.version, v.platform, \n" + \
              "       p.homepage_url, r.doc_url, v.is_current, \n" + \
              "       " + str(is_installed) + " as is_installed, r.stage, \n" + \
              "       r.sup_plat, v.release_date, p.short_desc, \n" + \
              "       r.disp_name, r.short_desc \n" + \
              "  FROM projects p, releases r, versions v, categories c \n" + \
              " WHERE r.project = p.project AND v.component = r.component \n" + \
              "   AND " + util.like_pf("v.platform") + " \n" + \
              "   AND p.category = c.category"

        if(p_comp!="all"):
          sql = sql + " AND r.component = '" + p_comp + "'"

        if (p_version != "" and p_version !="all"):
          sql = sql + " and v.version = '" + p_version + "'"

        sql = sql + " ORDER BY v.is_current desc,is_installed, c.category, p.project, r.component desc, v.version desc "

        if(p_version==""):
          sql = sql + " limit 1"

        c.execute(sql)
        data = c.fetchall()

        compJson = []
        kount = 0
        for row in data:
          kount = kount + 1
          cat = row[0]
          cat_desc = row[1]
          proj = row[2]
          comp = row[3]
          ver = row[4]
          plat = row[5]
          home_url = row[6]
          doc_url = row[7]
          is_current = row[8]
          is_installed = row[9]
          stage = row[10]
          sup_plat = row[11]
          rel_dt = str(row[12])
          short_desc = row[13]
          disp_name = row[14]
          release_desc = row[15]
          if len(rel_dt) == 8:
            release_date = rel_dt[:4] + '-' + rel_dt[4:6] + '-' + rel_dt[6:]
          else:
            release_date = rel_dt
          if len(install_dt) >= 8:
            install_date = install_dt[0:4] + '-' + install_dt[5:7] + '-' + install_dt[8:10]
          else:
            install_date = install_dt
          compDict = {}
          compDict['category']=cat
          compDict['project']=proj
          compDict['component']=comp
          compDict['platform']=plat
          compDict['home_url']=home_url
          compDict['doc_url']=doc_url
          compDict['is_installed']=is_installed
          compDict['short_desc']=short_desc
          compDict['disp_name']=disp_name
          compDict['release_desc']=release_desc
          current_version = meta.get_current_version(comp)
          is_current = 0
          compDict['version'] = ver
          if is_installed == 1:
            compDict['ins_release_dt'] = ins_release_dt[:4] + '-' + ins_release_dt[4:6] + '-' + ins_release_dt[6:]
            compDict['version'] = version
            is_update_available = 0
            cv = Version.coerce(current_version)
            iv = Version.coerce(version)
            if cv>iv:
              is_update_available=1
            if current_version == version:
              is_current = 1
            elif is_update_available == 1:
              compDict['current_version'] = current_version
          compDict['is_current']=is_current
          compDict['stage']=stage
          compDict['sup_plat']=sup_plat
          compDict['release_date']=release_date

          compDict['is_new'] = 0

          try:
            rd = datetime.datetime.strptime(release_date, '%Y-%m-%d')
            today_date = datetime.datetime.today()
            date_diff = (today_date - rd).days

            if date_diff <= 30:
              compDict['is_new'] = 1
          except Exception as e:
            pass

          compDict['install_date']=install_date
          compDict['datadir'] = datadir
          compDict['logdir'] = logdir
          compDict['svcname'] = svcname
          compDict['svcuser'] = svcuser
          compDict['port'] = port
          compDict['autostart'] = autostart
          if isRELNOTES:
            rel_version = current_version
            if (p_version != "" and p_version !="all"):
              rel_version = p_version
            rel_notes = str(util.get_relnotes(comp, rel_version))
            compDict['relnotes'] = rel_notes
            if isJSON:
              markdown_text = unicode(rel_notes,sys.getdefaultencoding(),errors='ignore').strip()
              html_text = mistune.markdown(markdown_text)
              compDict['rel_notes'] = html_text
          else:
            compDict['relnotes'] = ""
          if is_installed == 1 and port > 0:
              is_running = check_comp(comp, port, 0, True)
              if is_running == "NotInitialized":
                compDict['available_port'] = util.get_avail_port("PG Port", port, comp, isJSON=True)
                compDict['available_datadir'] = os.path.join(PGC_HOME, "data", comp)
                compDict['port'] = 0
              compDict['status'] = is_running
              compDict['current_logfile'] = ""
              if util.is_postgres(p_comp) and is_running != "NotInitialized":
                current_logfile = util.find_most_recent(logdir, "")
                if current_logfile:
                  compDict['current_logfile'] = os.path.join(logdir, current_logfile)
              if is_running == "Running" and util.is_postgres(p_comp):
                  try:
                    util.read_env_file(p_comp)
                    pg = PgInstance("localhost", "postgres", "postgres", port)
                    pg.connect()
                    server_running_version = pg.get_version()
                    version_info = server_running_version.split(",")[0].split()
                    running_version = version_info[1]
                    if not util.check_running_version(version, running_version):
                      compDict['status'] = "Stopped"
                    else:
                      compDict['built_on'] = version_info[3]
                      size = pg.get_cluster_size()
                      up_time = util.get_readable_time_diff(str(pg.get_uptime().seconds), precision=2)
                      compDict['data_size'] = size
                      compDict['up_time'] = up_time
                      if PGC_ISJSON == "False":
                        db_list = pg.get_database_list()
                        compDict['db_list'] = db_list
                        connections_list = pg.get_active_connections()
                        total = None
                        for c in connections_list:
                          if c['state'] == 'total':
                            total = c['count']
                          if c['state'] == 'max':
                            max_conn = c['count']
                        if total:
                          connections = str(total) + "/" + str(max_conn)
                          compDict['connections'] = connections
                    pg.close()
                  except Exception as e:
                    my_logger.error(traceback.format_exc())
                    pass

          compJson.append(compDict)

          if not isJSON:
            api.info_component(compDict, kount)
        if isJSON:
          print(json.dumps(compJson, sort_keys=True, indent=2))
      except Exception as e:
        fatal_sql_error(e, sql, "INFO")
    exit_cleanly(0)


  ## STATUS ####################################################
  if (p_mode == 'status'):
    for c in p_comp_list:
      check_status(c, p_mode)
    exit_cleanly(0)


  ## CLEAN ####################################################
  if (p_mode == 'clean'):
    conf_cache = PGC_HOME + os.sep + "conf" + os.sep + "cache" + os.sep + "*"
    files = glob.glob(conf_cache)
    for f in files:
      os.remove(f)
    exit_cleanly(0)


  ## LIST ##################################################################################
  if (p_mode == 'list'):
    meta.get_list(isSHOWDUPS, isEXTENSIONS, isJSON, isTEST, False, 
                  p_comp=p_comp, p_relnotes=isRELNOTES)


  ## DEPLIST ##############################################################################
  if (p_mode == 'deplist'):
    dl = get_depend_list(p_comp_list)
    exit_cleanly(0)

  if (p_mode == 'download'):
    if p_comp == "all":
      util.print_error("Invalid component parameter (all)")
      exit_cleanly(1)
    for c in p_comp_list:
      if(p_version==""):
        ver = meta.get_latest_ver_plat(c)
      else:
        ver = p_version
      base_name = c + "-" + ver
      conf_cache = "conf" + os.sep + "cache"
      bz2_file = conf_cache + os.sep + base_name + ".tar.bz2"
      if os.path.exists(bz2_file) and is_downloaded(base_name):
        msg = "File is already downloaded"
        util.exit_message(msg, 1)
      if not retrieve_comp(base_name, c):
        comment = "Download failed"
        util.exit_message(comment,1)
      cwd_file = os.getcwd() + os.sep + base_name + ".tar.bz2"
      copy2 (bz2_file, cwd_file)
      comment = "Successfully downloaded."
      file_size = util.get_file_size(os.path.getsize(bz2_file))
    exit_cleanly(0)


  ## REMOVE ##################################################
  if (p_mode == 'remove'):

    if p_comp == 'all':
      msg = 'You must specify component to remove.'
      my_logger.error(msg)
      return_code=1
      if isJSON:
        return_code = 0
        msg = '[{"status":"error","msg":"' + msg + '"}]'
      util.exit_message(msg, return_code)

    for c in p_comp_list:
      if c not in installed_comp_list:
        msg = c + " is not installed."
        print(msg)
        continue
      if util.get_platform() == "Windows" and c == "python2":
        msg = c + " cannot be removed."
        return_code = 1
        if isJSON:
          return_code = 0
          msg = '[{"status":"error","msg":"' + msg + '"}]'
        util.exit_message(msg, return_code)

      if is_depend_violation(c, p_comp_list):
        exit_cleanly(1)

      server_port = util.get_comp_port(c)

      server_running = False
      if server_port > "1":
        server_running = util.is_socket_busy(int(server_port), c)

      if server_running:
        run_script(c, "stop-" + c, "stop")
        time.sleep(5)

      remove_comp(c)

      extensions_list = meta.get_installed_extensions_list(c)
      for ext in extensions_list:
        update_component_state(ext, p_mode)

      update_component_state(c, p_mode)
      comment = "Successfully removed the component."
      if isJSON:
        msg = '[{"status":"complete","msg":"' + comment + '","component":"' + c + '"}]'
        print(msg)

    exit_cleanly(0)


  ## GROUPINSTALL ###########################################
  if (p_mode == 'groupinstall'):
    if len(args) == 3:
      exit_cleanly(group_install(args[2]))
    else:
      msg = 'GROUPINSTALL takes just one parameter'
      util.exit_message(msg, 1, isJSON)

    exit_cleanly(0)


  ## INSTALL ################################################
  if (p_mode == 'install'):

    if p_comp == 'all':
      msg = 'You must specify component to install.'
      my_logger.error(msg)
      return_code=1
      if isJSON:
        return_code = 0
        msg = '[{"status":"error","msg":"' + msg + '"}]'
      util.exit_message(msg, return_code)

    deplist = get_depend_list(p_comp_list)
    component_installed = False
    dependent_components = []
    installed_commponents = []
    dependencies = [ p for p in deplist if p not in p_comp_list ]
    for c in deplist:
      if requested_p_version and c in p_comp_list:
        status = install_comp(c,p_version,p_rver=requested_p_version)
        p_version = requested_p_version
      else:
        p_version = None
        status = install_comp(c)
      update_component_state(c, p_mode, p_version)
      isExt = meta.is_extension(c)
      if isExt:
        parent = util.get_parent_component(c,0)
      if status==1 and (c in p_comp_list or p_comp_list[0]=="all") :
        ## already installed
        pass
      elif status!=1:
        installed_comp_list.append(c)
        isExt = meta.is_extension(c)
        if isExt:
          util.create_manifest(c, parent)
          util.copy_extension_files(c, parent)
        script_name = "install-" + c
        run_script(c, script_name, "")
        if isJSON:
          json_dict={}
          json_dict['state'] = "install"
          json_dict['status'] = "complete"
          json_dict['msg'] = "Successfully installed the component."
          json_dict['component'] = c
          msg = json.dumps([json_dict])
          print(msg)
        if c in p_comp_list or p_comp_list[0] == "all":
          component_installed = True
          installed_commponents.append(c)
          if isExt:
            util.delete_dir(os.path.join(PGC_HOME, c))
        else:
          dependent_components.append(c)

    exit_cleanly(0)


  # Verify data & log directories ############################
  data_home = PGC_HOME + os.sep + 'data'
  if not os.path.exists(data_home):
    os.mkdir(data_home)
    data_logs = data_home + os.sep + 'logs'
    os.mkdir(data_logs)


  script_name = ""


  ## UPDATE ###################################################
  if (p_mode == 'update'):
    retrieve_remote()
    if not isJSON:
      print(" ")
    try:
      l = connL.cursor()
      sql = "SELECT component, version, platform, status FROM components"
      l.execute(sql)
      rows = l.fetchall()
      l.close()
      hasUpdates = 0
      hub_update = 0
      bam_update = 0
      for row in rows:
        c_comp = str(row[0])
        c_ver  = str(row[1])
        c_plat = str(row[2])
        c_stat = str(row[3])
        c = connL.cursor()
        sql="SELECT version FROM versions \n" + \
            " WHERE component = ? AND " + util.like_pf("platform") + " \n" + \
            "   AND is_current = 1"
        c.execute(sql, [c_comp])
        v_row = c.fetchone()
        if v_row == None:
          v_ver = c_ver
        else:
          v_ver = str(v_row[0])
        c.close()
        comp_ver_plat = c_comp + "-" + c_ver
        if c_plat > "":
          comp_ver_plat = comp_ver_plat + "-" + c_plat
        comp_ver_plat = comp_ver_plat + (' ' * (35 - len(comp_ver_plat)))

        msg = ""
        if c_ver >= v_ver:
          msg = comp_ver_plat + " is up to date."
        else:
          if c_comp=='hub':
            hub_update = 1
          elif c_comp=="bam2":
            bam_update = 1
          else:
            hasUpdates = 1
          msg = comp_ver_plat + " upgrade available to (" + v_ver + ")"
          my_logger.info(msg)
        if hub_update == 1:
          rc = upgrade_component('hub')
          hub_update = 0

      [interval, last_update_utc, next_update_utc, last_update_local, next_update_local, unique_id] = util.read_hosts('localhost')
      util.update_hosts('localhost', interval, unique_id, True)

      if isJSON:
        print('[{"status":"completed","has_updates":'+ str(hasUpdates) + '}]')
      else:
        if hasUpdates == 0 and bam_update == 0:
          if not isSILENT:
            print("No updates available.\n")
            meta.get_list(isSHOWDUPS, isEXTENSIONS, isJSON, isTEST, True, p_comp=p_comp)
        else:
          meta.get_list(isSHOWDUPS, isEXTENSIONS, isJSON, isTEST, False, p_comp=p_comp)
    except Exception as e:
      fatal_sql_error(e, sql, "update in mainline")


  ## ENABLE, DISABLE ###########################################
  if (p_mode == 'enable' or p_mode == 'disable' or p_mode == 'remove'):
    if p_comp == 'all':
      msg = 'You must ' + p_mode + ' one component at a time'
      util.exit_message(msg, 1, isJSON)
    if not util.is_server(p_comp) and p_mode == 'disable':
      msg = 'Only Server components can be disabled'
      util.exit_message(msg, 1, isJSON)
    update_component_state(p_comp, p_mode)


  ## CONFIG, INIT, RELOAD, ACTIVITY ########################
  if (p_mode in ['config', 'init', 'reload', 'activity', 'provision']):
    script_name = p_mode + "-" + p_comp
    sys.exit(run_script(p_comp, script_name, extra_args))


  ## STOP component(s) #########################################
  if ((p_mode == 'stop') or (p_mode == 'kill') or (p_mode == 'restart')):
    if p_comp == 'all':
      ## iterate through components in reverse list order
      for comp in reversed(dep9):
        script_name = "stop-" + comp[0]
        run_script(comp[0], script_name, p_mode)
    else:
      script_name = "stop-" + p_comp
      run_script(p_comp, script_name, p_mode)


  ## START, RESTART ############################################
  if ((p_mode == 'start')  or (p_mode == 'restart')):
    if p_comp == 'all':
      ## Iterate through components in primary list order.
      ## Components with a port of "1" are client components that
      ## are only launched when explicitely started
      for comp in dep9:
        if util.is_server(comp[0]):
          script_name = "start-" + comp[0]
          run_script(comp[0], script_name, p_mode)
    else:
      present_state = util.get_comp_state(p_comp)
      if (present_state == "NotInstalled"):
         msg = "Component '" + p_comp + "' is not installed."
         my_logger.info(msg)
         util.exit_message(msg, 0)
      if not util.is_server(p_comp):
         if isJSON:
            msg = "'" + p_comp + "' component cannot be started."
            my_logger.error(msg)
            util.print_error(msg)
            exit_cleanly(1)
      if not present_state == "Enabled":
        update_component_state(p_comp, "enable")
      script_name = "start-" + p_comp
      run_script(p_comp, script_name, p_mode)


  ## UPGRADE ##################################################
  if (p_mode == 'upgrade'):
    if p_comp == 'all':
      updates_comp = []
      comp_list = meta.get_list(False,False, False, False, False, p_return=True)
      for c in comp_list:
        if c.get("updates")==1:
          updates_comp.append(c)
      updates_cnt = len(updates_comp)
      if not isJSON and updates_cnt>0:
        headers = ['Category', 'Component', 'Version',
                   'ReleaseDt', 'Status', 'Updates']
        keys = ['category_desc', 'component', 'version',
                'release_date', 'status', 'current_version']
        print(api.format_data_to_table(updates_comp, keys, headers))
        if not isYES:
          try:
            p_update = raw_input ("These component(s) will be updated. Do you want to continue? (y/n):")
            if p_update.strip().lower() == "y":
              isYES=True
          except Exception as e:
            isYES=False
      upgrade_flag = 1
      if isYES:
        for comp in updates_comp:
          rc = upgrade_component(comp.get("component"))
          if rc == 0:
            upgrade_flag = 0
      if upgrade_flag == 1 and updates_cnt==0:
        msg = "All components are already upgraded to the latest version."
        print(msg)
        my_logger.info(msg)
    else:
      if len(p_comp_list)==1 and p_comp=="bam2" and isJSON:
        update_cmd = PGC_HOME + os.sep + "pgc upgrade bam2"
        scheduler_time = datetime.datetime.now() + datetime.timedelta(seconds=60)
        if util.get_platform()=="Windows":
          my_logger.info(scheduler_time)
          schtask_cmd = os.getenv("SYSTEMROOT", "") + os.sep + "System32" + os.sep + "schtasks"
          schedule_cmd = schtask_cmd + ' /create /RU System /tn BamUpgrade /tr "' + update_cmd + '"'\
                         + " /sc once /st " + scheduler_time.strftime('%H:%M') \
                         + " /sd " + scheduler_time.strftime('%m/%d/%Y')
          my_logger.info(schedule_cmd)

          # delete the previous scheduler if exists for upgrading the bam
          try:
            delete_previous_taks = schtask_cmd + " /delete /tn BamUpgrade /f"
            schedule_return = subprocess.Popen(delete_previous_taks, stdout=subprocess.PIPE,
                                               stderr=subprocess.PIPE, shell=True).communicate()
          except Exception as e:
            my_logger.error(traceback.format_exc())
            pass

          #create the scheduler to upgrade bam
          try:
            schedule_return = subprocess.Popen(schedule_cmd, stdout=subprocess.PIPE,
                                               stderr=subprocess.PIPE, shell=True).communicate()
            my_logger.info(schedule_return)
          except Exception as e:
            my_logger.error(traceback.format_exc())
            pass
        else:
          from crontab import CronTab
          cron_tab = CronTab(user=True)
          jobs = cron_tab.find_command(command=update_cmd)
          for job in jobs:
              job.delete()
          cron_job=cron_tab.new(command=update_cmd, comment="upgrade bam")
          cron_job.minute.on(scheduler_time.minute)
          cron_job.hour.on(scheduler_time.hour)
          cron_job.day.on(scheduler_time.day)
          cron_job.month.on(scheduler_time.month)
          cron_tab.write()
          cron_tab.run_scheduler()
      else:

        rc = upgrade_component(p_comp)

        if rc == 1:
          msg = "Nothing to upgrade."
          print(msg)
          my_logger.info(msg)


  ## DBSTAT ######################################
  if p_mode == 'dbstat':
    try:
      util.read_env_file(p_comp)
      port = util.get_comp_port(p_comp)
      pg = PgInstance("localhost", "postgres", "postgres", int(port))
      pg.connect()
      for st in p_dbstat_list:
        if (st == 'get_aggregate_tps'):
          print(json.dumps(pg.get_aggregate_tps()))
      pg.close()
    except Exception as e:
      my_logger.error(traceback.format_exc())


  ## VERIFY #############################################
  if p_mode == 'verify':
    util.verify(isJSON)
    exit_cleanly(0)


  ## SET #################################################
  if p_mode == 'set':
    if len(args) == 5:
      util.set_value(args[2], args[3], args[4])
    else:
      print("ERROR: The SET command must have 3 parameters.")
      exit_cleanly(1)
    exit_cleanly(0)


  ## GET ################################################
  if p_mode == 'get':
    if len(args) == 4:
      print(util.get_value(args[2], args[3]))
    else:
      print("ERROR: The GET command must have 2 parameters.")
      exit_cleanly(1)
    exit_cleanly(0)


  ## UNSET ##############################################
  if p_mode == 'unset':
    if len(args) == 4:
      util.unset_value(args[2], args[3])
    else:
      print("ERROR: The UNSET command must have 2 parameters.")
      exit_cleanly(1)
    exit_cleanly(0)


  ## USERADD ############################################
  if p_mode == 'useradd':
    if len(args) == 3:
      exit_cleanly(startup.useradd_linux(args[2]))
    else:
      print("ERROR: The USERADD command must have 1 parameter (svcuser).")
      exit_cleanly(1)


except Exception as e:
  msg = str(e)
  my_logger.error(traceback.format_exc())
  if isJSON:
    json_dict = {}
    json_dict['state'] = "error"
    json_dict['msg'] = msg
    msg = json.dumps([json_dict])
  print(msg)
  exit_cleanly(1)

exit_cleanly(0)
