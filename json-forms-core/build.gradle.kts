plugins {
    id("com.android.library")
    kotlin("multiplatform")
    `copper-leaf-base`
    `copper-leaf-version`
    `copper-leaf-lint`
    `copper-leaf-publish`
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
    js(BOTH) {
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
                api(project(":json-utils"))
                api("io.github.copper-leaf:ballast-core:1.2.1")
                api("io.github.copper-leaf:ballast-saved-state:1.2.1")
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
                implementation("io.kotest:kotest-runner-junit5:5.4.2")
                implementation("io.kotest:kotest-assertions-core:5.4.2")
                implementation("io.kotest:kotest-property:5.4.2")
                implementation("io.kotest:kotest-framework-datatest:5.4.2")
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
                implementation("io.kotest:kotest-runner-junit5:5.4.2")
                implementation("io.kotest:kotest-assertions-core:5.4.2")
                implementation("io.kotest:kotest-property:5.4.2")
                implementation("io.kotest:kotest-framework-datatest:5.4.2")
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
