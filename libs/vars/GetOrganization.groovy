def call(String gitUrl) {
  def org = gitUrl.split('/')
  def organization = org[org.length - 2]

  env.orgName = "${organization}"
}