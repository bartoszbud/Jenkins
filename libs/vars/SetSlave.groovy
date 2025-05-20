import groovy.json.JsonSlurper

def call() {
  def fileContent = libraryResource 'slaves.json'
  def getSlaves = new JsonSlurper().parseText(fileContent)

  def slaves = getSlaves.slaves.values().join(' || ')
  
  return slaves
}