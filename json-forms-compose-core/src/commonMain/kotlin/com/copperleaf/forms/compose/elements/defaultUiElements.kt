package com.copperleaf.forms.compose.elements

import com.copperleaf.forms.compose.form.Registered
import com.copperleaf.forms.compose.form.UiElement
import com.copperleaf.forms.compose.form.uiElement
import com.copperleaf.forms.compose.LocallyEnabled
import com.copperleaf.forms.core.Button
import com.copperleaf.forms.core.Group
import com.copperleaf.forms.core.HorizontalLayout
import com.copperleaf.forms.core.Label
import com.copperleaf.forms.core.VerticalLayout
import com.copperleaf.forms.core.ui.UiElement
import com.copperleaf.forms.core.vm.FormContract
import com.copperleaf.json.values.optional
import com.copperleaf.json.values.string

public fun VerticalLayout.element(): Registered<UiElement.ElementWithChildren, UiElementRenderer> = uiElement {
    column {
        element.elements.forEach {
            box {
                UiElement(it)
            }
        }
    }
}

public fun HorizontalLayout.element(): Registered<UiElement.ElementWithChildren, UiElementRenderer> = uiElement {
    row {
        element.elements.forEach {
            box {
                UiElement(it)
            }
        }
    }
}

public fun Label.element(): Registered<UiElement.ElementWithChildren, UiElementRenderer> = uiElement {
    val text = element.uiSchemaConfig.string("text")

    column {
        text(text)
    }
}

public fun Group.element(): Registered<UiElement.ElementWithChildren, UiElementRenderer> = uiElement {
    val label = element.uiSchemaConfig.optional { string("label") }

    column {
        if (label != null) {
            text(label)
            divider()
        }
        element.elements.forEach {
            box {
                UiElement(it)
            }
        }
    }
}



public fun Button.submit(): Registered<UiElement.ElementWithChildren, UiElementRenderer> = uiElement(
    tester = { optionFieldIs("action", "submit") }
) {
    if (vmState.saveType == FormContract.SaveType.OnCommit) {
        button(
            onClick = { vm.trySend(FormContract.Inputs.CommitChanges) },
            enabled = vmState.isValid,
        ) {
            text("Submit")
        }
    }
}

public fun Button.toggleDebug(): Registered<UiElement.ElementWithChildren, UiElementRenderer> = uiElement(
    tester = { optionFieldIs("action", "toggleDebug") }
) {
    row {
        checkbox(
            checked = vmState.debug,
            onCheckedChange = { vm.trySend(FormContract.Inputs.SetDebugMode(it)) },
            enabled = LocallyEnabled.current,
        ) { text("Debug") }
    }
}
