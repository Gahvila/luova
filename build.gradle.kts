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
    //paper
    compileOnly ("io.papermc.paper:paper-api:1.21.3-R0.1-SNAPSHOT")

    //plotsquared
    implementation(platform("com.intellectualsites.bom:bom-newest:1.50"))
    compileOnly("com.intellectualsites.plotsquared:plotsquared-core")
    compileOnly("com.intellectualsites.plotsquared:plotsquared-bukkit") { isTransitive = false }

    //commandapi
    compileOnly("dev.jorel:commandapi-annotations:9.6.1")
    implementation("dev.jorel:commandapi-bukkit-shade-mojang-mapped:9.6.1")
    annotationProcessor("dev.jorel:commandapi-annotations:9.6.1")

    //misc
    implementation("com.github.simplix-softworks:simplixstorage:3.2.7")
    implementation ("com.github.stefvanschie.inventoryframework:IF:0.10.18")
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

        relocate("dev.jorel.commandapi", "net.gahvila.luova.shaded.commandapi")
        relocate("de.leonhard.storage", "net.gahvila.luova.shaded.storage")
        relocate ("com.github.stefvanschie.inventoryframework", "net.gahvila.luova.shaded.inventoryframework")
    }

    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(21))
        }
    }
}