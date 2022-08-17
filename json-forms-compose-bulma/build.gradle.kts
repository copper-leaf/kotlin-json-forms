plugins {
    kotlin("multiplatform")
    `copper-leaf-base`
    `copper-leaf-version`
    `copper-leaf-lint`
    `copper-leaf-publish`
    id("org.jetbrains.compose")
}

kotlin {
    explicitApi()

    // targets
    js(IR) {
        browser {
            testTask {
                enabled = false
            }
        }
    }

    // sourcesets
    sourceSets {
        all {
            languageSettings.apply {
                optIn("kotlin.RequiresOptIn")
                optIn("kotlin.ExperimentalStdlibApi")
                optIn("kotlin.time.ExperimentalTime")
                optIn("kotlinx.coroutines.ExperimentalCoroutinesApi")
            }
        }

        // Common Sourcesets
        val commonMain by getting {
            dependencies {
            }
        }
        val commonTest by getting {
            dependencies {
            }
        }

        val jsMain by getting {
            dependencies {
                api(project(":json-forms-core"))
                api(project(":json-forms-compose-core"))
                api(compose.web.core)
            }
        }
        val jsTest by getting {
            dependencies {
            }
        }
    }
}

tasks.withType<JavaCompile> {
    sourceCompatibility = Config.javaVersion
    targetCompatibility = Config.javaVersion
}
tasks.withType<Test> {
    useJUnitPlatform()
    testLogging {
        showStandardStreams = true
    }
}
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = Config.javaVersion
    }
}
