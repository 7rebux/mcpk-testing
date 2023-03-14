@file:Suppress("SpellCheckingInspection")

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("com.github.johnrengelman.shadow") version "7.1.2"
    kotlin("jvm") version "1.8.10"
}

group = "de.nosswald"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
}

dependencies {
    compileOnly("org.bukkit:bukkit:1.8.8-R0.1-SNAPSHOT")
    compileOnly("com.github.azbh111:craftbukkit-1.8.8:R")

    shadow("org.jetbrains.kotlin:kotlin-stdlib")

    testImplementation("org.junit.jupiter:junit-jupiter-params:5.1.0")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.named<ShadowJar>("shadowJar") {
    manifest {
        attributes(mapOf("Main-Class" to "$group/server/Plugin"))
    }

    archiveBaseName.set("mcpk-testing")
    archiveClassifier.set("")
    destinationDirectory.set(File("./server/plugins"))
}

tasks.build {
    dependsOn("shadowJar")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}