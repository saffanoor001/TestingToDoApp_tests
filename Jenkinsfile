pipeline {
    agent any
    
    environment {
        DOCKER_IMAGE = 'markhobson/maven-chrome:latest'
        GIT_REPO = 'https://github.com/saffanoor001/TestingToDoApp_tests.git'
    }
    
    stages {
        stage('Checkout') {
            steps {
                script {
                    echo 'Checking out test code from GitHub...'
                    git branch: 'main', url: "${GIT_REPO}"
                    
                    // Capture committer email for notifications
                    env.COMMIT_EMAIL = sh(
                        script: "git --no-pager log -1 --pretty=format:'%ae'",
                        returnStdout: true
                    ).trim()
                }
            }
        }
        
        stage('Build') {
            steps {
                script {
                    echo 'Building the test project...'
                    sh 'mvn clean compile'
                }
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
                script {
                    echo 'Running Selenium tests in Docker container...'
                    try {
                        sh 'mvn -Dmaven.repo.local=/root/.m2/repository clean test'
                        currentBuild.result = 'SUCCESS'
                    } catch (Exception e) {
                        currentBuild.result = 'FAILURE'
                        throw e
                    }
                }
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
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
                to: "${env.COMMIT_EMAIL}",
                subject: "✅ SUCCESS : Todo App Tests - Build #${env.BUILD_NUMBER}",
                mimeType: 'text/html',
                attachLog: true,
                body: """
                    <html>
                    <body style="font-family: Arial, sans-serif;">
                        <h2 style="color: #155724;">✅ Pipeline Execution Successful</h2>
                        <p><strong>Committer:</strong> ${env.COMMIT_EMAIL}</p>
                        <p><a href="${env.BUILD_URL}">View Build</a></p>
                        <p><a href="${env.BUILD_URL}testReport/">View Test Report</a></p>
                    </body>
                    </html>
                """
            )
        }
        
        failure {
            emailext (
                to: "${env.COMMIT_EMAIL}",
                subject: "❌ FAILURE: Todo App Tests - Build #${env.BUILD_NUMBER}",
                mimeType: 'text/html',
                attachLog: true,
                body: """
                    <html>
                    <body style="font-family: Arial, sans-serif;">
                        <h2 style="color: #721c24;">❌ Pipeline Failed</h2>
                        <p><strong>Committer:</strong> ${env.COMMIT_EMAIL}</p>
                        <p><a href="${env.BUILD_URL}console">View Console Output</a></p>
                    </body>
                    </html>
                """
            )
        }
    }
}
