pipeline { 
  agent any
  options {
        ansiColor('xterm')
  }
  parameters {
    
    choice(name: 'ProjectID', choices: ['seraphic-ripple-372512', 'terraform-3325'], description: 'Select Project ID.')
    string(name: 'Diskname', defaultValue: '', description: 'Name of the Disk')
  }
  stages {
        
        stage('execute_playbook'){
             steps{
             sh """
             echo ${WORKSPACE}
             cd variable_test
             chmod 755 task.sh
             bash task.sh
             """
             } 
       }
  }                 
      
}    