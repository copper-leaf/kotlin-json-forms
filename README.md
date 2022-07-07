---
---

# Kotlin JSON Forms

> Customizable JSON Schema-based forms for Kotlin and Compose

![Kotlin Version](https://img.shields.io/badge/Kotlin-1.6.10-orange)
[![GitHub release (latest by date)](https://img.shields.io/github/v/release/copper-leaf/kotlin-json-forms)](https://github.com/copper-leaf/kotlin-json-forms/releases)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.copper-leaf/json-forms-core)](https://search.maven.org/artifact/io.github.copper-leaf/json-forms-core)

This project aims to reimplement [JSON Forms](https://github.com/eclipsesource/jsonforms) in Kotlin for use in 
Compose-based applications. Rather than defining its own formats or standards, this repo adopts JSON for form 
configuration and layout of well-known or standardized formats. Form fields and validation is provided in 
[JSON Schema format](https://json-schema.org/), and the way the form is laid out and displayed is through 
[JSON Forms format](https://github.com/eclipsesource/jsonforms) format. Using JSON as as the form definition gives an 
easy, portable way to display and dynamically change forms in your application.

This repo aims to support all features of the original [JSON Forms](https://github.com/eclipsesource/jsonforms) library,
which is written in Javascript, as well as following similar architectural patterns. For all topic related to the JSON 
format itself, that library's documentation will be a valid reference for this library.

See the video below for an example form in action:

https://user-images.githubusercontent.com/6157866/177850032-d2eca07a-7a72-4861-9b3c-9ef6f28409e9.mov

# Supported Platforms/Features

Currently, JSON Schema validation is provided by the JVM-only 
[json-kotlin-schema](https://github.com/pwall567/json-kotlin-schema), so is only available for JVM-compatible targets. 
In following the architecture of the original JSON Forms library, the core functionality is not tied to any particular 
UI framework, and displaying forms in Compose is an extension on top of the core library. 

Kotlin JSON Forms is currently available for Compose on Android and Desktop platforms. Adding support for Compose Web
is planned.

# Installation

```kotlin
repositories {
    mavenCentral()
}

// for plain JVM or Android projects
dependencies {
    implementation("io.github.copper-leaf:json-forms-core:{{site.version}}")
    implementation("io.github.copper-leaf:json-forms-compose:{{site.version}}")
}

// for multiplatform projects
kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("io.github.copper-leaf:json-forms-core:{{site.version}}")
                implementation("io.github.copper-leaf:json-forms-compose:{{site.version}}")
            }
        }
    }
}
```

# Documentation

See the [website](https://copper-leaf.github.io/json-forms/) for detailed documentation and usage instructions.

# License

Kotlin JSON Forms is licensed under the BSD 3-Clause License, see [LICENSE.md](https://github.com/copper-leaf/kotlin-json-forms/tree/main/LICENSE.md).

# References and Dependencies

- [JSON Schema formal specification](https://json-schema.org/)
- [Original JSON Forms Javascript library](https://github.com/eclipsesource/jsonforms)

This project depends on the following libraries:

- [json-kotlin-schema](https://github.com/pwall567/json-kotlin-schema) for providing JSON parsing and validation on JVM, 
  which itself depends on many other Java or Kotlin JSON libraries by the same author
- [Ballast](https://github.com/copper-leaf/ballast) for state management

