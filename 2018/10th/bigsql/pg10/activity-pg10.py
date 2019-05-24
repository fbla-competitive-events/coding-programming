from __future__ import print_function, division
 
####################################################################
######          Copyright (c)  2015-2017 OpenSCG          ##########
####################################################################

import os, sys, util

PGC_HOME = os.getenv('PGC_HOME', '')

sys.path.append(os.path.join(PGC_HOME, 'hub', 'scripts', 'lib'))

from PgInstance import PgInstance
import json

pgver = "pg10"

port = int(util.get_comp_port(pgver))

util.read_env_file(pgver)

try:
    pg = PgInstance("localhost", "postgres", "postgres", port)
    pg.connect()

    activities = pg.get_current_activity()
    pg.close()
    print(json.dumps(activities, indent=2))
except Exception as e:
    pass
sys.exit(0)
