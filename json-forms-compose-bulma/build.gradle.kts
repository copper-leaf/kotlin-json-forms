plugins {
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
            dependencies { }
        }
        val commonTest by getting {
            dependencies { }
        }

        // JS Sourcesets
        val jsMain by getting {
            dependencies {
                api(project(":json-forms-core"))
                api(project(":json-forms-compose-core"))
                api(compose.web.core)
            }
        }
        val jsTest by getting {
            dependencies { }
        }
    }
}
