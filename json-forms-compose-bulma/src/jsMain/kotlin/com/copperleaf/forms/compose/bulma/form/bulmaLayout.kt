package com.copperleaf.forms.compose.bulma.form

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.copperleaf.forms.compose.bulma.controls.checkbox
import com.copperleaf.forms.compose.bulma.controls.checkboxesEnum
import com.copperleaf.forms.compose.bulma.controls.checkboxesOneOf
import com.copperleaf.forms.compose.bulma.controls.control
import com.copperleaf.forms.compose.bulma.controls.dropdownEnum
import com.copperleaf.forms.compose.bulma.controls.dropdownOneOf
import com.copperleaf.forms.compose.bulma.controls.radioButtonEnum
import com.copperleaf.forms.compose.bulma.controls.radioButtonOneOf
import com.copperleaf.forms.compose.bulma.controls.switch
import com.copperleaf.forms.compose.bulma.elements.element
import com.copperleaf.forms.compose.controls.ControlRenderer
import com.copperleaf.forms.compose.elements.UiElementRenderer
import com.copperleaf.forms.compose.elements.element
import com.copperleaf.forms.compose.elements.submit
import com.copperleaf.forms.compose.elements.toggleDebug
import com.copperleaf.forms.compose.form.BasicForm
import com.copperleaf.forms.compose.form.FormScopeImpl
import com.copperleaf.forms.compose.form.Registered
import com.copperleaf.forms.core.ArrayControl
import com.copperleaf.forms.core.BooleanControl
import com.copperleaf.forms.core.Button
import com.copperleaf.forms.core.Categorization
import com.copperleaf.forms.core.Category
import com.copperleaf.forms.core.Group
import com.copperleaf.forms.core.HorizontalLayout
import com.copperleaf.forms.core.IntegerControl
import com.copperleaf.forms.core.Label
import com.copperleaf.forms.core.NumberControl
import com.copperleaf.forms.core.ObjectControl
import com.copperleaf.forms.core.StringControl
import com.copperleaf.forms.core.VerticalLayout
import com.copperleaf.forms.core.ui.UiElement
import com.copperleaf.forms.core.ui.UiSchema
import com.copperleaf.forms.core.vm.FormFieldsContract
import com.copperleaf.json.pointer.JsonPointer
import com.copperleaf.json.schema.JsonSchema
import com.copperleaf.json.schema.SchemaValidationResult
import kotlinx.serialization.json.JsonElement

@Composable
public fun BulmaForm(
    schema: JsonSchema,
    uiSchema: UiSchema,
    data: JsonElement,
    onDataChanged: (JsonElement) -> Unit,
) {
    var touchedProperties: Set<JsonPointer> by remember { mutableStateOf(emptySet()) }

    BulmaForm(
        schema = schema,
        uiSchema = uiSchema,
        data = data,
        touchedProperties = touchedProperties,
        postInputCallback = {
            val (newData, newTouchedProperties) = it.applyToState(data, touchedProperties)
            val isChanged = newData != data
            touchedProperties = newTouchedProperties

            if (isChanged) {
                onDataChanged(newData)
            }
        },
    )
}

@Composable
public fun BulmaForm(
    schema: JsonSchema,
    uiSchema: UiSchema,
    data: JsonElement,
    touchedProperties: Set<JsonPointer>,
    postInputCallback: (FormFieldsContract.Inputs) -> Unit,
    validationResult: SchemaValidationResult = schema.validate(data),
) {
    val formScope = FormScopeImpl(
        schema = schema,
        uiSchema = uiSchema,
        data = data,
        touchedProperties = touchedProperties,
        elements = UiElement.bulmaDefaults(),
        controls = UiElement.Control.bulmaDefaults(),
        designSystem = BulmaDesignSystem(),
        postInputCallback = postInputCallback,
        validationResult = validationResult,
    )
    formScope.BasicForm()
}

public fun UiElement.Control.Companion.bulmaDefaults(): List<Registered<UiElement.Control, ControlRenderer>> =
    listOf(
        // text fields
        StringControl.control(),

        // single-select
        StringControl.dropdownEnum(),
        StringControl.dropdownOneOf(),
        StringControl.radioButtonEnum(),
        StringControl.radioButtonOneOf(),

        // multi-select
        ArrayControl.checkboxesEnum(),
        ArrayControl.checkboxesOneOf(),

        // number controls
        IntegerControl.control(),
        NumberControl.control(),

        // boolean controls
        BooleanControl.checkbox(),
        BooleanControl.switch(),

        // composite controls
        ObjectControl.control(),
        ArrayControl.control(),
    )

public fun UiElement.Companion.bulmaDefaults(): List<Registered<UiElement.ElementWithChildren, UiElementRenderer>> =
    listOf(
        VerticalLayout.element(),
        HorizontalLayout.element(),

        Label.element(),

        Group.element(),
        Categorization.element(),
        Category.element(),

        Button.submit(),
        Button.toggleDebug(),
    )
