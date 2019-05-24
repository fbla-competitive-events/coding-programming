####################################################################
#########      Copyright (c) 2016-2017 BigSQL           ############
####################################################################

from azure.mgmt.compute import ComputeManagementClient
from azure.common.client_factory import get_client_from_auth_file
from azure.mgmt.rdbms.postgresql import PostgreSQLManagementClient
from azure.mgmt.network import NetworkManagementClient
from azure.mgmt.resource import ResourceManagementClient
from azure.mgmt.storage import StorageManagementClient
from azure.common.credentials import ServicePrincipalCredentials
from VirtualMachines import AzureVM
from Database import AzurePG
from common import exit_message, print_verbose
from util import get_credentials_by_type

instance_type_class_mapping = {
    'vm': ComputeManagementClient,
    'db': PostgreSQLManagementClient
}

def get_credentials(cred):
    subscription_id = str(cred["subscription_id"])
    credentials = ServicePrincipalCredentials(
        client_id = str(cred["client_id"]),
        secret = str(cred["client_secret"]),
        tenant = str(cred["tenant_id"])
    )
    return credentials, subscription_id


class AzureConnection(object):
    def __init__(self, instance_type=None, region = None):
        cred_json = get_credentials_by_type(cloud_type="azure")
        if len(cred_json) > 0:
            credentials = cred_json[0]['credentials'] if 'credentials' in cred_json[0] else {}
        else:
            raise IOError("please add credentials for %s." % "azure")
        if not set(("subscription_id", "client_id", "client_secret", "tenant_id")).issubset(credentials):
            raise IOError("subscription_id, client_id, secret and tenant  are required in credentials for %s." % "azure")

        self.instance_type = instance_type
        credentials, subscription_id = get_credentials(credentials)
        self.client = instance_type_class_mapping.get(self.instance_type)(credentials, subscription_id)
        self.resource_client = ResourceManagementClient(credentials, subscription_id)
        self.networkClient = NetworkManagementClient(credentials, subscription_id)
        self.compute_client = ComputeManagementClient(credentials, subscription_id)
        self.storage_client = StorageManagementClient(credentials, subscription_id)
        self.header_keys = []
        self.header_titles = []
        self.isJson = False
        self.verbose = False
        self.region = region

    def get_list(self, filter_params={}):
        if self.instance_type == "vm":
            return self.get_vm_list(filter_params=filter_params)
        if self.instance_type == "db":
            return self.get_db_list(filter_params=filter_params)

    def get_db_list(self, filter_params={}):
        p_instance = filter_params.get("instance", "")
        self.header_keys = ['region', 'instance', 'status', 'db_class', 'engine_version']
        self.header_titles = ['Region', 'Instance', 'Status', 'Class', 'Version']
        db_list = []
        for pg in self.client.servers.list():
            db = AzurePG(pg)
            if p_instance > "":
                if p_instance != db.instance:
                    continue
            if self.verbose:
                db.set_extra_info(pg)
                print_verbose(db.__dict__, p_type="pg")
            db_list.append(db.__dict__)
        return db_list

    def get_vm_list(self, filter_params={}):
        p_instance = filter_params.get("instance", "")
        self.header_keys = ['region', 'name', 'type']
        self.header_titles = ['Region', 'Name', 'Instance Type']
        vms = []
        for avm in self.client.virtual_machines.list_all():
            vm = AzureVM(avm)
            if p_instance > "":
                if p_instance != vm.name:
                    continue
                else:
                    dm = self.client.virtual_machines.get(vm.resource_group, vm.name, expand="instanceView")
                    vm.set_extra_info(dm.__dict__)
                    for n in dm.network_profile.network_interfaces:
                        name = " ".join(n.id.split('/')[-1:])
                        sub = "".join(n.id.split('/')[4])
                        ips = self.networkClient.network_interfaces.get(sub, name)
                        vm.set_extra_info(ips.__dict__)
                        if vm.public_ip_address is None:
                            ip_reference = ips.ip_configurations[0].public_ip_address.id.split('/')
                            public_ip = self.networkClient.public_ip_addresses.get(ip_reference[4], ip_reference[8])
                            vm.public_ip_address = public_ip.ip_address

            if self.verbose:
                print_verbose(vm.__dict__, p_type="vm")
            vms.append(vm.__dict__)
        return vms

    def create_node(self, params={}):
        if self.instance_type == "vm":
            return self.create_vm(create_params=params)
        if self.instance_type == "db":
            return self.create_db(create_params=params)

    def create_db(self, create_params):
        try:
            for params in create_params:
                server_name = params["instance"]
                administrator_login = params["master_user"]
                administrator_login_password = params["password"]
                storage_mb = params.get("allocated_storage")
                version = params["engine_version"]
                group_name = params["group_name"]
                region = params["region"]
                ssl_mode = params.get("ssl_mode",'Disabled')
                start_ip = params.get("start_ip", None)
                end_ip = params.get("end_ip", None)
                publicly_accessible = params.get("publicly_accessible", None)

                db_params = {
                  'location': region,
                  'properties': {
                    'version': version,
                    'create_mode': 'Default',
                    'administrator_login': administrator_login,
                    'administrator_login_password': administrator_login_password,
                    'ssl_enforcement':ssl_mode
                  }
                }
                if storage_mb:
                  db_params["properties"]["storage_mb"] = storage_mb

                response = self.client.servers.create_or_update(group_name, server_name, db_params)
                response.wait()

                # Creating and adding firewall to server
                if publicly_accessible == "Yes":
                    firewall_response = self.client.firewall_rules.create_or_update(group_name, server_name,
                                                                                    server_name + "-default-firewall-rule",
                                                                                    '0.0.0.0', '255.255.255.255')
                elif start_ip and end_ip:
                    firewall_response = self.client.firewall_rules.create_or_update(group_name, server_name,
                                                                                    server_name + "-default-firewall-rule",
                                                                                    start_ip, end_ip)
                firewall_response.wait()
                return response

        except Exception as e:
            msg1 = "Error while creating postgres instance in azure "
            exit_message(msg1 + str(e), 1, self.isJson)

        return 0

    def create_network_interface(self, group_name, location, server_name):
        """Create a Network Interface for a VM.
        """
        vnet_name = "{0}-vn".format(server_name)
        # Create VNet
        async_vnet_creation = self.networkClient.virtual_networks.create_or_update(
            group_name,
            vnet_name,
            {
                'location': location,
                'address_space': {
                    'address_prefixes': ['10.0.0.0/16']
                }
            }
        )
        async_vnet_creation.wait()

        # Create Subnet
        snet_name = "{0}-subnet".format(server_name)
        async_subnet_creation = self.networkClient.subnets.create_or_update(
            group_name,
            vnet_name,
            snet_name,
            {'address_prefix': '10.0.0.0/24'}
        )
        subnet_info = async_subnet_creation.result()

        # Create NIC
        nic_name = "{0}-nic".format(server_name)
        async_nic_creation = self.networkClient.network_interfaces.create_or_update(
            group_name,
            nic_name,
            {
                'location': location,
                'ip_configurations': [{
                    'name': "res-group-ip-config",
                    'subnet': {
                        'id': subnet_info.id
                    }
                }]
            }
        )
        return async_nic_creation.result()

    def create_vm(self, create_params):
        try:

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

                result = self.create_network_interface(group_name, region,computer_name)

                vm_params = {
                    'location': region,
                    'os_profile': {
                        'computer_name': computer_name,
                        'admin_username': admin_username,
                        'admin_password': password
                    },
                    'hardware_profile': {
                        'vm_size': vm_size
                    },
                    'storage_profile': {
                        'image_reference': {
                            'publisher': publisher,
                            'offer': offer,
                            'sku': sku,
                            'version': version
                        },
                    },
                    'network_profile': {
                        'network_interfaces': [{
                            'id': result.id,
                        }]
                    },
                }

                response = self.client.virtual_machines.create_or_update(
                  group_name, computer_name, vm_params)
                return response

        except Exception as e:
            msg1 = "Error while creating VM in azure "
            exit_message(msg1 + str(e), 1, self.isJson)

        return 0

    def get_resource_group(self):
        res_groups = []
        result = self.resource_client.resource_groups.list()
        for group in result:
            if self.region and self.region != group.location:
                continue
            res_group = {}
            res_group['region'] = group.location
            res_group['name'] = group.name
            res_groups.append(res_group)
        return res_groups

    def get_instance_types(self, region):
        result = []
        response = self.client.virtual_machine_sizes.list(region)
        for size in response:
            result.append(size.__dict__)
        return result

    def storage_accounts(self, group=None):
        result = []
        response = self.storage_client.storage_accounts.list_by_resource_group(group)
        for size in response:
            resp = {}
            resp['name'] = size.name
            resp['location'] = size.location
            result.append(resp)
        return result