plugins { id("mschout.all-conventions") }

group = "io.github.mschout"

val gitVersion: groovy.lang.Closure<String> by extra

version = gitVersion()

repositories { mavenCentral() }

dependencies {
  testImplementation(kotlin("test"))
  testImplementation(libs.kotest.runner.junit5)
  testImplementation(libs.kotest.assertions.core)
}
