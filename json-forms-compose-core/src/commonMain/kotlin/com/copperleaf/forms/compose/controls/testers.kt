package com.copperleaf.forms.compose.controls

import com.copperleaf.forms.core.ControlType
import com.copperleaf.forms.core.ui.UiElement
import com.copperleaf.json.values.boolean
import com.copperleaf.json.values.objectAt
import com.copperleaf.json.values.optional
import com.copperleaf.json.values.string
import kotlinx.serialization.json.jsonObject

public fun UiElement.Control.matchesControlType(type: ControlType): Boolean {
    return (this.controlType == type.type)
}

public fun UiElement.Control.optionIsEnabled(name: String): Boolean {
    return uiSchemaConfig
        .optional { objectAt("options") }
        ?.optional { boolean(name) } == true
}

public fun UiElement.Control.optionFieldIs(name: String, value: String): Boolean {
    return uiSchemaConfig
        .optional { objectAt("options") }
        ?.optional { string(name) } == value
}

public fun UiElement.Control.hasSchemaProperty(name: String): Boolean {
    return schemaConfig.jsonObject.containsKey(name)
}

public fun UiElement.Control.hasArrayItemType(type: ControlType): Boolean {
    return schemaConfig
        .optional { objectAt("items").string("type") } == type.type
}

public fun UiElement.Control.hasArrayItemProperty(name: String): Boolean {
    return schemaConfig
        .optional { objectAt("items").containsKey(name) } == true
}

public fun UiElement.Control.schemaPropertyIs(name: String, value: String): Boolean {
    return schemaConfig.optional { string(name) } == value
}

public fun UiElement.Control.schemaPropertyIsEnabled(name: String): Boolean {
    return schemaConfig
        .optional { boolean(name) } == true
}
