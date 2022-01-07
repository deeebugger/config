#!/usr/local/bin/groovy

pipeline {
  agent any

  stages {
    stage('set variables') {
      steps {
        timestamps {
          script {
            echo 'starting set up Global variables'
            ENVIRONMENT = "DEV"
            DOCKER_USER = "isharan"
            BULD_VERSION = sh (script: 'cat ~/version.txt', returnStdout: true).trim()
          }
        }
      }
    }
    stage('Checkout Config Repo') {
        steps {
          timestamps {
            //git branch: 'main',
             //   credentialsId: 'Jenkins_kube',
               // url: 'git@github.com:deeebugger/config.git',
               // poll: false
            checkout([$class: 'GitSCM', branches: [[name: 'main']], extensions: [], userRemoteConfigs: [[url: 'git@github.com:deeebugger/config.git']]])   
            sh "ls -lat"
                }
            }
    }
        stage('Udate TagVersion and User') {
        steps {
        timestamps {
          echo 'Docker Build'
          sh """
            chmod 775 TagVersion_update.sh 
            bash -x TagVersion_update.sh ${BULD_VERSION} ${DOCKER_USER}
            cat environments/DEV/deployments/deployment.yml
          """
        }
      }
    }
    stage('Push Config changes') {
        steps {
        timestamps {
          echo 'Git push'
          sh """
           # git fetch 
           # git remote set-url origin git@github.com:deeebugger/config.git
            git branch
            git add *
            git commit -m "Image version upgraded to ${BULD_VERSION}"
            git push origin main
          """
        }
      }
    }
  //   stage ('Promote_to_Staging') {
   //         steps {
   //             build job: 'STG_Deploy'
    //        }
   //     }
  }
}
