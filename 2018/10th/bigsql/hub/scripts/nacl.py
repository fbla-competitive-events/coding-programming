from __future__ import print_function, division

####################################################################
######          Copyright (c)  2015-2017 OpenSCG          ##########
####################################################################
import re
import os, sys, util

PGC_HOME = os.getenv('PGC_HOME')

devops_lib_path = os.path.join(PGC_HOME, "pgdevops", "lib")
if os.path.exists(devops_lib_path):
  if devops_lib_path not in sys.path:
    sys.path.append(devops_lib_path)

etc_dir = os.path.join(PGC_HOME, "data", "etc")
if not os.path.exists(etc_dir):
  os.mkdir(etc_dir)

base_log_dir = os.path.join(PGC_HOME, "data", "logs")
if not os.path.exists(base_log_dir):
  os.mkdir(base_log_dir)


log_dir = os.path.join(base_log_dir, "salt")
if not os.path.exists(log_dir):
  os.mkdir(log_dir)

log_file = os.path.join(log_dir, "salt.log")


def set_default_params(p_isJSON):
  if "--config-dir" not in sys.argv:
    sys.argv.append("--config-dir")
    sys.argv.append(etc_dir)
  if "--log-file" not in sys.argv:
    sys.argv.append("--log-file")
    sys.argv.append(log_file)
  if p_isJSON and "--out" not in sys.argv:
    sys.argv.append("--out")
    sys.argv.append("json")


def run_cloud(p_isJSON, p_cmd):
  set_default_params(p_isJSON)
  from salt.scripts import salt_cloud
  del sys.argv[1]
  sys.argv[0] = "pgc cloud"
  sys.exit(salt_cloud())
  return (0)


def run_ssh(p_isJSON, p_cmd):
  set_default_params(p_isJSON)
  from salt.scripts import salt_ssh
  del sys.argv[1]
  sys.argv[0] = "pgc ssh"
  sys.exit(salt_ssh())
  return (0)


def run_salt_cmd(p_salt_cmd, p_cmd, p_isJSON):
  try:
    import salt
  except ImportError as e:
    util.exit_message("Missing SaltStack", 1, p_isJSON)

  config = os.path.join("data", "pgdevops", "etc")
  config_dir = os.path.join(os.getenv("PGC_HOME"), config)
  if not os.path.exists(config_dir):
    util.exit_message("Missing CONFIG_DIR: " + config, 1, p_isJSON)

  cmd = p_cmd + " -c " + config_dir

  if p_isJSON:
    cmd = cmd + " --out=json"

  util.run_sudo(p_salt_cmd + " " + cmd, True)
