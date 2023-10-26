plugins {
    val kotlinVersion = "1.8.10"
    kotlin("jvm") version kotlinVersion
    id("net.mamoe.mirai-console") version "2.16.0"
    kotlin("plugin.serialization") version kotlinVersion
    //kotlin("js") version "1.4.20"
    //id("org.jetbrains.kotlin.plugin.serialization") version kotlinVersion
}
val ktor_version  = "2.3.5";

group = "io.huvz"
version = "0.1.0"

repositories {
    maven("https://maven.aliyun.com/repository/public")
    mavenCentral()
}
dependencies{
    // https://mvnrepository.com/artifact/org.graalvm.js/js
    implementation ("io.ktor:ktor-client-okhttp:$ktor_version")
    implementation ("io.ktor:ktor-client-auth:$ktor_version")
    implementation( "io.ktor:ktor-client-core:$ktor_version")
    implementation ("io.ktor:ktor-client-logging:$ktor_version")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor_version")
    implementation("io.ktor:ktor-client-cio:$ktor_version")
    implementation("io.ktor:ktor-client-content-negotiation:$ktor_version")

    // https://mvnrepository.com/artifact/org.jsoup/jsoup
    implementation("org.jsoup:jsoup:1.16.2")
    implementation("org.bouncycastle:bcpkix-jdk15on:1.69")
    implementation("org.bouncycastle:bcprov-jdk15on:1.69")
    implementation("io.ktor:ktor-client-okhttp-jvm:2.3.5")
}
mirai {
    jvmTarget = JavaVersion.VERSION_17
}
