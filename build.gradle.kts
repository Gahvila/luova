plugins {
    java
    id("com.gradleup.shadow") version "8.3.5"
}

group = "net.gahvila"
version = "1.0-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_21

repositories {
    mavenLocal()
    mavenCentral()
    maven { url = uri("https://repo.papermc.io/repository/maven-public/") }
}

dependencies {
    compileOnly ("io.papermc.paper:paper-api:1.21.3-R0.1-SNAPSHOT")
}

tasks {
    processResources {
        val props = mapOf("version" to project.version)
        inputs.properties(props)
        filteringCharset = "UTF-8"
        filesMatching("paper-plugin.yml") {
            expand(props)
        }
    }
    withType<JavaCompile> {
        options.encoding = "UTF-8"
    }
    build {
        dependsOn(shadowJar)
    }
    assemble {
        dependsOn(shadowJar)
    }
    shadowJar {
        archiveFileName.set("${rootProject.name}-${version}.jar")

        relocate("dev.jorel.commandapi", "net.gahvila.survival.shaded.commandapi")
        relocate("de.leonhard.storage", "net.gahvila.survival.shaded.storage")
        relocate ("com.github.stefvanschie.inventoryframework", "net.gahvila.survival.shaded.inventoryframework")
    }

    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(21))
        }
    }
}