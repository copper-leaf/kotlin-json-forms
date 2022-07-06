package com.copperleaf.forms.compose.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.input.TextFieldValue
import com.copperleaf.forms.compose.util.ControlRenderer
import com.copperleaf.forms.compose.util.Registered
import com.copperleaf.forms.compose.util.rememberUpdatableText
import com.copperleaf.forms.core.ArrayControl
import com.copperleaf.forms.core.BooleanControl
import com.copperleaf.forms.core.IntegerControl
import com.copperleaf.forms.core.NumberControl
import com.copperleaf.forms.core.ObjectControl
import com.copperleaf.forms.core.StringControl
import com.copperleaf.forms.core.ui.UiElement
import net.pwall.json.JSONString

public actual fun UiElement.Control.Companion.defaults(): List<Registered<UiElement.Control, ControlRenderer>> =
    listOf(
        StringControl.control(),
        StringControl.richText(),
        StringControl.dropdownEnum(),

        IntegerControl.control(),
        NumberControl.control(),
        BooleanControl.control(),
        ObjectControl.control(),
        ArrayControl.control(),
    )

public fun StringControl.dropdownEnum(): Registered<UiElement.Control, ControlRenderer> = uiControl(
    rank = 10,
    tester = { hasSchemaProperty("enum") }
) { control ->
    val vm = LocalViewModel.current
    val vmState by vm.observeStates().collectAsState()
    val currentPointer by dataPointer(control)

    val currentValue: State<String> = currentValueAtPointer(vmState, currentPointer, "") {
        it.toSimpleValue().toString()
    }

    val (text, updateText) = rememberUpdatableText(
        inputState = currentValue,
        onTextChange = { value ->
            updateFormState(vm, control, currentPointer, JSONString(value))
        }
    )

    var dropdownIsVisible by remember { mutableStateOf(false) }
    val allDropdownOptions: List<String> = remember {
        control.schemaConfig.getArray("enum").map { (it as JSONString).value }
    }
    val filteredDropdownOptions: List<String> = remember(text) {
        allDropdownOptions.filter { it.contains(text.text) }
    }

    Box(Modifier.fillMaxSize()) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { dropdownIsVisible = it.isFocused },
            value = text,
            onValueChange = updateText,
            label = { Text(control.label) },
            enabled = LocallyEnabled.current,
            trailingIcon = {
                IconButton(onClick = { dropdownIsVisible = true }, enabled = LocallyEnabled.current) {
                    if (dropdownIsVisible) {
                        Icon(Icons.Default.ArrowDropUp, "Close")
                    } else {
                        Icon(Icons.Default.ArrowDropDown, "Open")
                    }
                }
            }
        )

        DropdownMenu(
            expanded = dropdownIsVisible && LocallyEnabled.current,
            onDismissRequest = { dropdownIsVisible = false },
        ) {
            if (filteredDropdownOptions.isNotEmpty()) {
                filteredDropdownOptions.forEach { option ->
                    DropdownMenuItem(onClick = {
                        updateText(TextFieldValue(option))
                        dropdownIsVisible = false
                    }) {
                        Text(option)
                    }
                }
            } else {
                DropdownMenuItem(onClick = { }, enabled = false) {
                    Text("No Options")
                }
            }
        }
    }
}
