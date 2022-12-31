pipeline {
  agent any

  parameters {
    choice(name: 'ENV', choices: ['sb'], description: 'Select the environment to deploy into.')
    choice(name: 'TYPE', choices: ['cluster', 'customer'], description: 'Select whether this is a cluster deployment or a customer deployment.')
    string(name: 'CLUSTERNAME', defaultValue: '', description: 'Name of the cluster to run')
    string(name: 'CUSTOMERNAME', defaultValue: '', description: 'Name of the customer to run')
    choice(name: 'ACTION', choices: ['plan', 'apply', 'destroy'], description: 'Run terraform plan or terraform apply')
  }
  stages {
        stage('copy_credential') {
            steps {
                withCredentials([file(credentialsId: "gcloud_cred", variable: 'GC_KEY')]) {
                    sh """
                    cp $GC_KEY ${WORKSPACE}/cred.json
                    """
                }   
            }
        }
        stage('terraform_task'){
            steps {
                sh """
                terraform init 
                terraform plan -out=plan.tfplan
                """
                input(message: 'Click "proceed" to approve the above Terraform plan')
                sh """
                terraform apply --auto-approve
                """
            }
        }
   }
   post {
        always {
            echo '###### cleaning WorkSpace #######'
            cleanWs notFailBuild: true, patterns: [[pattern: '/creds.json', type: 'INCLUDE']]
        }
    }    
}
