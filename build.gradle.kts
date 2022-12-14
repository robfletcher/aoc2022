import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  kotlin("jvm") version "1.8.0-Beta"
  id("com.bnorm.power.kotlin-power-assert") version "0.12.0"
  kotlin("plugin.serialization") version "1.8.0-Beta"
  application
}

repositories {
  mavenCentral()
}

dependencies {
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
  implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")
}

tasks.withType<KotlinCompile> {
  kotlinOptions {
    languageVersion = "1.8"
  }
}

configure<com.bnorm.power.PowerAssertGradleExtension> {
  functions = listOf("kotlin.assert", "kotlin.check", "kotlin.require")
}

application {
  mainClass.set("MainKt")
  applicationDefaultJvmArgs = listOf("-ea")
}
