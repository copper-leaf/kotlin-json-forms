package com.copperleaf.forms.compose.widgets.material

import androidx.compose.runtime.Composable
import com.copperleaf.forms.compose.controls.ControlScope
import com.copperleaf.forms.compose.form.UiElement
import com.copperleaf.json.values.objectAt

@Composable
public fun ControlScope.objectWidget() {
    val properties = control.schemaConfig.objectAt("properties")

    properties.forEach { (key, _) ->
        val objectFieldControl = getChildObjectControl(key)
        UiElement(objectFieldControl)
    }
}
