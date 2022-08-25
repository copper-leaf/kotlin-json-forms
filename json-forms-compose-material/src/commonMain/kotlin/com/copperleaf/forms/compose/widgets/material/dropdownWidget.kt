package com.copperleaf.forms.compose.widgets.material

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.input.TextFieldValue
import com.copperleaf.forms.compose.controls.ControlScope
import com.copperleaf.forms.compose.widgets.dropdown.DropdownMenu
import com.copperleaf.forms.compose.widgets.dropdown.DropdownMenuItem
import com.copperleaf.forms.compose.widgets.text.rememberUpdatableText
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonPrimitive

@Composable
public fun ControlScope.dropdownWidget(
    getOptions: JsonElement.()->List<Pair<String, String>>
) {
    val currentValue = getTypedValue("") {
        it.jsonPrimitive.content
    }

    val (text, updateText) = rememberUpdatableText(
        initialValue = currentValue,
        onTextChange = { value ->
            updateFormState(value)
        }
    )

    var dropdownIsVisible by remember { mutableStateOf(false) }
    val allOptions: List<Pair<String, String>> = remember {
        control.schemaConfig.getOptions()
    }

    Box(Modifier.fillMaxSize()) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { dropdownIsVisible = it.isFocused },
            value = text,
            onValueChange = updateText,
            label = { Text(control.label) },
            enabled = isEnabled,

            trailingIcon = {
                IconButton(onClick = { dropdownIsVisible = true }, enabled = isEnabled) {
                    if (dropdownIsVisible) {
                        Icon(Icons.Default.ArrowDropUp, "Close")
                    } else {
                        Icon(Icons.Default.ArrowDropDown, "Open")
                    }
                }
            }
        )

        DropdownMenu(
            expanded = dropdownIsVisible && isEnabled,
            onDismissRequest = { dropdownIsVisible = false },
        ) {
            if (allOptions.isNotEmpty()) {
                allOptions.forEach { (const, title) ->
                    DropdownMenuItem(
                        onClick = {
                            updateText(TextFieldValue(const))
                            dropdownIsVisible = false
                        },
                        content = { Text(title) },
                    )
                }
            } else {
                DropdownMenuItem(onClick = { }, enabled = false) {
                    Text("No Options")
                }
            }
        }
    }
}
