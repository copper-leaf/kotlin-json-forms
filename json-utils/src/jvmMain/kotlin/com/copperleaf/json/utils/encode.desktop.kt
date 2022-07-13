package com.copperleaf.json.utils

import java.net.URLDecoder
import java.net.URLEncoder
import java.util.Base64

public actual fun String.urlEncode(): String {
    return URLEncoder.encode(this, Charsets.UTF_8)
}

public actual fun String.urlDecode(): String {
    return URLDecoder.decode(this, Charsets.UTF_8)
}

public actual fun String.base64Encode(): String {
    return Base64.getEncoder().encodeToString(this.toByteArray(Charsets.UTF_8))
}

public actual fun String.base64Decode(): String {
    return String(Base64.getDecoder().decode(this), Charsets.UTF_8)
}
