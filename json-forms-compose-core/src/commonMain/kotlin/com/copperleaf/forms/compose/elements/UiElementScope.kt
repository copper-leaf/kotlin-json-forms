package com.copperleaf.forms.compose.elements

import com.copperleaf.forms.compose.design.DesignSystem
import com.copperleaf.forms.core.ui.UiElement
import com.copperleaf.forms.core.vm.FormContract

public data class UiElementScope(
    val vmState: FormContract.State,
    val postInput: (FormContract.Inputs)->Unit,

    val designSystem: DesignSystem,
    val element: UiElement.ElementWithChildren,
) : DesignSystem by designSystem
