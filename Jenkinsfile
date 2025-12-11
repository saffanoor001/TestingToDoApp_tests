pipeline {
    agent any
    
    environment {
        DOCKER_IMAGE = 'markhobson/maven-chrome:latest'
        GIT_REPO = 'https://github.com/saffanoor001/TestingToDoApp_tests.git'
        APP_URL = 'http://13.51.205.213:8000'
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
                echo 'Building the test project with Maven...'
                sh 'mvn clean compile'
            }
        }

        stage('Check App URL') {
            steps {
                echo "Checking if app URL ${APP_URL} is reachable..."
                script {
                    def response = sh(script: "curl -Is ${APP_URL} | head -n 1", returnStdout: true).trim()
                    if (!response.contains("200")) {
                        error "App is not reachable at ${APP_URL}. Response: ${response}"
                    } else {
                        echo "App is reachable: ${response}"
                    }
                }
            }
        }
        
        stage('Test') {
            agent {
                docker {
                    image "${DOCKER_IMAGE}"
                    args '--network host --shm-size=2g' // host networking avoids network isolation issues
                }
            }
            steps {
                echo 'Running Selenium tests in Docker container...'
                sh 'mvn clean test'
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
            emailext (
                to: '${GIT_COMMITTER_EMAIL}',
                subject: "✅   SUCCESS : Todo App Tests - Build #${env.BUILD_NUMBER}",
                body: "Pipeline succeeded. Check build: ${env.BUILD_URL}",
                attachLog: true
            )
        }
        failure {
            emailext (
                to: '${GIT_COMMITTER_EMAIL}',
                subject: "❌ FAILURE: Todo App Tests - Build #${env.BUILD_NUMBER}",
                body: "Pipeline failed. Check console: ${env.BUILD_URL}console",
                attachLog: true
            )
        }
    }
}
