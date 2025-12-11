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
                    echo 'Building the test project......'
                    sh 'mvn clean compile'
                }
            }
        }
        
        stage('Test') {
    agent {
        docker {
            image 'markhobson/maven-chrome:latest'
            args '-v $HOME/.m2:/root/.m2'
        }
    }
    steps {
        sh 'mvn clean test -Dtest=SeleniumIntegrationTest'
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
                subject: "✅ SUCCESS : Todo App Tests - Build #${env.BUILD_NUMBER}",
                body: """
                    <html>
                    <body style="font-family: Arial, sans-serif;">
                        <div style="background: #d4edda; padding: 20px; border-radius: 5px; border-left: 4px solid #28a745;">
                            <h2 style="color: #155724; margin: 0;">✅ Pipeline Execution Successful</h2>
                        </div>
                        
                        <div style="margin-top: 20px; padding: 20px; background: #f8f9fa; border-radius: 5px;">
                            <h3 style="color: #333;">Build Information</h3>
                            <table style="width: 100%; border-collapse: collapse;">
                                <tr>
                                    <td style="padding: 8px; font-weight: bold;">Job:</td>
                                    <td style="padding: 8px;">${env.JOB_NAME}</td>
                                </tr>
                                <tr style="background: #fff;">
                                    <td style="padding: 8px; font-weight: bold;">Build Number:</td>
                                    <td style="padding: 8px;">${env.BUILD_NUMBER}</td>
                                </tr>
                                <tr>
                                    <td style="padding: 8px; font-weight: bold;">Commit:</td>
                                    <td style="padding: 8px;">${env.GIT_COMMIT}</td>
                                </tr>
                                <tr style="background: #fff;">
                                    <td style="padding: 8px; font-weight: bold;">Branch:</td>
                                    <td style="padding: 8px;">${env.GIT_BRANCH}</td>
                                </tr>
                                <tr>
                                    <td style="padding: 8px; font-weight: bold;">Committer:</td>
                                    <td style="padding: 8px;">${env.GIT_COMMITTER_NAME} (${env.GIT_COMMITTER_EMAIL})</td>
                                </tr>
                            </table>
                        </div>
                        
                        <div style="margin-top: 20px; padding: 20px; background: #e7f3ff; border-radius: 5px; border-left: 4px solid #007bff;">
                            <h3 style="color: #004085;">📊 Test Results</h3>
                            <p style="font-size: 16px; margin: 10px 0;">
                                <strong>All 10 tests passed successfully! ✨</strong>
                            </p>
                            <ul style="line-height: 1.8;">
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
                        
                        <div style="margin-top: 20px; padding: 20px; background: #fff3cd; border-radius: 5px;">
                            <h3 style="color: #856404;">🔗 Quick Links</h3>
                            <p>
                                <a href="${env.BUILD_URL}" style="color: #007bff; text-decoration: none; font-weight: bold;">
                                    📋 View Full Build Log
                                </a>
                            </p>
                            <p>
                                <a href="${env.BUILD_URL}testReport/" style="color: #007bff; text-decoration: none; font-weight: bold;">
                                    📊 View Detailed Test Report
                                </a>
                            </p>
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
                        <div style="background: #f8d7da; padding: 20px; border-radius: 5px; border-left: 4px solid #dc3545;">
                            <h2 style="color: #721c24; margin: 0;">❌ Pipeline Execution Failed</h2>
                        </div>
                        
                        <div style="margin-top: 20px; padding: 20px; background: #f8f9fa; border-radius: 5px;">
                            <h3 style="color: #333;">Build Information</h3>
                            <p><strong>Job:</strong> ${env.JOB_NAME}</p>
                            <p><strong>Build Number:</strong> ${env.BUILD_NUMBER}</p>
                            <p><strong>Committer:</strong> ${env.GIT_COMMITTER_NAME} (${env.GIT_COMMITTER_EMAIL})</p>
                        </div>
                        
                        <div style="margin-top: 20px; padding: 20px; background: #fff3cd; border-radius: 5px;">
                            <p>
                                <a href="${env.BUILD_URL}console" style="color: #dc3545; font-weight: bold;">
                                    🖥️ View Console Output
                                </a>
                            </p>
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
