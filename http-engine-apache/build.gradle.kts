dependencies {
  api(project(":mapper"))
  api(project(":http"))

  implementation(group = "org.apache.httpcomponents.client5", name = "httpclient5")
}
