package com.copperleaf.forms.compose.elements

import androidx.compose.runtime.Composable
import com.copperleaf.forms.compose.form.FormScope
import com.copperleaf.forms.core.ui.UiElement

@Composable
public fun FormScope.UiElementLayout(
    element: UiElement.ElementWithChildren,
) {
    val uiElementRenderer = getUiElement(element)

    if (uiElementRenderer != null) {
        column {
            val elementScope = getUiElementScope(element)
            uiElementRenderer(elementScope)
        }
    } else {
        text("No UI element with type '${element.elementType}' could be found.")
    }
}


