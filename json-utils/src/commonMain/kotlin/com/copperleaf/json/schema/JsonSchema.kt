package com.copperleaf.json.schema

import com.copperleaf.json.pointer.JsonPointer
import kotlinx.serialization.json.JsonElement

public expect class JsonSchema() {
    public fun validate(element: JsonElement): SchemaValidationResult

    public companion object {
        public fun parse(input: String): JsonSchema
    }
}

public sealed class SchemaValidationResult(public val isValid: Boolean) {
    public object Valid : SchemaValidationResult(true)
    public data class Invalid(val issues: Map<String, List<String>>) : SchemaValidationResult(true)
}

public fun SchemaValidationResult.issues(pointer: JsonPointer): List<String> {
    return when (this) {
        is SchemaValidationResult.Valid -> emptyList()
        is SchemaValidationResult.Invalid -> emptyList()
    }
}
