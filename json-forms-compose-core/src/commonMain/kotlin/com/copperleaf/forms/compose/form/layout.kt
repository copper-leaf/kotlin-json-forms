package com.copperleaf.forms.compose.form

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.copperleaf.forms.compose.LocalDesignSystem
import com.copperleaf.forms.compose.LocalFormConfig
import com.copperleaf.forms.compose.controls.ControlLayout
import com.copperleaf.forms.compose.elements.UiElementLayout
import com.copperleaf.forms.compose.rules.RuleLayout
import com.copperleaf.forms.core.ui.UiElement
import com.copperleaf.forms.core.vm.FormContractLite
import com.copperleaf.forms.core.vm.FormFieldsState

@Composable
public fun BasicForm(
    vmState: FormFieldsState,
    postInput: (FormContractLite.Inputs)->Unit,
    config: ComposeFormConfig,
) {
    CompositionLocalProvider(
        LocalFormConfig providesDefault config,
        LocalDesignSystem providesDefault config.designSystem,
    ) {
        UiElement(vmState.uiSchema.rootUiElement, vmState, postInput)
    }
}

@Composable
public fun UiElement(
    element: UiElement,
    vmState: FormFieldsState,
    postInput: (FormContractLite.Inputs)->Unit,
) {
    RuleLayout(element, vmState, postInput) {
        when (element) {
            is UiElement.ElementWithChildren -> {
                UiElementLayout(element, vmState, postInput)
            }

            is UiElement.Control -> {
                ControlLayout(element, vmState, postInput)
            }
        }
    }
}
