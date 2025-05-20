FROM jenkins/jenkins:2.492.3-lts-rhel-ubi9-jdk21

COPY --chown=jenkins:jenkins ./config/plugins.txt /var/jenkins_home/jcasc/plugins.txt
RUN jenkins-plugin-cli -f /var/jenkins_home/jcasc/plugins.txt
ENV JAVA_OPTS=-Djenkins.install.runSetupWizard=false
COPY --chown=jenkins:jenkins ./config/config.yaml /var/jenkins_home/jcasc/config.yaml
ENV CASC_JENKINS_CONFIG=/var/jenkins_home/jcasc/config.yaml