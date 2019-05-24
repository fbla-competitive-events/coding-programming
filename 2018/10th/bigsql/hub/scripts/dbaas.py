from __future__ import print_function, division

##################################################################
########      Copyright (c) 2015-2017 OpenSCG      ###############
##################################################################

import json, os, sys, sqlite3
import util, api, meta
from PgInstance import PgInstance

try:
  # For Python 3.0 and later
  from urllib import request as urllib2
except ImportError:
  # Fall back to Python 2's urllib2
  import urllib2

devops_lib_path = os.path.join(os.getenv("PGC_HOME"), "pgdevops", "lib")
if os.path.exists(devops_lib_path):
  if devops_lib_path not in sys.path:
    sys.path.append(devops_lib_path)

try:
  import boto3
except Exception as e:
  pass


DEVOPS_DB = os.getenv("PGC_HOME") + os.sep + "data" + os.sep + "pgdevops" + os.sep + "devops.db"
try:
  con = sqlite3.connect(DEVOPS_DB, check_same_thread=False)
except Exception as error:
  pass


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


def get_field(p_dict, p_field1, p_field2=None):
   try:
     if p_field2 is None:
       return(str(p_dict[p_field1]))
     else:
       return(str(p_dict[p_field1][p_field2]))
   except:
     return ""

def cloud_metalist(p_isJSON, p_isVERBOSE, p_type, p_region, p_version=None, cloud="aws", instance_type = "db", group = ""):
    from cloud import CloudConnection
    cloud = CloudConnection(cloud=cloud)
    client = cloud.get_client(instance_type=instance_type,region=p_region)
    if p_type == "instance-class":
      col_names = ["DBInstanceClass", "v_cpu", "memory", "iops", "network_performance"]
      col_titles = ["DBInstanceClass", "vCPU", "Memory", "IOPS Optimized", "Network Performance"]
      db_instance_meta_list = meta_list("", "", "aws-rds", "", True)
      result = client.instance_class(p_version, db_instance_meta_list = db_instance_meta_list)
    elif p_type == "rds-versions":
      col_names = ["DBEngineVersionDescription", "EngineVersion"]
      col_titles = ["DBEngineVersionDescription", "EngineVersion"]
      result = client.rds_versions()
    elif p_type == "res-group":
      col_names = ["name", "region"]
      col_titles = ["ResourceGroup", "Region"]
      result = client.get_resource_group()
    elif p_type == "instance-type":
      col_names = ["name", "number_of_cores", "resource_disk_size_in_mb", "memory_in_mb", "max_data_disk_count","os_disk_size_in_mb"]
      col_titles = ["Name", "number_of_cores", "resource_disk_size_in_mb", "memory_in_mb", "max_data_disk_count","os_disk_size_in_mb"]
      result = client.get_instance_types(p_region)
    elif p_type == "storage-accounts":
      col_names = ["name", "location"]
      col_titles = ["Name", "location"]
      result = client.storage_accounts(group)
    else:
      col_names = ["vpc", "subnet_group"]
      col_titles = ["VPC", "Subnet Group"]
      result  = client.subnet_groups(res_group=group)
    if p_isJSON:
      print(json.dumps(result, indent=2))
    else:
      print(api.format_data_to_table(result, col_names, col_titles))
    return (0)

def meta_list(p_isJSON, p_isVERBOSE, p_meta, p_instance, return_dict = False):
  repo = util.get_value("GLOBAL", "REPO")
  url = repo + "/" + p_meta + ".txt"
  try:
    response = urllib2.urlopen(url, timeout=15)
    meta_string = response.read()
  except Exception as e:
    util.exit_message("Cannot retrieve METALIST '" + url + "'", 1, p_isJSON)

  dict = []
  kount = 0

  for line in meta_string.splitlines():
    kount = kount + 1

    word = line.split("\t")
    for i in range(len(word)):
      word[i] = word[i].strip()

    ## process header rows
    if kount < 3:
      if kount == 1:
        ## row 1 contains column names
        num_cols = len(word)
        col_names = word
      else:
        ## row 2 contains column titles
        col_titles = word
      continue

    ## process valid detail rows
    if len(word) == num_cols:
      if p_instance:
        if word[0] != p_instance:
          continue
      d={}
      for i in range(len(word)):
        d[col_names[i]] = word[i]
      dict.append(d)

  if return_dict:
    return dict
  if p_isJSON:
    print(json.dumps(dict, sort_keys=True, indent=2))
  else:
    print("")
    print(api.format_data_to_table(dict, col_names, col_titles))

  return(0)


