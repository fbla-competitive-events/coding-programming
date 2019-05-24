from __future__ import print_function, division
 
####################################################################
######          Copyright (c)  2015-2017 OpenSCG          ##########
####################################################################

import os, sys
import util, startup

pgver = "pg10"

print(pgver + " reloading")

autostart = util.get_column('autostart', pgver)
if autostart == "on" and util.get_platform() == "Linux":
  rc = startup.reload_linux("postgresql" + pgver[2:4])
  sys.exit(rc)

PGC_HOME = os.getenv('PGC_HOME', '')
homedir = os.path.join(PGC_HOME, pgver)
datadir = util.get_column('datadir', pgver)

pg_ctl = os.path.join(homedir, "bin", "pg_ctl")
parms = ' reload -D "' + datadir + '"'
rc = util.system(pg_ctl + parms)

sys.exit(rc)
