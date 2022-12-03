plugins {
  kotlin("jvm") version "1.7.21"
  id("com.bnorm.power.kotlin-power-assert") version "0.12.0"
}

repositories {
  mavenCentral()
}

configure<com.bnorm.power.PowerAssertGradleExtension> {
  functions = listOf("kotlin.assert", "kotlin.check", "kotlin.require")
}
