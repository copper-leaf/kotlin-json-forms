package com.copperleaf.forms.compose.form

import com.copperleaf.forms.compose.controls.ControlRenderer
import com.copperleaf.forms.compose.controls.ControlScope
import com.copperleaf.forms.compose.design.DesignSystem
import com.copperleaf.forms.compose.elements.UiElementRenderer
import com.copperleaf.forms.compose.elements.UiElementScope
import com.copperleaf.forms.compose.rules.RuleScope
import com.copperleaf.forms.core.ui.Rule
import com.copperleaf.forms.core.ui.UiElement
import com.copperleaf.forms.core.ui.UiSchema
import com.copperleaf.forms.core.vm.FormFieldsContract
import com.copperleaf.json.pointer.JsonPointer
import com.copperleaf.json.schema.JsonSchema
import kotlinx.serialization.json.JsonElement

public interface FormScope : DesignSystem {
    public val schema: JsonSchema
    public val uiSchema: UiSchema
    public val data: JsonElement
    public val touchedProperties: Set<JsonPointer>
    public val isFormValid: Boolean

    public fun getRuleScope(
        uiElement: UiElement,
        rule: Rule,
        localArrayIndices: List<Int>,
    ): RuleScope

    public fun getUiElement(
        element: UiElement.ElementWithChildren,
    ): UiElementRenderer?

    public fun getUiElementScope(
        element: UiElement.ElementWithChildren,
    ): UiElementScope

    public fun getControl(
        control: UiElement.Control,
    ): ControlRenderer?

    public fun getControlScope(
        controlElement: UiElement.Control,
        localArrayIndices: List<Int>,
        locallyEnabled: Boolean,
    ): ControlScope

    public fun postInput(input: FormFieldsContract.Inputs)
}
