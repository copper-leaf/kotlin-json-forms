package com.copperleaf.forms.compose.elements

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import com.copperleaf.forms.compose.ui.LocalFormConfig
import com.copperleaf.forms.core.ui.UiElement

@Composable
public fun UiElementLayout(
    element: UiElement.ElementWithChildren,
) {
    val uiElementRenderer = LocalFormConfig.current.getElement(element)

    if (uiElementRenderer != null) {
        Column {
            val elementScope by remember {
                derivedStateOf {
                    UiElementScope(
                        element = element
                    )
                }
            }
            uiElementRenderer(elementScope)
        }
    } else {
        Text("No UI element with type '${element.elementType}' could be found.")
    }
}


