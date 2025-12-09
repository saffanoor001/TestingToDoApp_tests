pipeline {
    agent any
    
    tools {
        maven 'Maven'
    }
    
    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', 
                    url: 'https://github.com/saffanoor001/TestingToDoApp_tests.git'
            }
        }
        
        stage('Build') {
            steps {
                script {
                    echo 'Building the project with Maven...'
                    sh 'mvn clean compile'
                }
            }
        }
        
        stage('Test') {
            agent {
                docker {
                    image 'markhobson/maven-chrome:latest'
                    args '--shm-size=2g -v $HOME/.m2:/root/.m2:rw'
                    reuseNode true
                }
            }
            steps {
                script {
                    echo 'Running Selenium tests in Docker container...'
                    sh '''
                        mvn clean test -Dtest=SeleniumIntegrationTest
                    '''
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
                subject: "✅ Build Success: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                body: """
Build successful!

Job: ${env.JOB_NAME}
Build Number: ${env.BUILD_NUMBER}
Status: ${currentBuild.result}

Check details: ${env.BUILD_URL}
                """,
                to: '${GIT_COMMITTER_EMAIL}',
                attachLog: true
            )
        }
        failure {
            emailext (
                subject: "❌ Build Failed: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                body: """
Build failed!

Job: ${env.JOB_NAME}
Build Number: ${env.BUILD_NUMBER}
Status: ${currentBuild.result}

Check console: ${env.BUILD_URL}console
                """,
                to: '${GIT_COMMITTER_EMAIL}',
                attachLog: true
            )
        }
    }
}
