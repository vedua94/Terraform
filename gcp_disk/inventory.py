import os
import sys

workspace=os.environ.get('WORKSPACE')
projects_var=''

try:
    projectID=os.environ.get('ProjectID')
    
except:
    print('Error: One of the required parameters is not provided')
    sys.exit()

with open (f"{workspace}/gcp_disk/gcp_compute.yaml", "w") as inventory:
    inventory_string=f"""plugin: gcp_compute
projects: {projectID}
service_account_file: cred.json
auth_kind: serviceaccount
scopes:
  - 'https://www.googleapis.com/auth/cloud-platform'
  - 'https://www.googleapis.com/auth/compute.readonly'
hostnames:
  - name
compose:
  ansible_host: networkInterfaces[0].networkIP
keyed_groups:
  - key: project
    prefix: ""
    separator: ""
  - key: zone
    prefix: ""
    separator: ""
leading_separator: false
"""
    inventory.write(inventory_string)