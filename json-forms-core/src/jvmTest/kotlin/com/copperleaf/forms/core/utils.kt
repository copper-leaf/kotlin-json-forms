package com.copperleaf.forms.core

fun getJsonFromResources(name: String): String {
    return UiSchemaTests::class.java
        .getResourceAsStream(name)
        .let {
            if(it == null) {
                error("'${name}' not found")
            } else {
                it
            }
        }
        .bufferedReader()
        .readText()
}
