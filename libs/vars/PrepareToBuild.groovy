def call(String javaVersion, String mavenVersion, String gitCommit, String workingDirectory) {
    
    if ( javaVersion == "1.8" ) {
      javaVersion = "8"
    }

    def command = "podman run -i --rm --name maven-jdk${javaVersion}-${gitCommit} maven:${mavenVersion}-jdk${javaVersion} mvn -v"
    
    def stdOut = sh(script: command, returnStdout: true).trim()
    echo "${stdOut}"
}