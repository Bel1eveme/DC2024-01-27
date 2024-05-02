plugins {
    kotlin("jvm") version "1.9.22"
    id("io.ktor.plugin") version "2.3.8"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.22"
}

group = "by.bashlikovvv"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-test")

    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}