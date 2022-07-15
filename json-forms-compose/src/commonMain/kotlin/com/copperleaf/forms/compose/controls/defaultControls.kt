package com.copperleaf.forms.compose.controls

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Checkbox
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.RadioButton
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.copperleaf.forms.compose.form.Registered
import com.copperleaf.forms.compose.form.UiElement
import com.copperleaf.forms.compose.form.WithArrayIndex
import com.copperleaf.forms.compose.form.uiControl
import com.copperleaf.forms.compose.util.rememberUpdatableText
import com.copperleaf.forms.compose.util.updateText
import com.copperleaf.forms.compose.widgets.dropdown.DropdownMenu
import com.copperleaf.forms.compose.widgets.dropdown.DropdownMenuItem
import com.copperleaf.forms.compose.widgets.richtext.RichTextToolbar
import com.copperleaf.forms.compose.widgets.richtext.richTextToolbarShortcuts
import com.copperleaf.forms.core.ArrayControl
import com.copperleaf.forms.core.BooleanControl
import com.copperleaf.forms.core.IntegerControl
import com.copperleaf.forms.core.NumberControl
import com.copperleaf.forms.core.ObjectControl
import com.copperleaf.forms.core.StringControl
import com.copperleaf.forms.core.internal.resolveAsControl
import com.copperleaf.forms.core.ui.UiElement
import com.copperleaf.json.pointer.JsonPointerAction
import com.copperleaf.json.pointer.plus
import com.copperleaf.json.pointer.toUriFragment
import com.copperleaf.json.values.arrayAt
import com.copperleaf.json.values.objectAt
import com.copperleaf.json.values.string
import com.darkrockstudios.richtexteditor.model.RichTextValue
import com.darkrockstudios.richtexteditor.ui.RichTextEditor
import com.darkrockstudios.richtexteditor.ui.defaultRichTextFieldStyle
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.doubleOrNull
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

// StringControls
// ---------------------------------------------------------------------------------------------------------------------

public fun StringControl.control(): Registered<UiElement.Control, ControlRenderer> = uiControl {
    val currentValue = getTypedValue("") {
        it.jsonPrimitive.content
    }
    val (text, updateText) = rememberUpdatableText(
        initialValue = currentValue,
        onTextChange = { value ->
            updateFormState(value)
        }
    )

    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = text,
        onValueChange = updateText,
        label = { Text(control.label) },
        enabled = isEnabled,
        isError = validationErrors.isNotEmpty(),
    )
}

public fun StringControl.richText(): Registered<UiElement.Control, ControlRenderer> = uiControl(
    rank = 10,
    tester = { optionIsEnabled("richText") }
) {
    var value by remember { mutableStateOf(RichTextValue.get()) }

    RichTextToolbar(
        modifier = Modifier,
        value = value,
        onValueChange = { value = it },
    )

    RichTextEditor(
        modifier = Modifier
            .richTextToolbarShortcuts(value, { value = it })
            .defaultMinSize(minHeight = 120.dp)
            .border(width = 1.dp, color = MaterialTheme.colors.primary, shape = RoundedCornerShape(4.dp)),
        value = value,
        onValueChange = { value = it },
        textFieldStyle = defaultRichTextFieldStyle().copy(
            textColor = MaterialTheme.colors.onSurface,
            cursorColor = MaterialTheme.colors.primary
        ),
    )
}

