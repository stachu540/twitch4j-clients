dependencies {
  api(project(":mapper"))
  api(group = "commons-io", name = "commons-io")
  api(group = "org.apache.commons", name = "commons-text")
  api(group = "org.apache.commons", name = "commons-collections4")

  implementation(group = "io.reactivex.rxjava3", name = "rxjava")
  implementation(group = "io.projectreactor", name = "reactor-core")
  implementation(group = "org.jetbrains.kotlinx", name = "kotlinx-coroutines-core")

  implementation(group = "com.squareup.okhttp3", name = "okhttp")
  implementation(group = "org.apache.httpcomponents.client5", name = "httpclient5")
}
