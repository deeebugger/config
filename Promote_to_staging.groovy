#!/usr/local/bin/groovy

pipeline {
  agent any

  stages {
    stage('set variables') {
      steps {
        timestamps {
          script {
            echo 'starting set up Global variables'
            ENVIRONMENT = "STG"
            DOCKER_USER = "isharan"
            BULD_VERSION = sh (script: 'cat ~/version.txt', returnStdout: true).trim()
          }
        }
      }
    }
    stage('Checkout Config Repo') {
        steps {
          timestamps {
            git branch: 'main',
                credentialsId: 'Jenkins_kube',
                url: 'git@github.com:deeebugger/configs.git',
                poll: false
            sh "ls -lat"
                }
            }
    }
    stage('Push Config changes') {
        steps {
        timestamps {
          echo 'Git push'
          sh """
            git checkout staging
            git clone main
            git add *
            git commit -m "Cloned main into staging, Version ${BULD_VERSION}”
            git push origin staging
          """
        }
      }
    }
     stage ('Promote_to_Production') {
            steps {
                build job: 'PROD_Deploy'
                manual: true // Needs to be changed
            }
        }
  }
}
