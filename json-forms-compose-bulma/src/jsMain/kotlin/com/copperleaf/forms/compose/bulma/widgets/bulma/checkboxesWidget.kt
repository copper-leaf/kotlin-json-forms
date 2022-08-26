package com.copperleaf.forms.compose.bulma.widgets.bulma

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.copperleaf.forms.compose.bulma.controls.BulmaField
import com.copperleaf.forms.compose.controls.ControlScope
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonPrimitive
import org.jetbrains.compose.web.attributes.disabled
import org.jetbrains.compose.web.dom.CheckboxInput
import org.jetbrains.compose.web.dom.Label
import org.jetbrains.compose.web.dom.Text

@Composable
public fun ControlScope.checkboxWidget() {
    val currentValue = getTypedValue(false) {
        it.jsonPrimitive.booleanOrNull
    }
    BulmaField(control.label) {
        CheckboxInput(currentValue) {
            if (!isEnabled) {
                disabled()
            }
            onInput { event -> updateFormState(event.value) }
        }
    }
}

@Composable
public fun ControlScope.switchWidget() {
    checkboxWidget()
}

@Composable
public fun ControlScope.checkboxesWidget(
    getOptions: JsonElement.()->List<Pair<String, String>>
) {
    val selectedValues: List<String> = getTypedValue(emptyList()) {
        if (it == JsonNull) {
            emptyList()
        } else {
            it.jsonArray.map { it.jsonPrimitive.content }
        }
    }
    val allOptions: List<Pair<String, String>> = remember {
        control.schemaConfig.getOptions()
    }

    BulmaField(control.label) {
        allOptions.forEach { (optionValue, optionTitle) ->
            Label(null, { classes("checkbox") }) {
                CheckboxInput(optionValue in selectedValues) {
                    onClick {
                        if (optionValue in selectedValues) {
                            removeArrayItem(selectedValues.indexOf(optionValue))
                        } else {
                            addArrayItem(selectedValues.size, optionValue)
                        }
                    }
                    if (!isEnabled) {
                        disabled()
                    }
                }
                Text(" $optionTitle")
            }
        }
    }
}
