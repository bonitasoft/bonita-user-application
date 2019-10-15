pipeline {
    agent any
    options {
        timestamps()
    }
    stages {
        stage('Build and deploy') {
            steps {
                sh("./mvnw deploy -DskipTests -DaltDeploymentRepository=${env.ALT_DEPLOYMENT_REPOSITORY_TAG}")
            }
        }
    }
}