pipeline {
    agent any

    environment {
            DOCKER_TOKEN = credentials('docker-push-secret')
            DOCKER_USER = 'jgenc'
            DOCKER_SERVER = 'ghcr.io'
            DOCKER_PREFIX = 'ghcr.io/jgenc/distributed-backend'
    }

    stages {
      stage("Clone") {
        steps {
          git branch: 'main', url : 'git@github.com:jgenc/hua-distributed-project-backend.git'
        }
      }

      stage("Copy .env.example to .env") {
        steps {
          sh '''
            cp ./.env.example ./.env
          '''
        }
      }

      stage("Docker Build") {
        steps {
          sh '''
            HEAD_COMMIT=$(git rev-parse --short HEAD) 
            TAG=$HEAD_COMMIT-$BUILD_ID
            docker build --rm -t $DOCKER_PREFIX:$TAG -t $DOCKER_PREFIX:latest -f backend.dockerfile .
          '''
        }
      }
      
      stage("Docker Push") {
        steps {
          sh '''
            echo $DOCKER_TOKEN | docker login $DOCKER_SERVER -u $DOCKER_USER --password-stdin
            docker push $DOCKER_PREFIX --all-tags
          '''
        }
      }

      stage("Deploy container to Target VM") {
        steps {
          sh '''
            ansible-playbook -i ~/workspace/devops-project/hosts/azure.yml ~/workspace/devops-project/playbooks/backend-vm.yml
          '''
        }
      }

  }
}