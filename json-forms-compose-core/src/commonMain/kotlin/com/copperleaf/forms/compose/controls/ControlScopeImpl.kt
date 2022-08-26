package com.copperleaf.forms.compose.controls

import com.copperleaf.forms.compose.form.FormScope
import com.copperleaf.forms.core.internal.resolveAsControl
import com.copperleaf.forms.core.ui.UiElement
import com.copperleaf.forms.core.vm.FormFieldsContract
import com.copperleaf.json.pointer.JsonPointer
import com.copperleaf.json.pointer.JsonPointerAction
import com.copperleaf.json.pointer.plus
import com.copperleaf.json.pointer.toUriFragment
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

public data class ControlScopeImpl(
    private val formScope: FormScope,

    public override val control: UiElement.Control,
    public override val dataPointer: JsonPointer,
    public override val schemaPointer: JsonPointer,

    public override val currentValue: JsonElement,
    public override val isTouched: Boolean,
    public override val isEnabled: Boolean,
    public override val isControlValid: Boolean,
    public override val validationErrors: List<String>,
) : ControlScope, FormScope by formScope {
    public override fun updateFormState(
        value: Any?,
        pointer: JsonPointer,
    ) {
        postInput(
            FormFieldsContract.Inputs.UpdateFormState(
                pointer = pointer,
                action = JsonPointerAction.SetValue(value),
            )
        )
    }

    public override fun <T> getTypedValue(
        defaultValue: T,
        mapper: (JsonElement) -> T?,
    ): T {
        return currentValue
            .takeIf { it != JsonNull }
            ?.let(mapper)
            ?: defaultValue
    }

    override fun getChildObjectControl(key: String): UiElement.Control {
        return JsonObject(
            mapOf(
                "type" to JsonPrimitive("Control"),
                "scope" to JsonPrimitive((control.schemaScope + "/properties/$key").toUriFragment())
            )
        ).resolveAsControl(schema)
    }

    override fun getChildArrayControl(): UiElement.Control {
        return JsonObject(
            mapOf(
                "type" to JsonPrimitive("Control"),
                "scope" to JsonPrimitive((control.schemaScope + "/items").toUriFragment())
            )
        ).resolveAsControl(schema)
    }

    override fun addArrayItem(newIndex: Int, value: Any?, pointer: JsonPointer) {
        postInput(
            FormFieldsContract.Inputs.UpdateFormState(
                pointer = pointer + "/$newIndex",
                action = JsonPointerAction.SetValue(value),
            )
        )
        postInput(
            FormFieldsContract.Inputs.MarkAsTouched(
                pointer = pointer,
            )
        )
    }

    override fun removeArrayItem(indexToRemove: Int, pointer: JsonPointer) {
        postInput(
            FormFieldsContract.Inputs.UpdateFormState(
                pointer = dataPointer + "/$indexToRemove",
                action = JsonPointerAction.RemoveValue
            )
        )
    }
}
