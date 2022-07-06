package com.copperleaf.forms.core

fun getJsonFromResources(name: String): String {
    return TestFormSavedStateAdapter::class.java
        .getResourceAsStream(name)!!
        .bufferedReader()
        .readText()
}
