---
---

# Ballast

> Opinionated Application State Management framework for Kotlin Multiplatform

![Kotlin Version](https://img.shields.io/badge/Kotlin-1.6.10-orange)
[![GitHub release (latest by date)](https://img.shields.io/github/v/release/copper-leaf/ballast)](https://github.com/copper-leaf/ballast/releases)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.copper-leaf/ballast-core)](https://search.maven.org/artifact/io.github.copper-leaf/ballast-core)
[![Intellij Plugin Version](https://img.shields.io/jetbrains/plugin/v/18702-ballast)](https://plugins.jetbrains.com/plugin/18702-ballast)

```kotlin
```

# Supported Platforms/Features

# Installation

```kotlin
repositories {
    mavenCentral()
}

// for plain JVM or Android projects
dependencies {
    implementation("io.github.copper-leaf:ballast-core:{{site.version}}")
    implementation("io.github.copper-leaf:ballast-saved-state:{{site.version}}")
    implementation("io.github.copper-leaf:ballast-repository:{{site.version}}")
    implementation("io.github.copper-leaf:ballast-firebase-crashlytics:{{site.version}}")
    implementation("io.github.copper-leaf:ballast-firebase-analytics:{{site.version}}")
    implementation("io.github.copper-leaf:ballast-debugger:{{site.version}}")
    testImplementation("io.github.copper-leaf:ballast-test:{{site.version}}")
}

// for multiplatform projects
kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
              implementation("io.github.copper-leaf:ballast-core:{{site.version}}")
              implementation("io.github.copper-leaf:ballast-saved-state:{{site.version}}")
              implementation("io.github.copper-leaf:ballast-repository:{{site.version}}")
              implementation("io.github.copper-leaf:ballast-firebase-crashlytics:{{site.version}}")
              implementation("io.github.copper-leaf:ballast-firebase-analytics:{{site.version}}")
              implementation("io.github.copper-leaf:ballast-debugger:{{site.version}}")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation("io.github.copper-leaf:ballast-test:{{site.version}}")
            }
        }
    }
}
```

# Documentation

See the [website](https://copper-leaf.github.io/ballast/) for detailed documentation and usage instructions.

# License

Ballast is licensed under the BSD 3-Clause License, see [LICENSE.md](https://github.com/copper-leaf/ballast/tree/main/LICENSE.md).

# References
