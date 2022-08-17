package com.copperleaf.forms.compose.form

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.copperleaf.forms.compose.LocalDesignSystem
import com.copperleaf.forms.compose.LocalFormConfig
import com.copperleaf.forms.compose.LocalViewModel
import com.copperleaf.forms.compose.controls.ControlLayout
import com.copperleaf.forms.compose.elements.UiElementLayout
import com.copperleaf.forms.compose.rules.RuleLayout
import com.copperleaf.forms.core.ui.UiElement
import com.copperleaf.forms.core.vm.FormViewModel

@Composable
public fun BasicForm(
    viewModel: FormViewModel,
    config: ComposeFormConfig,
) {
    val vmState by viewModel.observeStates().collectAsState()

    if (vmState.isReady) {
        CompositionLocalProvider(
            LocalFormConfig providesDefault config,
            LocalViewModel providesDefault viewModel,
            LocalDesignSystem providesDefault config.designSystem,
        ) {
            UiElement(vmState.uiSchema!!.rootUiElement)
        }
    }
}

@Composable
public fun UiElement(
    element: UiElement,
) {
    RuleLayout(uiElement = element) {
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
