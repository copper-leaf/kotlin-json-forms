package com.copperleaf.forms.core

fun getJsonFromResources(name: String): String {
    return UiSchemaTests::class.java
        .getResourceAsStream(name)
        .let {
            it ?: error("'$name' not found")
        }
        .bufferedReader()
        .readText()
}
