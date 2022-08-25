package com.copperleaf.forms.core.vm

import com.copperleaf.forms.core.ui.UiSchema
import com.copperleaf.json.pointer.JsonPointer
import com.copperleaf.json.schema.JsonSchema
import com.copperleaf.json.schema.SchemaValidationResult
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull

public data class FormFieldsState(
    val schema: JsonSchema,
    val uiSchema: UiSchema,

    val data: JsonElement = JsonNull,
    val touchedProperties: Set<JsonPointer> = emptySet(),
) {
    val validationResult: SchemaValidationResult = schema.validate(data)
    val isValid: Boolean = validationResult.isValid

    public fun errors(pointer: JsonPointer): List<String> {
        return validationResult.issues(pointer)
    }
}
