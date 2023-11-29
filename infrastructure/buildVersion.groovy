pipeline {
    agent any
    options {
        timestamps()
        ansiColor('xterm')
        buildDiscarder(logRotator(numToKeepStr: '5'))
    }
    environment {
      JAVA_TOOL_OPTIONS = ''
      MAVEN_OPTS = '-Dstyle.color=always -Djansi.passthrough=true'
      GPG_PASSPHRASE = credentials('gpg-passphrase')
      JAVA_HOME = "${env.JAVA_HOME_17}"
    }
    stages {
        stage('Deploy to Artifactory') {
            steps {
                configFileProvider([configFile(fileId: 'maven-settings', variable: 'MAVEN_SETTINGS')]) {
                    sh("./mvnw --no-transfer-progress -B deploy -Dgpg.passphrase=\$GPG_PASSPHRASE -Prelease -DaltDeploymentRepository=${env.ALT_DEPLOYMENT_REPOSITORY_STAGING}")
                }
            }
        }
        stage('Upload BOS File') {
            steps {
                withCredentials([string(credentialsId: 'github-api', variable: 'GITHUB_API_TOKEN')]) {
                    sh "./infrastructure/upload-github-release-asset.sh github_api_token=$GITHUB_API_TOKEN tag=${params.version} filename=./target/bonita-user-application-${params.version}.bos" 
                }
            }
        }
    }
}
