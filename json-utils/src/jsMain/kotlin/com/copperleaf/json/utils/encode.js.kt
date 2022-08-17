package com.copperleaf.json.utils

public external fun encodeURIComponent(input: String): String
public external fun decodeURIComponent(input: String): String
public external fun atob(input: String): String
public external fun btoa(input: String): String

public actual fun String.urlEncode(): String {
    return encodeURIComponent(this)
}

public actual fun String.urlDecode(): String {
    return decodeURIComponent(this)
}

public actual fun String.base64Encode(): String {
    return atob(this)
}

public actual fun String.base64Decode(): String {
    return btoa(this)
}
