import groovy.json.JsonSlurper

def call() {
    def fileContenr = libraryResource: 'maven.json'
    
    def maven = new JsonSlurper().parseText(fileContent)
    
    return maven
}