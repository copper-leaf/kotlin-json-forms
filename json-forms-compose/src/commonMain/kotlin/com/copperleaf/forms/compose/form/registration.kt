package com.copperleaf.forms.compose.form

import com.copperleaf.forms.compose.controls.matchesControlType
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
    tester: (UiElement.ElementWithChildren) -> Boolean = { it.elementType == this@uiElement.type },
    renderer: UiElementRenderer,
): Registered<UiElement.ElementWithChildren, UiElementRenderer> {
    return Registered(
        rank = rank,
        tester = tester,
        renderer = renderer,
    )
}

public expect fun UiElement.Control.Companion.defaults(): List<Registered<UiElement.Control, ControlRenderer>>

public expect fun UiElement.Companion.defaults(): List<Registered<UiElement.ElementWithChildren, UiElementRenderer>>
