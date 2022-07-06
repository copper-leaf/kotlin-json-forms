package com.copperleaf.forms.compose.util

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import com.copperleaf.forms.compose.ui.defaults
import com.copperleaf.forms.core.ui.UiElement

public typealias UiElementRenderer = @Composable ColumnScope.(UiElement.ElementWithChildren) -> Unit
public typealias ControlRenderer = @Composable ColumnScope.(UiElement.Control) -> Unit

public data class Registered<T, U>(
    val rank: Int,
    val tester: (T) -> Boolean,
    val renderer: U,
)

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
