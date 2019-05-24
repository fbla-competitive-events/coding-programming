from __future__ import print_function, division
 
####################################################################
######          Copyright (c)  2015-2017 OpenSCG          ##########
####################################################################

import subprocess
import os
import sys

PGC_HOME = os.getenv("PGC_HOME", "")
sys.path.append(os.path.join(PGC_HOME, 'hub', 'scripts'))
sys.path.append(os.path.join(PGC_HOME, 'hub', 'scripts', 'lib'))

import util

util.set_lang_path()
 
pgver = "pg10"

dotver = pgver[2] + "." + pgver[3]

datadir = util.get_column('datadir', pgver)

logdir = util.get_column('logdir', pgver)

autostart = util.get_column('autostart', pgver)

pg_ctl = os.path.join(PGC_HOME, pgver, "bin", "pg_ctl")
logfile = util.get_column('logdir', pgver) + os.sep + "postgres.log"

util.read_env_file(pgver)

if util.get_platform() == "Windows":
  cmd = pg_ctl + ' start -s -w -D "' + datadir + '" '
  util.system(cmd)
elif util.get_platform() == "Darwin" and autostart == "on":
  postgres = os.path.join(PGC_HOME , pgver, "bin", "postgres")
  util.system(postgres +' -D ' + datadir + ' -r ' + logfile)  
else:
  cmd = pg_ctl + ' start -s -w -D "' + datadir + '" ' + '-l "' + logfile + '"'
  util.system(cmd)
