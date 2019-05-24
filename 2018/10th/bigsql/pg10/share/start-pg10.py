from __future__ import print_function, division
 
####################################################################
######          Copyright (c)  2015-2017 OpenSCG          ##########
####################################################################

import os, sys, subprocess, json
import util, startup

pgver = "pg10"
dotver = pgver[2] + "." + pgver[3]

PGC_HOME = os.getenv('PGC_HOME', '')

homedir = os.path.join(PGC_HOME, pgver)

logdir = os.path.join(homedir, pgver)

datadir = util.get_column('datadir', pgver)

isJson = os.getenv("isJson", None)

first_time="no"
if not os.path.isdir(datadir):
  rc=os.system(sys.executable + ' -u ' + homedir + os.sep + 'init-' + pgver + '.py')
  if rc == 0:
    rc=os.system(sys.executable + ' -u ' + homedir + os.sep + 'config-' + pgver + '.py')
  else:
    sys.exit(rc)
  datadir = util.get_column('datadir', pgver)
  first_time="yes"

autostart = util.get_column('autostart', pgver)
logfile   = util.get_column('logdir', pgver) + os.sep + "postgres.log"
svcname   = util.get_column('svcname', pgver, 'PostgreSQL ' + dotver + ' Server')
port      = util.get_column('port', pgver)


isJson = os.getenv("isJson", None)
msg = pgver + " starting on port " + str(port)
if isJson:
  jsonMsg = {}
  jsonMsg['status'] = "wip"
  jsonMsg['component'] = pgver
  jsonMsg['msg'] = msg
  print(json.dumps([jsonMsg]))
else:
  print(msg)

cmd = sys.executable + " " + homedir + os.sep  + "run-pgctl.py"

if util.get_platform() == "Windows":  
  if autostart == "on":
    command = 'sc start "'+ svcname + '"'
    print(' ' + command)
    util.system(command, is_admin=True)
  else:
    if isJson:
      isShell = False
      startCmd = cmd.split()
    else:
      isShell = True
      startCmd = "START /MIN " + cmd
    subprocess.Popen(startCmd, creationflags=subprocess.CREATE_NEW_PROCESS_GROUP, close_fds=True, shell=isShell)
elif util.get_platform() == "Darwin" and autostart == "on":
  if not os.path.isdir(logdir):
    os.mkdir(logdir)
  launctl_load_cmd = "launchctl start bigsql." + pgver
  os.system(launctl_load_cmd)
elif util.get_platform() == "Linux" and autostart == "on":
  startup.start_linux("postgresql" + pgver[2:4])
else:
  startCmd = cmd + ' &'
  subprocess.Popen(startCmd, preexec_fn=os.setpgrp(), close_fds=True, shell=True)
