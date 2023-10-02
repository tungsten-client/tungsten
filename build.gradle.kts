plugins {
    id("idea")

    id("java")
    id("java-library")

    id("fabric-loom") version "1.3-SNAPSHOT"
    id("com.github.johnrengelman.shadow") version "8.1.1"

    id("maven-publish")
}

version = properties["project_version"]!!
group = properties["maven_group"]!!

repositories {
    mavenLocal()
    mavenCentral()
    maven("https://jitpack.io")

    maven("https://maven.terraformersmc.com/releases")
    maven("https://maven.terraformersmc.com/snapshots")

    maven {
        name = "GithubPackages"
        url = uri("https://maven.pkg.github.com/tungsten-client/tungsten")
        credentials {
            username = project.findProperty("gpr.user") ?: System.getenv("USERNAME")
            password = project.findProperty("gpr.key") ?: System.getenv("TOKEN")
        }
    }
}

enum class IncludeMethod { NOT, SHADOW, INCLUDE }
dependencies {
    fun modDepend(
        includeMethod: IncludeMethod = IncludeMethod.NOT,
        notation: Any
    ) {
        when (includeMethod) {
            IncludeMethod.SHADOW -> shadow(dependencyNotation = notation)
            IncludeMethod.INCLUDE -> include(dependencyNotation = notation)
            else -> {}
        }
        modImplementation(dependencyNotation = notation)
    }

    fun modDepend(
        includeMethod: IncludeMethod = IncludeMethod.NOT,
        notation: String,
        action: ExternalModuleDependency.() -> Unit = {}
    ) {
        when (includeMethod) {
            IncludeMethod.SHADOW -> shadow(dependencyNotation = notation, dependencyConfiguration = action)
            IncludeMethod.INCLUDE -> include(dependencyNotation = notation, dependencyConfiguration = action)
            else -> {}
        }
        modImplementation(dependencyNotation = notation, dependencyConfiguration = action)
    }

    fun modDepend(
        includeMethod: IncludeMethod = IncludeMethod.NOT,
        group: String,
        name: String,
        version: String,
        action: ExternalModuleDependency.() -> Unit = {}
    ) {
        modDepend(includeMethod, "$group:$name:$version", action)
    }

    fun depend(
        includeMethod: IncludeMethod = IncludeMethod.NOT,
        notation: Any
    ) {
        when (includeMethod) {
            IncludeMethod.SHADOW -> shadow(dependencyNotation = notation)
            IncludeMethod.INCLUDE -> include(dependencyNotation = notation)
            else -> {}
        }
        implementation(dependencyNotation = notation)
    }

    fun depend(
        includeMethod: IncludeMethod = IncludeMethod.NOT,
        notation: String,
        action: ExternalModuleDependency.() -> Unit = {}
    ) {
        when (includeMethod) {
            IncludeMethod.SHADOW -> shadow(dependencyNotation = notation, dependencyConfiguration = action)
            IncludeMethod.INCLUDE -> include(dependencyNotation = notation, dependencyConfiguration = action)
            else -> {}
        }
        implementation(dependencyNotation = notation, dependencyConfiguration = action)
    }

    fun depend(
        includeMethod: IncludeMethod = IncludeMethod.NOT,
        group: String,
        name: String,
        version: String,
        action: ExternalModuleDependency.() -> Unit = {}
    ) {
        depend(includeMethod, "$group:$name:$version", action)
    }

    /* minecraft and mappings */
    minecraft(
        group = "com.mojang",
        name = "minecraft",
        version = properties["minecraft_version"].toString()
    )
    mappings("net.fabricmc:yarn:${properties["yarn_mappings"]!!}:v2") // (loom.officialMojangMappings())

    /* fabric */
    modDepend(
        includeMethod = IncludeMethod.NOT,
        group = "net.fabricmc",
        name = "fabric-loader",
        version = properties["dep_fabric_loader"].toString()
    )

    modDepend(
        includeMethod = IncludeMethod.NOT,
        group = "net.fabricmc.fabric-api",
        name = "fabric-api",
        version = properties["dep_fabric_api"].toString()
    )

    /* libs */
    annotationProcessor(
        group = "org.jetbrains",
        name = "annotations",
        version = properties["dep_ann_jbr"].toString()
    )
    depend(
        includeMethod = IncludeMethod.NOT,
        group = "org.jetbrains",
        name = "annotations",
        version = properties["dep_ann_jbr"].toString()
    )

    annotationProcessor(
        group = "org.projectlombok",
        name = "lombok",
        version = properties["dep_ann_lombok"].toString()
    )
    depend(
        includeMethod = IncludeMethod.NOT,
        group = "org.projectlombok",
        name = "lombok",
        version = properties["dep_ann_lombok"].toString()
    )

    depend(
        includeMethod = IncludeMethod.SHADOW,
        group = "com.labymedia",
        name = "ultralight-java-base",
        version = properties["dep_ultralight"].toString()
    )

    depend(
        includeMethod = IncludeMethod.SHADOW,
        group = "com.labymedia",
        name = "ultralight-java-databind",
        version = properties["dep_ultralight"].toString()
    )

    depend(
        includeMethod = IncludeMethod.SHADOW,
        group = "com.labymedia",
        name = "ultralight-java-databind",
        version = properties["dep_ultralight"].toString()
    )

    /* mods */
    modDepend(
        includeMethod = IncludeMethod.NOT,
        group = "com.terraformersmc",
        name = "modmenu",
        version = properties["dep_mod_modmenu"].toString()
    )

    implementation("org.eclipse.jgit:org.eclipse.jgit:6.7.0.202309050840-r")
}

tasks {
    processResources { inputs.property("version", project.version) }
    java {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
        withSourcesJar()
    }

    remapJar {
        dependsOn(":shadowJar")
        inputFile.set(shadowJar.get().archiveFile)
    }

    shadowJar {
        configurations = listOf(project.configurations.shadow.get())
        isZip64 = true
    }

    register<Jar>("generateDevJar") {
        dependsOn(jar)
        from(sourceSets.main.get().output) {
            archiveClassifier.set("dev")
            archiveFileName.set(archiveBaseName.get() + "-" + archiveClassifier.get() + "." + archiveExtension.get())
        }
    }

    build {
        doLast { shadowJar.get().archiveFile.get().asFile.delete() }
    }

}

configurations.shadow {
    isTransitive = false
}

publications {
    gpr(MavenPublication) {
         from(components.java)   
    }
}
}
