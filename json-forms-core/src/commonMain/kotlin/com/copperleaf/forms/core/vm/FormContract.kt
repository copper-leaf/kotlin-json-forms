package com.copperleaf.forms.core.vm

import com.copperleaf.forms.core.ui.UiSchema
import com.copperleaf.json.pointer.JsonPointer
import com.copperleaf.json.pointer.JsonPointerAction
import com.copperleaf.json.schema.JsonSchema
import com.copperleaf.json.schema.SchemaValidationResult
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull

public object FormContract {

    public enum class SaveType {
        OnValidChange, OnAnyChange, OnCommit
    }

    public data class State(
        val saveType: SaveType = SaveType.OnValidChange,
        val debug: Boolean = false,

        val schemaJson: JsonElement = JsonNull,
        val schema: JsonSchema? = null,
        val uiSchemaJson: JsonElement = JsonNull,
        val uiSchema: UiSchema? = null,

        val originalData: JsonElement = JsonNull,
        val updatedData: JsonElement = JsonNull,

        val touchedProperties: Set<JsonPointer> = emptySet(),
    ) {
        val isReady: Boolean = schema != null && uiSchema != null

        val validationResult: SchemaValidationResult? = schema?.validate(updatedData)
        val isValid: Boolean = validationResult?.isValid == true
        val isChanged: Boolean = originalData != updatedData

        public fun errors(pointer: JsonPointer): List<String> {
            return validationResult?.issues(pointer) ?: emptyList()
        }
    }

    public sealed class Inputs {
        public data class SetDebugMode(val isDebug: Boolean) : Inputs()
        public data class SetSaveType(val saveType: SaveType) : Inputs()

        public data class UpdateFormState(
            val pointer: JsonPointer,
            val action: JsonPointerAction,
        ) : Inputs()

        public object CommitChanges : Inputs()
    }

    public sealed class Events {

    }
}
