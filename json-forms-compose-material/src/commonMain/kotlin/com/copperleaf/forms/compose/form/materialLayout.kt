package com.copperleaf.forms.compose.form

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.copperleaf.forms.compose.controls.ControlRenderer
import com.copperleaf.forms.compose.elements.UiElementRenderer
import com.copperleaf.forms.core.ui.UiElement
import com.copperleaf.forms.core.vm.FormViewModel

@Composable
public fun MaterialForm(
    viewModel: FormViewModel,
    modifier: Modifier = Modifier,
    config: ComposeFormConfig = ComposeFormConfig(
        elements = UiElement.materialDefaults(),
        controls = UiElement.Control.materialDefaults(),
        designSystem = MaterialDesignSystem(),
    ),
) {
    Column(modifier) {
        BasicForm(viewModel, config)
    }
}

public expect fun UiElement.Control.Companion.materialDefaults(): List<Registered<UiElement.Control, ControlRenderer>>

public expect fun UiElement.Companion.materialDefaults(): List<Registered<UiElement.ElementWithChildren, UiElementRenderer>>
