package com.copperleaf.forms.compose.form

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.copperleaf.forms.compose.controls.ControlRenderer
import com.copperleaf.forms.compose.elements.UiElementRenderer
import com.copperleaf.forms.core.ui.UiElement
import com.copperleaf.forms.core.ui.UiSchema
import com.copperleaf.forms.core.vm.FormFieldsContract
import com.copperleaf.json.pointer.JsonPointer
import com.copperleaf.json.schema.JsonSchema
import com.copperleaf.json.schema.SchemaValidationResult
import kotlinx.serialization.json.JsonElement

@Composable
public fun MaterialForm(
    schema: JsonSchema,
    uiSchema: UiSchema,
    data: JsonElement,
    onDataChanged: (JsonElement)->Unit,
    modifier: Modifier = Modifier,
) {
    var touchedProperties: Set<JsonPointer> by remember { mutableStateOf(emptySet()) }

    MaterialForm(
        schema = schema,
        uiSchema = uiSchema,
        data = data,
        touchedProperties = touchedProperties,
        postInputCallback = {
            val (newData, newTouchedProperties) = it.applyToState(data, touchedProperties)
            val isChanged = newData != data
            touchedProperties = newTouchedProperties

            if(isChanged) {
                onDataChanged(newData)
            }
        },
        modifier = modifier,
    )
}

@Composable
public fun MaterialForm(
    schema: JsonSchema,
    uiSchema: UiSchema,
    data: JsonElement,
    touchedProperties: Set<JsonPointer>,
    postInputCallback: (FormFieldsContract.Inputs) -> Unit,
    modifier: Modifier = Modifier,
    validationResult: SchemaValidationResult = schema.validate(data),
) {
    Column(modifier) {
        val formScope = FormScopeImpl(
            schema = schema,
            uiSchema = uiSchema,
            data = data,
            touchedProperties = touchedProperties,
            elements = UiElement.materialDefaults(),
            controls = UiElement.Control.materialDefaults(),
            designSystem = MaterialDesignSystem(),
            postInputCallback = postInputCallback,
            validationResult = validationResult,
        )
        formScope.BasicForm()
    }
}

public expect fun UiElement.Control.Companion.materialDefaults(): List<Registered<UiElement.Control, ControlRenderer>>

public expect fun UiElement.Companion.materialDefaults(): List<Registered<UiElement.ElementWithChildren, UiElementRenderer>>
