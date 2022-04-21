plugins {
  signing
  `java-library`
  `maven-publish`
  kotlin("jvm") version "1.5.0"
  id("io.freefair.lombok") version "6.4.3"
  id("org.jetbrains.dokka") version "1.4.32"
  id("com.coditory.manifest") version "0.1.14"
}
allprojects {
  repositories {
    mavenCentral()
  }

  tasks {
    // disable 'lombok.config' generation
    withType<io.freefair.gradle.plugins.lombok.tasks.GenerateLombokConfig> {
      enabled = false
    }
  }
}

subprojects {
  apply(plugin = "kotlin")
  apply(plugin = "signing")
  apply(plugin = "java-library")
  apply(plugin = "maven-publish")
  apply(plugin = "io.freefair.lombok")
  apply(plugin = "com.coditory.manifest")

  base {
    archivesBaseName = artifactId
  }

  lombok {
    version.set("1.18.20")
  }

  // Source Compatibility
  java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
    withSourcesJar()
    withJavadocJar()
  }

  dependencies {
    constraints {
      // Reactive Streams / Coroutines
      implementation(group = "io.reactivex.rxjava3", name = "rxjava", version = "3.0.13")
      implementation(group = "io.projectreactor", name = "reactor-core", version = "3.4.5")
      implementation(group = "org.jetbrains.kotlinx", name = "kotlinx-coroutines-core", version = "1.4.3")

      // Apache Commons
      implementation(group = "commons-io", name = "commons-io", version = "2.8.0")
      implementation(group = "org.apache.commons", name = "commons-text", version = "1.9")
      implementation(group = "org.apache.commons", name = "commons-collections4", version = "4.4")

      // Clients
      implementation(group = "com.squareup.okhttp3", name = "okhttp", version = "4.9.1")
      implementation(group = "com.neovisionaries", name = "nv-websocket-client", version = "2.14")
      implementation(group = "org.apache.httpcomponents.client5", name = "httpclient5", version = "5.0.3")

      implementation(group = "com.google.code.gson", name = "gson", version = "2.8.6")
    }
    implementation(group = "org.jetbrains", name = "annotations", version = "20.1.0")
    // Test
    testImplementation(platform("org.junit:junit-bom:5.7.1"))
    testImplementation(group = "org.junit.jupiter", name = "junit-jupiter")
    testImplementation(group = "org.mockito", name = "mockito-core", version = "3.11.0")
    testImplementation(group = "org.mockito", name = "mockito-junit-jupiter", version = "3.9.0")
    testImplementation(group = "ch.qos.logback", name = "logback-classic", version = "1.2.3")
  }

  publishing {
    repositories {
      maven {
        name = "maven"
        url = uri(project.mavenRepositoryUrl)
        credentials {
          username = project.mavenRepositoryUsername
          password = project.mavenRepositoryPassword
        }
      }
    }

    publications {
      create<MavenPublication>("main") {
        from(components["java"])
        pom.default()
      }
    }
  }

  signing {
    useGpgCmd()
    sign(publishing.publications["main"])
  }

  // Source encoding
  tasks {
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
      kotlinOptions.jvmTarget = "1.8"
    }

    // javadoc / html5 support
    withType<Javadoc> {
      if (JavaVersion.current().isJava9Compatible) {
        (options as StandardJavadocDocletOptions).addBooleanOption("html5", true)
      }
    }

    // jar artifact id and version
    withType<Jar> {
      dependsOn(project.tasks.manifest)
      manifest.from(File(buildDir, "resources/main/META-INF/MANIFEST.MF"))
    }

    // compile options
    withType<JavaCompile> {
      options.encoding = "UTF-8"
    }

    // javadoc & delombok
    val delombok by getting(io.freefair.gradle.plugins.lombok.tasks.Delombok::class)
    javadoc {
      dependsOn(delombok)
      source(delombok)
      options {
        title = "${base.archivesBaseName} (v${project.version})"
        windowTitle = "${base.archivesBaseName} (v${project.version})"
        encoding = "UTF-8"
      }
    }

    // test
    test {
      doFirst {
        var argv = listOf("--add-opens", "java.base/java.lang=ALL-UNNAMED", "--illegal-access=warn")
        if (JavaVersion.current().isJava9Compatible) {
          if (jvmArgs != null && jvmArgs!!.isNotEmpty()) {
            argv = jvmArgs!! + argv
          }
          jvmArgs = argv
        }
      }
      useJUnitPlatform {
//        includeTags("unittest")
//        excludeTags("integration")
      }
    }
  }
}
