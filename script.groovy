def buildJar() {
    echo "Building the application..."
    sh "mvn package"
}

def buildImage() {
    echo "Building the docker image"
    withCredentials([usernamePassword(credentialsId: 'docker-creds', passwordVariable: 'PASS', usernameVariable: "USER")]) {
        sh 'docker build -t nikolozjakhua/my-app:jma-2.0 .'
        sh "echo $PASS | docker login -u $USER --password-stdin"
        sh 'docker push nikolozjakhua/my-app:jma-2.0'
    }
}

def deployApp(){
    echo "Deploying the application..."
}
