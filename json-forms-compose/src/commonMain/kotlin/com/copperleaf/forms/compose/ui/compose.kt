package com.copperleaf.forms.compose.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.copperleaf.forms.compose.util.ComposeFormConfig
import com.copperleaf.forms.core.ui.UiElement
import com.copperleaf.forms.core.vm.FormContract
import com.copperleaf.forms.core.vm.FormViewModel

@Composable
public fun RenderForm(
    viewModel: FormViewModel,
    modifier: Modifier = Modifier,
    config: ComposeFormConfig = ComposeFormConfig(),
) {
    Column(modifier) {
        val vmState by viewModel.observeStates().collectAsState()

        if (vmState.isReady) {
            CompositionLocalProvider(
                LocalFormConfig providesDefault config,
                LocalViewModel providesDefault viewModel,
            ) {
                RenderGenericUiElement(vmState.uiSchema!!.rootUiElement)
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
public fun RenderGenericUiElement(
    element: UiElement,
) {
    RuleLayout(uiElement = element, animated = true) {
        when (element) {
            is UiElement.ElementWithChildren -> {
                RenderUiElement(element)
            }
            is UiElement.Control -> {
                RenderUiControl(element)
            }
        }
    }
}

@Composable
public fun RenderUiElement(
    element: UiElement.ElementWithChildren,
) {
    val uiElementRenderer = LocalFormConfig.current.getElement(element)

    if (uiElementRenderer != null) {
        Column {
            uiElementRenderer(element)
        }
    } else {
        Text("No UI element with type '${element.elementType}' could be found.")
    }
}

@Composable
public fun RenderUiControl(
    control: UiElement.Control,
) {
    val controlRenderer = LocalFormConfig.current.getControl(control)

    if (controlRenderer != null) {
        ControlLayout(control, controlRenderer)
    } else {
        Text("No UI control with type '${control.controlType}' could be found.")
    }
}
