package com.copperleaf.forms.compose.elements

import com.copperleaf.forms.core.UiElementType
import com.copperleaf.forms.core.ui.UiElement
import com.copperleaf.json.values.boolean
import com.copperleaf.json.values.objectAt
import com.copperleaf.json.values.optional
import com.copperleaf.json.values.string

public fun UiElement.ElementWithChildren.matchesElementType(type: UiElementType): Boolean {
    return (this.elementType == type.type)
}

public fun UiElement.ElementWithChildren.optionIsEnabled(name: String): Boolean {
    return uiSchemaConfig
        .optional { objectAt("options") }
        ?.optional { boolean(name) } == true
}

public fun UiElement.ElementWithChildren.optionFieldIs(name: String, value: String): Boolean {
    val optionsObject = uiSchemaConfig.optional { objectAt("options") }
    val optionsProperty = optionsObject?.optional { string(name) }
    return optionsProperty == value
}
