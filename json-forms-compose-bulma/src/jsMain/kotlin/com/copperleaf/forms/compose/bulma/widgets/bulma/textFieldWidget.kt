package com.copperleaf.forms.compose.bulma.widgets.bulma

import androidx.compose.runtime.Composable
import com.copperleaf.forms.compose.bulma.controls.BulmaField
import com.copperleaf.forms.compose.controls.ControlScope
import kotlinx.serialization.json.JsonElement
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.attributes.disabled
import org.jetbrains.compose.web.dom.Input

@Composable
public fun <T> ControlScope.textFieldWidget(
    inputType: InputType<T>,
    defaultValue: T,
    mapper: (JsonElement) -> T?,
    mapStateToText: (T) -> String,
) {
    val currentValue = getTypedValue(defaultValue, mapper)

    BulmaField(control.label) {
        Input(
            type = inputType,
        ) {
            value(mapStateToText(currentValue))
            classes("input")
            if (!isEnabled) {
                disabled()
            }
            onInput { event -> updateFormState(event.value) }
        }
    }
}
