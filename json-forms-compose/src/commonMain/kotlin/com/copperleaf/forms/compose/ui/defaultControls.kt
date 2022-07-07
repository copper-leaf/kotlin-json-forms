package com.copperleaf.forms.compose.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Checkbox
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.copperleaf.forms.compose.util.ControlRenderer
import com.copperleaf.forms.compose.util.Registered
import com.copperleaf.forms.compose.util.RichTextToolbar
import com.copperleaf.forms.compose.util.rememberUpdatableText
import com.copperleaf.forms.compose.util.richTextToolbarShortcuts
import com.copperleaf.forms.compose.util.updateText
import com.copperleaf.forms.core.ArrayControl
import com.copperleaf.forms.core.BooleanControl
import com.copperleaf.forms.core.ControlType
import com.copperleaf.forms.core.IntegerControl
import com.copperleaf.forms.core.NumberControl
import com.copperleaf.forms.core.ObjectControl
import com.copperleaf.forms.core.StringControl
import com.copperleaf.forms.core.internal.resolveAsControl
import com.copperleaf.forms.core.ui.UiElement
import com.copperleaf.json.pointer.JsonPointerAction
import com.copperleaf.json.pointer.defaultValueForType
import com.copperleaf.json.pointer.plus
import com.copperleaf.json.pointer.toJsonValue
import com.darkrockstudios.richtexteditor.model.RichTextValue
import com.darkrockstudios.richtexteditor.ui.RichTextEditor
import com.darkrockstudios.richtexteditor.ui.defaultRichTextFieldStyle
import net.pwall.json.JSONArray
import net.pwall.json.JSONObject
import net.pwall.json.JSONString

public fun UiElement.Control.matchesControlType(type: ControlType): Boolean {
    return (this.controlType == type.type)
}

public fun UiElement.Control.optionIsEnabled(name: String): Boolean {
    return uiSchemaConfig
        .getObject("options")
        ?.getBoolean(name) == true
}

public fun UiElement.Control.hasSchemaProperty(name: String): Boolean {
    return schemaConfig.containsKey(name)
}

public fun ControlType.uiControl(
    rank: Int = 0,
    tester: UiElement.Control.() -> Boolean = { true },
    renderer: ControlRenderer,
): Registered<UiElement.Control, ControlRenderer> {
    return Registered(
        rank = rank,
        tester = { it.matchesControlType(this) && tester(it) },
        renderer = renderer,
    )
}

public expect fun UiElement.Control.Companion.defaults(): List<Registered<UiElement.Control, ControlRenderer>>

public fun StringControl.control(): Registered<UiElement.Control, ControlRenderer> = uiControl {
    val currentValue = getTypedValue("") {
        it.toSimpleValue().toString()
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
        enabled = LocallyEnabled.current,
        isError = validationErrors.isNotEmpty(),
    )
}

public fun StringControl.richText(): Registered<UiElement.Control, ControlRenderer> = uiControl(
    rank = 10,
    tester = { optionIsEnabled("richText") }
) {
    val currentValue = getTypedValue("") {
        it.toSimpleValue().toString()
    }
    var value by remember { mutableStateOf(RichTextValue.get()) }

    RichTextToolbar(
        modifier = Modifier,
        value = value,
        onValueChange = { value = it },
    )

    RichTextEditor(
        modifier = Modifier.richTextToolbarShortcuts(value, { value = it }),
        value = value,
        onValueChange = { value = it },
        textFieldStyle = defaultRichTextFieldStyle().copy(
            textColor = MaterialTheme.colors.onSurface,
        ),
    )
}

public fun IntegerControl.control(): Registered<UiElement.Control, ControlRenderer> = uiControl {
    val currentValue = getTypedValue(0) {
        it.toSimpleValue().toString().toIntOrNull()
    }

    val updatableText = rememberUpdatableText(
        initialValue = currentValue,
        mapStateToText = { it.toString() },
        onTextChange = { value ->
            value.toIntOrNull()?.let { intValue ->
                updateFormState(intValue)
            }
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
        enabled = LocallyEnabled.current,
    )
}

public fun NumberControl.control(): Registered<UiElement.Control, ControlRenderer> = uiControl {
    val currentValue = getTypedValue(0.0) {
        it.toSimpleValue().toString().toDoubleOrNull()
    }

    val updatableText = rememberUpdatableText(
        initialValue = currentValue,
        mapStateToText = { it.toString() },
        onTextChange = { value ->
            value.toDoubleOrNull()?.let { doubleValue ->
                updateFormState(doubleValue.toBigDecimal())
            }
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
        enabled = LocallyEnabled.current,
    )
}

public fun BooleanControl.control(): Registered<UiElement.Control, ControlRenderer> = uiControl {
    val currentValue = getTypedValue(false) {
        it.toSimpleValue().toString().toBooleanStrictOrNull() ?: false
    }

    Row(verticalAlignment = Alignment.CenterVertically) {
        Checkbox(
            checked = currentValue,
            onCheckedChange = {
                updateFormState(it)
            },
            enabled = LocallyEnabled.current,
        )
        Text(control.label)
    }
}

public fun ObjectControl.control(): Registered<UiElement.Control, ControlRenderer> = uiControl {
    val properties = control.schemaConfig.getObject("properties") ?: emptyMap()

    properties.forEach { (key, schemaValue) ->
        val objectFieldControl by remember {
            derivedStateOf {
                JSONObject(
                    mapOf(
                        "type" to JSONString("Control"),
                        "scope" to JSONString("${control.schemaScope}/properties/$key")
                    )
                ).resolveAsControl(vmState.schemaJson!!)
            }
        }

        RenderUiControl(
            objectFieldControl
        )
    }
}

public fun ArrayControl.control(): Registered<UiElement.Control, ControlRenderer> = uiControl {
    val properties = control.schemaConfig.getObject("properties") ?: emptyMap()

    val currentDataValue: JSONArray = getTypedValue(JSONArray()) {
        it as JSONArray
    }

    val items: JSONObject = control.schemaConfig.requireObject("items")
    val itemsType: String = items.requireString("type")

    Row {
        Button(onClick = {
            sendFormAction(
                pointer = dataPointer + "/${currentDataValue.size}",
                action = JsonPointerAction.SetValue(itemsType.defaultValueForType().toJsonValue()),
            )
        }) {
            Text("Add New Item")
        }
    }

    currentDataValue.forEachIndexed { index, jsonValue ->
        WithArrayIndex(index) {
            Card(Modifier.fillMaxWidth()) {
                Box(Modifier.padding(8.dp)) {
                    Column {
                        Button(onClick = {
                            sendFormAction(
                                pointer = dataPointer + "/$index",
                                action = JsonPointerAction.RemoveValue
                            )
                        }) {
                            Text("Remove")
                        }

                        val arrayFieldControl by remember {
                            derivedStateOf {
                                JSONObject(
                                    mapOf(
                                        "type" to JSONString("Control"),
                                        "scope" to JSONString("${control.schemaScope}/items")
                                    )
                                ).resolveAsControl(vmState.schemaJson!!)
                            }
                        }
                        RenderUiControl(
                            arrayFieldControl
                        )
                    }
                }
            }
        }
    }
}

// Control utils
// ---------------------------------------------------------------------------------------------------------------------

@Composable
public fun WithArrayIndex(
    index: Int,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(LocalArrayIndices provides (LocalArrayIndices.current + index)) {
        content()
    }
}

