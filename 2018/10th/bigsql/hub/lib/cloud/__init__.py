####################################################################
#########      Copyright (c) 2016-2017 BigSQL           ############
####################################################################

import os
import sys

devops_lib_path = os.path.join(os.getenv("PGC_HOME"), "pgdevops", "lib")
if os.path.exists(devops_lib_path):
    if devops_lib_path not in sys.path:
        sys.path.append(devops_lib_path)

from awscloud import AwsConnection
from azurecloud import AzureConnection

class_mapping = {
    'aws': AwsConnection,
    'azure': AzureConnection
}


class CloudConnection(object):

    def __init__(self, cloud=None):
        self.cloud = cloud
        pass

    def get_client(self, instance_type=None, region=None):
        if instance_type == "vm" and self.cloud in ("azure", "vmware"):
            from compute import ComputeNodes
            return ComputeNodes(cloud=self.cloud)
        else:
            if region:
                return class_mapping.get(self.cloud)(instance_type=instance_type, region=region)
            else:
                return class_mapping.get(self.cloud)(instance_type=instance_type)

