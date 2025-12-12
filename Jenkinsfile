pipeline {
    agent any
    
    stages {
        stage('Checkout') {
            steps {
                echo 'Checking out test code from GitHub...'
                git branch: 'main', 
                    url: 'https://github.com/saffanoor001/TestingToDoApp_tests.git'
            }
        }
        
        stage('Build') {
            agent {
                docker {
                    image 'markhobson/maven-chrome:latest'
                    args '--shm-size=2g -u root -v $HOME/.m2:/root/.m2:rw'
                    reuseNode true
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
                    image 'markhobson/maven-chrome:latest'
                    args '--shm-size=2g -u root -v $HOME/.m2:/root/.m2:rw'
                    reuseNode true
                }
            }
            steps {
                echo 'Running Selenium tests...'
                sh '''
                    mvn test -Dtest=SeleniumIntegrationTest || true
                '''
            }
            post {
                always {
                    junit allowEmptyResults: true, testResults: '**/target/surefire-reports/*.xml'
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

Check details: ${env.BUILD_URL}
                """,
                to: 'your_email@example.com',
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

Check console: ${env.BUILD_URL}console
                """,
                to: 'your_email@example.com',
                attachLog: true
            )
        }
    }
}
