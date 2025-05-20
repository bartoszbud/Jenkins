def envMap = ['orgs':'Organization', 
              'dsl':'DSL']

for (i in envMap) {
  if ( "$i.key" == "dsl" ) {
    listView("$i.key") {
        description("$i.value jobs")
        filterBuildQueue()
        filterExecutors()
        jobs {
            regex("^($i.key)-(.*?)")
        }
        columns {
            status()
            weather()
            name()
            descriptionColumn()
            lastSuccess()
            lastFailure()
            lastDuration()
            buildButton()
        }
    }} else if ( "$i.key" == "orgs" ) {
    listView("$i.key") {
        description("$i.value folders")
        filterBuildQueue()
        filterExecutors()
        jobs {
            regex("(^($i.key)-(.*?)")
        }
        columns {
            status()
            weather()
            name()
            descriptionColumn()
            lastSuccess()
            lastFailure()
            lastDuration()
            buildButton()
        }
    }} else {
    listView("$i.key") {
        description("Deploy to $i.value environment jobs")
        filterBuildQueue()
        filterExecutors()
        jobs {
            //regex("^(.*?)-$i.key")
        }
        columns {
            status()
            weather()
            name()
            descriptionColumn()
            lastSuccess()
            lastFailure()
            lastDuration()
            buildButton()
        }
    }}
}