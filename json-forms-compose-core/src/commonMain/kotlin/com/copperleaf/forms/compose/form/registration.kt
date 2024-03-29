package com.copperleaf.forms.compose.form

import com.copperleaf.forms.compose.controls.ControlRenderer
import com.copperleaf.forms.compose.controls.matchesControlType
import com.copperleaf.forms.compose.elements.UiElementRenderer
import com.copperleaf.forms.compose.elements.matchesElementType
import com.copperleaf.forms.core.ControlType
import com.copperleaf.forms.core.UiElementType
import com.copperleaf.forms.core.ui.UiElement

public fun ControlType.uiControl(
    rank: Int = 0,
    tester: UiElement.Control.() -> Boolean = { true },
    renderer: ControlRenderer,
): Registered<UiElement.Control, ControlRenderer> {
    return Registered(
        rank = rank,
        tester = { it.matchesControlType(this) && tester(it) },
        renderer = renderer,
    )
}

public fun UiElementType.uiElement(
    rank: Int = 0,
    tester: UiElement.ElementWithChildren.() -> Boolean = { true },
    renderer: UiElementRenderer,
): Registered<UiElement.ElementWithChildren, UiElementRenderer> {
    return Registered(
        rank = rank,
        tester = { it.matchesElementType(this) && tester(it) },
        renderer = renderer,
    )
}
