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
    public enum class ValidationMode {
        ValidateAndShow, ValidateAndHide, NoValidation
    }

    public data class State(
        val saveType: SaveType = SaveType.OnCommit,
        val validationMode: ValidationMode = ValidationMode.ValidateAndShow,
        val debug: Boolean = false,
        val readOnly: Boolean = false,

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
        // Inputs for updating the configuration or data of the form after it has been initialized
        public data class SetDebugMode(val isDebug: Boolean) : Inputs()
        public data class SetValidationMode(val validationMode: ValidationMode) : Inputs()
        public data class SetSaveType(val saveType: SaveType) : Inputs()
        public data class SetReadOnly(val readOnly: Boolean) : Inputs()
        public data class UpdateSchema(val schemaJson: JsonElement) : Inputs()
        public data class UpdateUiSchema(val uiSchemaJson: JsonElement) : Inputs()
        public data class FormDataChangedExternally(val newFormData: JsonElement) : Inputs()

        // These should only be used by Controls
        public data class UpdateFormState(
            val pointer: JsonPointer,
            val action: JsonPointerAction,
        ) : Inputs()
        public data class MarkAsTouched(
            val pointer: JsonPointer,
        ) : Inputs()

        // This may be used by a Submit Button, or anywhere in your UI to manually trigger a "save" of the form data
        public object CommitChanges : Inputs()
    }

    public sealed class Events {

    }
}
