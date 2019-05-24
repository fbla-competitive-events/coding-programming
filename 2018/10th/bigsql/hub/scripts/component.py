from __future__ import print_function, division
 
####################################################################
######          Copyright (c)  2015-2017 OpenSCG          ##########
####################################################################

import os, sys, util

def init_comp(p_comp, p_pidfile=''):
  print(" ")
  print("## initializing " + p_comp +  " ##################")

  PGC_HOME = os.getenv('PGC_HOME', '')

  datadir = os.path.join(PGC_HOME, 'data', p_comp)
  if os.path.isdir(datadir):
    print("## " + p_comp + " already configured.")
    return(1)
  else:
    os.mkdir(datadir)
  util.set_column("datadir", p_comp, datadir)

  if p_pidfile != '':
    pidfilepath = os.path.join(datadir, p_pidfile)
    util.set_column("pidfile", p_comp, pidfilepath)

  logdir = os.path.join(PGC_HOME, 'data', 'logs', p_comp)
  if not os.path.isdir(logdir):
    os.mkdir(logdir)
  util.set_column("logdir", p_comp, logdir)

  return(0)


def start_comp(p_comp, p_homedir, p_start_cmd):
  print(p_comp + " starting")

  os.chdir(p_homedir)

  datadir = util.get_column("datadir", p_comp)
  if datadir == "" or not (os.path.isdir(datadir)):
    os.system(sys.executable + " -u init-" + p_comp + ".py")

  os.system(p_start_cmd)
  return(0)


def stop_comp(p_comp):
  pidfile = util.get_column("pidfile", p_comp)
  if os.path.isfile(pidfile):
    print(p_comp + " stopping")
    try:
      with open(pidfile, 'r') as f:
        pid = f.readline().rstrip(os.linesep)
      util.kill_pid(int(pid))
      os.remove(pidfile)
    except Exception as e:
      print(str(e))
  else:
    print(p_comp + " is not running")

  return 0


def check_pid_status(p_comp, p_pidfile):
  if os.path.isfile(p_pidfile):
    try:
      with open(p_pidfile, 'r') as f:
        pid = f.readline().rstrip(os.linesep)
      if util.is_pid_running(pid):
        print(p_comp + " running as pid " + str(pid))
      else:
        print(p_comp + " is not running as pid " + str(pid))
    except Exception as e:
      print(str(e))
  else:
    print(p_comp + " is not running")

