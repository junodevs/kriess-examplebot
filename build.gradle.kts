import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    idea
    `application`

    kotlin("jvm") version "1.4.10"
    id("com.github.johnrengelman.shadow") version "6.0.0"
}

group = "tech.junodevs.examplebot"
version = "0.1.0"

application {
    mainClassName = "tech.junodevs.examplebot.ExampleBotKt"
}

repositories {
    mavenCentral()
    jcenter()

    maven {
        name = "brettb-repo"
        url = uri("https://repo.brettb.xyz")
    }
}

dependencies {
    listOf("stdlib-jdk8", "reflect").forEach { implementation(kotlin(it)) }

    // JDA + Command Handler
    implementation("net.dv8tion:JDA:4.2.0_207")
    implementation("tech.junodevs.discord:kriess:0.2.0")

    // Logger
    implementation("ch.qos.logback:logback-classic:1.2.3")

    // Utilities
    implementation("org.yaml:snakeyaml:1.27")
}

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions.jvmTarget = "1.8"

tasks.withType<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar> {
    archiveClassifier.set("")
}