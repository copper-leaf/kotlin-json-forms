plugins {
    id("com.android.library")
    kotlin("multiplatform")
    `copper-leaf-base`
    `copper-leaf-version`
    `copper-leaf-lint`
    `copper-leaf-publish`
    id("org.jetbrains.compose")
}

description = "Opinionated Application State Management framework for Kotlin Multiplatform"

@Suppress("UnstableApiUsage")
android {
    compileSdk = 31
    defaultConfig {
        minSdk = 24
        targetSdk = 31
        testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        val release by getting {
            isMinifyEnabled = false
        }
    }
    sourceSets {
        getByName("main") {
            setRoot("src/androidMain")
        }
        getByName("androidTest") {
            setRoot("src/androidTest")
        }
    }
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
            isReturnDefaultValues = true
        }
    }
    lint {
        disable("GradleDependency")
    }
}

kotlin {
    explicitApi()

    // targets
    jvm { }
    android {
        publishAllLibraryVariants()
    }
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
            languageSettings {
                optIn("kotlin.RequiresOptIn")
                optIn("kotlin.ExperimentalStdlibApi")
                optIn("kotlin.time.ExperimentalTime")
                optIn("kotlinx.coroutines.ExperimentalCoroutinesApi")
            }
        }

        // Common Sourcesets
        val commonMain by getting {
            dependencies {
                api(project(":json-forms-core"))
                api(compose.runtime)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }

        val jvmMain by getting {
            dependencies {
            }
        }
        val jvmTest by getting {
            dependencies {
            }
        }

        // Android JVM Sourcesets
        val androidMain by getting {
            dependencies {
            }
        }
        val androidAndroidTestRelease by getting { }
        val androidTest by getting {
            dependsOn(androidAndroidTestRelease)
            dependencies {
            }
        }

        val jsMain by getting {
            dependencies {
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
