import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.9.21"
    application
}

group = "org.develnext.jphp.ext.discordextension"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven(url = "https://jitpack.io")
}

sourceSets.getByName("main") {
    java.srcDir("src-jvm/main/kotlin")
    resources.srcDir("src-jvm/main/resources")
}
sourceSets.getByName("test") {
    java.srcDir("src-jvm/test/java")
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    implementation("com.github.caoimhebyrne:KDiscordIPC:0.2.2")
    implementation(files("C:\\Program Files (x86)\\DevelNext\\lib\\jphp-runtime.jar"))
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainClassName = "MainKt"
}