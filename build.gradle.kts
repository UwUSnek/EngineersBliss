plugins {
    id("dev.kikugie.loom-back-compat")  // Applies the correct loom variant based on the Minecraft version
    id("maven-publish")                 // Maven publishing
}




//! DO NOT set group = ...!
version = "${property("mod.version")}+${sc.current.version}"
base.archivesName = property("mod.id") as String

val requiredJava: JavaVersion = when {
    sc.current.parsed >= "26.1"   -> JavaVersion.VERSION_25
    sc.current.parsed >= "1.20.5" -> JavaVersion.VERSION_21
    sc.current.parsed >= "1.18"   -> JavaVersion.VERSION_17
    sc.current.parsed >= "1.17"   -> JavaVersion.VERSION_16
    else                          -> JavaVersion.VERSION_1_8
}




repositories {
    /**
     * Restricts dependency search of the given [groups] to the [maven URL][url],
     * improving the setup speed.
     */
    fun strictMaven(url: String, alias: String, vararg groups: String) = exclusiveContent {
        forRepository { maven(url) { name = alias } }
        filter { groups.forEach(::includeGroup) }
    }
    strictMaven("https://www.cursemaven.com",     "CurseForge", "curse.maven")
    strictMaven("https://api.modrinth.com/maven", "Modrinth",   "maven.modrinth")
    maven { url = uri("https://jitpack.io") }
    mavenCentral()
}




//! Can use `mod{dependency type}` even on 26.1+ - loom-back-compat converts them
dependencies {

    // Minecraft
    minecraft("com.mojang:minecraft:${sc.current.version}") // Base Minecraft
    loomx.applyMojangMappings()                             // Applies Mojang Mappings on obfuscated versions

    // Bundled dependencies
    implementation("com.github.weisj:jsvg:2.1.0")
    include("com.github.weisj:jsvg:2.1.0")

    // Required dependencies
    implementation("net.fabricmc:fabric-loader:${property("deps.fabric_loader")}")
    implementation("net.fabricmc.fabric-api:fabric-api:${property("deps.fabric-api")}")

    // Optional dependencies
    compileOnly("maven.modrinth:sodium:mc26.1.1-0.8.9-fabric")
}




loom {
    splitEnvironmentSourceSets()
    mods {
        create("engineers-bliss") {
            sourceSet(sourceSets["main"])
            sourceSet(sourceSets["client"])
        }
    }

    fabricModJsonPath = rootProject.file("src/main/resources/fabric.mod.json") // Useful for interface injection
    // accessWidenerPath = sc.process( //TODO access wideners are disabled rn. prob not gonna need them though
    //     rootProject.file("src/main/resources/template.ct"),
    //     "build/processed.ct"
    // )

    decompilerOptions.named("vineflower") {
        options.put("mark-corresponding-synthetics", "1") // Adds names to lambdas - useful for mixins
    }

    runConfigs.all {
        preferGradleTask = true
        generateRunConfig = true
        runDirectory = project.file("run")              // Separate run directories. Keep one per distinct version
        jvmArguments.add("-Dmixin.debug.export=true")   // Export transformed classes for debugging
    }
}




java {
    withSourcesJar()
    targetCompatibility = requiredJava
    sourceCompatibility = requiredJava

    toolchain {
        vendor = JvmVendorSpec.ADOPTIUM
        languageVersion = JavaLanguageVersion.of(requiredJava.majorVersion)
    }
}




tasks {
    processResources {
        fun MutableMap<String, String>.register(key: String, property: String) {
            val value: String = sc.properties[property]
            inputs.property(key, value)
            set(key, value)
        }

        val props = buildMap {
            register("id",           "mod.id")
            register("name",         "mod.name")
            register("version",      "mod.version")
            register("description",  "mod.description")

            put("java_version",         requiredJava.majorVersion)
            register("minecraft",       "mod.minecraft")
            register("fabric_loader",   "deps.fabric_loader")
            register("fabric_api",      "deps.fabric-api")
            register("sodium_version",  "deps.sodium")
            register("modmenu_version", "deps.modmenu")
        }
        filesMatching("fabric.mod.json") { expand(props) }

        val mixinJava = "JAVA_${requiredJava.majorVersion}"
        filesMatching("*.mixins.json") { expand("java" to mixinJava) }
    }


    // Expand java version for the Client. //! processResources processes Main only.
    named<ProcessResources>("processClientResources") {
        val mixinJava = "JAVA_${requiredJava.majorVersion}"
        filesMatching("*.mixins.json") { expand("java" to mixinJava) }
    }


    // Includes the license file in the built mod
    withType<Jar> {
        val name = project.property("mod.id")
        inputs.property("mod_id", name)
        from("../../LICENSE") { rename { "$it-$name" } }
    }


    register<Copy>("buildAndCollect") {
        group = "build"
        description = "Builds mod jars and copies results to `build/libs/{mod version}/`"

        inputs.property("version", project.property("mod.version"))
        // loomx.mod(Sources)Jar returns the jar task for the applied loom variant
        from(loomx.modJar.flatMap { it.archiveFile }, loomx.modSourcesJar.flatMap { it.archiveFile })
        into(rootProject.layout.buildDirectory.file("libs/${project.property("mod.version")}"))
    }
}






//FIXME do something about this? we have split sources
// loom {
//     splitEnvironmentSourceSets()
//     mods {
//         create("engineers-bliss") {
//             sourceSet(sourceSets["main"])
//             sourceSet(sourceSets["client"])
//         }
//     }
// }


// Maven publication
publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }
    repositories {
        // add publish repos here
    }
}
// // This can be used for publishing on Modrinth and Curseforge
// val compatibleVersions: List<String> = sc.properties.rawOrNull("mod", "mc_releases")
//     ?.asList().orEmpty().map { it.toString() }