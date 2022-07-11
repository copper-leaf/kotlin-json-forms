package com.copperleaf.forms.compose.form

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.copperleaf.forms.compose.controls.ControlLayout
import com.copperleaf.forms.compose.elements.UiElementLayout
import com.copperleaf.forms.compose.rules.RuleLayout
import com.copperleaf.forms.compose.ui.LocalFormConfig
import com.copperleaf.forms.compose.ui.LocalViewModel
import com.copperleaf.forms.core.ui.UiElement
import com.copperleaf.forms.core.vm.FormContract
import com.copperleaf.forms.core.vm.FormViewModel

@Composable
public fun Form(
    viewModel: FormViewModel,
    modifier: Modifier = Modifier,
    config: ComposeFormConfig = ComposeFormConfig(
        elements = UiElement.defaults(),
        controls = UiElement.Control.defaults(),
    ),
) {
    Column(modifier) {
        val vmState by viewModel.observeStates().collectAsState()

        if (vmState.isReady) {
            CompositionLocalProvider(
                LocalFormConfig providesDefault config,
                LocalViewModel providesDefault viewModel,
            ) {
                UiElement(vmState.uiSchema!!.rootUiElement)
            }

            if (vmState.saveType == FormContract.SaveType.OnCommit) {
                Button(
                    onClick = { viewModel.trySend(FormContract.Inputs.CommitChanges) },
                    enabled = vmState.isValid,
                ) {
                    Text("Submit")
                }
            }
        }
    }
}

@Composable
public fun UiElement(
    element: UiElement,
) {
    RuleLayout(uiElement = element, animated = true) {
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
