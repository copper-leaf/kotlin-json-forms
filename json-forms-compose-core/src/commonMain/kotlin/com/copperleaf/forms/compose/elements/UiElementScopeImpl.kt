package com.copperleaf.forms.compose.elements

import com.copperleaf.forms.compose.form.FormScope
import com.copperleaf.forms.core.ui.UiElement

public data class UiElementScopeImpl(
    private val formScope: FormScope,
    public override val element: UiElement.ElementWithChildren,
) : UiElementScope, FormScope by formScope
