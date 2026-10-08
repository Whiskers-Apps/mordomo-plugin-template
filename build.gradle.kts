plugins {
    kotlin("jvm") version "2.3.21"
    alias(libs.plugins.kotlin.serialization)
}

group = "org.example"
version = "1.0.0"

repositories {
    mavenLocal()
    mavenCentral()
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    testImplementation(kotlin("test"))
    implementation(libs.kotlinx.serialization.json)

    // Repo
    implementation("com.github.Whiskers-Apps:mordomo-core:1.0.1")

// Local
//    implementation("org.whiskersapps:mordomo-core:1.0.1")
}

kotlin {
    jvmToolchain(21)
}

tasks.withType<Jar> {
    archiveFileName.set("plugin.jar")

    manifest {
        attributes["Main-Class"] = "MainKt"
    }

    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    from(configurations.runtimeClasspath.get().filter { it.exists() }.map { if (it.isDirectory) it else zipTree(it) })
}

tasks.test {
    useJUnitPlatform()
}