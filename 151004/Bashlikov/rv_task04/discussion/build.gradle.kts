
val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project

val koin_version: String = "3.6.0-wasm-alpha2"

val cassandra_core_version: String by project

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
    implementation(project(":core"))

    implementation("io.ktor:ktor-server-core-jvm")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm")
    implementation("io.ktor:ktor-server-content-negotiation-jvm")
    implementation("io.ktor:ktor-server-netty-jvm")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    testImplementation("io.ktor:ktor-server-tests-jvm")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
    implementation("io.ktor:ktor-client-core:$ktor_version")
    implementation("io.ktor:ktor-client-cio:$ktor_version")
    implementation("io.ktor:ktor-client-logging:$ktor_version")
    implementation("io.ktor:ktor-server-double-receive:$ktor_version")

    implementation("io.insert-koin:koin-ktor:$koin_version")
    implementation("io.insert-koin:koin-logger-slf4j:$koin_version")

    implementation("com.datastax.cassandra:cassandra-driver-core:3.0.8")
    implementation("com.datastax.oss:java-driver-core:4.0.1")
    implementation("com.ing.data:cassandra-jdbc-wrapper:4.11.1")

    implementation("org.apache.kafka:kafka-clients:3.3.1")
}
