package com.copperleaf.forms.compose.form

public data class Registered<T, U>(
    val rank: Int,
    val tester: (T) -> Boolean,
    val renderer: U,
)
