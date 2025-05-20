def call() {
    pipeline {
      agent {
        label ("${SetSlave()}")
      }
      
      environment {
        def gitBranch = "${BRANCH_NAME}"
        def gitCommit = "${GIT_COMMIT}"
        def gitUrl = "${GIT_URL}"
        def workingDirectory = "${WORKSPACE}"
        def organization = "${GetOrganization("${env.gitUrl}")}"
        def recipients = "${SetRecipients("${env.organization}")}"
        def pom = readMavenPom file: 'pom.xml'
        def artifactId = "${pom.artifactId}"
        def artifactVersion = "${pom.version}"
        def javaVersion = "${pom.properties.'java.version'}"
        def mavenVersion = "${SetMaven().mavenVersion}"
      }

      options {
        buildDiscarder(logRotator(numToKeepStr: '5'))
        disableConcurrentBuilds()
      }

      stages {
        stage('Prepare') {
          steps {
            PrepareToBuild(javaVersion, mavenVersion, gitCommit, workingDirectory)
          }
        }
        stage('Build') {
          steps {
            BuildArtifact(javaVersion, mavenVersion, gitCommit, workingDirectory)
          }
        }
        stage('Test') {
          steps {
            Test()
          }
        }
        stage('Push image to registry') {
          steps {
            PushImageToRegistry()
          }
        }
        stage('Workspace clean') {
          steps {
            cleanWs()
          }
        }
      }

      post {
        success {
          emailext attachLog: false,
                   subject: '$PROJECT_NAME - Build # $BUILD_NUMBER - $BUILD_STATUS',
                   body: '$PROJECT_NAME package successfully built. Build URL: $BUILD_URL',
                   recipientProviders: [requestor()], to: "${env.recipients}"
        }
        failure {
          emailext attachLog: false,
                   subject: '$PROJECT_NAME - Build # $BUILD_NUMBER - $BUILD_STATUS',
                   body: '$PROJECT_NAME package was not successfully built. Build URL: $BUILD_URL',
                   recipientProviders: [requestor()], to: "${env.recipients}"
        }
      }
    }
}
