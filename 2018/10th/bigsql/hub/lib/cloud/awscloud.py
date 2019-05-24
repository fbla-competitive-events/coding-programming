####################################################################
#########      Copyright (c) 2016-2017 BigSQL           ############
####################################################################

import boto3
from VirtualMachines import AwsVM
from Database import AwsRDS
from common import message, exit_message, print_verbose
instance_type_mapping = {
    "db" : "rds",
    "vm" : "ec2"
}
from util import get_credentials_by_type

def get_client(instance_type,region):
    cred_json = get_credentials_by_type(cloud_type="aws")
    if region:
        if len(cred_json) > 0:
            credentials = cred_json[0]['credentials']
            return boto3.client(instance_type, aws_access_key_id=credentials['access_key_id'],
                                aws_secret_access_key=credentials["secret_access_key"], region_name=region)
        else:
            return boto3.client(instance_type, region_name=region)
    else:
        try:
            if len(cred_json) > 0:
                credentials = cred_json[0]['credentials']
                return boto3.client(instance_type, aws_access_key_id=credentials['access_key_id'], aws_secret_access_key=credentials["secret_access_key"])
            else:
                return boto3.client(instance_type)
        except Exception as ex:
            return None

class AwsConnection(object):

    def __init__(self, instance_type=None, region = None):
        self.instance_type = instance_type_mapping.get(instance_type)
        self.header_keys = []
        self.header_titles = []
        self.isJson = False
        self.verbose = False
        self.client = get_client(self.instance_type, region)

    def get_list(self, filter_params={}):
        if self.instance_type == "rds":
            return self.get_rds_list(filter_params=filter_params)
        elif self.instance_type == "ec2":
            return self.get_vm_list(filter_params=filter_params)

    def get_rds_list(self, filter_params={}):
        self.header_keys = ['region', 'instance', 'status', 'db_class', 'engine_version']
        self.header_titles = ['Region', 'Instance', 'Status', 'Class', 'Version']
        p_region = filter_params.get("region", "")
        p_instance = filter_params.get("instance", "")
        rds_regions = []
        available_rds_regions = boto3.session.Session().get_available_regions("rds")
        if p_region > "":
            if p_region in available_rds_regions:
                rds_regions = [p_region]
            else:
                msg = str(p_region) + " is not a valid region for rds."
                exit_message(msg, 1, self.isJson)
        else:
            rds_regions = available_rds_regions

        # get all of the postgres db instances
        pg_list = []
        for region in rds_regions:
            msg = "Searching " + region + "..."
            message(msg, "info", self.isJson)
            rds = get_client('rds', region=region)
            dbs = rds.describe_db_instances()
            for db in dbs['DBInstances']:
                if db['Engine'] in ["postgres"]:
                    if p_instance > "":
                        if p_instance != db['DBInstanceIdentifier']:
                            continue
                    rds = AwsRDS(db)
                    rds.region = region
                    if p_instance:
                        extras_args = {}
                        extras_args['ec2'] = get_client('ec2', region=region)
                        rds.set_extra_info(db, extras_args)
                    if self.verbose:
                        print_verbose(rds.__dict__, p_type="pg")
                    pg_list.append(rds.__dict__)
        return pg_list


    def get_vm_list(self, filter_params={}):
        self.header_keys = ['region', 'name', 'type', 'public_ips', 'private_ips', 'state']
        self.header_titles = ['Region', 'Name', 'Instance Type', 'Public Ips', 'Private Ips', 'state']
        p_region = filter_params.get("region", "")
        p_instance = filter_params.get("instance", "")
        info = False
        if p_instance:
            info = True
        ec2_regions = []
        available_ec2_regions = boto3.session.Session().get_available_regions(self.instance_type)
        if p_region > "":
            if p_region in available_ec2_regions:
                ec2_regions = [p_region]
            else:
                msg = str(p_region) + " is not a valid region for ec2."
                exit_message(msg, 1, self.isJson)
        else:
            ec2_regions = available_ec2_regions

        ec2_hosts = []
        for region in ec2_regions:
            try:
                msg = "Searching " + region + "..."
                message(msg, "info", self.isJson)
                ec2 = get_client('ec2', region=region)
                hosts = ec2.describe_instances()
                for host in hosts['Reservations']:
                    for h in host['Instances']:
                        if p_instance > "":
                            if p_instance != h['InstanceId']:
                                continue
                        vm = AwsVM(h)
                        vm.region = region
                        if p_instance:
                            vm.set_extra_info(h)
                        if self.verbose:
                            print_verbose(vm.__dict__)
                        ec2_hosts.append(vm.__dict__)
            except Exception as e:
                pass

        return ec2_hosts

    def create_node(self, p_dict):
        if self.instance_type == "rds":
            return self.create_db(p_dict)
        elif self.instance_type == "ec2":
            return self.create_vm(p_dict)

    def create_db(self, p_dict):
        ### optional parms #############
        engine = "postgres"
        multi_az = False
        public_access = False
        storage_encrypted = False
        monitoring_interval = 0  # Valid Values: 0, 1, 5, 10, 15, 30, 60

        ### mandatory parms ############
        for d in p_dict:
            try:
                p_region = d['region']
                db_params = {}
                db_params['DBName'] = d['db_name']
                db_params['DBInstanceClass'] = d['db_class']
                db_params['DBInstanceIdentifier'] = d['instance']
                db_params['MasterUsername'] = d['master_user']
                db_params['MasterUserPassword'] = d['password']
                #db_params['DBSubnetGroupName'] = d['subnet_group']
                db_params['Engine'] = d.get("engine", engine)
                db_params['MultiAZ'] = d.get("multi_az", multi_az)
                db_params['PubliclyAccessible'] = d.get("public_accessible", public_access)
                db_params['StorageEncrypted'] = d.get("storage_encrypted", storage_encrypted)
                db_params['MonitoringInterval'] = d.get("monitoring_interval", monitoring_interval)

                s_port = str(d['port'])
                try:
                    db_params['Port'] = int(s_port)
                except Exception as e:
                    exit_message("port must be an integer", 1, self.isJson)

                db_params['StorageType'] = d['storage_type']
                s_allocated_storage = str(d['allocated_storage'])
                try:
                    db_params['AllocatedStorage'] = int(s_allocated_storage)
                except Exception as e:
                    exit_message("allocated storage GB must be an integer", 1, self.isJson)

                # All optional Params
                if d.get("availability_zone"):
                    db_params['AvailabilityZone'] = d.get("availability_zone")

                if d.get("security_groups"):
                    # List of security groups
                    db_params['DBSecurityGroups'] = d.get("security_groups")

                if d.get("vpc_security_group_ids"):
                    # List of VPC security groups
                    db_params['VpcSecurityGroupIds'] = d.get("vpc_security_group_ids")

                if d.get("maintenance_window"):
                    db_params['PreferredMaintenanceWindow'] = d.get("maintenance_window")

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
                exit_message("missing required field " + str(e), 1, self.isJson)

        try:
            response = get_client("rds", p_region).create_db_instance(**db_params)
            return response
        except Exception as e:
            msg1 = "rds.create_db_instance(): "
            exit_message(msg1 + str(e), 1, self.isJson)
            
        return (0)

    def create_vm(self, p_dict):
        response = {
            'state': 'complete',
            'msg': 'Instance created successfully.'
        }
        for d in p_dict:
            p_region = d['region']
            ec2_params = {}
            ec2_params['MinCount'] = 1
            ec2_params['MaxCount'] = 1

            try:
                ec2_params['ImageId'] = d['image_id']
                ec2_params['InstanceType'] = d['instance_type']
            except Exception as e:
                exit_message("missing required field " + str(e), 1, self.isJson)

            if d.get("kernel_id"):
                ec2_params['KernelId'] = d.get("kernel_id")

            if d.get("keyname"):
                ec2_params['KeyName'] = d.get("key_name")

            if d.get("monitoring"):
                ec2_params['Monitoring'] = d.get("monitoring")

            if d.get("ramdisk_id"):
                ec2_params['RamdiskId'] = d.get("ram_disk_id")

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
                ec2_params['DryRun'] = d.get("dry_run")

            if d.get("ebs_optimized"):
                ec2_params['EbsOptimized'] = d.get("ebs_optimized")

            if d.get("shutdown_behaviour"):
                ec2_params['InstanceInitiatedShutdownBehavior'] = d.get("shutdown_behaviour")

            if d.get("private_ip_address"):
                ec2_params['PrivateIpAddress'] = d.get("private_ip_address")

            if d.get("tags"):
                tags = []
                for tag in d.get("tags").keys():
                    tags.append({'Key': tag, "Value": d.get("tags").get(tag)})
                ec2_params['TagSpecifications'] = [{
                    "ResourceType": "instance",
                    "Tags": tags
                }]

        try:
            cred_json = get_credentials_by_type(cloud_type="aws")
            if len(cred_json) > 0:
                credentials = cred_json[0]['credentials']
                ec2 = boto3.resource("ec2", aws_access_key_id=credentials['access_key_id'],
                                    aws_secret_access_key=credentials["secret_access_key"],region_name=p_region)
            else:
                ec2 = boto3.resource("ec2",region_name=p_region)
            instance = ec2.create_instances(**ec2_params)
        except Exception as e:
            response['state'] = 'failed'
            response['msg'] = "ec2.create_instances(): " + str(e)
            return response

        return response

    def instance_class(self, p_version=None, db_instance_meta_list=[]):
        result = []
        db_instances = self.client.describe_orderable_db_instance_options(Engine="postgres",
                                                                          EngineVersion=p_version)
        instance_classes = []
        for d in db_instances.get("OrderableDBInstanceOptions"):
            meta_list_result = db_instance_meta_list
            if d.get("DBInstanceClass") not in instance_classes:
                instance_class = {}
                instance_class['DBInstanceClass'] = d.get("DBInstanceClass")
                instance_class["iops"] = ""
                instance_class["v_cpu"] = ""
                instance_class["network_performance"] = ""
                instance_class["memory"] = ""
                for i in meta_list_result:
                    if d.get("DBInstanceClass") == i.get("instance", ""):
                        instance_class["iops"] = i.get("iops", "")
                        instance_class["v_cpu"] = i.get("v_cpu", "")
                        instance_class["network_performance"] = i.get("network_performance", "")
                        instance_class["memory"] = i.get("memory", "")
                        break
                result.append(instance_class)
                instance_classes.append(d.get("DBInstanceClass"))
        return result

    def rds_versions(self):
        result = []
        rds_engine_versions = self.client.describe_db_engine_versions(Engine="postgres")
        db_option_groups = self.client.describe_option_groups(EngineName="postgres")
        option_groups_list = db_option_groups.get("OptionGroupsList")
        db_engine_versions = rds_engine_versions.get("DBEngineVersions")
        db_parameter_groups = self.client.describe_db_parameter_groups()
        for ver in db_engine_versions:
            version = {}
            version['EngineVersion'] = ver.get("EngineVersion")
            version['DBEngineVersionDescription'] = ver.get("DBEngineVersionDescription")
            param_groups = []
            groups = db_parameter_groups.get("DBParameterGroups")
            for group in groups:
                if ver.get("DBParameterGroupFamily") == group.get("DBParameterGroupFamily"):
                    param_group = {}
                    param_group['DBParameterGroupFamily'] = group.get("DBParameterGroupFamily")
                    param_group['DBParameterGroupName'] = group.get("DBParameterGroupName")
                    param_group['DBParameterGroupArn'] = group.get("DBParameterGroupArn")
                    param_groups.append(param_group)
            version['DBParameterGroups'] = param_groups
            option_groups = []
            for option_group in option_groups_list:
                if ver.get("EngineVersion").startswith(option_group.get("MajorEngineVersion")):
                    option = {}
                    option['OptionGroupName'] = option_group.get("OptionGroupName")
                    option['OptionGroupArn'] = option_group.get("OptionGroupArn")
                    option_groups.append(option)
            version['OptionGroups'] = option_groups
            result.append(version)
        result = sorted(result, key=lambda t: t["EngineVersion"], reverse=True)
        return result

    def subnet_groups(self, res_group=None):
        result = []
        if self.instance_type == 'rds':
            subnet_groups = self.client.describe_db_subnet_groups()
            vpc_groups = subnet_groups.get("DBSubnetGroups")
        else:
            subnet_groups = self.client.describe_subnets()
            vpc_groups = subnet_groups.get("Subnets")
        for vpc_group in vpc_groups:
            group = {}
            group['vpc'] = vpc_group.get("VpcId")
            group['zones'] = []
            if self.instance_type == 'rds':
                group['subnet_group'] = vpc_group.get("DBSubnetGroupName")
                vpc_subnets = vpc_group.get("Subnets", [])
                for subnet in vpc_subnets:
                    zone = {}
                    zone['name'] = subnet.get("SubnetAvailabilityZone").get("Name")
                    zone['identifier'] = subnet.get("SubnetIdentifier")
                    group['zones'].append(zone)
            else:
                group['subnet_group'] = vpc_group.get("SubnetId")
                zone = {}
                zone['name'] = vpc_group.get("AvailabilityZone")
                zone['identifier'] = vpc_group.get("SubnetId")
                group['zones'].append(zone)
            result.append(group)
        return result

