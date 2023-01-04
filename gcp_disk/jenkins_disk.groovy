pipeline { 
  agent any
  options {
        ansiColor('xterm')
  }
  parameters {
    
    choice(name: 'ProjectID', choices: ['seraphic-ripple-372512', 'terraform-3325'], description: 'Select Project ID.')
    string(name: 'Diskname', defaultValue: '', description: 'Name of the Disk')
    string(name: 'Disksize', defaultValue: '', description: 'Please provide total disk size in GB')
    string(name: 'Instance', defaultValue: '', description: 'Name of the Instanace')
  }
  stages {
        stage('copy_credential') {
            steps {
                withCredentials([file(credentialsId: "gcloud_cred", variable: 'GC_KEY')]) {
                    sh """
                    cp $GC_KEY ${WORKSPACE}/gcp_disk/cred.json
                    """
                }   
            }
        }
        stage('execute_playbook'){
             steps{
             sh """
             cd ${WORKSPACE}/gcp_disk/
             python3 inventory.py
             ansible ${Instance} -i gcp_compute.yaml --list-hosts
             """
                input message: 'Please verify the Instance', ok: 'Approve'               
             sh """   
             cd ${WORKSPACE}/gcp_disk/
             ansible-playbook update_disk.yaml -i gcp_compute.yaml -l ${Instance} -e "disk_name=${Diskname} disk_size=${Disksize}"
             """
             } 
       } 
  }                 
    post {
        always {
        echo '###### cleaning WorkSpace #######'
        cleanWs notFailBuild: true, patterns: [[pattern: '**/cred.json', type: 'INCLUDE']]
        }
    }
      
}    
