package com.copperleaf.forms.core.ui

import com.copperleaf.forms.core.internal.resolveUiSchema
import com.copperleaf.json.schema.JsonSchema
import kotlinx.serialization.json.JsonElement

public class UiSchema(
    public val rootUiElement: UiElement,
    public val json: JsonElement
) {

    public companion object {
        public fun parse(schema: JsonSchema, uiSchemaJson: JsonElement): UiSchema {
            return uiSchemaJson.resolveUiSchema(schema)
        }
    }
}