def instances_list(p_isJSON, p_isVERBOSE, p_region, p_instance, p_instance_type=None, p_cloud=None, p_resource=None):
  try:
    filter_params = {}
    filter_params['region'] = p_region
    filter_params['instance'] = p_instance
    filter_params['resource'] = p_resource
    from cloud import CloudConnection
    cloud = CloudConnection(cloud=p_cloud)
    client = cloud.get_client(instance_type=p_instance_type)
    client.isJson = p_isJSON
    client.verbose = p_isVERBOSE
    instances_list = client.get_list(filter_params)
    if p_isJSON:
      json_dict = {}
      json_dict['data'] = instances_list
      json_dict['state'] = 'completed'
      print(json.dumps([json_dict]))
    elif not p_isVERBOSE:
      keys = client.header_keys
      headers = client.header_titles
      print("")
      print(api.format_data_to_table(instances_list, keys, headers))
  except Exception as e:
    util.exit_message(str(e), 1, p_isJSON)
  return (0)


def instancelist(p_isJSON, p_isVERBOSE, p_list_type, p_region, p_instance, p_email, p_cloud=None, p_resource=None):
  preferred_list_types = ["postgres", "db", "vm"]
  list_type = p_list_type.lower()

  if list_type in ("pg", "postgres", "postgresql"):
    return(pglist(p_isJSON, p_isVERBOSE, p_email=p_email))
  elif list_type in ("rds", "db", "vm", "ec2"):
    if p_cloud:
      return (instances_list(p_isJSON, p_isVERBOSE, p_region, p_instance,
                             p_instance_type=list_type, p_cloud=p_cloud,
                             p_resource=p_resource))
    else:
      msg = p_list_type + " requires cloud type argument (--cloud)"
      util.exit_message(msg, 1, p_isJSON)

  else:
    msg = p_list_type + " not valid for dbaas.instancelist()." + "  \n try: " + str(preferred_list_types)
    util.exit_message(msg, 1, p_isJSON)
  

def pglist(p_isJSON, p_isVERBOSE, p_region="", p_email=""):
  try:
    c = con.cursor()
    sql = "SELECT u.email, g.name as server_group, s.name as server_name, \n" + \
          "       s.host, s.port, s.maintenance_db as db, s.username as db_user, \n" + \
          "       s.id as sid, g.id as gid, s.password as pwd \n" + \
          "  FROM server s, user u, servergroup g \n" + \
          " WHERE s.user_id = u.id AND s.servergroup_id = g.id \n" + \
          "   AND u.email LIKE ? \n" + \
          "ORDER BY 1, 2, 3"
    c.execute(sql, [p_email])
    svr_list = c.fetchall()
    svrs = []
    for row in svr_list:
      svr_dict = {}
      svr_dict['email'] = str(row[0])
      svr_dict['server_group'] = str(row[1])
      svr_dict['server_name'] = str(row[2])
      svr_dict['host'] = str(row[3])
      svr_dict['port'] = str(row[4])
      svr_dict['db'] = str(row[5])
      svr_dict['db_user'] = str(row[6])
      svr_dict['sid'] = str(row[7])
      svr_dict['gid'] = str(row[8])
      has_pwd = False
      if row[9]:
        has_pwd = True
      svr_dict['has_pwd'] = has_pwd
      svrs.append(svr_dict)
  except Exception as error:
    msg = "pgDevOps must be installed & initialized."
    util.exit_message(msg, 1, p_isJSON)

  keys = ['email', 'server_group', 'server_name', 'host', 'port', 'db', 'db_user' ]
  headers = ['Email Address', 'Server Group', 'Server Name', 'Host', 'Port', 'DB', 'DB User' ]

  if p_isJSON:
    print(json.dumps(svrs, sort_keys=True, indent=2))
  else:
    print("")
    print(api.format_data_to_table(svrs, keys, headers))

  return(0)


