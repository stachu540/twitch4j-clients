dependencies {
  api(project(":event"))

  implementation(group = "com.squareup.okhttp3", name = "okhttp")
  implementation(group = "com.neovisionaries", name = "nv-websocket-client")
}
