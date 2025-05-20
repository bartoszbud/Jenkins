def jobsMap = [
    'view-generator': [name: 'dsl-view-generator', description: 'Generate views jobs', script: 'dslViewGenerator.groovy'],
    'deploy-generator': [name: 'dsl-deploy-job-generator', description: 'Generate deploy jobs', script: 'dslDeployJobGenerator.groovy'],
    'build-generator': [name: 'dsl-build-job-generator', description: 'Generate build jobs', script: 'dslBuildJobGenerator.groovy'],
    'orgs-generator': [name: 'dsl-orgs-generator', description: 'Generate organization folders', script: 'dslOrgsGenerator.groovy'],
    ]

for (i in jobsMap) {
  job("$i.value.name") {
    
    logRotator {
        numToKeep(10)
    }
    
    description("$i.value.description")
    
    scm {
      git {
        remote {
          url('http://gitea.lab.pl/Jenkins/jenkins.git')
        }
        branch('main')
      }
    }
    steps {
      jobDsl {
        targets('jobs/src/'+i.value.script+'')
        removedJobAction('DELETE')
      } 
    }
  }
}