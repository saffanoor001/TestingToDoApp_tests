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
                    args '--shm-size=2g'
                }
            }
            steps {
                script {
                    echo 'Running Selenium tests in Docker container...'
                    try {
                        sh 'mvn clean test'
                        currentBuild.result = 'SUCCESS'
                    } catch (Exception e) {
                        currentBuild.result = 'FAILURE'
                        throw e
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
            emailext (
                to: '${GIT_COMMITTER_EMAIL}',
                subject: "✅ SUCCESS: Todo App Tests - Build #${env.BUILD_NUMBER}",
                body: """
                    <html>
                    <body style="font-family: Arial, sans-serif;">
                        <div style="background: #d4edda; padding: 20px; border-radius: 5px;">
                            <h2 style="color: #155724;">✅ Pipeline Execution Successful</h2>
                        </div>
                        <div style="margin-top: 20px; padding: 20px; background: #f8f9fa;">
                            <h3>Build Information</h3>
                            <p><strong>Job:</strong> ${env.JOB_NAME}</p>
                            <p><strong>Build Number:</strong> ${env.BUILD_NUMBER}</p>
                            <p><strong>Committer:</strong> ${env.GIT_COMMITTER_NAME} (${env.GIT_COMMITTER_EMAIL})</p>
                        </div>
                        <div style="margin-top: 20px; padding: 20px; background: #e7f3ff;">
                            <h3>📊 Test Results</h3>
                            <p><strong>All 10 tests passed successfully! ✨</strong></p>
                            <ul>
                                <li>User Registration Test</li>
                                <li>Valid Login Test</li>
                                <li>Invalid Login Test</li>
                                <li>User Logout Test</li>
                                <li>Create Todo Test</li>
                                <li>View Todos Test</li>
                                <li>Update Todo Test</li>
                                <li>Delete Todo Test</li>
                                <li>Complete Todo Test</li>
                                <li>Search Functionality Test</li>
                            </ul>
                        </div>
                        <div style="margin-top: 20px;">
                            <p><a href="${env.BUILD_URL}" style="color: #007bff; font-weight: bold;">📋 View Full Build Log</a></p>
                            <p><a href="${env.BUILD_URL}testReport/" style="color: #007bff; font-weight: bold;">📊 View Detailed Test Report</a></p>
                        </div>
                    </body>
                    </html>
                """,
                mimeType: 'text/html',
                attachLog: true
            )
        }
        failure {
            emailext (
                to: '${GIT_COMMITTER_EMAIL}',
                subject: "❌ FAILURE: Todo App Tests - Build #${env.BUILD_NUMBER}",
                body: """
                    <html>
                    <body style="font-family: Arial, sans-serif;">
                        <div style="background: #f8d7da; padding: 20px; border-radius: 5px;">
                            <h2 style="color: #721c24;">❌ Pipeline Execution Failed</h2>
                        </div>
                        <div style="margin-top: 20px; padding: 20px; background: #f8f9fa;">
                            <p><strong>Job:</strong> ${env.JOB_NAME}</p>
                            <p><strong>Build Number:</strong> ${env.BUILD_NUMBER}</p>
                            <p><strong>Committer:</strong> ${env.GIT_COMMITTER_NAME}</p>
                        </div>
                        <div style="margin-top: 20px;">
                            <p><a href="${env.BUILD_URL}console" style="color: #dc3545; font-weight: bold;">🖥️ View Console Output</a></p>
                        </div>
                    </body>
                    </html>
                """,
                mimeType: 'text/html',
                attachLog: true
            )
        }
    }
}
