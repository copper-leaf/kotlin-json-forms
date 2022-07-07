package com.copperleaf.forms.core.vm

import com.copperleaf.forms.core.internal.getValidationErrorMessages
import com.copperleaf.forms.core.ui.UiSchema
import com.copperleaf.json.pointer.JsonPointerAction
import net.pwall.json.JSONValue
import net.pwall.json.pointer.JSONPointer
import net.pwall.json.schema.JSONSchema
import net.pwall.json.schema.output.DetailedOutput

public object FormContract {

    public enum class SaveType {
        OnValidChange, OnAnyChange, OnCommit
    }

    public data class State(
        val saveType: SaveType = SaveType.OnValidChange,
        val debug: Boolean = false,

        val schemaJson: JSONValue? = null,
        val schema: JSONSchema? = null,
        val uiSchemaJson: JSONValue? = null,
        val uiSchema: UiSchema? = null,

        val originalData: JSONValue? = null,
        val updatedData: JSONValue? = null,
    ) {
        val isReady: Boolean = schema != null && uiSchema != null
//        val basicValidationOutput: BasicOutput? = schema?.validateBasic(updatedData)
        val detailedValidationOutput: DetailedOutput? = schema?.validateDetailed(updatedData)
        val isValid: Boolean = detailedValidationOutput?.valid == true
        val isChanged: Boolean = originalData != updatedData

        public fun errors(pointer: String): List<String> {
            return detailedValidationOutput?.getValidationErrorMessages(pointer) ?: emptyList()
        }
    }

    public sealed class Inputs {
        public data class SetDebugMode(val isDebug: Boolean) : Inputs()
        public data class SetSaveType(val saveType: SaveType) : Inputs()

        public data class UpdateFormState(
            val pointer: JSONPointer,
            val action: JsonPointerAction,
        ) : Inputs()

        public object CommitChanges : Inputs()
    }

    public sealed class Events {

    }
}
