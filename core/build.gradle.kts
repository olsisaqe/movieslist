import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
}

kotlin {
    val serialization_version = "0.13.0"
    val ktor_version = "1.2.4"
    val coroutines_version = "1.3.2"

    //select iOS target platform depending on the Xcode environment variables
    val iOSTarget: (String, KotlinNativeTarget.() -> Unit) -> KotlinNativeTarget =
        if (System.getenv("SDK_NAME")?.startsWith("iphoneos") == true)
            ::iosArm64
        else
            ::iosX64

    iOSTarget("ios") {
        binaries {
            framework {
                baseName = "core"
            }
        }
    }
    jvm("android")

    sourceSets["commonMain"].dependencies {
        api("org.jetbrains.kotlin:kotlin-stdlib-common")
        api("org.jetbrains.kotlinx:kotlinx-serialization-runtime-common:$serialization_version")
        api("org.jetbrains.kotlinx:kotlinx-coroutines-core-common:$coroutines_version")
        implementation("io.ktor:ktor-client-core:$ktor_version")
        implementation("io.ktor:ktor-client-json:$ktor_version")
        implementation("io.ktor:ktor-client-logging:$ktor_version")
        implementation("io.ktor:ktor-client-serialization:$ktor_version")
    }

    sourceSets["androidMain"].dependencies {
        api("org.jetbrains.kotlin:kotlin-stdlib")
        api("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutines_version")
        api("org.jetbrains.kotlinx:kotlinx-serialization-runtime:$serialization_version")
        api("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutines_version")
        implementation("io.ktor:ktor-client-okhttp:$ktor_version")
        implementation("io.ktor:ktor-client-json-jvm:$ktor_version")
        implementation("io.ktor:ktor-client-logging-jvm:$ktor_version")
        implementation("io.ktor:ktor-client-serialization-jvm:$ktor_version")
    }

    sourceSets["iosMain"].dependencies {
        api("org.jetbrains.kotlinx:kotlinx-serialization-runtime-native:$serialization_version")
        api("org.jetbrains.kotlinx:kotlinx-coroutines-core-native:$coroutines_version")
        implementation("io.ktor:ktor-client-ios:$ktor_version")
        implementation("io.ktor:ktor-client-json-native:$ktor_version")
        implementation("io.ktor:ktor-client-logging-native:$ktor_version")
        implementation("io.ktor:ktor-client-serialization-native:$ktor_version")
    }
}


val packForXcode by tasks.creating(Sync::class) {
    group = "build"

    //selecting the right configuration for the iOS framework depending on the Xcode environment variables
    val mode = System.getenv("CONFIGURATION") ?: "DEBUG"
    val framework = kotlin.targets.getByName<KotlinNativeTarget>("ios").binaries.getFramework(mode)

    inputs.property("mode", mode)
    dependsOn(framework.linkTask)

    val targetDir = File(buildDir, "xcode-frameworks")
    from({ framework.outputDirectory })
    into(targetDir)

    doLast {
        val gradlew = File(targetDir, "gradlew")
        gradlew.writeText(
            "#!/bin/bash\nexport 'JAVA_HOME=${System.getProperty("java.home")}'\ncd '${rootProject.rootDir}'\n./gradlew \$@\n"
        )
        gradlew.setExecutable(true)
    }
}

tasks.register<Exec>("iosLaunchSimulator") {
    dependsOn("iosInstallSimulator")
    executable
}

tasks.getByName("build").dependsOn(packForXcode)