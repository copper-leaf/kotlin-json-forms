package com.copperleaf.forms.compose.form

import com.copperleaf.forms.compose.controls.ControlRenderer
import com.copperleaf.forms.compose.controls.ControlScope
import com.copperleaf.forms.compose.controls.ControlScopeImpl
import com.copperleaf.forms.compose.design.DesignSystem
import com.copperleaf.forms.compose.elements.UiElementRenderer
import com.copperleaf.forms.compose.elements.UiElementScope
import com.copperleaf.forms.compose.elements.UiElementScopeImpl
import com.copperleaf.forms.compose.rules.RuleScope
import com.copperleaf.forms.compose.rules.RuleScopeImpl
import com.copperleaf.forms.core.ui.Rule
import com.copperleaf.forms.core.ui.UiElement
import com.copperleaf.forms.core.ui.UiSchema
import com.copperleaf.forms.core.vm.FormFieldsContract
import com.copperleaf.json.pointer.JsonPointer
import com.copperleaf.json.pointer.findOrNull
import com.copperleaf.json.pointer.findOrThrow
import com.copperleaf.json.pointer.reifyPointer
import com.copperleaf.json.schema.JsonSchema
import com.copperleaf.json.schema.SchemaValidationResult
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull

public class FormScopeImpl(
    override val schema: JsonSchema,
    override val uiSchema: UiSchema,
    override val data: JsonElement,
    override val touchedProperties: Set<JsonPointer>,
    private val elements: List<Registered<UiElement.ElementWithChildren, UiElementRenderer>>,
    private val controls: List<Registered<UiElement.Control, ControlRenderer>>,
    private val designSystem: DesignSystem,
    private val postInputCallback: (FormFieldsContract.Inputs) -> Unit,
    private val validationResult: SchemaValidationResult = schema.validate(data),
) : FormScope, DesignSystem by designSystem {

    public override val isFormValid: Boolean = validationResult.isValid

    override fun getRuleScope(
        uiElement: UiElement,
        rule: Rule,
        localArrayIndices: List<Int>,
    ): RuleScope {
        val currentDataPointer = rule.dataScope.reifyPointer(localArrayIndices)
        val currentSchemaPointer = rule.schemaScope
        val currentValue = runCatching { data.findOrThrow(currentDataPointer) }.getOrDefault(JsonNull)

        val result = rule.conditionSchema.validate(currentValue)

        val effect = if (result.isValid) rule.effect else rule.effect.inverse()

        return RuleScopeImpl(
            formScope = this,
            rule = rule,
            dataPointer = currentDataPointer,
            schemaPointer = currentSchemaPointer,

            isRuleValid = result.isValid,

            isEnabled = effect.isEnabled,
            isVisible = effect.isVisible,
            currentValue = currentValue,
        )
    }

    public override fun getUiElement(
        element: UiElement.ElementWithChildren,
    ): UiElementRenderer? {
        return elements
            .asSequence()
            .filter { it.tester(element) }
            .maxByOrNull { it.rank }
            ?.renderer
    }

    override fun getUiElementScope(
        element: UiElement.ElementWithChildren,
    ): UiElementScope {
        return UiElementScopeImpl(
            formScope = this,
            element = element
        )
    }

    public override fun getControl(
        control: UiElement.Control,
    ): ControlRenderer? {
        return controls
            .asSequence()
            .filter { it.tester(control) }
            .maxByOrNull { it.rank }
            ?.renderer
    }

    override fun getControlScope(
        controlElement: UiElement.Control,
        localArrayIndices: List<Int>,
        locallyEnabled: Boolean,
    ): ControlScope {
        val currentDataPointer = controlElement.dataScope.reifyPointer(localArrayIndices)
        val currentSchemaPointer = controlElement.schemaScope
        val validationErrors = validationResult.issues(currentDataPointer)
        val currentValue = data.findOrNull(currentDataPointer)
        val isTouched = currentDataPointer in touchedProperties

        return ControlScopeImpl(
            formScope = this,

            control = controlElement,
            dataPointer = currentDataPointer,
            schemaPointer = currentSchemaPointer,

            isControlValid = validationErrors.isEmpty(),
            validationErrors = validationErrors,

            isTouched = isTouched,

            isEnabled = locallyEnabled,
            currentValue = currentValue,
        )
    }

    override fun postInput(input: FormFieldsContract.Inputs) {
        postInputCallback(input)
    }
}
