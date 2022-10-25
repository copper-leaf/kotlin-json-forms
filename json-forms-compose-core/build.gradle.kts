plugins {
    `copper-leaf-android`
    `copper-leaf-targets`
    `copper-leaf-compose`
    `copper-leaf-base`
    `copper-leaf-version`
    `copper-leaf-testing`
    `copper-leaf-lint`
    `copper-leaf-publish`
}

description = "Kotlin Multiplatform String markup and formatting utility"

kotlin {
    sourceSets {
        // Common Sourcesets
        val commonMain by getting {
            dependencies {
                api(project(":json-forms-core"))
                api(compose.runtime)
            }
        }
        val commonTest by getting {
            dependencies { }
        }

        // plain JVM Sourcesets
        val jvmMain by getting {
            dependencies { }
        }
        val jvmTest by getting {
            dependencies { }
        }

        // Android JVM Sourcesets
        val androidMain by getting {
            dependencies { }
        }
        val androidTest by getting {
            dependencies { }
        }
    }
}
