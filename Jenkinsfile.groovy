#!/usr/bin/env groovy

node("master") {
    def image_name = "demo"
    def app_name = "demo"

    try {

        stage('Checkout'){
            checkout scm
        }

        stage('Increment Docker Tag'){
            TAG = sh (
                    script: "$scripts_path/get_tag.sh $image_name",
                    returnStdout: true
            ).trim()
            echo "${app_name} tag is ${TAG}"
        }

        stage('Docker Image') {
            docker.withRegistry('https://XXXX.dkr.ecr.us-east-1.amazonaws.com', 'ecr:us-east-1:demo-ecr-credentials') {
                def customImage = docker.build("cuit/$image_name:${TAG}")
                customImage.push()
            }
        }

        stage('Deploy') {
        } 
    } catch (err) {
        currentBuild.result = "FAILURE"
        throw err
    }
}