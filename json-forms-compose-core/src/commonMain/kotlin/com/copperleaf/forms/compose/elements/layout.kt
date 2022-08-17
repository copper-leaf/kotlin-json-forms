package com.copperleaf.forms.compose.elements

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.copperleaf.forms.compose.LocalDesignSystem
import com.copperleaf.forms.compose.LocalFormConfig
import com.copperleaf.forms.compose.LocalViewModel
import com.copperleaf.forms.core.ui.UiElement

@Composable
public fun UiElementLayout(
    element: UiElement.ElementWithChildren,
) {
    val uiElementRenderer = LocalFormConfig.current.getElement(element)
    val designSystem = LocalDesignSystem.current

    if (uiElementRenderer != null) {
        designSystem.column {
            val vm = LocalViewModel.current
            val vmState by vm.observeStates().collectAsState()

            val elementScope by remember(element, vm, vmState, designSystem) {
                derivedStateOf {
                    UiElementScope(
                        vm = vm,
                        designSystem = designSystem,
                        vmState = vmState,
                        element = element
                    )
                }
            }
            uiElementRenderer(elementScope)
        }
    } else {
        LocalDesignSystem.current.text("No UI element with type '${element.elementType}' could be found.")
    }
}


