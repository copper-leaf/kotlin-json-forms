plugins {
    id("com.android.application")
    kotlin("multiplatform")
    `copper-leaf-base`
    `copper-leaf-version`
    `copper-leaf-lint`
    `copper-leaf-publish`
    id("org.jetbrains.compose")
}

description = "Opinionated Application State Management framework for Kotlin Multiplatform"

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
    // targets
    jvm { }
    android {
    }

    // sourcesets
    sourceSets {
        all {
            languageSettings.apply {
            }
        }

        // Common Sourcesets
        val commonMain by getting {
            dependencies {
                api(project(":json-forms-compose"))
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))

                implementation("io.kotest:kotest-runner-junit5:5.3.0")
                implementation("io.kotest:kotest-assertions-core:5.3.0")
                implementation("io.kotest:kotest-property:5.3.0")
                implementation("io.kotest:kotest-framework-datatest:5.3.0")
            }
        }

        @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
        val jvmMain by getting {
            dependencies {
                api(compose.desktop.currentOs)
                api(compose.desktop.components.splitPane)
            }
        }
        val jvmTest by getting {
            dependencies {
            }
        }

        // Android JVM Sourcesets
        val androidMain by getting {
            dependencies {
                api(libs.androidx.compose.activity)
            }
        }
        val androidAndroidTestRelease by getting { }
        val androidTest by getting {
            dependsOn(androidAndroidTestRelease)
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

compose.desktop {
    application {
        mainClass = "com.copperleaf.forms.example.desktop.MainKt"
    }
}
