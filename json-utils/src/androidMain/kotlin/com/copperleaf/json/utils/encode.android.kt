package com.copperleaf.json.utils

import android.util.Base64
import java.net.URLDecoder
import java.net.URLEncoder

public actual fun String.urlEncode(): String {
    return URLEncoder.encode(this, Charsets.UTF_8.name())
}

public actual fun String.urlDecode(): String {
    return URLDecoder.decode(this, Charsets.UTF_8.name())
}

public actual fun String.base64Encode(): String {
    return Base64.encodeToString(this.toByteArray(Charsets.UTF_8), Base64.DEFAULT)
}

public actual fun String.base64Decode(): String {
    return String(Base64.decode(this.toByteArray(Charsets.UTF_8), Base64.DEFAULT), Charsets.UTF_8)
}
