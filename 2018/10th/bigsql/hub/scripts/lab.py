from __future__ import print_function, division

##################################################################
########    Copyright (c) 2015-2017 OpenSCG        ###############
##################################################################

import json
import util, meta, api

def list(p_isJSON):

  l_list = meta.get_lab_list()

  keys = ['lab', 'disp_name', 'enabled', 'credit_to' ]
  headers = ['Lab', 'Name', 'Enabled?', 'Credits']

  if p_isJSON:
    print(json.dumps(l_list, sort_keys=True, indent=2))
  else:
    print(api.format_data_to_table(l_list, keys, headers))

  return 0

