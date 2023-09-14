pipeline {
    agent any
    options {
        timestamps()
        ansiColor('xterm')
        buildDiscarder(logRotator(numToKeepStr: '3'))
    }
    environment {
        JAVA_HOME = "${env.JAVA_HOME_11}"
        JAVA_TOOL_OPTIONS = ''
        MAVEN_OPTS = '-Dstyle.color=always -Djansi.passthrough=true'
    }
    stages {
        stage('Build') {
            steps {
                configFileProvider([configFile(fileId: 'maven-settings', variable: 'MAVEN_SETTINGS')]) {
                    script {
                        try {
                            sh("./mvnw -s $MAVEN_SETTINGS --no-transfer-progress -B verify")
                        } finally {
                            archiveArtifacts artifacts: '**/target/bonita-user-application-*.bos'
                        }
                    }
                }
            }
        }
        stage('Deploy') {
            when {
                expression { return env.BRANCH_NAME == 'master' || env.BRANCH_NAME == 'dev' || env.BRANCH_NAME?.startsWith('release-') || env.BRANCH_NAME?.matches('9\\..+\\.x') }
            }
            steps {
                configFileProvider([configFile(fileId: 'maven-settings', variable: 'MAVEN_SETTINGS')]) {
                    // On artifactory (private)
                    sh("./mvnw -s $MAVEN_SETTINGS --no-transfer-progress -B deploy -DaltDeploymentRepository=${env.ALT_DEPLOYMENT_REPOSITORY_SNAPSHOTS}")
                    // On ossrh snapshots (public)
                    sh("./mvnw -s $MAVEN_SETTINGS --no-transfer-progress -B deploy -Possrh")
                }
            }
        }
    }
}
