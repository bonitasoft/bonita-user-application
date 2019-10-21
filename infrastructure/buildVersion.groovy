pipeline {
    agent any
    options {
        timestamps()
    }
    stages {
        stage('Build and deploy') {
            steps {
                sh "./mvnw deploy -DskipTests -DaltDeploymentRepository=${env.ALT_DEPLOYMENT_REPOSITORY_TAG}"
                withCredentials([string(credentialsId: 'github-api', variable: 'GITHUB_API_TOKEN')]) {
                    sh "./infrastructure/upload-github-release-asset.sh github_api_token=$GITHUB_API_TOKEN tag=${params.version} filename=./target/bonita-user-application-${params.version}.bos" 
                }
               
            }
        }
    }
}
