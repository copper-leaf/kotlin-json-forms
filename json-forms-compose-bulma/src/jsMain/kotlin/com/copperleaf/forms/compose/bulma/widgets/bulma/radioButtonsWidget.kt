package com.copperleaf.forms.compose.bulma.widgets.bulma

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.copperleaf.forms.compose.bulma.controls.BulmaField
import com.copperleaf.forms.compose.controls.ControlScope
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonPrimitive
import org.jetbrains.compose.web.attributes.disabled
import org.jetbrains.compose.web.attributes.name
import org.jetbrains.compose.web.dom.Label
import org.jetbrains.compose.web.dom.RadioInput
import org.jetbrains.compose.web.dom.Text

@Composable
public fun ControlScope.radioButtonsWidget(
    getOptions: JsonElement.()->List<Pair<String, String>>
) {
    val currentValue = getTypedValue("") {
        it.jsonPrimitive.content
    }

    val allOptions: List<Pair<String, String>> = remember {
        control.schemaConfig.getOptions()
    }

    BulmaField(control.label) {
        allOptions.forEach { (optionValue, optionTitle) ->
            Label(null, { classes("radio") }) {
                RadioInput(currentValue == optionValue) {
                    onClick { updateFormState(optionValue) }
                    if (!isEnabled) {
                        disabled()
                    }
                    name(control.label)
                }
                Text(" ${optionTitle}")
            }
        }
    }
}
