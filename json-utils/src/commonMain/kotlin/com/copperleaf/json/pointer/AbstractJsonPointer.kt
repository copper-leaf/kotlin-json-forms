package com.copperleaf.json.pointer

public data class AbstractJsonPointer(
    public val tokens: List<String>
) {
    public companion object {
        public fun parse(pointer: String): AbstractJsonPointer {
            check(pointer.startsWith("#")) { "$pointer does not represent a URI fragment" }

            val tokens = pointer.tokenizeJsonPointerString()

            return AbstractJsonPointer(tokens)
        }
    }
}