def is_in_pglist(p_email, p_server_group, p_server_name, p_host, p_port, p_db, p_db_user):
  try:
    c = con.cursor()
    sql = "SELECT count(*) \n" + \
          "  FROM server s, user u, servergroup g \n" + \
          " WHERE s.user_id = u.id AND s.servergroup_id = g.id \n" + \
          "   AND u.email = ? AND g.name = ? AND s.name = ? AND s.host = ? \n" + \
          "   AND s.port = ? AND s.maintenance_db = ? AND s.username = ?"
    c.execute(sql, [p_email, p_server_group, p_server_name, p_host, p_port, p_db, p_db_user])
    data = c.fetchone()
    if data[0] > 0:
      return True
  except Exception as e:
    meta.fatal_sql_error(e, sql, "is_in_pglist()")
  
  return False 


def verify_ami(p_isJSON, p_ami):
  ami_url="http://169.254.169.254/latest/meta-data/"
  try:
    response = urllib2.urlopen(ami_url + "instance-id", timeout=2)
    out = response.read()
  except Exception as e:
    p_msg = "This machine is not an AMI Instance"
    if p_isJSON:
      json_dict = {}
      json_dict['state'] = "info"
      json_dict['msg'] = p_msg
      print(json.dumps([json_dict]))
    else:
      print(p_msg)
    sys.exit(2)

  if out != p_ami:
    p_msg = "Incorrect AMI Instance ID"
    if p_isJSON:
      json_dict = {}
      json_dict['state'] = "error"
      json_dict['msg'] = p_msg
      print(json.dumps([json_dict]))
    else:
      print(p_msg)
    sys.exit(3)

  return(0)


