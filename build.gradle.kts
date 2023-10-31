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
    implementation("junit:junit:4.13.1")
// https://mvnrepository.com/artifact/org.jetbrains.kotlinx/kotlinx-serialization-json-jvm
    runtimeOnly("org.jetbrains.kotlinx:kotlinx-serialization-json-jvm:1.6.0")
// https://mvnrepository.com/artifact/org.bouncycastle/bcprov-jdk18on
    implementation("org.bouncycastle:bcprov-jdk18on:1.76")



    // https://mvnrepository.com/artifact/org.graalvm.polyglot/polyglot
        implementation("org.graalvm.polyglot:polyglot:23.1.1")
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.squareup.okhttp3:okhttp:4.9.1")
    implementation ("com.squareup.okhttp3:logging-interceptor:4.9.1")

    // https://mvnrepository.com/artifact/org.jsoup/jsoup
    implementation("org.jsoup:jsoup:1.16.2")


}
mirai {
    jvmTarget = JavaVersion.VERSION_17
}
