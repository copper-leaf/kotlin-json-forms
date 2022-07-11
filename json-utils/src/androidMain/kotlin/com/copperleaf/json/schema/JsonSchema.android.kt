package com.copperleaf.json.schema

import kotlinx.serialization.json.JsonElement

public actual class JsonSchema {

    public actual fun validate(element: JsonElement): SchemaValidationResult {
        return SchemaValidationResult.Valid
    }

    public actual companion object {
        public actual fun parse(input: String): JsonSchema {
            return JsonSchema()
        }
    }
}

