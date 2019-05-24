from __future__ import print_function, division

#####################################################
####      Copyright (c) 2015-2017 OpenSCG        ####
#####################################################

import sys, os, sqlite3, json
from semantic_version import Version
import api, util
import datetime
import mistune


def get_groupinstall_list(p_install_group):
  try:
    c = con.cursor()
    sql = "SELECT depend_order, project FROM installgroups WHERE project_group = ? ORDER BY 1"
    c.execute(sql, [p_install_group])
    return(c.fetchall())

  except Exception as e:
    fatal_sql_error(e, sql, "meta.get_groupinstall_list()")


def default(p_isJSON, p_action, p_group, p_user, p_area, p_key, p_value):
  if p_action == "get":
     def_value = get_default(p_isJSON, p_group, p_user, p_area, p_key, p_value)
     print(str(def_value))
  elif p_action == "set":
     set_default(p_isJSON, p_group, p_user, p_area, p_key, p_value) 
  else:
     list_defaults(p_isJSON) 

  return(0)


def list_defaults(p_isJSON):
  try:
    c = con.cursor()
    sql = "SELECT user_group, user_name, section, d_key, d_value \n" + \
          "  FROM defaults \n" + \
          "ORDER BY 1, 2, 3, 4"
    c.execute(sql)
    dataset = c.fetchall()
    dict = []
    for data in dataset:
      d = {}
      d['group'] = str(data[0])
      d['user'] = str(data[1])
      d['section'] = str(data[2])
      d['key'] = str(data[3])
      d['value'] = str(data[4])
      dict.append(d)

  except Exception as e:
    fatal_sql_error(e, sql, "meta.list_defaults()")

  if p_isJSON:
    print(json.dumps(dict, sort_keys=True, indent=2))
  else:
    print("")
    print(api.format_data_to_table(dict, 
      ["group", "user", "section", "key", "value"],
      ["Group", "User", "Section", "Key", "Value"]))

  return(0)


def set_default(p_isJSON, p_group, p_user, p_section, p_key, p_value):
  try:
    c = con.cursor()

    sql = "DELETE FROM defaults \n" + \
      "WHERE user_group = ? AND user_name = ? AND section = ? AND d_key = ?"
    c.execute(sql, [p_group, p_user, p_section, p_key])

    if p_value != "None":
      sql = "INSERT INTO defaults \n" + \
        "(user_group, user_name, section, d_key, d_value) VALUES (?, ?, ?, ?, ?)"
      c.execute(sql, [p_group, p_user, p_section, p_key, p_value])

    con.commit()
    c.close()
  except Exception as e:
    fatal_sql_error(e, sql, "meta.set_default()")
 
  return(0)


def get_default(p_isJSON, p_group, p_user, p_section, p_key, p_value):
  try:
    c = con.cursor()
    sql = "SELECT d_value \n" + \
          "  FROM defaults \n" + \
          " WHERE user_group = ? and user_name = ? \n" + \
          "   AND section = ? AND d_key = ?"
    c.execute(sql, [p_group, p_user, p_section, p_key])
    data = c.fetchone()
    if data:
      return(str(data[0]))
  except Exception as e:
    fatal_sql_error(e, sql, "meta.get_default()")

  return(p_value)


def is_any_autostart():
  try:
    c = con.cursor()
    sql = "SELECT count(*) FROM components WHERE autostart = 'on'"
    c.execute(sql)
    data = c.fetchone()
    if data[0] > 0:
      return True
  except Exception as e:
    fatal_sql_error(e, sql, "meta.is_any_autostart()")

  return False


def put_components(p_comp, p_proj, p_ver, p_plat, p_port, p_stat,
                      p_autos, p_datadir, p_logdir, p_svcname, p_svcuser):
  try:
    c = con.cursor()
    sql = "DELETE FROM components WHERE component = ?"
    c.execute(sql, [p_comp])
    sql = "INSERT INTO components \n" + \
          "  (component, project, version, platform, port, status, \n" + \
          "   autostart, datadir, logdir, svcname, svcuser) \n" + \
          "VALUES \n" + \
          "  (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
    c.execute(sql, [p_comp, p_proj, p_ver, p_plat, p_port, p_stat,
                    p_autos, p_datadir, p_logdir, p_svcname, p_svcuser])
    con.commit()
    c.close()
  except Exception as e:
    fatal_sql_error(e, sql, "meta.put_components()")
  return


