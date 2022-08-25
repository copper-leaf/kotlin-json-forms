package com.copperleaf.forms.compose.widgets.material

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.copperleaf.forms.compose.controls.ControlScope
import com.copperleaf.forms.compose.widgets.text.rememberUpdatableText
import com.copperleaf.forms.compose.widgets.text.updateText
import kotlinx.serialization.json.JsonElement

@Composable
public fun <T : Any> ControlScope.textFieldWidget(
    defaultValue: T,
    mapper: (JsonElement) -> T?,
    mapStateToText: (T) -> String,
    nudgeUp: ((T) -> T)? = null,
    nudgeDown: ((T) -> T)? = null,
) {
    val currentValue = getTypedValue(defaultValue, mapper)

    val updatableText = rememberUpdatableText<T>(
        initialValue = currentValue,
        mapStateToText = mapStateToText,
        onTextChange = { value ->
            // send the value as a well-formed T value, if possible. Otherwise, still send the data to the ViewModel,
            // but as a string
            updateFormState(value.toIntOrNull() ?: value)
        }
    )
    val (text, updateText) = updatableText

    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = text,
        onValueChange = updateText,
        label = { Text(control.label) },
        trailingIcon = if (nudgeUp != null && nudgeDown != null) {
            {
                Column(Modifier.fillMaxHeight(), verticalArrangement = Arrangement.SpaceEvenly) {
                    Icon(Icons.Default.KeyboardArrowUp, "Up", modifier = Modifier.clickable {
                        updatableText.updateText(mapStateToText(nudgeUp(currentValue)))
                    })
                    Icon(Icons.Default.KeyboardArrowDown, "Down", modifier = Modifier.clickable {
                        updatableText.updateText(mapStateToText(nudgeDown(currentValue)))
                    })
                }
            }
        } else {
            null
        },
        enabled = isEnabled,
    )
}
