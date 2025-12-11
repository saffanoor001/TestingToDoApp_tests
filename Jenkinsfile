pipeline {
    agent any
    
    environment {
        DOCKER_IMAGE = 'markhobson/maven-chrome:latest'
        GIT_REPO = 'https://github.com/saffanoor001/TestingToDoApp_tests.git'
        MAVEN_REPO = "${env.WORKSPACE}/.m2" // Workspace-local Maven repo
    }
    
    stages {
        stage('Checkout') {
            steps {
                echo 'Checking out test code from GitHub...'
                git branch: 'main', url: "${GIT_REPO}"
            }
        }
        
        stage('Build') {
            steps {
                echo 'Building the test project inside Docker...'
                script {
                    docker.image(DOCKER_IMAGE).inside("-v ${MAVEN_REPO}:/root/.m2 --shm-size=2g") {
                        sh 'mvn clean compile -Dmaven.repo.local=/root/.m2'
                    }
                }
            }
        }
        
        stage('Test') {
            steps {
                echo 'Running Selenium tests in headless Chrome inside Docker...'
                script {
                    docker.image(DOCKER_IMAGE).inside("-v ${MAVEN_REPO}:/root/.m2 --shm-size=2g") {
                        sh 'mvn clean test -Dmaven.repo.local=/root/.m2 -Dtest=SeleniumIntegrationTest'
                    }
                }
            }
            post {
                always {
                    junit '**/target/surefire-reports/*.xml'
                }
            }
        }
    }
    
    post {
        always {
            echo 'Pipeline execution completed'
        }
        success {
            echo "Build succeeded! You can add emailext here."
        }
        failure {
            echo "Build failed! You can add emailext here."
        }
    }
}
