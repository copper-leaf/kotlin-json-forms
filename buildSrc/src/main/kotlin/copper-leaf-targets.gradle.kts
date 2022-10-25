@file:Suppress("UNUSED_VARIABLE")

import org.gradle.accessors.dm.LibrariesForLibs

plugins {
    kotlin("multiplatform")
}

val libs = the<LibrariesForLibs>()
val customProperties = Config.customProperties(project)

kotlin {
    explicitApi()

    // targets
    if (customProperties["copperleaf.targets.jvm"] == true) {
        jvm { }
    }
    if (customProperties["copperleaf.targets.android"] == true) {
        android {
            publishAllLibraryVariants()
        }
    }
    if (customProperties["copperleaf.targets.js"] == true) {
        js(BOTH) {
            browser {
                testTask {
                    enabled = false
                }
            }
        }
    }
    if (customProperties["copperleaf.targets.ios"] == true) {
        nativeTargetGroup(
            "ios",
            iosArm32(),
            iosArm64(),
            iosX64(),
            iosSimulatorArm64(),
        )
    }

    // sourcesets
    sourceSets {
        all {
            languageSettings.apply {
            }
        }

        // Common Sourcesets
        val commonMain by getting {
            dependencies { }
        }
        val commonTest by getting {
            dependencies { }
        }

        if (customProperties["copperleaf.targets.jvm"] == true) {
            val jvmMain by getting {
                dependencies { }
            }
            val jvmTest by getting {
                dependencies { }
            }
        }

        // Android JVM Sourcesets
        if (customProperties["copperleaf.targets.android"] == true) {
            val androidMain by getting {
                dependencies { }
            }
            val androidTest by getting {
                dependencies { }
            }
        }

        if (customProperties["copperleaf.targets.js"] == true) {
            // JS Sourcesets
            val jsMain by getting {
                dependencies { }
            }
            val jsTest by getting {
                dependencies { }
            }
        }

        if (customProperties["copperleaf.targets.ios"] == true) {
            // iOS Sourcesets
            val iosMain by getting {
                dependencies { }
            }
            val iosTest by getting {
                dependencies { }
            }
        }
    }
}

tasks.withType<JavaCompile> {
    sourceCompatibility = Config.javaVersion
    targetCompatibility = Config.javaVersion
}
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = Config.javaVersion
    }
}
