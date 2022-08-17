package com.copperleaf.forms.compose.bulma.controls

import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.copperleaf.forms.compose.LocallyEnabled
import com.copperleaf.forms.compose.controls.ControlRenderer
import com.copperleaf.forms.compose.controls.hasArrayItemProperty
import com.copperleaf.forms.compose.controls.hasArrayItemType
import com.copperleaf.forms.compose.controls.hasSchemaProperty
import com.copperleaf.forms.compose.controls.optionFieldIs
import com.copperleaf.forms.compose.controls.optionIsEnabled
import com.copperleaf.forms.compose.form.Registered
import com.copperleaf.forms.compose.form.UiElement
import com.copperleaf.forms.compose.form.WithArrayIndex
import com.copperleaf.forms.compose.form.uiControl
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
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.doubleOrNull
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.jetbrains.compose.web.attributes.disabled
import org.jetbrains.compose.web.attributes.name
import org.jetbrains.compose.web.attributes.selected
import org.jetbrains.compose.web.dom.A
import org.jetbrains.compose.web.dom.Aside
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.CheckboxInput
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Label
import org.jetbrains.compose.web.dom.Li
import org.jetbrains.compose.web.dom.NumberInput
import org.jetbrains.compose.web.dom.Option
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.RadioInput
import org.jetbrains.compose.web.dom.Select
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.dom.TextInput
import org.jetbrains.compose.web.dom.Ul

@Composable
public fun BulmaField(label: String? = null, content: @Composable () -> Unit) {
    Div({ classes("field") }) {
        if (label != null) {
            Text(label)
        }
        Div({ classes("control") }) {
            content()
        }
    }
}

// Text Field Controls
// ---------------------------------------------------------------------------------------------------------------------

