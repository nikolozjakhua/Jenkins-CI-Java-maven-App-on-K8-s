dev gv

pipeline {
    agent any
    tools {
        maven 'Maven'
    }
    stages {
        stage ("init") {
            steps {
                script {
                    gv = load "script.groovy"
                }
            }
        }
        stage ("build jar") {
            steps {
                script {
                    gv.buildJar()
                }
            }
        }
        stage ("build image") {
            steps {
                script {
                    gv.buildImage()
                }
            }
        }
        stage ("Deploying") {
            steps {
                script {
                    gv.deployApp()
                }
            }
        }
    }

}