public fun StringControl.dropdownEnum(): Registered<UiElement.Control, ControlRenderer> = uiControl(
    rank = 20,
    tester = { hasSchemaProperty("enum") }
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
    val allDropdownOptions: List<String> = remember {
        control.schemaConfig.arrayAt("enum").map { it.jsonPrimitive.content }
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
            if (allDropdownOptions.isNotEmpty()) {
                allDropdownOptions.forEach { option ->
                    DropdownMenuItem(
                        onClick = {
                            updateText(TextFieldValue(option))
                            dropdownIsVisible = false
                        },
                        content = { Text(option) },
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

public fun StringControl.dropdownOneOf(): Registered<UiElement.Control, ControlRenderer> = uiControl(
    rank = 21,
    tester = { hasSchemaProperty("oneOf") }
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
    val allDropdownOptions: List<Pair<String, String>> = remember {
        control.schemaConfig.arrayAt("oneOf").map {
            it.jsonObject.string("const") to it.jsonObject.string("title")
        }
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
            if (allDropdownOptions.isNotEmpty()) {
                allDropdownOptions.forEach { (const, title) ->
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

public fun StringControl.radioButtonEnum(): Registered<UiElement.Control, ControlRenderer> = uiControl(
    rank = 30,
    tester = { hasSchemaProperty("enum") && optionFieldIs("format", "radio") }
) {
    val currentValue = getTypedValue("") {
        it.jsonPrimitive.content
    }

    val allDropdownOptions: List<String> = remember {
        control.schemaConfig.arrayAt("enum").map { it.jsonPrimitive.content }
    }

    Row(Modifier.fillMaxSize(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        allDropdownOptions.forEach {
            Row(Modifier.clickable { updateFormState(it) }) {
                RadioButton(
                    selected = currentValue == it,
                    onClick = null,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(it)
            }
        }
    }
}

public fun StringControl.radioButtonOneOf(): Registered<UiElement.Control, ControlRenderer> = uiControl(
    rank = 31,
    tester = { hasSchemaProperty("oneOf") && optionFieldIs("format", "radio") }
) {
    val currentValue = getTypedValue("") {
        it.jsonPrimitive.content
    }

    val allDropdownOptions: List<Pair<String, String>> = remember {
        control.schemaConfig.arrayAt("oneOf").map {
            it.jsonObject.string("const") to it.jsonObject.string("title")
        }
    }

    Row(Modifier.fillMaxSize(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        allDropdownOptions.forEach { (const, title) ->
            Row(Modifier.clickable { updateFormState(const) }) {
                RadioButton(
                    selected = currentValue == const,
                    onClick = null,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(title)
            }
        }
    }
}

// Number Controls
// ---------------------------------------------------------------------------------------------------------------------

public fun IntegerControl.control(): Registered<UiElement.Control, ControlRenderer> = uiControl {
    val currentValue = getTypedValue(0) {
        it.jsonPrimitive.intOrNull
    }

    val updatableText = rememberUpdatableText(
        initialValue = currentValue,
        mapStateToText = { it.toString() },
        onTextChange = { value ->
            // send the value as a well-formed Int, if possible. Otherwise, still send the data to the ViewModel, but as
            // a string
            updateFormState(value.toIntOrNull() ?: value)
        }
    )
    val (text, updateText) = updatableText
    val nudge = { amount: Int ->
        updatableText.updateText((currentValue + amount).toString())
    }

    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = text,
        onValueChange = updateText,
        label = { Text(control.label) },
        trailingIcon = {
            Column(Modifier.fillMaxHeight(), verticalArrangement = Arrangement.SpaceEvenly) {
                Icon(Icons.Default.KeyboardArrowUp, "Up", modifier = Modifier.clickable { nudge(1) })
                Icon(Icons.Default.KeyboardArrowDown, "Down", modifier = Modifier.clickable { nudge(-1) })
            }
        },
        enabled = isEnabled,
    )
}

public fun NumberControl.control(): Registered<UiElement.Control, ControlRenderer> = uiControl {
    val currentValue = getTypedValue(0.0) {
        it.jsonPrimitive.doubleOrNull
    }

    val updatableText = rememberUpdatableText(
        initialValue = currentValue,
        mapStateToText = { it.toString() },
        onTextChange = { value ->
            // send the value as a well-formed Double, if possible. Otherwise, still send the data to the ViewModel, but
            // as a string
            updateFormState(value.toDoubleOrNull() ?: value)
        }
    )
    val (text, updateText) = updatableText
    val nudge = { amount: Double ->
        updatableText.updateText((currentValue + amount).toString())
    }

    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = text,
        onValueChange = updateText,
        label = { Text(control.label) },
        trailingIcon = {
            Column(Modifier.fillMaxHeight(), verticalArrangement = Arrangement.SpaceEvenly) {
                Icon(Icons.Default.KeyboardArrowUp, "Up", modifier = Modifier.clickable { nudge(1.0) })
                Icon(Icons.Default.KeyboardArrowDown, "Down", modifier = Modifier.clickable { nudge(-1.0) })
            }
        },
        enabled = isEnabled,
    )
}

// Boolean Controls
// ---------------------------------------------------------------------------------------------------------------------

public fun BooleanControl.checkbox(): Registered<UiElement.Control, ControlRenderer> = uiControl {
    val currentValue = getTypedValue(false) {
        it.jsonPrimitive.booleanOrNull
    }

    Row(verticalAlignment = Alignment.CenterVertically) {
        Checkbox(
            checked = currentValue,
            onCheckedChange = {
                updateFormState(it)
            },
            enabled = isEnabled,
        )
        Text(control.label)
    }
}

public fun BooleanControl.switch(): Registered<UiElement.Control, ControlRenderer> = uiControl(
    rank = 10,
    tester = {
        optionIsEnabled("toggle")
    }
) {
    val currentValue = getTypedValue(false) {
        it.jsonPrimitive.booleanOrNull
    }

    Row(verticalAlignment = Alignment.CenterVertically) {
        Switch(
            checked = currentValue,
            onCheckedChange = {
                updateFormState(it)
            },
            enabled = isEnabled,
        )
        Text(control.label)
    }
}

// Composite Controls
// ---------------------------------------------------------------------------------------------------------------------

public fun ObjectControl.control(): Registered<UiElement.Control, ControlRenderer> = uiControl {
    val properties = control.schemaConfig.objectAt("properties")

    properties.forEach { (key, _) ->
        val objectFieldControl by remember {
            derivedStateOf {
                JsonObject(
                    mapOf(
                        "type" to JsonPrimitive("Control"),
                        "scope" to JsonPrimitive((control.schemaScope + "/properties/$key").toUriFragment())
                    )
                ).resolveAsControl(vmState.schemaJson)
            }
        }

        UiElement(
            objectFieldControl
        )
    }
}

public fun ArrayControl.control(): Registered<UiElement.Control, ControlRenderer> = uiControl {
    val currentDataValue: JsonArray = getTypedValue(JsonArray(emptyList())) {
        if (it == JsonNull) {
            JsonArray(emptyList())
        } else {
            it.jsonArray
        }
    }

    Row {
        Button(
            onClick = {
                sendFormAction(
                    pointer = dataPointer + "/${currentDataValue.size}",
                    action = JsonPointerAction.SetValue(JsonNull),
                )
                markAsTouched()
            },
            enabled = isEnabled,
        ) {
            Text("Add New Item")
        }
    }

    Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        currentDataValue.forEachIndexed { index, _ ->
            WithArrayIndex(index) {
                Card(Modifier.fillMaxWidth(), elevation = 4.dp) {
                    Box(Modifier.padding(8.dp)) {
                        Column {
                            Button(
                                onClick = {
                                    sendFormAction(
                                        pointer = dataPointer + "/$index",
                                        action = JsonPointerAction.RemoveValue
                                    )
                                },
                                enabled = isEnabled,
                            ) {
                                Text("Remove")
                            }

                            val arrayFieldControl by remember {
                                derivedStateOf {
                                    JsonObject(
                                        mapOf(
                                            "type" to JsonPrimitive("Control"),
                                            "scope" to JsonPrimitive((control.schemaScope + "/items").toUriFragment())
                                        )
                                    ).resolveAsControl(vmState.schemaJson)
                                }
                            }
                            UiElement(
                                arrayFieldControl
                            )
                        }
                    }
                }
            }
        }
    }
}
