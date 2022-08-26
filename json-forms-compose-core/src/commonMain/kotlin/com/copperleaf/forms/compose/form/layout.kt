package com.copperleaf.forms.compose.form

import androidx.compose.runtime.Composable
import com.copperleaf.forms.compose.controls.ControlLayout
import com.copperleaf.forms.compose.elements.UiElementLayout
import com.copperleaf.forms.compose.rules.RuleLayout
import com.copperleaf.forms.core.ui.UiElement

@Composable
public fun FormScope.BasicForm() {
    UiElement(uiSchema.rootUiElement)
}

@Composable
public fun FormScope.UiElement(
    element: UiElement,
) {
    RuleLayout(element) {
        when (element) {
            is UiElement.ElementWithChildren -> {
                UiElementLayout(element)
            }

            is UiElement.Control -> {
                ControlLayout(element)
            }
        }
    }
}
