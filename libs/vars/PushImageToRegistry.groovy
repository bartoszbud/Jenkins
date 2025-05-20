import groovy.json.JsonSlurperClassic

def call(String gitBranch, String artifactId, String artifactVersion) {
    
  def fileContent = libraryResource 'registry.json'
  def getRegistry = new JsonSlurperClassic().parseText(fileContent)
  def currentTimestamp = new Date().format("yyyyMMdd-HHmmss")
  def registry = getRegistry.registry.ctrInternal

  withCredentials([usernamePassword(credentialsId: 'nexus', passwordVariable: 'PASSWORD', usernameVariable: 'USERNAME')]) {
    def registryLogin = "podman login ${registry} -u ${USERNAME} -p ${DOCKERHUB_PASSWORD}"
    def registryLogout = "podman logout ${registry}"
    def snashotPush = "podman push ${registry}/${artifactId}:${artifactVersion} ${registry}/${artifactId}:${artifactVersion}-${currentTimestamp}"
    def releasePush = "podman push ${registry}/${artifactId}:${artifactVersion} ${registry}/${artifactId}:${artifactVersion}"
    
    def login = sh(script: registryLogin, returnStdout: true).trim()
    echo "${login}"

    if ("${gitBranch}".startsWith("release/*")) {
      def imagePush = sh(script: releasePush, returnStdout: true).trim()
      def registryLogoutOut = sh(script: registryLogout, returnStdout: true).trim()
      echo "${imagePush}"
    } else {
      def imagePush = sh(script: snashotPush, returnStdout: true).trim()
      def registryLogoutOut = sh(script: registryLogout, returnStdout: true).trim()
      echo "${imagePush}"
    }
  }