def rdslist(p_isJSON, p_isVERBOSE, p_region="", p_instance="", p_email="", p_engines=["postgres"]):

  if p_region is None:
    p_region = ""
  if p_instance is None:
    p_instance = ""

  try:
    rds_regions=[]
    available_rds_regions = boto3.session.Session().get_available_regions("rds")
    if p_region > "":
      if p_region in available_rds_regions:
        rds_regions = [p_region]
      else:
        msg = str(p_region) + " is not a valid region for rds."
        util.exit_message(msg, 1, p_isJSON)
    else:
      rds_regions = available_rds_regions

    # get all of the postgres db instances
    pg_list = []
    for region in rds_regions:
      msg = "Searching " + region + "..."
      util.message(msg, "info", p_isJSON)
      rds = boto3.client('rds', region_name=region)

      dbs = rds.describe_db_instances()
      for db in dbs['DBInstances']:
          if db['Engine'] in p_engines:
            ec2 = boto3.client('ec2', region_name=region)
            pg_dict = {}
            pg_dict['engine'] = get_field(db, 'Engine')
            pg_dict['region'] = region

            pg_dict['instance'] = get_field(db, 'DBInstanceIdentifier')
            if p_instance > "":
              if p_instance != pg_dict['instance']:
                continue
            pg_dict['arn'] = get_field(db, 'DBInstanceArn')

            pg_dict['master_user'] = get_field(db, 'MasterUsername')
            pg_dict['status'] = get_field(db, 'DBInstanceStatus')
            pg_dict['address'] = get_field(db, 'Endpoint', 'Address')
            pg_dict['port'] = get_field(db, 'Endpoint', 'Port')
            pg_dict['dbname'] = get_field(db, 'DBName')

            pg_dict['db_class'] = get_field(db, 'DBInstanceClass')
            pg_dict['engine_version'] = get_field(db, 'EngineVersion')
            pg_dict['auto_minor_upgrade'] = get_field(db, 'AutoMinorVersionUpgrade')

            try:
              pg_dict['create_time'] = convert_tz(get_field(db, 'InstanceCreateTime'))
            except Exception as e:
              pg_dict['create_time'] = ""

            pg_dict['iops'] = get_field(db, 'Iops')

            pg_dict['storage_allocated'] = get_field(db, 'AllocatedStorage')
            storage_type = get_field(db, 'StorageType')
            if storage_type == 'standard':
              pg_dict['storage_type'] = storage_type + ": Magnetic"
            elif storage_type == 'gp2':
              pg_dict['storage_type'] = storage_type + ": General Purpose (SSD)"
            elif storage_type == 'io2':
              pg_dict['storage_type'] = storage_type + ": Provisioned IOPS (SSD)"
            else:
              pg_dict['storage_type'] = storage_type
            pg_dict['storage_encrypted'] = get_field(db, 'StorageEncrypted')

            pg_dict['maint_window'] = get_field(db, 'PreferredMaintenanceWindow')
            pg_dict['backup_window'] = get_field(db, 'PreferredBackupWindow')
            pg_dict['backup_retention'] = get_field(db, 'BackupRetentionPeriod')

            try:
              pg_dict['latest_restorable'] = convert_tz(get_field(db, 'LatestRestorableTime'))
            except Exception as e:
              pg_dict['latest_restorable'] = ""

            pg_dict['az_is_multi'] = get_field(db, 'MultiAZ')
            pg_dict['az_primary'] = get_field(db, 'AvailabilityZone')
            pg_dict['az_secondary'] = get_field(db, 'SecondaryAvailabilityZone')
            pg_dict['publicly_accessible'] = get_field(db, 'PubliclyAccessible')

            pg_dict['monitoring_interval'] = get_field(db, 'MonitoringInterval')
            pg_dict['monitoring_resource_arn'] = get_field(db, 'EnhancedMonitoringResourceArn')
            pg_dict['monitoring_role_arn'] = get_field(db, 'MonitoringRoleArn')

            pg_dict['subnet_group'] = get_field(db, 'DBSubnetGroup', 'DBSubnetGroupName')

            pg_dict['vpc'] = ""
            try:
              vpc_id = get_field(db, 'DBSubnetGroup', 'VpcId')
              pg_dict['vpc_id'] = vpc_id
              pg_dict['vpc'] = vpc_id
              my_vpcs = ec2.describe_vpcs(VpcIds=[vpc_id,])
              for my_vpc in my_vpcs['Vpcs']:
                for tag in my_vpc['Tags']:
                  if tag['Key'] == "Name":
                    vpc_name = tag['Value']
                    pg_dict['vpc'] = vpc_name
                    break
            except Exception as e:
              pass

            try:
              pg_dict['is_in_pglist'] = is_in_pglist(p_email, pg_dict['region'],
                pg_dict['instance'], pg_dict['address'], pg_dict['port'],
                pg_dict['dbname'], pg_dict['master_user'])
            except Exception as e:
              pass
            pg_list.append(pg_dict)
  except KeyboardInterrupt as e:
    util.exit_message("Keyboard Interrupt", 1, p_isJSON)
  except Exception as e:
    exc_type, exc_obj, exc_tb = sys.exc_info()
    fname = os.path.split(exc_tb.tb_frame.f_code.co_filename)[1]
    print(exc_type, fname, exc_tb.tb_lineno)
    msg = "Unable to run rds.describe_db_instances().  \n" + str(e)
    util.exit_message(msg, 1, p_isJSON)

  if p_isJSON:
    json_dict = {}
    json_dict['data'] = pg_list
    json_dict['state'] = 'completed'
    print(json.dumps([json_dict]))
    return(0)

  if p_isVERBOSE:
    print_verbose(pg_list)
    return(0)

  keys    = ['region', 'instance', 'status', 'dbname', 'db_class', 'vpc', 'az_is_multi']
  headers = ['Region', 'Instance', 'Status', 'DBName', 'Class', 'VPC', 'MultiAZ']
  print("")
  print(api.format_data_to_table(pg_list, keys, headers))

  return(0)


