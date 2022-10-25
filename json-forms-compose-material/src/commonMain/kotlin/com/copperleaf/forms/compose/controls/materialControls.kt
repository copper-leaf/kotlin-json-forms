package com.copperleaf.forms.compose.controls

import com.copperleaf.forms.compose.form.Registered
import com.copperleaf.forms.compose.form.uiControl
import com.copperleaf.forms.compose.widgets.material.arrayWidget
import com.copperleaf.forms.compose.widgets.material.checkboxWidget
import com.copperleaf.forms.compose.widgets.material.checkboxesWidget
import com.copperleaf.forms.compose.widgets.material.dropdownWidget
import com.copperleaf.forms.compose.widgets.material.objectWidget
import com.copperleaf.forms.compose.widgets.material.radioButtonsWidget
import com.copperleaf.forms.compose.widgets.material.switchWidget
import com.copperleaf.forms.compose.widgets.material.textFieldWidget
import com.copperleaf.forms.core.ArrayControl
import com.copperleaf.forms.core.BooleanControl
import com.copperleaf.forms.core.IntegerControl
import com.copperleaf.forms.core.NumberControl
import com.copperleaf.forms.core.ObjectControl
import com.copperleaf.forms.core.StringControl
import com.copperleaf.forms.core.ui.UiElement
import com.copperleaf.json.values.arrayAt
import com.copperleaf.json.values.objectAt
import com.copperleaf.json.values.string
import kotlinx.serialization.json.doubleOrNull
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

// Text Field Controls
// ---------------------------------------------------------------------------------------------------------------------

public fun StringControl.control(): Registered<UiElement.Control, ControlRenderer> = uiControl {
    textFieldWidget(
        defaultValue = "",
        mapper = { it.jsonPrimitive.content },
        mapStateToText = { it },
    )
}

// Single-Select Controls
// ---------------------------------------------------------------------------------------------------------------------

public fun StringControl.dropdownEnum(): Registered<UiElement.Control, ControlRenderer> = uiControl(
    rank = 20,
    tester = { hasSchemaProperty("enum") }
) {
    dropdownWidget {
        arrayAt("enum").map { it.jsonPrimitive.content to it.jsonPrimitive.content }
    }
}

public fun StringControl.dropdownOneOf(): Registered<UiElement.Control, ControlRenderer> = uiControl(
    rank = 21,
    tester = { hasSchemaProperty("oneOf") }
) {
    dropdownWidget {
        arrayAt("oneOf").map {
            it.jsonObject.string("const") to it.jsonObject.string("title")
        }
    }
}

public fun StringControl.radioButtonEnum(): Registered<UiElement.Control, ControlRenderer> = uiControl(
    rank = 30,
    tester = { hasSchemaProperty("enum") && optionFieldIs("format", "radio") }
) {
    radioButtonsWidget {
        arrayAt("enum").map { it.jsonPrimitive.content to it.jsonPrimitive.content }
    }
}

public fun StringControl.radioButtonOneOf(): Registered<UiElement.Control, ControlRenderer> = uiControl(
    rank = 31,
    tester = { hasSchemaProperty("oneOf") && optionFieldIs("format", "radio") }
) {
    radioButtonsWidget {
        arrayAt("oneOf").map {
            it.jsonObject.string("const") to it.jsonObject.string("title")
        }
    }
}

// Multi-Select Controls
// ---------------------------------------------------------------------------------------------------------------------

public fun ArrayControl.checkboxesEnum(): Registered<UiElement.Control, ControlRenderer> = uiControl(
    rank = 20,
    tester = { hasSchemaProperty("uniqueItems") && hasArrayItemType(StringControl) && hasArrayItemProperty("enum") }
) {
    checkboxesWidget {
        objectAt("items")
            .arrayAt("enum")
            .map { it.jsonPrimitive.content to it.jsonPrimitive.content }
    }
}

public fun ArrayControl.checkboxesOneOf(): Registered<UiElement.Control, ControlRenderer> = uiControl(
    rank = 21,
    tester = { hasSchemaProperty("uniqueItems") && hasArrayItemProperty("oneOf") }
) {
    checkboxesWidget {
        objectAt("items")
            .arrayAt("oneOf")
            .map { it.string("const") to it.string("title") }
    }
}

// Number Controls
// ---------------------------------------------------------------------------------------------------------------------

public fun IntegerControl.control(): Registered<UiElement.Control, ControlRenderer> = uiControl {
    textFieldWidget(
        defaultValue = 0,
        mapper = { it.jsonPrimitive.intOrNull },
        mapStateToText = { it.toString() },
        nudgeUp = { it + 1 },
        nudgeDown = { it - 1 },
    )
}

public fun NumberControl.control(): Registered<UiElement.Control, ControlRenderer> = uiControl {
    textFieldWidget(
        defaultValue = 0.0,
        mapper = { it.jsonPrimitive.doubleOrNull },
        mapStateToText = { it.toString() },
        nudgeUp = { it + 1.0 },
        nudgeDown = { it - 1.0 },
    )
}

// Boolean Controls
// ---------------------------------------------------------------------------------------------------------------------

public fun BooleanControl.checkbox(): Registered<UiElement.Control, ControlRenderer> = uiControl {
    checkboxWidget()
}

public fun BooleanControl.switch(): Registered<UiElement.Control, ControlRenderer> = uiControl(
    rank = 10,
    tester = { optionIsEnabled("toggle") }
) {
    switchWidget()
}

// Composite Controls
// ---------------------------------------------------------------------------------------------------------------------

public fun ObjectControl.control(): Registered<UiElement.Control, ControlRenderer> = uiControl {
    objectWidget()
}

public fun ArrayControl.control(): Registered<UiElement.Control, ControlRenderer> = uiControl {
    arrayWidget("Add new ${control.label}")
}

public fun ArrayControl.arrayOfObjects(): Registered<UiElement.Control, ControlRenderer> = uiControl(
    rank = 10,
    tester = { hasArrayItemType(ObjectControl) }
) {
    arrayWidget("Add to ${control.label}")
}
