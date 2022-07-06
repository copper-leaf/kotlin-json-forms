package com.copperleaf.forms.compose.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Checkbox
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.copperleaf.forms.core.vm.FormContract
import com.copperleaf.forms.core.vm.FormViewModel
import com.copperleaf.json.pointer.JsonPointerAction
import com.copperleaf.json.pointer.asPointer
import com.darkrockstudios.richtexteditor.model.RichTextValue
import com.darkrockstudios.richtexteditor.ui.RichTextEditor
import com.darkrockstudios.richtexteditor.ui.defaultRichTextFieldStyle
import net.pwall.json.JSONBoolean
import net.pwall.json.JSONDecimal
import net.pwall.json.JSONInteger
import net.pwall.json.JSONObject
import net.pwall.json.JSONString
import net.pwall.json.JSONValue
import net.pwall.json.pointer.JSONPointer

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

expect public fun UiElement.Control.Companion.defaults(): List<Registered<UiElement.Control, ControlRenderer>>

public fun StringControl.control(): Registered<UiElement.Control, ControlRenderer> = uiControl { control ->
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

    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = text,
        onValueChange = updateText,
        label = { Text(control.label) },
        enabled = LocallyEnabled.current,
    )
}

public fun StringControl.richText(): Registered<UiElement.Control, ControlRenderer> = uiControl(
    rank = 10,
    tester = { optionIsEnabled("richText") }
) { control ->
    val vm = LocalViewModel.current
    val vmState by vm.observeStates().collectAsState()
    val currentPointer by dataPointer(control)

    val currentValue: State<String> = currentValueAtPointer(vmState, currentPointer, "") {
        it.toSimpleValue().toString()
    }

    var value by remember { mutableStateOf(RichTextValue.get()) }

//    val (text, updateText) = rememberUpdatableRichText(
//        inputState = currentValue,
//        onTextChange = { value ->
//            updateFormState(vm, control, currentPointer, JSONString(value))
//        }
//    )

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

public fun IntegerControl.control(): Registered<UiElement.Control, ControlRenderer> = uiControl { control ->
    val vm = LocalViewModel.current
    val vmState by vm.observeStates().collectAsState()
    val currentPointer by dataPointer(control)

    val currentValue: State<Int> = currentValueAtPointer(vmState, currentPointer, 0) {
        it.toSimpleValue().toString().toIntOrNull() ?: 0
    }

    val updatableText = rememberUpdatableText(
        inputState = currentValue,
        mapStateToText = { it.toString() },
        onTextChange = { value ->
            value.toIntOrNull()?.let { intValue ->
                updateFormState(vm, control, currentPointer, JSONInteger(intValue))
            }
        }
    )
    val (text, updateText) = updatableText
    val nudge = { amount: Int ->
        updatableText.updateText((currentValue.value + amount).toString())
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

public fun NumberControl.control(): Registered<UiElement.Control, ControlRenderer> = uiControl { control ->
    val vm = LocalViewModel.current
    val vmState by vm.observeStates().collectAsState()
    val currentPointer by dataPointer(control)

    val currentValue: State<Double> = currentValueAtPointer(vmState, currentPointer, 0.0) {
        it.toSimpleValue().toString().toDoubleOrNull() ?: 0.0
    }

    val updatableText = rememberUpdatableText(
        inputState = currentValue,
        mapStateToText = { it.toString() },
        onTextChange = { value ->
            value.toDoubleOrNull()?.let { doubleValue ->
                updateFormState(vm, control, currentPointer, JSONDecimal(doubleValue.toBigDecimal()))
            }
        }
    )
    val (text, updateText) = updatableText
    val nudge = { amount: Double ->
        updatableText.updateText((currentValue.value + amount).toString())
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

public fun BooleanControl.control(): Registered<UiElement.Control, ControlRenderer> = uiControl { control ->
    val vm = LocalViewModel.current
    val vmState by vm.observeStates().collectAsState()
    val currentPointer by dataPointer(control)

    val currentValue: Boolean by currentValueAtPointer(vmState, currentPointer, false) {
        it.toSimpleValue().toString().toBooleanStrictOrNull() ?: false
    }
    val postUpdate = { value: Boolean ->
        updateFormState(vm, control, currentPointer, JSONBoolean(value))
    }

    Row(verticalAlignment = Alignment.CenterVertically) {
        Checkbox(
            checked = currentValue,
            onCheckedChange = {
                postUpdate(it)
            },
            enabled = LocallyEnabled.current,
        )
        Text(control.label)
    }
}

public fun ObjectControl.control(): Registered<UiElement.Control, ControlRenderer> = uiControl { control ->
    val properties = control.schemaConfig.getObject("properties") ?: emptyMap()
    val vm = LocalViewModel.current
    val vmState by vm.observeStates().collectAsState()

    properties.forEach { (key, schemaValue) ->
        val objectFieldControl by remember {
            derivedStateOf {
                JSONObject(
                    mapOf(
                        "type" to JSONString("Control"),
                        "scope" to JSONString("#${control.schemaScope}/properties/$key")
                    )
                ).resolveAsControl(vmState.schemaJson!!)
            }
        }

        RenderUiControl(
            objectFieldControl
        )
    }
}

public fun ArrayControl.control(): Registered<UiElement.Control, ControlRenderer> = uiControl { control ->
//    val currentScope = LocalJsonPointer.current
//
//    currentValue?.forEachIndexed { index, jsonElement ->
//        CompositionLocalProvider(LocalJsonPointer provides (currentScope + JsonPointer.ArrayIndex(index))) {
//            RenderUiControl(
//                Form.UiSchema.Element(
//                    type = "Control",
//                    scope = ref.trimEnd('/') + "/properties/$key",
//                )
//            )
//        }
//    }
}

// Control utils
// ---------------------------------------------------------------------------------------------------------------------

@Composable
public fun <T> currentValueAtPointer(
    vmState: FormContract.State,
    currentPointer: JSONPointer,
    defaultValue: T,
    mapper: (JSONValue) -> T,
): State<T> {
    return remember(vmState.updatedData) {
        derivedStateOf {
            runCatching {
                mapper(currentPointer.find(vmState.updatedData))
            }.getOrDefault(defaultValue)
        }
    }
}

@Composable
public fun currentJsonValueAtPointer(
    vmState: FormContract.State,
    currentPointer: JSONPointer,
): State<JSONValue?> {
    return remember(vmState.updatedData) {
        derivedStateOf {
            runCatching {
                currentPointer.find(vmState.updatedData)
            }.getOrNull()
        }
    }
}

public fun updateFormState(
    vm: FormViewModel,
    control: UiElement.Control,
    currentPointer: JSONPointer,
    value: JSONValue?,
) {
    vm.trySend(
        FormContract.Inputs.UpdateFormState(
            pointer = currentPointer,
            action = JsonPointerAction.SetValue(value),
        )
    )
}

@Composable
public fun dataPointer(
    control: UiElement.Control,
): State<JSONPointer> {
    return remember(control.dataScope) {
        derivedStateOf { control.dataScope.asPointer() }
    }
}

@Composable
public fun schemaPointer(
    control: UiElement.Control,
): State<JSONPointer> {
    return remember(control.schemaScope) {
        derivedStateOf { control.schemaScope.asPointer() }
    }
}

@Composable
public fun DebugControl(control: UiElement.Control) {
    val vmState by LocalViewModel.current.observeStates().collectAsState()

    if (vmState.debug) {
        DebugControlContents(control)
    }
}

@Composable
public fun DebugControlContents(control: UiElement.Control) {
    Text("schema scope: ${schemaPointer(control).value.toURIFragment()}")
    Text("data scope: ${dataPointer(control).value.toURIFragment()}")
    Text("type: ${control.controlType}")
    Text("Required: ${control.required}")
    Text("Has Rule?: ${control.rule != null}")
}