def is_extension(ext_comp):
  try:
    c = con.cursor()
    sql = "SELECT c.component,c.project,p.category " + \
          "  FROM projects p,components c " + \
          " WHERE c.component='" + ext_comp + "' " + \
          "   AND c.project=p.project " + \
          "   AND p.category=2"
    c.execute(sql)
    data = c.fetchone()
    if not data:
      return False
  except Exception as e:
    fatal_sql_error(e,sql,"meta.is_extension()")
  return True


def get_lab_list():
  repo = util.get_value('GLOBAL', 'REPO')
  try:
    c = con.cursor()
    sql = "SELECT lab, disp_name, credit_to, short_desc, coalesce(auth_type,''), \n" + \
          "       coalesce(enabled_image,''), coalesce(disabled_image,'') \n" + \
          "  FROM labs ORDER BY 1"
    c.execute(sql)
    l_list = c.fetchall()
  except Exception as e:
    fatal_sql_error(e,sql,"meta.lab_list()")
  labs = []
  for row in l_list:
    lab_dict = {}
    lab = str(row[0])
    lab_dict['enabled'] = util.get_value("labs", lab)
    lab_dict['lab'] = lab
    lab_dict['disp_name'] = str(row[1])
    lab_dict['credit_to'] = str(row[2])
    lab_dict['short_desc'] = str(row[3])
    lab_dict['auth_type'] = str(row[4])
    lab_dict['enabled_image_url'] = repo + "/" + str(row[5])
    lab_dict['disabled_image_url'] = repo + "/" + str(row[6])
    labs.append(lab_dict)
  
  return labs


def get_available_component_list():
  try:
    c = con.cursor()
    sql = "SELECT v.component FROM versions v WHERE v.is_current = 1 \n" + \
          "   AND " + util.like_pf("v.platform")
    c.execute(sql)
    t_comp = c.fetchall()
    r_comp = []
    for comp in t_comp:
      r_comp.append(str(comp[0]))
  except Exception as e:
    fatal_sql_error(e,sql,"meta.get_available_component_list()")
  return  r_comp


def get_all_components_list(p_component=None, p_version=None, p_platform=None):
  try:
    c = con.cursor()
    sql = "SELECT v.component, v.version, v.platform" + \
          "  FROM versions v "
    if p_version is None:
      sql = sql + " WHERE v.is_current = 1 "
    elif p_version=="all":
      sql = sql + " WHERE v.is_current >= 0 "

    if (p_platform is None):
      sql = sql + " AND " + util.like_pf("v.platform") + " "
    if p_component:
      sql = sql + " AND v.component = '" + p_component + "'"
    if p_version and p_version!="all":
      sql = sql + " AND v.version = '" + p_version + "'"

    c.execute(sql)
    t_comp = c.fetchall()
    r_comp = []
    for comp in t_comp:
      if p_platform=="all":
        if comp[2]:
          platforms = comp[2].split(',')
          for p in platforms:
            comp_dict = {}
            comp_dict['component']= str(comp[0])
            version = str(comp[1]) + "-" + p.strip()
            comp_dict['version']= version
            r_comp.append(comp_dict)
        else:
          comp_dict = {}
          comp_dict['component']= str(comp[0])
          version = str(comp[1])
          if comp[2]:
            if p_platform is None:
              version = str(comp[1]) + "-" + util.get_pf()
            else:
              version = str(comp[1]) + "-" + p_platform
          comp_dict['version']= version
          r_comp.append(comp_dict)
      else:
        comp_dict = {}
        comp_dict['component']= str(comp[0])
        version = str(comp[1])
        if comp[2]:
          if p_platform is None:
            version = str(comp[1]) + "-" + util.get_pf()
          else:
            version = str(comp[1]) + "-" + p_platform
        comp_dict['version']= version
        r_comp.append(comp_dict)
  except Exception as e:
    fatal_sql_error(e,sql,"meta.get_all_components_list()")
  return  r_comp


def update_component_version(p_app, p_version):
  try: 
    c = con.cursor()
    update_date=datetime.datetime.utcnow().strftime('%Y-%m-%d %H:%M:%S')
    sql = "UPDATE components SET version = ?,install_dt = ? WHERE component = ?"
    c.execute(sql, [p_version,update_date, p_app])
    con.commit()
    c.close()
  except Exception as e:
    fatal_sql_error(e,sql,"meta.update_component_version()")

  return


