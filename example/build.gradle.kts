plugins {
    id("com.android.application")
    kotlin("multiplatform")
    `copper-leaf-base`
    `copper-leaf-version`
    `copper-leaf-lint`
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
    // targets
    jvm { }
    android {
    }
    js(IR) {
        browser {
            testTask {
                enabled = false
            }
            binaries.executable()
        }
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
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }

        @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
        val jvmMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation(compose.desktop.components.splitPane)
                implementation(project(":json-forms-compose-material"))
            }
        }
        val jvmTest by getting {
            dependencies {
            }
        }

        // Android JVM Sourcesets
        val androidMain by getting {
            dependencies {
                implementation(libs.androidx.compose.activity)
                implementation(project(":json-forms-compose-material"))
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
                implementation(compose.web.core)
                implementation(project(":json-forms-compose-bulma"))
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
        freeCompilerArgs += listOf(
            "-Xopt-in=kotlin.RequiresOptIn",
            "-Xopt-in=kotlin.ExperimentalStdlibApi",
            "-Xopt-in=kotlin.time.ExperimentalTime",
            "-Xopt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
            "-Xopt-in=androidx.compose.ui.ExperimentalComposeUiApi",
            "-Xopt-in=androidx.compose.material.ExperimentalMaterialApi",
            "-Xopt-in=androidx.compose.animation.ExperimentalAnimationApi",
            "-Xopt-in=org.jetbrains.compose.splitpane.ExperimentalSplitPaneApi",
        )
    }
}

compose.desktop {
    application {
        mainClass = "com.copperleaf.forms.example.desktop.MainKt"
    }
}
