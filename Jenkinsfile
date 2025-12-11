pipeline {
    agent any

    environment {
        DOCKER_IMAGE = 'markhobson/maven-chrome:latest'
        GIT_REPO = 'https://github.com/saffanoor001/TestingToDoApp_tests.git'
        M2_REPO = '/var/lib/jenkins/.m2'
    }

    stages {
        stage('Checkout') {
            steps {
                script {
                    echo 'Checking out test code from GitHub...'
                    git branch: 'main', url: "${GIT_REPO}"
                }
            }
        }

        stage('Build') {
            agent {
                docker {
                    image "${DOCKER_IMAGE}"
                    args "-v ${M2_REPO}:/root/.m2 --shm-size=2g"
                }
            }
            steps {
                script {
                    echo 'Building the test project inside Docker...'
                    sh 'mvn clean compile -Dmaven.repo.local=/root/.m2'
                }
            }
        }

        stage('Test') {
            agent {
                docker {
                    image "${DOCKER_IMAGE}"
                    args "-v ${M2_REPO}:/root/.m2 --shm-size=2g"
                }
            }
            steps {
                script {
                    echo 'Running Selenium tests inside Docker...'
                    sh 'mvn clean test -Dmaven.repo.local=/root/.m2 -Dtest=SeleniumIntegrationTest'
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
            emailext (
                to: '${GIT_COMMITTER_EMAIL}',
                subject: "✅ SUCCES: Todo App Tests - Build #${env.BUILD_NUMBER}",
                body: """<html>
                    <body style="font-family: Arial, sans-serif;">
                        <div style="background: #d4edda; padding: 20px; border-radius: 5px; border-left: 4px solid #28a745;">
                            <h2 style="color: #155724; margin: 0;">✅ Pipeline Execution Successful</h2>
                        </div>
                        <p>Build #${env.BUILD_NUMBER} completed successfully.</p>
                        <p><a href="${env.BUILD_URL}">View Build Details</a></p>
                    </body>
                </html>""",
                mimeType: 'text/html',
                attachLog: true
            )
        }
        failure {
            emailext (
                to: '${GIT_COMMITTER_EMAIL}',
                subject: "❌ FAILURE: Todo App Tests - Build #${env.BUILD_NUMBER}",
                body: """<html>
                    <body style="font-family: Arial, sans-serif;">
                        <div style="background: #f8d7da; padding: 20px; border-radius: 5px; border-left: 4px solid #dc3545;">
                            <h2 style="color: #721c24; margin: 0;">❌ Pipeline Execution Failed</h2>
                        </div>
                        <p>Build #${env.BUILD_NUMBER} failed. Check console output:</p>
                        <p><a href="${env.BUILD_URL}console">View Console Output</a></p>
                    </body>
                </html>""",
                mimeType: 'text/html',
                attachLog: true
            )
        }
    }
}
