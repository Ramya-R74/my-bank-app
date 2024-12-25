pipeline{
  agent { label "dev" };
    stages{
      stage("Fetch the code"){
        steps{
          git url: "https://github.com/Ramya-R74/my-bank-app.git", branch: "master"
        }
    }
      stage("Build the code"){
        steps{
          sh "docker build -t bankapp:latest . "
        }
    }
      stage("Push the image to DockerHub"){
        steps{
          withCredentials([usernamePassword(
            credentialsId:"DockerHub",
            usernameVariable:"docUser",
            passwordVariable:"docPasswd"
          )]){
            sh "echo $docPasswd | docker login -u $docUser --password-stdin"
            sh "docker image ag bankapp:latest ${env.docUser}/bankapp:latest"
            sh "docker push ${env.docUser}/bankapp:latest"
          }
        }
    }
    stage("Deploy the code"){
        steps{
          sh "docker-compose down && docker-compose up -d --build bankapp"
        }
    }
    } 
