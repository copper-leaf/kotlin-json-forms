package com.copperleaf.forms.compose.elements

import androidx.compose.runtime.Composable
import com.copperleaf.forms.compose.LocalDesignSystem
import com.copperleaf.forms.compose.LocalFormConfig
import com.copperleaf.forms.core.ui.UiElement
import com.copperleaf.forms.core.vm.FormContractLite
import com.copperleaf.forms.core.vm.FormFieldsState

@Composable
public fun UiElementLayout(
    element: UiElement.ElementWithChildren,
    vmState: FormFieldsState,
    postInput: (FormContractLite.Inputs)->Unit,
) {
    val uiElementRenderer = LocalFormConfig.current.getElement(element)
    val designSystem = LocalDesignSystem.current

    if (uiElementRenderer != null) {
        designSystem.column {
            val elementScope = UiElementScope(
                vmState = vmState,
                postInput = postInput,
                designSystem = designSystem,
                element = element
            )
            uiElementRenderer(elementScope)
        }
    } else {
        LocalDesignSystem.current.text("No UI element with type '${element.elementType}' could be found.")
    }
}


