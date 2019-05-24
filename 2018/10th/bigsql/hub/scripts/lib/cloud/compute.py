####################################################################
######          Copyright (c)  2015-2017 OpenSCG          ##########
####################################################################
import os
from salt.cloud import CloudClient
from salt.client import LocalClient
import json
import sys

from util import get_credentials_by_type

PGC_HOME = os.getenv("PGC_HOME", "")
devops_lib_path = os.path.join(PGC_HOME, "pgdevops", "lib")
if os.path.exists(devops_lib_path):
    if devops_lib_path not in sys.path:
        sys.path.append(devops_lib_path)
    
cloud_file = os.path.join(PGC_HOME, "data", "etc", "cloud")

drivers = {
  'azure': 'azurearm',
  'aws': 'ec2',
  'vmware': 'vmware'
}

log_file = os.path.join(PGC_HOME, "data", "logs", "salt", "salt.log")
pki_dir = os.path.join(PGC_HOME, "data", "etc", "minion")


class ComputeNodes(object):

    def __init__(self, cloud="azure"):
        cred_json = get_credentials_by_type(cloud_type=cloud)
        if len(cred_json) > 0:
            credentials = cred_json[0]['credentials'] if 'credentials' in cred_json[0] else {}
        else:
            raise IOError("please add credentials for %s."%cloud)
        if cloud=="azure":
            if not set(("subscription_id", "client_id", "client_secret", "tenant_id")).issubset(credentials):
                raise IOError("subscription_id, client_id, secret and tenant  are required in credentials for %s." % cloud)
            provider_dict = {}
            provider_dict['driver'] = "azurearm"
            provider_dict['secret'] = str(credentials["client_secret"])
            provider_dict['client_id'] = str(credentials['client_id'])
            provider_dict['subscription_id'] = str(credentials["subscription_id"])
            provider_dict['tenant'] = str(credentials["tenant_id"])
            self.provider_opts = provider_dict

        elif cloud=="vmware":
            if not set(("user", "password", "url")).issubset(credentials):
                raise IOError("user, password, url are required in credentials for %s." % cloud)
            provider_dict = {}
            provider_dict['driver']="vmware"
            provider_dict['user'] = str(credentials["user"])
            provider_dict['password'] = str(credentials["password"])
            provider_dict['url'] = str(credentials["url"])
            self.provider_opts = provider_dict
        self.provider = "bigsql-provider"
        self.cloud = cloud
        self.header_keys = ['region', 'name', 'type', 'public_ips', 'private_ips', 'state']
        self.header_titles = ['Region', 'Name', 'Instance Type', 'Public Ips', 'Private Ips', 'state']

    def get_opts(self,region=None, group=None):
        if not os.path.exists("/tmp/salt/"):
            os.mkdir("/tmp/salt/")
        if not os.path.exists("/tmp/salt/cache"):
            os.mkdir("/tmp/salt/cache")
        if not os.path.exists("/tmp/salt/run"):
            os.mkdir("/tmp/salt/run")
        if not os.path.exists("/tmp/salt/run/master"):
            os.mkdir("/tmp/salt/run/master")
        mopts = {}
        mopts["cachedir"] = "/tmp/salt/cache"
        mopts["sock_dir"] = "/tmp/salt/run/master"
        mopts["transport"] = "zeromq"
        mopts["extension_modules"] = ""
        mopts["file_roots"] = {}
        lc = LocalClient(mopts=mopts)
        opts = lc.opts
        opts["parallel"] = True
        opts["pki_dir"] = pki_dir
        opts["extension_modules"] = ""
        opts["cachedir"] = "/tmp/salt/cloud"
        opts['log_level'] = "debug"
        opts['log_level_logfile'] = "debug"
        opts["log_file"] = log_file
        opts["update_cachedir"] = False
        opts["log_file"] = log_file
        opts["log_level_logfile"] = "debug"
        opts["log_file"] = log_file

        opts['providers'] = {}
        if self.cloud=="azure":
            if region:
                self.provider_opts['location'] = region
            if group:
                self.provider_opts['resource_group'] = group
            opts['providers'] = {
              self.provider: {
                'azurearm': self.provider_opts
              }
            }
        elif self.cloud=="vmware":
            opts['providers'] = {
                self.provider: {
                    'vmware': self.provider_opts
                }
            }
        elif self.cloud=="aws":
            opts['providers'] = {}
        return opts

    def get_aws_instance_info(self, vm_obj):
        # TODO: Need to implement getting the addtional info of aws
        vm = {}
        return vm

    def get_azure_instance_info(self, vm_obj):
        vm = {}
        vm['resource_group'] = vm_obj.get("resource_group")
        vm['state'] = vm_obj.get("provisioning_state")
        network_profile = vm_obj.get("network_profile")
        nics = network_profile.get("network_interfaces")
        public_ips = []
        private_ips = []

        for nic in nics:
            nw = nics.get(nic)
            ip_confs = nw.get("ip_configurations")
            for ip_conf in ip_confs:
                if ip_confs.get(ip_conf).get("public_ip_address"):
                    public_ip = str(ip_confs.get(ip_conf).get("public_ip_address").get("ip_address"))
                    if public_ip:
                        public_ips.append(public_ip)
                if ip_confs.get(ip_conf).get("private_ip_address"):
                    private_ip = str(ip_confs.get(ip_conf).get("private_ip_address"))
                    if private_ip:
                        private_ips.append(private_ip)

        vm['public_ips'] = public_ips
        vm['private_ips'] = private_ips
        vm['type'] = vm_obj.get("hardware_profile").get("vm_size")
        return vm

    def get_list(self, filter_params={}):
        vms = []
        driver_name = drivers.get(self.cloud)
        p_instance = filter_params.get("instance", "")
        opts = self.get_opts()
        cloud_client = CloudClient(opts=opts)

        if p_instance:
            list_nodes = cloud_client.action(fun="show_instance", instance=p_instance)
        else:
            list_nodes = cloud_client.action(fun="list_nodes_full", provider=self.provider)

        providers_nodes_list = list_nodes.get(self.provider)

        if providers_nodes_list and providers_nodes_list.get(driver_name):
            nodes = providers_nodes_list.get(driver_name)
            for name in nodes:
                vm_obj = nodes.get(name)
                vm = {}
                vm['id'] = vm_obj.get("id")
                vm['name'] = vm_obj.get("name")
                vm['region'] = vm_obj.get("location")
                vm['type'] = vm_obj.get("size")
                vm['state'] = vm_obj.get("state")
                vm['public_ips'] = vm_obj.get("public_ips")
                vm['private_ips'] = vm_obj.get("private_ips")
                if driver_name=="vmware":
                    vm['name'] = vm_obj.get("id")
                    vm['type'] = ""

                if p_instance:
                    if self.cloud == "azure":
                        add_obj = self.get_azure_instance_info(vm_obj)
                        vm.update(add_obj)

                vms.append(vm)
        return vms

    def create_node(self, create_params):
      from salt.log import setup_logfile_logger
      setup_logfile_logger(log_file, log_level="debug")
      result = {}
      if self.cloud=="azure":
        result = self.create_azure_vm(create_params=create_params)
      elif self.cloud=="vmware":
        result = self.create_vmware_vm(create_params=create_params)
      return result

    def create_azure_vm(self, create_params):
        response = {
            'state': 'complete',
            'msg': 'Instance created successfully.'
        }
        opts = self.get_opts()
        cloud_client = CloudClient(opts=opts)
        for params in create_params:
            computer_name = params["computer_name"]
            admin_username = params["admin_username"]
            password = params["password"]
            vm_size = params["vm_size"]
            publisher = params["publisher"]
            offer = params["offer"]
            sku = params["sku"]
            version = params["version"]
            group_name = params["group_name"]
            region = params["region"]
            if params.get("network_name"):
                network_name = params["network_name"]
            else:
                list_networks = self.list_networks(group_name)
                network_name=list_networks[0]['name']

            storage_account = params["storage_account"]

            image = "{0}|{1}|{2}|{3}".format(publisher, offer, sku, version)
            result = dict()
            # kwargs = dict()
            # kwargs['group'] = group_name
            # kwargs['network'] = network_name
            # list_subnets = cloud_client.action("list_subnets", provider=self.provider, kwargs=kwargs)
            # subnet_names = list_subnets.get(self.provider).get("azurearm").keys()
            # subnet_name = subnet_names[0]

            result = cloud_client.create(provider=self.provider, names=[computer_name],
                                         image=image,
                                         size=vm_size,
                                         ssh_username=admin_username,
                                         ssh_password=password,
                                         resource_group=group_name,
                                         location=region,
                                         public_ip=True,
                                         network_resource_group=group_name,
                                         storage_account=storage_account,
                                         network=network_name,
                                         # subnet=subnet_name,
                                         deploy=False)
            if computer_name in result and len(result[computer_name]) == 1 and 'Error' in result[computer_name]:
                response['state'] = 'failed'
                response['msg'] = result[computer_name]['Error']
                return response

            return response


    def create_vmware_vm(self, create_params):
        response = {
            'state' : 'complete',
            'msg' : 'Instance created successfully.'
        }
        opts = self.get_opts()
        cloud_client = CloudClient(opts=opts)
        for params in create_params:
            computer_name = params["computer_name"]
            admin_username = params["admin_username"]
            password = params["password"]
            cpus = params.get("num_cpus",1)

            result = dict()
            args = {
                "provider":self.provider,
                "names":[computer_name],
                "ssh_username":admin_username,
                "password":password,
                "num_cpus":cpus,
                "public_ip":True,
                "deploy":False
            }
            if params.get("vm_size"):
                args["size"] = params["cm_size"]
            if params.get("clonefrom"):
                args['clonefrom'] = params["clonefrom"]

            result = cloud_client.create(**args)
            if computer_name in result and len(result[computer_name]) == 1 and 'Error' in result[computer_name]:
                response['state'] = 'failed'
                response['msg'] = result[computer_name]['Error']
                return response
            return response

    def storage_accounts(self, group):
        result = []
        opts = self.get_opts(group=group)
        cloud_client = CloudClient(opts=opts)
        driver_name = drivers.get(self.cloud)
        response = cloud_client.action(fun="list_storage_accounts", provider=self.provider)
        accounts_response = response.get(self.provider)

        if accounts_response.get(driver_name):
            accounts = accounts_response.get(driver_name)
            for account in accounts:
                storage_account = accounts.get(account)
                resp = {}
                resp['name'] = storage_account.get('name')
                resp['location'] = storage_account.get('location')
                result.append(resp)
        return result

    def get_instance_types(self, region):
        result = []
        opts = self.get_opts(region=region)
        cloud_client = CloudClient(opts=opts)
        driver_name = drivers.get(self.cloud)
        response = cloud_client.action(fun="avail_sizes", provider=self.provider)
        sizes_response = response.get(self.provider)
        if sizes_response.get(driver_name):
            sizes = sizes_response.get(driver_name)
            for size in sizes:
                result.append(sizes[size])
        return result

    def list_networks(self, group):
        result = []
        opts = self.get_opts(group=group)
        cloud_client = CloudClient(opts=opts)
        driver_name = drivers.get(self.cloud)
        response = cloud_client.action(fun="list_networks", provider=self.provider, kwargs={'group':group})
        nw_response = response.get(self.provider)
        if nw_response.get(driver_name):
            nws = nw_response.get(driver_name)
            for nw in nws:
                resp = {}
                resp['name'] = nw
                result.append(resp)
        return result

    def subnet_groups(self, res_group=None):
        result = []
        opts = self.get_opts()
        cloud_client = CloudClient(opts=opts)
        driver_name = drivers.get(self.cloud)
        response = cloud_client.action(fun="list_networks", provider=self.provider, kwargs={'group':res_group})
        network_response = response.get(self.provider)
        if network_response.get(driver_name):
            networks = network_response.get(driver_name)
            for network in networks:
                nw_response = {}
                nw_response['vpc'] = network
                #nw_response['subnet_group'] = []
                for subnet in networks[network].get('subnets'):
                    nw_response['subnet_group'] = subnet
                result.append(nw_response)
        return result
