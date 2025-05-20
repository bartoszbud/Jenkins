import groovy.json.JsonSlurper

def call(String artifactId, String artifactVersion) {

  def fileContent = libraryResource 'registry.json'
  def getRegistry = new JsonSlurper().parseText(fileContent)
  def registry = getRegistry.registry.ctrInternal
  def command = "podman build -t ${registry}/${artifactId}:${artifactVersion} -f ./Dockerfile"

  def stdOut = sh(script: command, returnStdout: true).trim()
  echo "${stdOut}"

  env.imageName = "${registry}/${artifactId}:${artifactVersion}"
}