## Get Component Version & Platform ########################################
def get_ver_plat(p_comp):
  try:
    c = con.cursor()
    sql = "SELECT version, platform FROM components WHERE component = ?"
    c.execute(sql,[p_comp])
    data = c.fetchone()
    if data is None:
      return "-1"
  except Exception as e:
    fatal_sql_error(e,sql,"meta.get_ver_plat()")
  version = str(data[0])
  platform = str(data[1])
  if platform == "":
    return(version)
  return(version + "-" + platform)


## Get latest current version & platform ###################################
def get_latest_ver_plat(p_comp, p_new_ver=""):
  try: 
    c = con.cursor()
    sql = "SELECT version, platform, is_current, release_date \n" + \
          "  FROM versions \n" + \
          " WHERE component = ? \n" + \
          "   AND " + util.like_pf("platform") + "\n" + \
          "ORDER BY 3 DESC, 4 DESC"
    c.execute(sql,[p_comp])
    data = c.fetchone()
    if data is None:
      return "-1"
  except Exception as e:
    fatal_sql_error(e,sql,"meta.get_latest_ver_plat()")
  if p_new_ver == "":
    version = str(data[0])
  else:
    version = p_new_ver
  platform = str(data[1])
  pf = util.get_pf()
  if platform == "":
    ver_plat = version
  else:
    if pf in platform:
      ver_plat = version + "-" + pf
    else:
      ver_plat = version + "-linux64"
  return(ver_plat)


## Get platform specific version for component ###############################
def get_platform_specific_version(p_comp, p_ver):
  try:
    c = con.cursor()
    sql = "SELECT version, platform FROM versions " + \
          " WHERE component = ? " + \
          "   AND " + util.like_pf("platform") + \
          "   AND version = ?"
    c.execute(sql,[p_comp,p_ver])
    data = c.fetchone()
    if data is None:
      return "-1"
  except Exception as e:
    fatal_sql_error(e,sql,"meta.get_platform_specific_version()")
  version = str(data[0])
  platform = str(data[1])
  if platform == "":
    return(version)
  return(version + "-" + util.get_pf())


