dependencies {
  implementation(group = "com.google.code.gson", name = "gson")

  api(platform("com.fasterxml.jackson:jackson-bom:2.13.2.20220324"))
  implementation(group = "com.fasterxml.jackson.core", name = "jackson-databind")
}
