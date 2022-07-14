package com.copperleaf.forms.compose.elements

import com.copperleaf.forms.core.ui.UiElement
import com.copperleaf.forms.core.vm.FormContract
import com.copperleaf.forms.core.vm.FormViewModel

public data class UiElementScope(
    val vm: FormViewModel,
    val vmState: FormContract.State,
    val element: UiElement.ElementWithChildren,
)
