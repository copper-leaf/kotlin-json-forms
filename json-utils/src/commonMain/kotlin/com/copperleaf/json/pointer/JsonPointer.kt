package com.copperleaf.json.pointer

public data class JsonPointer internal constructor(
    public val tokens: List<String>
) {
    public companion object {
        public fun parse(pointer: String): JsonPointer {
            check(pointer.startsWith("#")) { "$pointer does not represent a URI fragment" }

            val tokens = pointer.tokenizeJsonPointerString()

            check(!tokens.contains("[]")) {
                "JsonPointer cannot contain any array-index placeholders. Use AbstractJsonPointer to represent a pointer " +
                    "that must be reified at runtime with proper array indices."
            }

            return JsonPointer(tokens)
        }
    }
}

