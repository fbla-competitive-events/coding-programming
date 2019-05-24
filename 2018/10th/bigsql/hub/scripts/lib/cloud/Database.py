####################################################################
#########      Copyright (c) 2016-2017 BigSQL           ############
####################################################################

from common import get_field, convert_tz, get_obj_value
from cloud_mapping import pg_list_dict
import datetime

class Database(object):

    def __init__(self):
        self.id = ""
        self.instance = ""
        self.user = ""
        self.db_name = ""
        self.host = ""
        self.region = ""
        self.status = ""
        self.db_class = ""
        self.engine_version = ""
        self.storage_allocated = ""
        self.port = ""

    def _set_extra_info(self, db_object, cloud_type=None):
        for k in pg_list_dict.keys():
            if hasattr(self, k):
                continue
            param = pg_list_dict.get(k)
            param_value = ""
            if param.get(cloud_type):
                cloud_keys = param.get(cloud_type)['key']
                if len(cloud_keys) > 0:
                    param_value = get_obj_value(db_object, cloud_keys)
                    if isinstance(param_value, datetime.datetime):
                        try:
                            param_value = convert_tz(str(param_value))
                        except Exception as e:
                            param_value = ""
                if not hasattr(self, k):
                    setattr(self, k, param_value)


class AwsRDS(Database):

    def __init__(self, db_object, info=False):
        Database.__init__(self)
        self.id = get_field(db_object, 'DBInstanceArn')
        self.instance = get_field(db_object, 'DBInstanceIdentifier')
        self.user = get_field(db_object, 'MasterUsername')
        self.db_name = get_field(db_object, 'DBName')
        self.host = get_field(db_object, 'Endpoint', 'Address')
        self.status = get_field(db_object, 'DBInstanceStatus')
        self.db_class = get_field(db_object, 'DBInstanceClass')
        self.engine_version = get_field(db_object, 'EngineVersion')
        self.storage_allocated = get_field(db_object, 'AllocatedStorage')
        self.port = get_field(db_object, 'Endpoint', 'Port')

    def set_extra_info(self, db_object, extra_args=None):
        ec2 = extra_args.get("ec2")
        db = db_object
        pg_dict = {}

        storage_type = get_field(db, 'StorageType')
        if storage_type == 'standard':
            pg_dict['storage_type'] = storage_type + ": Magnetic"
        elif storage_type == 'gp2':
            pg_dict['storage_type'] = storage_type + ": General Purpose (SSD)"
        elif storage_type == 'io2':
            pg_dict['storage_type'] = storage_type + ": Provisioned IOPS (SSD)"
        else:
            pg_dict['storage_type'] = storage_type

        pg_dict['vpc'] = ""
        try:
            vpc_id = get_field(db, 'DBSubnetGroup', 'VpcId')
            pg_dict['vpc'] = vpc_id
            my_vpcs = ec2.describe_vpcs(VpcIds=[vpc_id, ])
            for my_vpc in my_vpcs['Vpcs']:
                for tag in my_vpc['Tags']:
                    if tag['Key'] == "Name":
                        vpc_name = tag['Value']
                        pg_dict['vpc'] = vpc_name
                        break
        except Exception as e:
            pass

        for d in pg_dict.keys():
            if not hasattr(self, d):
                setattr(self, d, pg_dict[d])
        self._set_extra_info(db_object, cloud_type="aws")

class AzurePG(Database):

    def __init__(self, db_object, info=False):
        Database.__init__(self)
        self.id = db_object.id
        self.instance = db_object.name
        self.user = "{0}@{1}".format(db_object.administrator_login, db_object.name)
        self.db_name = "postgres"
        self.host = db_object.fully_qualified_domain_name
        self.region = db_object.location
        self.status = db_object.user_visible_state
        self.db_class = db_object.sku.name
        self.engine_version = db_object.version
        self.storage_allocated = int(db_object.storage_mb/1024)
        self.port = 5432
        id = db_object.id.split("/")
        self.resource_group = id[4].strip()

    def set_extra_info(self, db_object):
        self._set_extra_info(db_object, cloud_type="azure")
        pass
