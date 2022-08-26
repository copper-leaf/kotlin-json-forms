package com.copperleaf.forms.compose.controls

import androidx.compose.runtime.Composable
import com.copperleaf.forms.compose.LocalArrayIndices
import com.copperleaf.forms.compose.LocallyEnabled
import com.copperleaf.forms.compose.form.FormScope
import com.copperleaf.forms.core.ui.UiElement

@Composable
public fun FormScope.ControlLayout(
    controlElement: UiElement.Control,
) {
    column {
        val controlRenderer = getControl(controlElement)
        if (controlRenderer != null) {
            val controlScope = getControlScope(
                controlElement = controlElement,
                localArrayIndices = LocalArrayIndices.current,
                locallyEnabled = LocallyEnabled.current,
            )

            controlRenderer(controlScope)
            if (!controlScope.isControlValid) {
                controlScope.validationErrors.forEach { text(it, isError = true) }
            }
        } else {
            text("No UI control with type '${controlElement.controlType}' could be found.", isError = true)
        }
    }
}