def print_verbose(p_list):
  kount = 0
  for pg_dict in p_list:
    kount = kount + 1
    print("################################################")
    print("#             region: " + pg_dict['region'])
    print("#                arn: " + pg_dict['arn'])
    print("#           instance: " + pg_dict['instance'])
    print("#             status: " + pg_dict['status'])
    print("#             dbname: " + pg_dict['dbname'])
    print("#        master_user: " + pg_dict['user'])
    print("#               port: " + pg_dict['port'])
    print("#            address: " + pg_dict['host'])
    print("#           db_class: " + pg_dict['db_class'])
    print("#     engine_version: " + pg_dict['engine_version'])
    print("# auto_minor_upgrade: " + pg_dict['auto_minor_upgrade'])
    print("#        create_time: " + pg_dict['create_time'])
    print("#publicly_accessible: " + pg_dict['publicly_accessible'])
    print("#  storage_allocated: " + pg_dict['storage_allocated'] + " GB")
    print("#       storage_type: " + pg_dict['storage_type'])

    if pg_dict['iops'] > "":
      display_iops = pg_dict['iops']
    else:
      display_iops = "disabled"
    print("#               iops: " + display_iops)

    print("#  storage_encrypted: " + pg_dict['storage_encrypted'])
    print("#                vpc: " + pg_dict['vpc'])

    print("#       maint_window: " + pg_dict['maint_window'])
    print("#      backup_window: " + pg_dict['backup_window'])
    print("#   backup_retention: " + pg_dict['backup_retention'] + " days")
    print("#  latest_restorable: " + pg_dict['latest_restorable'])

    print("#        az_is_multi: " + pg_dict['az_is_multi'])
    print("#         az_primary: " + pg_dict['az_primary'])
    if pg_dict['az_secondary'] > "":
      print("#       az_secondary: " + pg_dict['az_secondary'])

    if pg_dict['monitoring_interval'] == "0":
      display_intvl = "disabled"
    else:
      display_intvl = pg_dict['monitoring_interval'] + " seconds"
    print("#   monitor_interval: " + display_intvl)

  if kount > 0:
    print("################################################")

  return(kount)


def create(p_isJSON, p_type, p_json, p_cloud):
  if p_type not in ['db', 'vm']:
    util.exit_message("type parm must be 'db' or 'vm'", 1, p_isJSON)

  try:
    dict = json.loads(p_json)
  except Exception as e:
    util.exit_message("invalid json-object parameter", 1, p_isJSON)

  from cloud import CloudConnection
  cloud = CloudConnection(cloud=p_cloud)
  client = cloud.get_client(instance_type=p_type)
  client.isJson = p_isJSON
  rc = client.create_node(dict)

  return(rc)


def create_ec2(p_isJSON, p_region, p_dict):

  ### mandatory parms ############
  for d in p_dict:
    ec2_params = {}
    ec2_params['MinCount'] = 1
    ec2_params['MaxCount'] = 1

    try:
      ec2_params['ImageId'] = d['image_id']
      ec2_params['InstanceType'] = d['instance_type']
    except Exception as e:
        util.exit_message("missing required field " + str(e), 1, p_isJSON)

    if d.get("kernel_id"):
      ec2_params['KernelId'] = d.get("kernel_id")

    if d.get("key_name"):
      ec2_params['KeyName'] = d.get("key_name")

    if d.get("monitoring"):
      ec2_params['Monitoring'] = d.get("monitoring")

    if d.get("ramdisk_id"):
      ec2_params['RamdiskId'] = d.get("ramdisk_id")

    if d.get("subnet_id"):
      ec2_params['SubnetId'] = d.get("subnet_id")

    if d.get("user_data"):
      ec2_params['UserData'] = d.get("user_data")

    if d.get("additional_info"):
      ec2_params['AdditionalInfo'] = d.get("additional_info")

    if d.get("client_token"):
      ec2_params['ClientToken'] = d.get("client_token")

    if d.get("disable_api_termination"):
      ec2_params['DisableApiTermination'] = d.get("disable_api_termination")

    if d.get("dryrun"):
      ec2_params['DryRun'] = d.get("dryrun")

    if d.get("ebs_optimized"):
      ec2_params['EbsOptimized'] = d.get("ebs_optimized")

    if d.get("shutdown_behaviour"):
      ec2_params['InstanceInitiatedShutdownBehavior'] = d.get("shutdown_behaviour")

    if d.get("private_ip_address"):
      ec2_params['PrivateIpAddress'] = d.get("private_ip_address")

  try:
    ec2 = boto3.resource('ec2', region_name=p_region)

    instance = ec2.create_instances(**ec2_params)
  except Exception as e:
    msg1 = "ec2.create_instances(): "
    util.exit_message(msg1 + str(e), 1, p_isJSON)

  return(0)


