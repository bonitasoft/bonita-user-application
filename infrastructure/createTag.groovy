#!/usr/bin/env groovy

pipeline {
    agent any
    options {
        timestamps()
        skipDefaultCheckout true
    }
    stages {
        stage('Init') {
            steps{
                script{
                  git branch: params.BASE_BRANCH, url: 'git@github.com:bonitasoft/bonita-user-application.git'
                }
            }

        }
        stage('Tag') {
            steps {
                script {
                    sh "./infrastructure/release.sh ${params.TAG_NAME} true"
                }
            }
        }
    }
}