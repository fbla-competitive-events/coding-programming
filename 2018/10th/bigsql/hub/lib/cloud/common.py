####################################################################
#########      Copyright (c) 2016-2017 BigSQL           ############
####################################################################

import sys
import json
from cloud_mapping import vm_list_dict, pg_list_dict

def message(p_msg, p_state="info", p__isjson=False):
    if p_state == "error":
        prefix = "ERROR: "
    else:
        prefix = ""

    if p__isjson:
        json_dict = {}
        json_dict['state'] = p_state
        json_dict['msg'] = p_msg
        print (json.dumps([json_dict]))
    else:
        print (prefix + p_msg)

    return


def exit_message(p_msg, p_rc, p__isjson=False):
    if p_rc == 0:
        message(p_msg, "info", p__isjson)
    else:
        message(p_msg, "error", p__isjson)
    sys.exit(p_rc)


def get_field(p_dict, p_field1, p_field2=None):
    try:
        if p_field2 is None:
            return (str(p_dict[p_field1]))
        else:
            return (str(p_dict[p_field1][p_field2]))
    except:
        return ""


## try to convert a timestamp to local timezone ##########
def convert_tz(in_timestamp):
  try:
    from   dateutil.parser import parse
    import dateutil.tz
    in_date = parse(in_timestamp)
    localtz = dateutil.tz.tzlocal()
    out_timestamp = str(in_date.astimezone(localtz).replace(microsecond=0))
    return(out_timestamp)
  except ImportError:
    return(in_timestamp)


def print_verbose(p_dict, p_type=None):
    list_dict = vm_list_dict
    if p_type=="pg":
        list_dict = pg_list_dict
    try:
        print ("*****************************************************")
        for k in p_dict.keys():
            l = 30 - len(list_dict[k]["title"])
            s = " " * l
            print ("#{0}{1}: {2}".format(s, str(list_dict[k]["title"]), str(p_dict.get(k))))
        print ("*****************************************************")
    except Exception as e:
        import traceback
        print (traceback.format_exc())


def get_obj_value(obj, obj_keys):
    if type(obj) is dict:
        initial_value = obj
        for k in obj_keys:
            initial_value = initial_value.get(k)
        return initial_value


def convert(name):
    import re
    s1 = re.sub('(.)([A-Z][a-z]+)', r'\1_\2', name)
    return re.sub('([a-z0-9])([A-Z])', r'\1_\2', s1).lower()
