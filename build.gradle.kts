plugins {
  signing
  `java-library`
  `maven-publish`
  kotlin("jvm") version "1.5.21"
  id("io.freefair.lombok") version "6.1.0-m3"
  id("org.jetbrains.dokka") version "1.5.0"
  id("com.coditory.manifest") version "0.1.14"
  id("com.github.johnrengelman.shadow") version "7.0.0"
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
  apply(plugin = "com.github.johnrengelman.shadow")

  base {
    archivesName.set(artifactId)
  }

  lombok {
    version.set("1.18.20")
  }

  // Source Compatibility
  java {
    modularity.inferModulePath.set(true)
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
    withSourcesJar()
    withJavadocJar()
  }

  dependencies {
    constraints {
      // Reactive Streams / Coroutines
      implementation(group = "io.reactivex.rxjava3", name = "rxjava", version = "3.0.13")
      implementation(group = "io.projectreactor", name = "reactor-core", version = "3.4.8")
      implementation(group = "org.jetbrains.kotlinx", name = "kotlinx-coroutines-core", version = "1.5.1")

      // Apache Commons
      implementation(group = "commons-io", name = "commons-io", version = "2.11.0")
      implementation(group = "org.apache.commons", name = "commons-text", version = "1.9")
      implementation(group = "org.apache.commons", name = "commons-collections4", version = "4.4")

      // Clients
      implementation(group = "com.squareup.okhttp3", name = "okhttp", version = "4.9.1")
      implementation(group = "org.apache.httpcomponents.client5", name = "httpclient5", version = "5.1")

      implementation(group = "com.google.code.gson", name = "gson", version = "2.8.7")
    }
    implementation(group = "org.jetbrains", name = "annotations", version = "21.0.1")
    // Test
    testImplementation(platform("org.junit:junit-bom:5.7.2"))
    testImplementation(group = "org.junit.jupiter", name = "junit-jupiter")
    testImplementation(group = "org.mockito", name = "mockito-core", version = "3.11.2")
    testImplementation(group = "org.mockito", name = "mockito-junit-jupiter", version = "3.11.2")
    testImplementation(group = "ch.qos.logback", name = "logback-classic", version = "1.2.5")
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

    // shadowjar & relocation
    val relocateShadowJar by creating(com.github.jengelman.gradle.plugins.shadow.tasks.ConfigureShadowRelocation::class) {
      target = shadowJar.get()
      prefix = "com.github.twitch4j.clients.shadow.${"v$version".replace(".", "_")}"
    }

    // jar artifact id and version
    withType<Jar> {
      if (this is com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar) {
        dependsOn(relocateShadowJar)
        archiveClassifier.set("shaded")
        assemble.configure { dependsOn(this@withType) }
      }
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
        title = "${base.archivesName.get()} (v${project.version})"
        windowTitle = "${base.archivesName.get()} (v${project.version})"
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
