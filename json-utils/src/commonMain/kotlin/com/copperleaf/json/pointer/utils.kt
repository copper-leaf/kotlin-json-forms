package com.copperleaf.json.pointer

// JsonPointer Utils
// ---------------------------------------------------------------------------------------------------------------------

public fun JsonPointer.toUriFragment(): String {
    return buildString {
        append('#')
        tokens.forEach {
            append('/')
            append(escapeJsonPointerReferenceToken(it))
        }
    }
}

public fun JsonPointer.parent(): JsonPointer {
    return JsonPointer(tokens = this.tokens.dropLast(1))
}

public val JsonPointer.current: String?
    get() {
        return this.tokens.lastOrNull()
    }

public operator fun JsonPointer.plus(suffix: String) : JsonPointer {
    return JsonPointer.parse(this.toUriFragment() + suffix)
}

// Abstract JsonPointer Utils
// ---------------------------------------------------------------------------------------------------------------------

public fun AbstractJsonPointer.toAbstractUriFragment(): String {
    return buildString {
        append('#')
        tokens.forEach {
            append('/')
            append(escapeJsonPointerReferenceToken(it))
        }
    }
}

public fun AbstractJsonPointer.toUriFragment(arrayIndices: List<Int>): String {
    val numberOfAbstractTokens = tokens.count { it == "[]" }
    check(arrayIndices.size == numberOfAbstractTokens) {
        "${this.toAbstractUriFragment()} expects exactly $numberOfAbstractTokens array indices"
    }

    return buildString {
        append('#')
        var nextArrayIndex = 0
        tokens.forEach {
            append('/')
            if (it == "[]") {
                // we have a placeholder array index, replace it with the actual local array index value
                append(escapeJsonPointerReferenceToken(arrayIndices[nextArrayIndex].toString()))
                nextArrayIndex++
            } else {
                // we have a regular index, just append it
                append(escapeJsonPointerReferenceToken(it))
            }
        }
    }
}

public fun AbstractJsonPointer.reifyPointer(arrayIndices: List<Int>): JsonPointer {
    return JsonPointer.parse(this.toUriFragment(arrayIndices))
}
