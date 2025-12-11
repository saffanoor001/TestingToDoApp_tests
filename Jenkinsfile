pipeline {
    agent any

    environment {
        DOCKER_IMAGE = 'markhobson/maven-chrome:latest'
        GIT_REPO = 'https://github.com/saffanoor001/TestingToDoApp_tests.git'
    }

    stages {
        stage('Checkout') {
            steps {
                echo 'Checking out test code from GitHub...'
                git branch: 'main', url: "${GIT_REPO}"
            }
        }

        stage('Build & Test in Docker') {
            steps {
                script {
                    docker.image("${DOCKER_IMAGE}").inside("--shm-size=2g -u 111:113") {
                        // Use workspace-local Maven repo to avoid permission issues
                        sh 'mkdir -p ${WORKSPACE}/.m2'
                        sh 'mvn clean compile -Dmaven.repo.local=${WORKSPACE}/.m2'
                        sh 'mvn test -Dmaven.repo.local=${WORKSPACE}/.m2 -Dtest=SeleniumIntegrationTest'
                    }
                }
            }
            post {
                always {
                    // Archive test results
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
                <html><body>
                <h2>✅ Pipeline Execution Successful</h2>
                <p>Build Number: ${env.BUILD_NUMBER}</p>
                <p>Job: ${env.JOB_NAME}</p>
                <p><a href="${env.BUILD_URL}">View Build Details</a></p>
                <p><a href="${env.BUILD_URL}testReport/">View Test Report</a></p>
                </body></html>
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
                <html><body>
                <h2>❌ Pipeline Execution Failed</h2>
                <p>Build Number: ${env.BUILD_NUMBER}</p>
                <p>Job: ${env.JOB_NAME}</p>
                <p><a href="${env.BUILD_URL}console">View Console Output</a></p>
                </body></html>
                """,
                mimeType: 'text/html',
                attachLog: true
            )
        }
    }
}
