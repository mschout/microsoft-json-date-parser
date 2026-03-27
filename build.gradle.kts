import com.vanniktech.maven.publish.JavadocJar
import com.vanniktech.maven.publish.KotlinJvm

plugins {
  id("mschout.all-conventions")
  alias(libs.plugins.dokka)
  alias(libs.plugins.maven.publish)
}

group = "io.github.mschout"

val gitVersion: groovy.lang.Closure<String> by extra

version = gitVersion()

repositories { mavenCentral() }

dependencies {
  testImplementation(kotlin("test"))
  testImplementation(libs.kotest.runner.junit5)
  testImplementation(libs.kotest.assertions.core)
}

mavenPublishing {
  configure(KotlinJvm(javadocJar = JavadocJar.Dokka("dokkaGenerateModuleHtml")))

  publishToMavenCentral()

  signAllPublications()

  coordinates(group.toString(), "microsoft-json-date-parser", version.toString())

  pom {
    name.set("Jackson Datatype MS Date")
    description.set(
        "Java/Kotlin OffsetDateTime parser for Microsoft JSON date format (/Date(ticks+offset)/) strings"
    )
    url.set("https://github.com/mschout/microsoft-json-date-parser")

    licenses {
      license {
        name.set("The Apache License, Version 2.0")
        url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
      }
    }

    developers {
      developer {
        id.set("mschout")
        name.set("Michael Schout")
        url.set("https://github.com/mschout")
      }
    }

    scm {
      url.set("https://github.com/mschout/microsoft-json-date-parser")
      connection.set("scm:git:git://github.com/mschout/microsoft-json-date-parser.git")
      developerConnection.set("scm:git:ssh://git@github.com/mschout/microsoft-json-date-parser.git")
    }
  }
}
