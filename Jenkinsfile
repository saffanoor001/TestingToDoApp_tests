pipeline {
    agent any
    
    environment {
        DOCKER_IMAGE = 'todo-app-tests'
        APP_CONTAINER = 'todo-app'
    }
    
    stages {
        stage('Checkout Code') {
            steps {
                echo '========================================='
                echo 'STAGE 1: Checking out code from GitHub'
                echo '========================================='
                checkout scm
            }
        }
        
        stage('Setup Application') {
            steps {
                echo '========================================='
                echo 'STAGE 2: Starting Todo Application'
                echo '========================================='
                script {
                    sh '''
                        # Stop and remove existing containers
                        docker stop ${APP_CONTAINER} || true
                        docker rm ${APP_CONTAINER} || true
                        
                        # Remove old database to ensure fresh state
                        rm -f instance/todo.db || true
                        
                        # Start Flask application in background
                        docker run -d \
                            --name ${APP_CONTAINER} \
                            -p 80:80 \
                            --network host \
                            -v $(pwd):/app \
                            -w /app \
                            python:3.9-slim \
                            bash -c "pip install -r requirements.txt && python app.py"
                        
                        # Wait for application to be ready
                        echo "Waiting for application to start..."
                        sleep 15
                        
                        # Test if app is responding
                        curl -f http://localhost:80 || (echo "App failed to start" && exit 1)
                        echo "Application is running successfully!"
                    '''
                }
            }
        }
        
        stage('Build Test Environment') {
            steps {
                echo '========================================='
                echo 'STAGE 3: Building Docker image for tests'
                echo '========================================='
                script {
                    sh '''
                        docker build -t ${DOCKER_IMAGE} -f Dockerfile .
                        echo "Test environment built successfully!"
                    '''
                }
            }
        }
        
        stage('Run Selenium Tests') {
            steps {
                echo '========================================='
                echo 'STAGE 4: Running Selenium Tests'
                echo '========================================='
                script {
                    sh '''
                        docker run --rm \
                            --network host \
                            -v $(pwd)/tests:/tests \
                            ${DOCKER_IMAGE} \
                            python -m unittest test_todo_app.TodoAppTests -v
                    '''
                }
            }
        }
    }
    
    post {
        always {
            echo '========================================='
            echo 'CLEANUP: Stopping application'
            echo '========================================='
            script {
                sh '''
                    docker stop ${APP_CONTAINER} || true
                    docker rm ${APP_CONTAINER} || true
                    docker rmi ${DOCKER_IMAGE} || true
                '''
            }
        }
        success {
            echo '========================================='
            echo '✅ PIPELINE COMPLETED SUCCESSFULLY!'
            echo 'All tests passed.'
            echo '========================================='
        }
        failure {
            echo '========================================='
            echo '❌ PIPELINE FAILED!'
            echo 'Check test results above.'
            echo '========================================='
        }
    }
}
