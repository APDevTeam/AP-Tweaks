plugins {
    `java-library`
    `maven-publish`
    id("io.github.apdevteam.github-packages") version "1.2.2"
}

repositories {
    gradlePluginPortal()
    mavenLocal()
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven { githubPackage("apdevteam/movecraft")(this) }
}

dependencies {
    api("org.jetbrains:annotations-java5:24.1.0")
    compileOnly("org.spigotmc:spigot-api:1.14.4-R0.1-SNAPSHOT")
    compileOnly("net.countercraft:movecraft:+")
}

group = "io.github.apdevteam"
version = System.getenv("RELEASE_VERSION")?.takeIf { it.isNotBlank() }
    ?: runCatching {
        val sha = ProcessBuilder("git", "rev-parse", "--short", "HEAD")
            .start().inputStream.bufferedReader().readLine() ?: "unknown"
        val tag = ProcessBuilder("git", "describe", "--tags", "--abbrev=0")
            .start().inputStream.bufferedReader().readLine() ?: "untagged"
        val dirty = ProcessBuilder("git", "status", "--porcelain")
            .start().inputStream.bufferedReader().readLine() != null
        if (dirty) "$tag+$sha-dirty" else "$tag+$sha"
    }.getOrElse { "unknown" }
description = "AP-Tweaks"
java.toolchain.languageVersion = JavaLanguageVersion.of(21)

tasks.jar {
    archiveBaseName.set("AP-Tweaks")
    archiveClassifier.set("")
    archiveVersion.set("")
}

tasks.processResources {
    from(rootProject.file("LICENSE.md"))
    filesMatching("*.yml") {
        expand(mapOf("projectVersion" to project.version))
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "io.github.apdevteam"
            artifactId = "AP-Tweaks"
            version = "${project.version}"

            artifact(tasks.jar)
        }
    }
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/apdevteam/movecraft-coreprotect")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }
}
