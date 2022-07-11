package com.copperleaf.json.utils

public fun String.indent(width: Int): String {
    return CharArray(width) { ' ' }.concatToString() + this
}
