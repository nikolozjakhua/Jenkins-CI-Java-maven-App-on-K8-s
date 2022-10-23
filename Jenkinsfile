pipeline {
    agent any
    tools {
        maven 'Maven'
    }
    stages {
        stage ("build jar") {
            steps {
                script {
                    echo "Building the application"
                    sh 'mvn package'
                }
            }
        }
        stage ("build image") {
            steps {
                script {
                    echo "Building the docker image"
                    withCredentials([usernamePassword(credentialsId: 'docker-creds', passwordVariable: 'PASS', usernameVariable: "USER")]) {
                        sh 'docker build -t nikolozjakhua/my-app:jma-2.0 .'
                        sh "echo $PASS | docker login -u $USER --password-stdin"
                        sh 'docker push nikolozjakhua/my-app:jma-2.0'
                    }

                }
            }
        }
        stage ("Deploying") {
            steps {
                script {
                    echo "Deploying the Application..."
                }
            }
        }
    }

}
