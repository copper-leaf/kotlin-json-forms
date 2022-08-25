package com.copperleaf.forms.compose.controls

import com.copperleaf.forms.compose.design.DesignSystem
import com.copperleaf.forms.core.ui.UiElement
import com.copperleaf.forms.core.vm.FormContract
import com.copperleaf.json.pointer.JsonPointer
import com.copperleaf.json.pointer.JsonPointerAction
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull

public data class ControlScope(
    val vmState: FormContract.State,
    val postInput: (FormContract.Inputs)->Unit,

    val designSystem: DesignSystem,
    val control: UiElement.Control,
    val dataPointer: JsonPointer,
    val schemaPointer: JsonPointer,

    val isValid: Boolean,
    val validationErrors: List<String>,

    val isTouched: Boolean,
    val isChanged: Boolean,

    val isEnabled: Boolean,

    val currentValue: JsonElement,
) : DesignSystem by designSystem {
    public fun updateFormState(
        value: Any?,
    ) {
        postInput(
            FormContract.Inputs.UpdateFormState(
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
            FormContract.Inputs.UpdateFormState(
                pointer = pointer,
                action = action,
            )
        )
    }

    public fun markAsTouched(
        pointer: JsonPointer = dataPointer,
    ) {
        postInput(
            FormContract.Inputs.MarkAsTouched(
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
