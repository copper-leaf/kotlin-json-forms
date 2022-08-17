package com.copperleaf.forms.core.internal

import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

@Suppress("UNUSED_PARAMETER")
internal fun createDefaultUiSchema(schema: JsonElement): JsonElement {
    return JsonObject(
        mapOf(
            "type" to JsonPrimitive("Control"),
            "scope" to JsonPrimitive("#"),
        )
    )
}
