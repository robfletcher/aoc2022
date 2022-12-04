plugins {
  kotlin("jvm") version "1.8.0-Beta" apply false
  id("com.bnorm.power.kotlin-power-assert") version "0.12.0" apply false
}

allprojects {
  repositories {
    mavenCentral()
  }
}

subprojects {
  apply(plugin = "org.jetbrains.kotlin.jvm")
  apply(plugin = "org.gradle.application")
  apply(plugin = "com.bnorm.power.kotlin-power-assert")

  configure<com.bnorm.power.PowerAssertGradleExtension> {
    functions = listOf("kotlin.assert", "kotlin.check", "kotlin.require")
  }

  configure<JavaApplication> {
    mainClass.set("MainKt")
    applicationDefaultJvmArgs += "-ea"
  }
}
