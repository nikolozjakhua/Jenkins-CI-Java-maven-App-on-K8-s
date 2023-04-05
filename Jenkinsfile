pipeline {
    agent any
    tools {
        maven 'Maven'
    }
    stages {
        stage('increment version') {
            steps {
                script {
                    echo 'incrementing app version...'
                    sh "mvn build-helper:parse-version versions:set -DnewVersion=\\\${parsedVersion.majorVersion}.\\\${parsedVersion.minorVersion}.\\\${parsedVersion.nextIncrementalVersion} versions:commit"
                    def matcher = readFile('pom.xml') =~ '<version>(.+)</version>'
                    def version = matcher[0][1]
                    IMAGE_NAME = "$version-$BUILD_NUMBER"
                }
            }
        }
        stage ("build jar") {
            steps {
                script {
                    echo "Building the application"
                    sh 'mvn clean package'
                }
            }
        }
        stage ("build image") {
            steps {
                script {
                    echo "Building the docker image"
                    withCredentials([usernamePassword(credentialsId: 'docker-creds', passwordVariable: 'PASS', usernameVariable: "USER")]) {
                        sh "docker build -t $USERNAME/my-app:$IMAGE_NAME ."
                        sh "echo $PASS | docker login -u $USER --password-stdin"
                        sh "docker push $USERNAME/my-app:$IMAGE_NAME"
                    }
                }
            }
        }
        stage ("Deploying") {
            environment {
                AWS_ACCESS_KEY_ID = credentials("jenkins_aws_access_key_id")
                AWS_SECRET_ACCESSKEY = credentials("jenkins_aws_secret")
                APP_NAME = 'java-maven-app'
            }
            steps {
                script {
                    echo "deploying the application..."
                    sh 'envsubst < deployment.yaml | kubectl apply -f -'
                    sh 'envsubst < service.yaml | kubectl apply -f -'
                }
            }
        }
        stage ("Commit Version Update") {
            steps {
                script {
                    withCredentials([sshUserPrivateKey(credentialsId: 'github-creds', keyFileVariable: '', passphraseVariable: 'PWD', usernameVariable: 'USER')]) {
                        sh 'git config --global user.email $EMAIL'
                        sh 'git config --global user.name $USERNAME'
                        sh 'git status'
                        sh 'git config -l'
                        sh "git remote add origin git@github.com:${USER}/java-maven.git"
                        sh 'git add .'
                        sh "git commit -m 'Ci: Version bump $IMAGE_NAME'"
                        sh "git push origin HEAD:main"
                    }
                }
            }
        }
    }
}
