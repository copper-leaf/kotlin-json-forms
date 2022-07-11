package com.copperleaf.forms.compose.controls

import com.copperleaf.forms.core.ControlType
import com.copperleaf.forms.core.ui.UiElement
import com.copperleaf.json.values.boolean
import com.copperleaf.json.values.objectAt
import com.copperleaf.json.values.optional
import kotlinx.serialization.json.jsonObject

public fun UiElement.Control.matchesControlType(type: ControlType): Boolean {
    return (this.controlType == type.type)
}

public fun UiElement.Control.optionIsEnabled(name: String): Boolean {
    return uiSchemaConfig
        .optional { objectAt("options") }
        ?.optional { boolean(name) } == true
}

public fun UiElement.Control.hasSchemaProperty(name: String): Boolean {
    return schemaConfig.jsonObject.containsKey(name)
}
