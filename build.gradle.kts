val ktorVersion: String by extra("2.3.13")
val kotlinVersion: String by extra("1.9.22")
val logbackVersion: String by extra("1.5.18")
val exposedVersion: String by extra("0.48.0")
val mariaDBVersion: String by extra("3.5.3")
val hickariCPVersion: String by extra("6.3.0")

plugins {
    kotlin("jvm") version "1.9.22"

    id("io.ktor.plugin") version "3.1.2"
    id("org.jetbrains.kotlin.plugin.serialization") version "2.1.20"
}

group = "com.test.ktor"
version = "0.0.1"
application {
    mainClass.set("com.test.ktor.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-auth-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-resources:$ktorVersion")
    implementation("io.ktor:ktor-server-core:$ktorVersion")
    implementation("io.ktor:ktor-server-cors-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-swagger:$ktorVersion")
    implementation("io.ktor:ktor-server-html-builder:$ktorVersion")
    implementation("io.ktor:ktor-server-call-logging-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-content-negotiation-jvm:$ktorVersion")
    implementation("io.ktor:ktor-serialization-jackson-jvm:$ktorVersion")
    implementation("io.ktor:ktor-serialization-jackson-jvm:$ktorVersion")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
    implementation("io.ktor:ktor-server-netty-jvm:$ktorVersion")
    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    implementation(kotlin("stdlib-jdk8"))

    implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-dao:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
    implementation("org.mariadb.jdbc:mariadb-java-client:$mariaDBVersion")
    implementation("com.zaxxer:HikariCP:$hickariCPVersion")

    implementation("io.ktor:ktor-server-openapi:$ktorVersion")

    testImplementation("io.ktor:ktor-server-tests-jvm:${ktorVersion}")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:${kotlinVersion}")
}

kotlin {
    jvmToolchain(17)
}
