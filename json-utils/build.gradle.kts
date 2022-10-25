plugins {
    `copper-leaf-android`
    `copper-leaf-targets`
    `copper-leaf-base`
    `copper-leaf-version`
    `copper-leaf-testing`
//    `copper-leaf-lint`
    `copper-leaf-publish`
}

description = "Kotlin Multiplatform String markup and formatting utility"

kotlin {
    sourceSets {
        // Common Sourcesets
        val commonMain by getting {
            dependencies {
                api("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.3")
            }
        }
        val commonTest by getting {
            dependencies { }
        }

        // plain JVM Sourcesets
        val jvmMain by getting {
            dependencies {
                implementation("org.apache.commons:commons-lang3:3.12.0")
                implementation("net.pwall.json:json-kotlin-schema:0.35")
            }
        }
        val jvmTest by getting {
            dependencies { }
        }

        // Android JVM Sourcesets
        val androidMain by getting {
            dependencies {
                implementation("org.apache.commons:commons-lang3:3.12.0")
                implementation("net.pwall.json:json-kotlin-schema:0.35")
            }
        }
        val androidTest by getting {
            dependencies { }
        }

        // JS Sourcesets
        val jsMain by getting {
            dependencies {
                implementation(npm("ajv", "8.11.0", generateExternals = false))
                implementation(npm("ajv-formats", "2.1.1", generateExternals = false))
            }
        }
        val jsTest by getting {
            dependencies { }
        }
    }
}