def create_rds(p_isJSON, p_region, p_dict):
  ### optional parms #############
  engine = "postgres"
  multi_az = False
  public_access = False
  storage_encrypted = False
  monitoring_interval = 0  # Valid Values: 0, 1, 5, 10, 15, 30, 60

  ### mandatory parms ############
  for d in p_dict:
    try:
      db_params = {}
      db_params['DBName'] = d['dbname']
      db_params['DBInstanceClass'] = d['db_class']
      db_params['DBInstanceIdentifier'] = d['instance']
      db_params['MasterUsername'] = d['master_user']
      db_params['MasterUserPassword'] = d['password']
      db_params['DBSubnetGroupName'] = d['subnet_group']
      db_params['Engine'] = d.get("engine", engine)
      db_params['MultiAZ'] = d.get("multi_az", multi_az)
      db_params['PubliclyAccessible'] = d.get("public_accessible", public_access)
      db_params['StorageEncrypted'] = d.get("storage_encrypted", storage_encrypted)
      db_params['MonitoringInterval'] = d.get("monitoring_interval", monitoring_interval)

      s_port = str(d['port'])
      try:
        db_params['Port'] = int(s_port)
      except Exception as e:
        util.exit_message("port must be an integer", 1, p_isJSON)

      db_params['StorageType'] = d['storage_type']
      s_allocated_storage = str(d['allocated_storage'])
      try:
        db_params['AllocatedStorage'] = int(s_allocated_storage)
      except Exception as e:
        util.exit_message("allocated storage GB must be an integer", 1, p_isJSON)

      # All optional Params
      if d.get("availability_zone"):
        db_params['AvailabilityZone'] = d.get("availability_zone")

      if d.get("security_groups"):
        # List of security groups
        db_params['DBSecurityGroups'] = d.get("security_groups")

      if d.get("vpc_security_group_ids"):
        # List of VPC security groups
        db_params['VpcSecurityGroupIds'] = d.get("vpc_security_group_ids")

      if d.get("maintanance_window"):
        db_params['PreferredMaintenanceWindow'] = d.get("maintanance_window")

      if d.get("db_parameter_group"):
        db_params['DBParameterGroupName'] = d.get("db_parameter_group")

      if d.get("backup_retention_period"):
        # between 0 to 35
        db_params['BackupRetentionPeriod'] = d.get("backup_retention_period")

      if d.get("backup_window"):
        db_params['PreferredBackupWindow'] = d.get("backup_window")

      if d.get("engine_version"):
        db_params['EngineVersion'] = d.get("engine_version")

      if d.get("version_upgrade"):
        db_params['AutoMinorVersionUpgrade'] = d.get("version_upgrade")

      if d.get("licence_model"):
        db_params['LicenseModel'] = d.get("licence_model")

      if d.get("iops"):
        db_params['Iops'] = d.get("iops")

      if d.get("option_group_name"):
        db_params['OptionGroupName'] = d.get("option_group_name")

      if d.get("charset"):
        db_params['CharacterSetName'] = d.get("charset")

      if d.get("tags"):
        # List of dict Eg : [ {"Key":'tag name',"Value":value}, ....]
        db_params['Tags'] = d.get("tags")

      if d.get("cluster_identifier"):
        db_params['DBClusterIdentifier'] = d.get("cluster_identifier")

      if d.get("tde_arn"):
        db_params['TdeCredentialArn'] = d.get("tde_arn")

      if d.get("tde_arn_pwd"):
        db_params['TdeCredentialPassword'] = d.get("tde_arn_pwd")

      if d.get("kms_key_id"):
        db_params['KmsKeyId'] = d.get("kms_key_id")

      if d.get("domain"):
        db_params['Domain'] = d.get("domain")

      if d.get("copy_tags"):
        db_params['CopyTagsToSnapshot'] = d.get("copy_tags")

      if d.get("monitor_arn"):
        db_params['MonitoringRoleArn'] = d.get("monitor_arn")

      if d.get("iam_role"):
        db_params['DomainIAMRoleName'] = d.get("iam_role")

      break
    except Exception as e:
        util.exit_message("missing required field " + str(e), 1, p_isJSON)

  try:  
    rds = boto3.client('rds', region_name=p_region)
    response = rds.create_db_instance(**db_params)
    return response
  except Exception as e:
    msg1 = "rds.create_db_instance(): "
    util.exit_message(msg1 + str(e), 1, p_isJSON)

  return(0)


