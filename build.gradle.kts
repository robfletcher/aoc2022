import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  kotlin("jvm") version "1.8.0-Beta"
  id("com.bnorm.power.kotlin-power-assert") version "0.12.0"
}

repositories {
  mavenCentral()
}

tasks.withType<KotlinCompile> {
  kotlinOptions {
    languageVersion = "1.8"
    allWarningsAsErrors = true
  }
}

configure<com.bnorm.power.PowerAssertGradleExtension> {
  functions = listOf("kotlin.assert", "kotlin.check", "kotlin.require")
}
