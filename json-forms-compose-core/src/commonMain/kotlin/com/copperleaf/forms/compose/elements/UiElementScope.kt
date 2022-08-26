package com.copperleaf.forms.compose.elements

import com.copperleaf.forms.compose.form.FormScope
import com.copperleaf.forms.core.ui.UiElement

public interface UiElementScope : FormScope {
    public val element: UiElement.ElementWithChildren
}
