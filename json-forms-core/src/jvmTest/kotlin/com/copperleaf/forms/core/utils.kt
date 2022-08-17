package com.copperleaf.forms.core

fun getJsonFromResources(name: String): String {
    return TestFormSavedStateAdapter::class.java
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