## get list of installed & available components ###############################
def get_list(p_isOLD, p_isExtensions, p_isJSON, p_isTEST, p_showLATEST, p_comp=None, p_relnotes=None, p_return=False):
  r_sup_plat = util.like_pf("r.sup_plat")

  if p_isOLD:
    exclude_comp = ""
  else:
    exclude_comp = " AND v.component NOT IN (SELECT component FROM components)"

  parent_comp_condition = ""
  installed_category_conditions = " AND p.category > 0 "
  available_category_conditions = " AND p.category <> 2 "
  ext_component = ""

  if p_isExtensions:
    installed_category_conditions = " AND p.category = 2 "
    available_category_conditions = " AND p.category = 2 "
    if p_comp != "all":
      ext_component = " AND parent = '" + p_comp + "' "

  installed = \
    "SELECT p.category, g.description as category_desc, c.component, c.version, c.port, c.status, r.stage, \n" + \
    "       coalesce((select is_current from versions where c.component = component AND c.version = version),0), \n" + \
    "       c.datadir, p.short_desc, \n" + \
    "       coalesce((select parent from versions where c.component = component and c.version = version),'') as parent, \n" + \
    "       coalesce((select release_date from versions where c.component = component and c.version = version),'20160101'), \n" + \
    "       c.install_dt, r.disp_name, r.short_desc, \n" + \
    "       coalesce((select release_date from versions where c.component = component and is_current = 1),'20160101') \n" + \
    "  FROM components c, releases r, projects p, categories g \n" + \
    " WHERE c.component = r.component AND r.project = p.project \n" + \
    "   AND p.category = g.category \n"  + \
    "   AND " + r_sup_plat + installed_category_conditions + ext_component

  available = \
    "SELECT c.category, c.description, v.component, v.version, 0, 'NotInstalled', \n" + \
    "       r.stage, v.is_current, '', p.short_desc, v.parent as parent, v.release_date, '', \n" + \
    "       r.disp_name, r.short_desc, \n" + \
    "       coalesce((select release_date from versions where v.component = component and is_current = 1),'20160101') \n" + \
    "  FROM versions v, releases r, projects p, categories c \n" + \
    " WHERE v.component = r.component AND r.project = p.project \n" + \
    "   AND p.category = c.category \n" + \
    "   AND " + util.like_pf("v.platform") + " \n" + \
    "   AND " + r_sup_plat + exclude_comp + available_category_conditions + ext_component

  extensions = \
    "SELECT c.category, c.description, v.component, v.version, 0, 'NotInstalled', \n" + \
    "       r.stage, v.is_current, '', p.short_desc, v.parent as parent, v.release_date, '', \n" + \
    "       r.disp_name, r.short_desc, \n" + \
    "       coalesce((select release_date from versions where v.component = component and is_current = 1),'20160101') \n" + \
    "  FROM versions v, releases r, projects p, categories c \n" + \
    " WHERE v.component = r.component AND r.project = p.project \n" + \
    "   AND p.category = 2 AND p.category = c.category \n" + \
    "   AND " + util.like_pf("v.platform") + " \n" + \
    "   AND v.parent in (select component from components) AND " + r_sup_plat + exclude_comp

  if p_isExtensions:
    sql = installed + "\n UNION \n" + available + "\n ORDER BY 1, 3, 4, 6"
  else:
    sql = installed + "\n UNION \n" + available + "\n UNION \n" + extensions + "\n ORDER BY 1, 3, 4, 6"

  try:
    c = con.cursor()
    c.execute(sql)
    data = c.fetchall()

    headers = ['Category', 'Component', 'Version', 
               'ReleaseDt', 'Status', 'Updates']
    keys    = ['category_desc', 'component', 'version', 
               'release_date', 'status', 'current_version']

    jsonList = []
    kount = 0
    previous_version = None
    previous_comp = None
    for row in data:
      compDict = {}
      kount = kount + 1

      category = str(row[0])
      category_desc = str(row[1])
      comp = str(row[2])
      version = str(row[3])
      port = str(row[4])

      if previous_comp and previous_version:
        if previous_comp == comp and previous_version == version:
          continue

      previous_version = version
      previous_comp = comp

      if str(row[5]) == "Enabled":
        status = "Installed"
      else:
        status = str(row[5])
      if status == "NotInstalled" and p_isJSON == False:
        status = ""

      stage = str(row[6])
      if stage == "prod" and p_isJSON == False:
        stage = ""
      if stage == "test" and status in ("", "NotInstalled"):
        if not p_isTEST:
          continue

      is_current = str(row[7])
      if is_current == "0" and status in ("", "NotInstalled"):
        if not p_isOLD:
          continue

      current_version = get_current_version(comp)
      is_update_available = 0
      cv = Version.coerce(current_version)
      iv = Version.coerce(version)
      if cv>iv:
        is_update_available = 1

      if is_update_available==0:
        updates = 0
        current_version = ""
      else:
        updates = 1

      if (port == "0") or (port == "1"):
        port = ""

      datadir = row[8]
      if row[8] is None:
        datadir = ""
      else:
        datadir = str(row[8]).strip()

      short_desc = row[9]

      parent = row[10]

      disp_name = row[13]
      release_desc = row[14]

      release_date = '1970-01-01'
      curr_rel_date = '1970-01-01'
      curr_rel_dt=str(row[15])
      rel_dt = str(row[11])
      if len(rel_dt) == 8:
        release_date = rel_dt[0:4] + "-" + rel_dt[4:6] + "-" + rel_dt[6:8]
        curr_rel_date = curr_rel_dt[0:4] + "-" + curr_rel_dt[4:6] + "-" + curr_rel_dt[6:8]


      compDict['is_new'] = 0

      try:
        rd = datetime.datetime.strptime(release_date, '%Y-%m-%d')
        today_date = datetime.datetime.today()
        date_diff = (today_date - rd).days

        if date_diff <= 30:
          compDict['is_new'] = 1
        if p_showLATEST and date_diff > 30:
          continue
      except Exception as e:
        pass

      if util.is_postgres(comp) or comp in ("pgdevops"):
        if port > "" and status == "Installed" and datadir == "":
          status = "NotInitialized"
          port = ""

      ins_date = str(row[12])
      install_date=""
      compDict['is_updated'] = 0
      if ins_date:
        install_date = ins_date[0:4] + "-" + ins_date[5:7] + "-" + ins_date[8:10]

        try:
          insDate = datetime.datetime.strptime(install_date, '%Y-%m-%d')
          today_date = datetime.datetime.today()
          date_diff = (today_date - insDate).days
          if date_diff <= 30:
            compDict['is_updated'] = 1
        except Exception as e:
          pass

      if p_relnotes and p_isJSON:
        rel_version=version
        if current_version!="":
          rel_version=current_version
        rel_notes = str(util.get_relnotes(comp, rel_version))
        markdown_text = unicode(rel_notes,sys.getdefaultencoding(),errors='ignore').strip()
        html_text = mistune.markdown(markdown_text)
        compDict['rel_notes'] = html_text

      compDict['category'] = category
      compDict['category_desc'] = category_desc
      compDict['component'] = comp
      compDict['version'] = version
      compDict['short_desc'] = short_desc
      compDict['disp_name'] = disp_name
      compDict['release_desc'] = release_desc
      compDict['port'] = port
      compDict['release_date'] = release_date
      compDict['install_date'] = install_date
      compDict['curr_release_date'] = curr_rel_date
      compDict['status'] = status
      compDict['stage'] = stage
      compDict['updates'] = updates
      compDict['is_current'] = is_current
      compDict['current_version'] = current_version
      compDict['parent'] = parent
      jsonList.append(compDict)

    if p_return:
      return jsonList

    if p_isJSON:
      print(json.dumps(jsonList, sort_keys=True, indent=2))
    else:
      if p_showLATEST:
        print("New components released in the last 30 days.")
      print(api.format_data_to_table(jsonList, keys, headers))

  except Exception as e:
    fatal_sql_error(e, sql, "meta.get_list()")
  sys.exit(0)    


