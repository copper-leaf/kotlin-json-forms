package com.copperleaf.forms.compose.controls

import com.copperleaf.forms.compose.form.FormScope
import com.copperleaf.forms.core.ui.UiElement
import com.copperleaf.json.pointer.JsonPointer
import kotlinx.serialization.json.JsonElement

public interface ControlScope : FormScope {
    public val control: UiElement.Control
    public val dataPointer: JsonPointer
    public val schemaPointer: JsonPointer

    public val currentValue: JsonElement
    public val isTouched: Boolean
    public val isEnabled: Boolean
    public val isControlValid: Boolean
    public val validationErrors: List<String>

    public fun updateFormState(
        value: Any?,
        pointer: JsonPointer = dataPointer,
    )

    public fun <T> getTypedValue(
        defaultValue: T,
        mapper: (JsonElement) -> T?,
    ): T

    public fun getChildObjectControl(
        key: String
    ): UiElement.Control

    public fun getChildArrayControl(): UiElement.Control

    public fun addArrayItem(
        newIndex: Int,
        value: Any?,
        pointer: JsonPointer = dataPointer,
    )

    public fun removeArrayItem(
        indexToRemove: Int,
        pointer: JsonPointer = dataPointer,
    )
}
