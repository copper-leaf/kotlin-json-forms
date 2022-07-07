package com.copperleaf.forms.compose.controls

import com.copperleaf.forms.core.ui.UiElement
import com.copperleaf.forms.core.vm.FormContract
import com.copperleaf.forms.core.vm.FormViewModel
import com.copperleaf.json.pointer.JsonPointerAction
import com.copperleaf.json.pointer.toJsonValue
import net.pwall.json.JSONValue
import net.pwall.json.pointer.JSONPointer

public data class ControlScope(
    private val vm: FormViewModel,
    val vmState: FormContract.State,
    val control: UiElement.Control,
    val dataPointer: JSONPointer,
    val schemaPointer: JSONPointer,
    val isValid: Boolean,
    val validationErrors: List<String>,

    val isEnabled: Boolean,

    val currentValue: JSONValue?,
) {
    public fun updateFormState(
        value: Any?,
    ) {
        vm.trySend(
            FormContract.Inputs.UpdateFormState(
                pointer = dataPointer,
                action = JsonPointerAction.SetValue(value.toJsonValue()),
            )
        )
    }

    public fun sendFormAction(
        action: JsonPointerAction,
        pointer: JSONPointer = dataPointer,
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
        mapper: (JSONValue) -> T?,
    ): T {
        return currentValue?.let(mapper) ?: defaultValue
    }
}
