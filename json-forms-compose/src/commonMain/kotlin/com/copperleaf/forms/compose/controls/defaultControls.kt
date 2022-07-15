package com.copperleaf.forms.compose.controls

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.copperleaf.forms.compose.form.Registered
import com.copperleaf.forms.compose.form.UiElement
import com.copperleaf.forms.compose.form.WithArrayIndex
import com.copperleaf.forms.compose.form.uiControl
import com.copperleaf.forms.compose.widgets.codeeditor.model.CodeLang
import com.copperleaf.forms.compose.widgets.codeeditor.prettify.PrettifyParser
import com.copperleaf.forms.compose.widgets.codeeditor.theme.CodeThemeType
import com.copperleaf.forms.compose.widgets.codeeditor.utils.parseCodeAsAnnotatedString
import com.copperleaf.forms.compose.widgets.dropdown.DropdownMenu
import com.copperleaf.forms.compose.widgets.dropdown.DropdownMenuItem
import com.copperleaf.forms.compose.widgets.richtext.RichTextToolbar
import com.copperleaf.forms.compose.widgets.richtext.rememberUpdatableRichText
import com.copperleaf.forms.compose.widgets.richtext.richTextShortcuts
import com.copperleaf.forms.compose.widgets.text.rememberUpdatableAnnotatedText
import com.copperleaf.forms.compose.widgets.text.rememberUpdatableText
import com.copperleaf.forms.compose.widgets.text.updateText
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
import com.copperleaf.json.values.optional
import com.copperleaf.json.values.string
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

// Text Field Controls
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
    val currentValue = getTypedValue("") {
        it.jsonPrimitive.content
    }
    val (text, updateText) = rememberUpdatableRichText(currentValue) {
        updateFormState(it)
    }

    Text(control.label, style = MaterialTheme.typography.subtitle1)
    RichTextToolbar(
        modifier = Modifier,
        value = text,
        onValueChange = updateText,
    )

    RichTextEditor(
        modifier = Modifier
            .richTextShortcuts(text, updateText)
            .defaultMinSize(minHeight = 120.dp)
            .border(width = 1.dp, color = MaterialTheme.colors.primary, shape = RoundedCornerShape(4.dp)),
        value = text,
        onValueChange = updateText,
        textFieldStyle = defaultRichTextFieldStyle().copy(
            textColor = MaterialTheme.colors.onSurface,
            cursorColor = MaterialTheme.colors.primary
        ),
    )
}

public fun StringControl.codeEditor(): Registered<UiElement.Control, ControlRenderer> = uiControl(
    rank = 20,
    tester = { optionIsEnabled("codeEditor") }
) {
    val language = control.uiSchemaConfig.optional {
        this.objectAt("options").string("lang").let { languageName ->
            CodeLang.values().firstOrNull { language ->
                languageName in language.value || languageName == language.name
            }
        }
    } ?: CodeLang.HTML
    val parser = remember { PrettifyParser() } // try getting from LocalPrettifyParser.current
    val theme = remember { CodeThemeType.Monokai.getTheme() }
    val currentValue = getTypedValue(AnnotatedString("")) {
        parseCodeAsAnnotatedString(
            parser = parser,
            theme = theme,
            lang = language,
            code = it.jsonPrimitive.content
        )
    }
    val (text: TextFieldValue, updateText: (TextFieldValue) -> Unit) = rememberUpdatableAnnotatedText(
        initialValue = currentValue,
        onTextChange = { value ->
            updateFormState(value.text)
        }
    )

    var lineTops by remember { mutableStateOf(emptyArray<Float>()) }
    val density = LocalDensity.current

    Text(control.label, style = MaterialTheme.typography.subtitle1)
    Row(
        modifier = Modifier
            .background(MaterialTheme.colors.onSurface.copy(alpha = 0.5f))
            .border(width = 1.dp, color = MaterialTheme.colors.onSurface.copy(alpha = 0.25f))
    ) {
        if (lineTops.isNotEmpty()) {
            Box(modifier = Modifier.padding(horizontal = 4.dp)) {
                lineTops.forEachIndexed { index, top ->
                    Text(
                        modifier = Modifier.offset(y = with(density) { top.toDp() }),
                        text = index.toString(),
                        color = MaterialTheme.colors.onBackground.copy(.3f)
                    )
                }
            }
        }
        BasicTextField(
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = 120.dp),
            value = text,
            onValueChange = updateText,
            enabled = isEnabled,
            onTextLayout = { result ->
                lineTops = Array(result.lineCount) { result.getLineTop(it) }
            }
        )
    }
}

