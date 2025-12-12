pipeline {
    agent any

    environment {
        DOCKER_IMAGE = 'markhobson/maven-chrome:latest'
        GIT_REPO = 'https://github.com/saffanoor001/TestingToDoApp_tests.git'
        NOTIFY_EMAIL = 'your_email@example.com'
    }

    stages {
        stage('Checkout') {
            steps {
                echo 'Checking out test code from GitHub...'
                git branch: 'main', url: "${GIT_REPO}"
            }
        }

        stage('Build') {
            agent {
                docker {
                    image "${DOCKER_IMAGE}"
                    args '--shm-size=2g -v /var/lib/jenkins/.m2:/root/.m2'
                }
            }
            steps {
                echo 'Building the test project...'
                sh 'mvn clean compile'
            }
        }

        stage('Test') {
            agent {
                docker {
                    image "${DOCKER_IMAGE}"
                    args '--shm-size=2g -v /var/lib/jenkins/.m2:/root/.m2'
                }
            }
            steps {
                echo 'Running Selenium tests in Docker container...'
                sh 'mvn test'
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
                to: "${NOTIFY_EMAIL}",
                subject: "✅ SUCCESS: Todo App Tests - Build #${env.BUILD_NUMBER}",
                body: "Pipeline executed successfully. View details at: ${env.BUILD_URL}",
                attachLog: true
            )
        }
        failure {
            emailext (
                to: "${NOTIFY_EMAIL}",
                subject: "❌ FAILURE: Todo App Tests - Build #${env.BUILD_NUMBER}",
                body: "Pipeline failed. Check the console output at: ${env.BUILD_URL}console",
                attachLog: true
            )
        }
    }
}
