####################################################################
#########      Copyright (c) 2016-2017 BigSQL           ############
####################################################################
from common import convert_tz, get_obj_value, convert
from cloud_mapping import vm_list_dict
import datetime

class VirtualMachine(object):

    def __init__(self):
        self.id = ""
        self.name = ""
        self.type = ""
        self.region = ""
        self.state = ""
        self.public_ips = ""
        self.private_ips = ""

    def get_common_attr(self, vm_object):
        self.id = vm_object.get("id")
        self.name = vm_object.get("name")
        self.region = vm_object.get("location")
        self.type = vm_object.get("size")
        self.state = vm_object.get("state")
        self.public_ips = vm_object.get("public_ips")
        self.private_ips = vm_object.get("private_ips")


class AwsVM(VirtualMachine):

    def __init__(self, vm_object, info=False):
        VirtualMachine.__init__(self)
        self.type = vm_object.get('InstanceType')
        self.id = vm_object.get("InstanceId")
        self.name = ""
        self.private_ips = vm_object.get("PrivateIpAddress")
        self.public_ips = vm_object.get("PublicIpAddress")
        self.state = vm_object.get("State").get("Name")
        if vm_object.get('Tags'):
            for tag in vm_object.get('Tags'):
                if tag['Key'] == "Name":
                    self.name = tag['Value']
                    break

    def set_extra_info(self, vm_object, cloud_type="aws"):
        for k in vm_list_dict.keys():
            if hasattr(self, k):
                continue
            param = vm_list_dict.get(k)
            param_value = ""
            if param.get(cloud_type):
                cloud_keys = param.get(cloud_type)['key']
                if len(cloud_keys)>0:
                    param_value = get_obj_value(vm_object, cloud_keys)
                    if isinstance(param_value, datetime.datetime):
                        try:
                            param_value = convert_tz(str(param_value))
                        except Exception as e:
                            param_value = ""
                    if k == "security_groups":
                        new_list_value = []
                        for p in param_value:
                            new_param_dict = {}
                            new_param_dict["group_name"] = p.get("GroupName")
                            new_param_dict["group_id"] = p.get("GroupId")
                            new_list_value.append(new_param_dict)
                        param_value=new_list_value
                    if k == "tags":
                        new_list_value = []
                        for p in param_value:
                            new_list_value.append(p.get("Value"))
                        param_value = new_list_value

                if not hasattr(self, k):
                    setattr(self, k, param_value)


class AzureVM(VirtualMachine):

    def __init__(self, vm_object, info=False):
        VirtualMachine.__init__(self)
        self.id = getattr(vm_object, "id")
        self.name = getattr(vm_object, "name")
        self.region = getattr(vm_object, "location")
        hardware_profile = getattr(vm_object, "hardware_profile")
        self.type = getattr(hardware_profile, "vm_size")
        id = getattr(vm_object, "id").split("/")
        self.resource_group = id[4].strip()

    def set_extra_info(self, vm_object, cloud_type="azure"):
        if vm_object.get("ip_configurations"):
            for ip in vm_object.get("ip_configurations"):
                self.public_ip_address =  ip.public_ip_address.ip_address
                self.private_ip_address = ip.private_ip_address

        if vm_object.get("instance_view") and hasattr(vm_object.get("instance_view"), "statuses"):
            self.state = vm_object.get("instance_view").statuses[1].display_status

        if vm_object.get("os_profile"):
            self.computer_name = vm_object.get("os_profile").computer_name
            self.admin_username = vm_object.get("os_profile").admin_username

        if vm_object.get("provisioning_state"):
            self.provisioning_state = vm_object.get("provisioning_state")
