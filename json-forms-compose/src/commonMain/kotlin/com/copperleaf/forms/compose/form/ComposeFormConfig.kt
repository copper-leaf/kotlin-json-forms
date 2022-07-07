package com.copperleaf.forms.compose.form

import com.copperleaf.forms.core.ui.UiElement

public class ComposeFormConfig(
    private val elements: List<Registered<UiElement.ElementWithChildren, UiElementRenderer>> = UiElement.defaults(),
    private val controls: List<Registered<UiElement.Control, ControlRenderer>> = UiElement.Control.defaults(),
) {
    public fun getElement(element: UiElement.ElementWithChildren): UiElementRenderer? {
        return elements
            .asSequence()
            .filter { it.tester(element) }
            .maxByOrNull { it.rank }
            ?.renderer
    }

    public fun getControl(control: UiElement.Control): ControlRenderer? {
        return controls
            .asSequence()
            .filter { it.tester(control) }
            .maxByOrNull { it.rank }
            ?.renderer
    }
}
