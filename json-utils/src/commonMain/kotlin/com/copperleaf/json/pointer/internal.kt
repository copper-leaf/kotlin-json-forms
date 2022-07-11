package com.copperleaf.json.pointer

internal fun escapeJsonPointerReferenceToken(token: String): String {
    val len = token.length
    var i = 0
    var ch: Char
    while (true) {
        if (i >= len)
            return token
        ch = token[i]
        if (ch == '~' || ch == '/')
            break
        i++
    }
    val sb = StringBuilder(len + 8)
    sb.append(token, 0, i)
    while (true) {
        when (ch) {
            '~' -> sb.append("~0")
            '/' -> sb.append("~1")
            else -> sb.append(ch)
        }
        if (++i >= len)
            return sb.toString()
        ch = token[i]
    }
}

internal fun unescapeJsonPointerReferenceToken(token: String): String {
    val len = token.length
    var i = 0
    while (true) {
        if (i >= len)
            return token
        if (token[i] == '~')
            break
        i++
    }
    val sb = StringBuilder(len)
    sb.append(token, 0, i)
    while (true) {
        if (++i >= len)
            error("Illegal token in JSON Pointer $token")
        when (token[i]) {
            '0' -> sb.append('~')
            '1' -> sb.append('/')
            else -> error("Illegal token in JSON Pointer $token")
        }
        while (true) {
            if (++i >= len)
                return sb.toString()
            when (val ch = token[i]) {
                '~' -> break
                else -> sb.append(ch)
            }
        }
    }
}

internal fun String.tokenizeJsonPointerString(): List<String> {
    return this
        .split("/")
        .dropWhile { it == "#" }
        .map { unescapeJsonPointerReferenceToken(it) }
}
