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
                        sh "docker build -t nikolozjakhua/my-app:$IMAGE_NAME ."
                        sh "echo $PASS | docker login -u $USER --password-stdin"
                        sh "docker push nikolozjakhua/my-app:$IMAGE_NAME"
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
        stage ("Commit version update") {
            steps {
                script {
                    withCredentials([sshUserPrivateKey(credentialsId: "yourkeyid"]) {
                        sh 'git remote set-url origin git@github.com:nikolozjakhua/java-maven.git'
                        sh 'git add .'
                        sh "git commit -m 'ci: version bump $BUILD_NUMBER'"
                        sh 'git push origin HEAD:main'
                    }
                }
            }
        }
    }

}
