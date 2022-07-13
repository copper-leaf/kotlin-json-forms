package com.copperleaf.json.schema

import com.copperleaf.json.pointer.JsonPointer
import kotlinx.serialization.json.JsonElement

public expect class JsonSchema(input: String) {
    public fun validate(element: JsonElement): SchemaValidationResult

    public companion object {
        public fun parse(input: String): JsonSchema
    }
}

public sealed class SchemaValidationResult(public val isValid: Boolean) {
    public abstract fun issues(pointer: JsonPointer): List<String>

    public object Valid : SchemaValidationResult(true) {
        public override fun issues(pointer: JsonPointer): List<String> = emptyList()
    }

    public abstract class Invalid : SchemaValidationResult(false) {
    }
}