public fun StringControl.control(): Registered<UiElement.Control, ControlRenderer> = uiControl {
    val currentValue = getTypedValue("") {
        it.jsonPrimitive.content
    }
    val isEnabled = LocallyEnabled.current

    BulmaField(control.label) {
        TextInput(currentValue) {
            classes("input")
            if (!isEnabled) {
                disabled()
            }
            onInput { event -> updateFormState(event.value) }
        }
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

    val allOptions: List<String> = remember {
        control.schemaConfig.arrayAt("enum").map { it.jsonPrimitive.content }
    }

    val isEnabled = LocallyEnabled.current

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
                allOptions.forEach { optionValue ->
                    Option(optionValue, {
                        if (currentValue == optionValue) {
                            selected()
                        }
                        if (!isEnabled) {
                            disabled()
                        }
                    }) {
                        Text(optionValue)
                    }
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

    val allOptions: List<Pair<String, String>> = remember {
        control.schemaConfig.arrayAt("oneOf").map {
            it.jsonObject.string("const") to it.jsonObject.string("title")
        }
    }

    val isEnabled = LocallyEnabled.current

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

    BulmaField(control.label) {
        allOptions.forEach { optionValue ->
            Label(null, { classes("radio") }) {
                RadioInput(currentValue == optionValue) {
                    onClick { updateFormState(optionValue) }
                    if (!isEnabled) {
                        disabled()
                    }
                    name(control.label)
                }
                Text(" $optionValue")
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

    BulmaField(control.label) {
        allOptions.forEach { optionValue ->
            Label(null, { classes("checkbox") }) {
                CheckboxInput(optionValue in selectedValues) {
                    onClick {
                        if (optionValue in selectedValues) {
                            sendFormAction(
                                pointer = dataPointer + "/${selectedValues.indexOf(optionValue)}",
                                action = JsonPointerAction.RemoveValue,
                            )
                        } else {
                            sendFormAction(
                                pointer = dataPointer + "/${selectedValues.size}",
                                action = JsonPointerAction.SetValue(optionValue),
                            )
                        }
                        markAsTouched()
                    }
                    if (!isEnabled) {
                        disabled()
                    }
                }
                Text(" $optionValue")
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

    BulmaField(control.label) {
        allOptions.forEach { (optionValue, optionTitle) ->
            Label(null, { classes("checkbox") }) {
                CheckboxInput(optionValue in selectedValues) {
                    onClick {
                        if (optionValue in selectedValues) {
                            sendFormAction(
                                pointer = dataPointer + "/${selectedValues.indexOf(optionValue)}",
                                action = JsonPointerAction.RemoveValue,
                            )
                        } else {
                            sendFormAction(
                                pointer = dataPointer + "/${selectedValues.size}",
                                action = JsonPointerAction.SetValue(optionValue),
                            )
                        }
                        markAsTouched()
                    }
                    if (!isEnabled) {
                        disabled()
                    }
                }
                Text(" $optionTitle")
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
    val isEnabled = LocallyEnabled.current

    BulmaField(control.label) {
        NumberInput(currentValue) {
            classes("input")
            if (!isEnabled) {
                disabled()
            }
            onInput { event -> event.value?.toInt()?.let { updateFormState(it) } }
        }
    }
}

public fun NumberControl.control(): Registered<UiElement.Control, ControlRenderer> = uiControl {
    val currentValue = getTypedValue(0.0) {
        it.jsonPrimitive.doubleOrNull
    }
    val isEnabled = LocallyEnabled.current

    BulmaField(control.label) {
        NumberInput(currentValue) {
            classes("input")
            if (!isEnabled) {
                disabled()
            }
            onInput { event -> event.value?.let { updateFormState(it) } }
        }
    }
}

// Boolean Controls
// ---------------------------------------------------------------------------------------------------------------------

public fun BooleanControl.checkbox(): Registered<UiElement.Control, ControlRenderer> = uiControl {
    val currentValue = getTypedValue(false) {
        it.jsonPrimitive.booleanOrNull
    }
    val isEnabled = LocallyEnabled.current

    BulmaField(control.label) {
        CheckboxInput(currentValue) {
            if (!isEnabled) {
                disabled()
            }
            onInput { event -> event.value?.let { updateFormState(it) } }
        }
    }
}

public fun BooleanControl.switch(): Registered<UiElement.Control, ControlRenderer> = uiControl(
    rank = 10,
    tester = { optionIsEnabled("toggle") }
) {
    val currentValue = getTypedValue(false) {
        it.jsonPrimitive.booleanOrNull
    }
    val isEnabled = LocallyEnabled.current

    BulmaField(control.label) {
        CheckboxInput(currentValue) {
            value(currentValue.toString())
            if (!isEnabled) {
                disabled()
            }
            onInput { event -> event.value?.let { updateFormState(it) } }
        }
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
    var selectedItem: Int? by remember { mutableStateOf(null) }

    Div({ classes("columns") }) {
        Div({ classes("column", "is-4") }) {
            Aside({ classes("menu") }) {
                P({ classes("menu-list") }) {
                    Ul {
                        currentDataValue.forEachIndexed { index, currentListItemValue ->
                            val listItemLabel: String = when (currentListItemValue) {
                                is JsonPrimitive -> {
                                    currentListItemValue.contentOrNull ?: "Item $index"
                                }

                                is JsonArray -> {
                                    "List #$index (${currentListItemValue.size} items)"
                                }

                                is JsonObject -> {
                                    listOf("name", "title", "label")
                                        .mapNotNull { key -> currentListItemValue.optional { string(key) } }
                                        .firstOrNull()
                                        ?: "Item $index"
                                }
                            }

                            Li({
                                if (index == selectedItem) {
                                    classes("is-active")
                                }
                                onClick { selectedItem = index }
                            }) {
                                A(null, {
                                    if (index == selectedItem) {
                                        classes("is-active")
                                    }
                                    onClick { selectedItem = index }
                                }) { Text(listItemLabel) }
                            }
                        }

                        Li({
                            onClick {
                                selectedItem = currentDataValue.lastIndex + 1
                                sendFormAction(
                                    pointer = dataPointer + "/${currentDataValue.size}",
                                    action = JsonPointerAction.SetValue(JsonNull),
                                )
                                markAsTouched()
                            }
                        }) { A { Text("Add New Item") } }
                    }
                }
            }
        }
        Div({ classes("column", "is-8") }) {
            selectedItem?.let { index ->
                WithArrayIndex(index) {
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

                    Div {
                        Button(
                            {
                                classes("button")
                                onClick {
                                    sendFormAction(
                                        pointer = dataPointer + "/$index",
                                        action = JsonPointerAction.RemoveValue
                                    )
                                }
                                if (!isEnabled) {
                                    disabled()
                                }
                            }
                        ) {
                            Text("Remove")
                        }
                    }
                }
            }
        }
    }
}
