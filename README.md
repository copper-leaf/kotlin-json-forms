---
---

# Kotlin JSON Forms

> Customizable JSON Schema-based forms for Kotlin and Compose

![Kotlin Version](https://img.shields.io/badge/Kotlin-1.7.20-orange)
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

https://user-images.githubusercontent.com/6157866/179068902-a8e3da2e-2a00-4a30-8c92-596f2ded2d3d.mov

# Supported Platforms/Features

This project currently supports Android and Desktop targets with the `json-forms-compose-material` artifact, which uses
[json-kotlin-schema](https://github.com/pwall567/json-kotlin-schema) for schema validation.

JavaScript is also supported for Compose DOM (no Canvas implementation yet), using the `json-forms-compose-bulma` 
artifact. This displays forms based on [Bulma CSS](https://bulma.io/) for styling and class names. JS target uses 
[Ajv](https://github.com/ajv-validator/ajv) for schema validation (the same validator library used in the original
[JSON Forms](https://github.com/eclipsesource/jsonforms) library). 

# Installation

```kotlin
repositories {
    mavenCentral()
}

// for plain JVM or Android projects
dependencies {
    implementation("io.github.copper-leaf:json-forms-compose-material:{{site.version}}")
}

// for multiplatform projects
kotlin {
    sourceSets {
        val jvmMain by getting {
            dependencies {
                // Forms using Material components for Compose Desktop
                implementation("io.github.copper-leaf:json-forms-compose-material:{{site.version}}")
            }
        }
        val androidMain by getting {
            dependencies {
                // Forms using Material components for Compose Android
                implementation("io.github.copper-leaf:json-forms-compose-material:{{site.version}}")
            }
        }
        val commonMain by getting {
            dependencies {
                // Forms using HTML widgets for Compose Web DOM, using Bulma CSS framework for styling
                implementation("io.github.copper-leaf:json-forms-compose-bulma:{{site.version}}")
            }
        }
    }
}
```

# Documentation

See the [website](https://copper-leaf.github.io/kotlin-json-forms/) for detailed documentation and usage instructions.

The documentation site, and this library in general, is very much a POC at this point, and everything is subject to 
change without warning. But you can always find the most up-to-date usage in the
[example apps](https://github.com/copper-leaf/kotlin-json-forms/tree/main/example).

# License

Kotlin JSON Forms is licensed under the BSD 3-Clause License, see [LICENSE.md](https://github.com/copper-leaf/kotlin-json-forms/tree/main/LICENSE.md).

# References and Dependencies

- [JSON Schema formal specification](https://json-schema.org/)
- [Original JSON Forms Javascript library](https://github.com/eclipsesource/jsonforms)

This project depends on the following libraries:

- [json-kotlin-schema](https://github.com/pwall567/json-kotlin-schema) for providing JSON parsing and validation on JVM,
  which itself depends on many other Java or Kotlin JSON libraries by the same author
- [Ajv](https://github.com/ajv-validator/ajv) for providing JSON parsing and validation on JS
- [richtext-compose-multiplatform](https://github.com/Wavesonics/richtext-compose-multiplatform) for rich text editor capabilities
- [compose-code-editor](https://github.com/Qawaz/compose-code-editor) for code editor capabilities