def dbdumprest(p_isJSON, p_cmd, p_dbname, p_host, p_port, p_user, p_file, p_format, 
               p_options, p_passwd="", p_dbtype="pg"):
  f_options = " ".join(p_options)
  if p_dbtype != "pg":
    msg = p_dbtype + " is an invalid dbtype for dbaas.dbdumprest()"
    util.exit_message(msg, 1, p_isJSON)

  if p_cmd == "dbdump":
    cmd = "pg_dump"
  elif p_cmd == "dbrestore" and 'p' == p_format:
    cmd = "psql"
  elif p_cmd == "dbrestore":
    cmd = "pg_restore"
  else:
    msg = p_cmd + " is an invalid command for dbaas.dbdumprest()"
    util.exit_message(msg, 1, p_isJSON)
  cmd = cmd + " -w -d " + str(p_dbname)
  cmd = cmd + " -h " + str(p_host)
  cmd = cmd + " -p " + str(p_port)
  cmd = cmd + " -U " + str(p_user)
  if p_cmd == "dbrestore" and 'p' != p_format:
    pass
  else:
    cmd = cmd + ' -f "' + str(p_file) + '"'
  cmd = cmd + " -F " + str(p_format)
  cmd = cmd + " " + str(f_options)
  if p_cmd == "dbrestore" and 'p' != p_format:
    cmd = cmd + ' "' + str(p_file) + '"'
  if p_passwd != "*":
    os.environ['PGPASSWORD'] = p_passwd

  # Check on remote server version
  source_server = PgInstance(str(p_host),str(p_user), str(p_dbname), int(p_port), str(p_passwd))
  try:
    source_server.connect()
  except Exception as ex:
    e_msg = str(ex)
    if e_msg.find('timeout expired') >= 0 or e_msg.find('timed out') >= 0:
      util.message('Connection timed out with ' + str(p_host), 'error', p_isJSON)
    else:
      util.message(str(ex), 'error', p_isJSON)
    return (1)
  source_version = source_server.get_version().split(" ")[1]
  source_server.close()

  from semantic_version import Version
  pg_version = Version.coerce(source_version)

  component_required = p_dbtype + str(pg_version.major) + str(pg_version.minor)

  if pg_version.minor==0:
    component_required = p_dbtype + str(pg_version.major)

  if util.get_comp_state(component_required) == 'NotInstalled':
    util.message('component_required: ' + str(component_required),'error',p_isJSON)
    return (2)

  # Point to proper pg_dump
  full_pgdump_path=os.getenv("PGC_HOME") + os.sep + component_required + os.sep + "bin" + os.sep
  cmd = full_pgdump_path + cmd
  util.message(cmd, "info", p_isJSON)

  rc = os.system(cmd)
  if rc != 0:
    return (1)

  return (0)


def dbconfig(isJSON, hostname, p_user, p_passwd, p_port, p_dbname, p_setting={}, p_reset=[]):
  jsonDict = {}
  rc = 0
  msg = ""
  try:
    source_server = PgInstance(str(hostname), str(p_user), str(p_dbname), int(p_port), str(p_passwd))
    source_server.connect()
    params=[]
    if len(p_reset)>0:
      for p in p_reset:
        params.append(p.strip())
        source_server.reset_config(p)
        source_server.reload_config()
    else:
      for key in p_setting:
        params.append(str(key))
        source_server.config(key, p_setting[key])
        source_server.reload_config()
    msg = "Updated {0} params successfully.".format(params)
    restart_required_params = source_server.restart_required(list_params=params)
    if len(restart_required_params)>0:
      msg = msg + "\nRestart required for these params {0}.".format((restart_required_params))
    jsonDict['status'] = "success"
    source_server.close()
  except Exception as e:
    rc = 1
    msg = str(e)
    if msg.find('timeout expired') >= 0 or msg.find('timed out') >= 0:
      msg = 'Connection timed out'
    jsonDict['status'] = "error"
  if isJSON:
    jsonDict['msg'] = msg
    print (json.dumps([jsonDict]))
  else:
    print (msg)
  return rc


