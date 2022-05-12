
properties([[$class: 'BuildDiscarderProperty', strategy: [$class: 'LogRotator', artifactDaysToKeepStr: '', artifactNumToKeepStr: '', daysToKeepStr: '', numToKeepStr: '5']]])

timestamps {
    ansiColor('xterm') {
        node {
            stage('Setup') {
                checkout scm
            }

            stage('Build') {
                try {
                    def currentBranch = env.BRANCH_NAME
                    def isBaseBranch = currentBranch == 'master' || currentBranch == 'dev' || currentBranch?.startsWith('release-') || currentBranch?.matches('7\\..+\\.x')
                    configFileProvider([configFile(fileId: 'maven-settings', variable: 'MAVEN_SETTINGS')]) {
                        if(!isBaseBranch){
                            sh "./mvnw -s ${MAVEN_SETTINGS} --no-transfer-progress -B clean verify -Djvm=${env.JAVA_HOME_11}/bin/java"
                        }else{
                            sh "./mvnw -s ${MAVEN_SETTINGS} --no-transfer-progress -B clean deploy -Djvm=${env.JAVA_HOME_11}/bin/java -DaltDeploymentRepository=${env.ALT_DEPLOYMENT_REPOSITORY_SNAPSHOTS}"
                        }
                    }
                    archiveArtifacts '**/target/bonita-user-application-*.bos'
                } finally {
                    junit allowEmptyResults : true, testResults: '**/target/surefire-reports/*.xml'
                }
            }
        }
    }
}
