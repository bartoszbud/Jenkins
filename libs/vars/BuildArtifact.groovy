def call(String javaVersion, String mavenVersion, String gitCommit, String workingDirectory) {
    
  def command = "podman run -i --rm --name maven-jdk${javaVersion}-${gitCommit} \
                --security-opt label=disable -w /usr/src/mymaven -v ${workingDirectory}:/usr/src/mymaven \
                maven:${mavenVersion}-jdk${javaVersion} \
                mvn clean install -U -DskipTests=true -Dmaven.test.skip=true -Dmaven.javadoc.skip=true"
    
  def stdOut = sh(script: command, returnStdout: true).trim()
  echo "${stdOut}"
}