package com.copperleaf.forms.compose.controls

import com.copperleaf.forms.core.ui.UiElement
import com.copperleaf.forms.core.vm.FormContract
import com.copperleaf.forms.core.vm.FormViewModel
import com.copperleaf.json.pointer.JsonPointer
import com.copperleaf.json.pointer.JsonPointerAction
import kotlinx.serialization.json.JsonElement

public data class ControlScope(
    val vm: FormViewModel,
    val vmState: FormContract.State,
    val control: UiElement.Control,
    val dataPointer: JsonPointer,
    val schemaPointer: JsonPointer,

    val isValid: Boolean,
    val validationErrors: List<String>,

    val isTouched: Boolean,
    val isChanged: Boolean,

    val isEnabled: Boolean,

    val currentValue: JsonElement,
) {
    public fun updateFormState(
        value: Any?,
    ) {
        vm.trySend(
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
        vm.trySend(
            FormContract.Inputs.UpdateFormState(
                pointer = pointer,
                action = action,
            )
        )
    }

    public fun <T> getTypedValue(
        defaultValue: T,
        mapper: (JsonElement) -> T?,
    ): T {
        return currentValue.let(mapper) ?: defaultValue
    }
}
