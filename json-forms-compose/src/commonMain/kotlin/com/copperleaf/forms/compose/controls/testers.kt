package com.copperleaf.forms.compose.controls

import com.copperleaf.forms.core.ControlType
import com.copperleaf.forms.core.ui.UiElement

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