// Single-Select Controls
// ---------------------------------------------------------------------------------------------------------------------

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
    val allOptions: List<String> = remember {
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
            if (allOptions.isNotEmpty()) {
                allOptions.forEach { option ->
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
    val allOptions: List<Pair<String, String>> = remember {
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

public fun StringControl.radioButtonEnum(): Registered<UiElement.Control, ControlRenderer> = uiControl(
    rank = 30,
    tester = { hasSchemaProperty("enum") && optionFieldIs("format", "radio") }
) {
    val currentValue = getTypedValue("") {
        it.jsonPrimitive.content
    }

    val allOptions: List<String> = remember {
        control.schemaConfig.arrayAt("enum").map { it.jsonPrimitive.content }
    }

    Row(Modifier.fillMaxSize(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        allOptions.forEach {
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

    val allOptions: List<Pair<String, String>> = remember {
        control.schemaConfig.arrayAt("oneOf").map {
            it.jsonObject.string("const") to it.jsonObject.string("title")
        }
    }

    Row(Modifier.fillMaxSize(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        allOptions.forEach { (const, title) ->
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

// Multi-Select Controls
// ---------------------------------------------------------------------------------------------------------------------

public fun ArrayControl.checkboxesEnum(): Registered<UiElement.Control, ControlRenderer> = uiControl(
    rank = 20,
    tester = { hasSchemaProperty("uniqueItems") && hasArrayItemType(StringControl) && hasArrayItemProperty("enum") }
) {
    val selectedValues: List<String> = getTypedValue(emptyList()) {
        if (it == JsonNull) {
            emptyList()
        } else {
            it.jsonArray.map { it.jsonPrimitive.content }
        }
    }
    val allOptions: List<String> = remember {
        control.schemaConfig.objectAt("items").arrayAt("enum").map { it.jsonPrimitive.content }
    }

    Row(Modifier.fillMaxSize(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        allOptions.forEach { option ->
            Row(Modifier.clickable {
                if (option in selectedValues) {
                    sendFormAction(
                        pointer = dataPointer + "/${selectedValues.indexOf(option)}",
                        action = JsonPointerAction.RemoveValue,
                    )
                } else {
                    sendFormAction(
                        pointer = dataPointer + "/${selectedValues.size}",
                        action = JsonPointerAction.SetValue(option),
                    )
                }
                markAsTouched()
            }) {
                Checkbox(
                    checked = option in selectedValues,
                    onCheckedChange = null,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(option)
            }
        }
    }
}

public fun ArrayControl.checkboxesOneOf(): Registered<UiElement.Control, ControlRenderer> = uiControl(
    rank = 21,
    tester = { hasSchemaProperty("uniqueItems") && hasArrayItemProperty("oneOf") }
) {
    val selectedValues: List<String> = getTypedValue(emptyList()) {
        if (it == JsonNull) {
            emptyList()
        } else {
            it.jsonArray.map { it.jsonPrimitive.content }
        }
    }
    val allOptions: List<Pair<String, String>> = remember {
        control.schemaConfig.objectAt("items").arrayAt("oneOf").map { it.string("const") to it.string("title") }
    }

    Row(Modifier.fillMaxSize(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        allOptions.forEach { (const, title) ->
            Row(Modifier.clickable {
                if (const in selectedValues) {
                    sendFormAction(
                        pointer = dataPointer + "/${selectedValues.indexOf(const)}",
                        action = JsonPointerAction.RemoveValue,
                    )
                } else {
                    sendFormAction(
                        pointer = dataPointer + "/${selectedValues.size}",
                        action = JsonPointerAction.SetValue(const),
                    )
                }
                markAsTouched()
            }) {
                Checkbox(
                    checked = const in selectedValues,
                    onCheckedChange = null,
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
