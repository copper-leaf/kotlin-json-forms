package com.copperleaf.forms.compose.elements

import com.copperleaf.forms.compose.design.DesignSystem
import com.copperleaf.forms.core.ui.UiElement
import com.copperleaf.forms.core.vm.FormContractLite
import com.copperleaf.forms.core.vm.FormFieldsState

public data class UiElementScope(
    val vmState: FormFieldsState,
    val postInput: (FormContractLite.Inputs)->Unit,

    val designSystem: DesignSystem,
    val element: UiElement.ElementWithChildren,
) : DesignSystem by designSystem
