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
            BULD_VERSION = sh (script: 'date +%Y%m%d%H%M', returnStdout: true).trim()
          }
        }
      }
    }
        stage('Checkout Code Repo') {
        steps {
          timestamps {
            git branch: 'main',
                credentialsId: 'Jenkins_kube',
                url: 'git@github.com:deeebugger/webapp.git'
            sh "ls -lat"
                }
            }
        }
        stage('Docker Build') {
        steps {
        timestamps {
          echo 'Docker Build'
          sh """
          echo "I'll uncomment below 2 lines once my jenkins is ready with docker"
          docker build -t sample-webapp .
          cp ~/config.json /var/lib/jenkins/.docker/config.json
          docker login
          docker tag sample-webapp:latest ${DOCKER_USER}/sample-web:${BULD_VERSION}
          echo ${BULD_VERSION} > ~/version.txt
          echo "I'll add awss3 push command later once jenkins is ready with ec2role"
          cat version.txt
          """
        }
      }
    }
        stage('Docker Push') {
        steps {
        timestamps {
          echo 'Docker Build'
          sh """
            docker push ${DOCKER_USER}/sample-web:${BULD_VERSION}
          """
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
        stage ('Invoke_deploy_pipeline') {
            steps {
                build job: 'Dev_Deploy'
            }
        }
  }
}