## Check if component required for platform #########
def is_dependent_platform(p_comp):
  try:
    c = con.cursor()
    sql = "SELECT platform FROM versions WHERE component = ?"
    c.execute(sql,[p_comp])
    data = c.fetchone()
    if data is None:
      return False
  except Exception as e:
    fatal_sql_error(e,sql,"meta.is_dependent_platform()")
  platform = str(data[0])
  if len(platform.strip()) == 0 or util.has_platform(platform) >= 0:
    return True
  return False


## get component version ############################
def get_version(p_comp):
  try:
    c = con.cursor()
    sql = "SELECT version FROM components WHERE component = ?"
    c.execute(sql,[p_comp])
    data = c.fetchone()
    if data is None:
      return ""
  except Exception as e:
    fatal_sql_error(e,sql,"meta.get_version()")
  return str(data[0])


## Get Current Version ###################################################
def get_current_version(p_comp):
  try:
    c = con.cursor()
    sql = "SELECT version FROM versions WHERE component = ? AND is_current=1"
    c.execute(sql,[p_comp])
    data = c.fetchone()
    if data is None:
      sql = "SELECT version, release_date FROM versions WHERE component = ? ORDER BY 2 DESC"
      c.execute(sql,[p_comp])
      data = c.fetchone()
      if data is None:
        return ""
  except Exception as e:
    fatal_sql_error(e,sql,"meta.get_current_version()")
  return str(data[0])


def get_dependent_components(p_comp):
  data = []
  sql = "SELECT c.component FROM projects p, components c \n" + \
        " WHERE p.project = c.project AND p.depends = \n" + \
        "   (SELECT project FROM releases \n" + \
        "     WHERE component = '" + p_comp + "')"
  try:
    c = con.cursor()
    c.execute(sql)
    data = c.fetchall()
  except Exception as e:
    fatal_sql_error(e,sql,"meta.get_dependent_components()")
  return data


def get_component_list():
  try:
    c = con.cursor()
    sql = "SELECT component FROM components"
    c.execute(sql)
    t_comp = c.fetchall()
    r_comp = []
    for comp in t_comp:
      r_comp.append(str(comp[0]))
  except Exception as e:
    fatal_sql_error(e,sql,"meta.get_component_list()")
  return r_comp;


def get_installed_extensions_list(parent_c):
  try:
    c = con.cursor()
    sql = "SELECT c.component FROM versions v ,components c " + \
          "WHERE v.component = c.component AND v.parent='" + parent_c + "'"
    c.execute(sql)
    t_comp = c.fetchall()
    r_comp = []
    for comp in t_comp:
      r_comp.append(str(comp[0]))
  except Exception as e:
    fatal_sql_error(e,sql,"meta.get_installed_extensions_list()")
  return r_comp;


def fatal_sql_error(err, sql, func):
  print("################################################")
  print("# FATAL SQL Error in " + func)
  print("#    SQL Message =  " + err.args[0])
  print("#  SQL Statement = " + sql)
  print("################################################")
  sys.exit(1)


## MAINLINE ################################################################
con = sqlite3.connect(os.getenv("PGC_HOME") + os.sep + "conf" + os.sep + "pgc_local.db", check_same_thread=False)
