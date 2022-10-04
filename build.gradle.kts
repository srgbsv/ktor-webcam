val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project

plugins {
    application
    kotlin("jvm") version "1.7.10"
    id("io.ktor.plugin") version "2.1.0"
}

group = "com.devijulias"
version = "0.0.1"
application {
    mainClass.set("com.devijulias.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core-jvm:$ktor_version")
    //implementation("io.ktor:ktor-server-tomcat-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-cio-jvm:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    testImplementation("io.ktor:ktor-server-tests-jvm:$ktor_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
    runtimeOnly("io.ktor:ktor-utils:$ktor_version")
    // https://mvnrepository.com/artifact/org.jetbrains.kotlinx/kotlinx-datetime-jvm
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")
    // https://mvnrepository.com/artifact/org.bytedeco/javacv-platform
    implementation("org.bytedeco:javacv-platform:1.5.7")
}