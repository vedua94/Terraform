pipeline { 
  agent any
  options {
        ansiColor('xterm')
  }
  parameters {
    
    choice(name: 'ProjectID', choices: ['seraphic-ripple-372512', 'terraform-3325'], description: 'Select Project ID.')
    choice(name: 'Zone', choices: ['us-central1-a', 'us-central1-b'], description: 'Select zone.')
    string(name: 'Diskname', defaultValue: '', description: 'Name of the Disk')
    string(name: 'Disksize', defaultValue: '', description: 'Name of the Disk')
  }
  stages {
        stage('copy_credential') {
            steps {
                withCredentials([file(credentialsId: "gcloud_cred", variable: 'GC_KEY')]) {
                    sh """
                    sudo cp $GC_KEY ${WORKSPACE}/gcp_disk/cred.json
                    """
                }   
            }
        }
        stage('execute_playbook'){
             steps{
             sh """
             cd ${WORKSPACE}/gcp_disk/
             sudo ansible-playbook update_disk.yaml -e "disk_project=${ProjectID} disk_zone=${Zone} disk_name=${Diskname} disk_size=${Disksize}"
             """
             } 
       } 
  }                 
    post {
        always {
        echo '###### cleaning WorkSpace #######'
        cleanWs notFailBuild: true, patterns: [[pattern: '**/creds.json', type: 'INCLUDE']]
        }
    }
      
}    
