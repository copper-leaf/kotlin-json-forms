package com.copperleaf.forms.compose.bulma.widgets.bulma

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.copperleaf.forms.compose.bulma.controls.BulmaField
import com.copperleaf.forms.compose.controls.ControlScope
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonPrimitive
import org.jetbrains.compose.web.attributes.disabled
import org.jetbrains.compose.web.attributes.selected
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Option
import org.jetbrains.compose.web.dom.Select
import org.jetbrains.compose.web.dom.Text

@Composable
public fun ControlScope.dropdownWidget(
    getOptions: JsonElement.()->List<Pair<String, String>>
) {
    val currentValue = getTypedValue("") {
        it.jsonPrimitive.content
    }

    val allOptions: List<Pair<String, String>> = remember {
        control.schemaConfig.getOptions()
    }

    BulmaField(control.label) {
        Div({ classes("select") }) {
            Select(
                {
                    onInput { event -> updateFormState(event.value.toString()) }
                    if (!isEnabled) {
                        disabled()
                    }
                }
            ) {
                allOptions.forEach { (optionValue, optionTitle) ->
                    Option(optionValue, {
                        if (currentValue == optionValue) {
                            selected()
                        }
                        if (!isEnabled) {
                            disabled()
                        }
                    }) {
                        Text(optionTitle)
                    }
                }
            }
        }
    }
}
