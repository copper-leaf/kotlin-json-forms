package com.copperleaf.forms.compose.controls

import com.copperleaf.forms.compose.design.DesignSystem
import com.copperleaf.forms.core.ui.UiElement
import com.copperleaf.forms.core.vm.FormContractLite
import com.copperleaf.forms.core.vm.FormFieldsState
import com.copperleaf.json.pointer.JsonPointer
import com.copperleaf.json.pointer.JsonPointerAction
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull

public data class ControlScope(
    val vmState: FormFieldsState,
    val postInput: (FormContractLite.Inputs)->Unit,

    val designSystem: DesignSystem,
    val control: UiElement.Control,
    val dataPointer: JsonPointer,
    val schemaPointer: JsonPointer,

    val currentValue: JsonElement,
    val isTouched: Boolean,
    val isEnabled: Boolean,
    val isValid: Boolean,
    val validationErrors: List<String>,
) : DesignSystem by designSystem {
    public fun updateFormState(
        value: Any?,
    ) {
        postInput(
            FormContractLite.Inputs.UpdateFormState(
                pointer = dataPointer,
                action = JsonPointerAction.SetValue(value),
            )
        )
    }

    public fun sendFormAction(
        action: JsonPointerAction,
        pointer: JsonPointer = dataPointer,
    ) {
        postInput(
            FormContractLite.Inputs.UpdateFormState(
                pointer = pointer,
                action = action,
            )
        )
    }

    public fun markAsTouched(
        pointer: JsonPointer = dataPointer,
    ) {
        postInput(
            FormContractLite.Inputs.MarkAsTouched(
                pointer = pointer,
            )
        )
    }

    public fun <T> getTypedValue(
        defaultValue: T,
        mapper: (JsonElement) -> T?,
    ): T {
        return currentValue
            .takeIf { it != JsonNull }
            ?.let(mapper)
            ?: defaultValue
    }
}
