plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
    google()
    mavenCentral()
    maven(url = "https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

dependencies {
    implementation("com.android.tools.build:gradle:7.0.3")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.10")
    implementation("org.jetbrains.compose:compose-gradle-plugin:1.2.0-alpha01-dev745")
    implementation("com.squareup.okhttp3:okhttp:4.9.3")
}
