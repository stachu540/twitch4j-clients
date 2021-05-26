dependencies {
  api(project(":mapper"))
  api(project(":http"))

  implementation(group = "com.squareup.okhttp3", name = "okhttp")
}
