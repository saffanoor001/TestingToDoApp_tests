pipeline {
    agent any
    
    environment {
        DOCKER_IMAGE = 'markhobson/maven-chrome:latest'
        GIT_REPO = 'https://github.com/saffanoor001/TestingToDoApp_tests.git'
        NOTIFY_EMAIL = 'your_email@example.com'  // <-- Replace with your real email
        MAVEN_REPO_VOLUME = "${HOME}/.m2:/root/.m2" // mount local Maven repo
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
                    args "--shm-size=2g -v ${MAVEN_REPO_VOLUME}"
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
                    args "--shm-size=2g -v ${MAVEN_REPO_VOLUME}"
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
                subject: "✅ SUCCESS : Todo App Tests - Build #${env.BUILD_NUMBER}",
                body: """
                    <html>
                    <body style="font-family: Arial, sans-serif;">
                        <h2 style="color: green;">✅ Pipeline Execution Successful</h2>
                        <p><strong>Job:</strong> ${env.JOB_NAME}</p>
                        <p><strong>Build Number:</strong> ${env.BUILD_NUMBER}</p>
                        <p><strong>Committer:</strong> ${env.GIT_COMMITTER_NAME} (${env.GIT_COMMITTER_EMAIL})</p>
                        <p><a href="${env.BUILD_URL}" style="font-weight:bold;">📋 View Full Build Log</a></p>
                        <p><a href="${env.BUILD_URL}testReport/" style="font-weight:bold;">📊 View Detailed Test Report</a></p>
                    </body>
                    </html>
                """,
                mimeType: 'text/html',
                attachLog: true
            )
        }
        failure {
            emailext (
                to: "${NOTIFY_EMAIL}",
                subject: "❌ FAILURE: Todo App Tests - Build #${env.BUILD_NUMBER}",
                body: """
                    <html>
                    <body style="font-family: Arial, sans-serif;">
                        <h2 style="color: red;">❌ Pipeline Execution Failed</h2>
                        <p><strong>Job:</strong> ${env.JOB_NAME}</p>
                        <p><strong>Build Number:</strong> ${env.BUILD_NUMBER}</p>
                        <p><strong>Committer:</strong> ${env.GIT_COMMITTER_NAME}</p>
                        <p><a href="${env.BUILD_URL}console" style="font-weight:bold;">🖥️ View Console Output</a></p>
                    </body>
                    </html>
                """,
                mimeType: 'text/html',
                attachLog: true
            )
        }
    }
}
