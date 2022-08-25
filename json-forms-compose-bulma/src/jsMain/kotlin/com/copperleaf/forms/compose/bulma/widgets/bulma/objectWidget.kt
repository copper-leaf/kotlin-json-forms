package com.copperleaf.forms.compose.bulma.widgets.bulma

import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.copperleaf.forms.compose.controls.ControlScope
import com.copperleaf.forms.compose.form.UiElement
import com.copperleaf.forms.core.internal.resolveAsControl
import com.copperleaf.json.pointer.plus
import com.copperleaf.json.pointer.toUriFragment
import com.copperleaf.json.values.objectAt
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

@Composable
public fun ControlScope.objectWidget() {
    val properties = control.schemaConfig.objectAt("properties")

    properties.forEach { (key, _) ->
        val objectFieldControl by remember {
            derivedStateOf {
                JsonObject(
                    mapOf(
                        "type" to JsonPrimitive("Control"),
                        "scope" to JsonPrimitive((control.schemaScope + "/properties/$key").toUriFragment())
                    )
                ).resolveAsControl(vmState.schemaJson)
            }
        }

        UiElement(
            objectFieldControl,
            vmState,
            postInput,
        )
    }
}
