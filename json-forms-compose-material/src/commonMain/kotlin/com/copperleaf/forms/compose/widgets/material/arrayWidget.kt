package com.copperleaf.forms.compose.widgets.material

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.copperleaf.forms.compose.controls.ControlScope
import com.copperleaf.forms.compose.form.UiElement
import com.copperleaf.forms.compose.form.WithArrayIndex
import com.copperleaf.forms.compose.widgets.dropdown.IconButtonWithDescription
import com.copperleaf.forms.core.internal.resolveAsControl
import com.copperleaf.json.pointer.JsonPointerAction
import com.copperleaf.json.pointer.plus
import com.copperleaf.json.pointer.toUriFragment
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonArray

@Composable
public fun ControlScope.arrayWidget(
    addButtonText: String
) {
    val currentDataValue: JsonArray = getTypedValue(JsonArray(emptyList())) {
        if (it == JsonNull) {
            JsonArray(emptyList())
        } else {
            it.jsonArray
        }
    }

    Column(Modifier.fillMaxWidth()) {
        Row(Modifier.fillMaxWidth()) {
            Column(Modifier.weight(1f)) {
                Text(control.label, style = MaterialTheme.typography.subtitle1)
            }
            Column(Modifier.weight(1f)) {
                IconButtonWithDescription(
                    Icons.Default.Add,
                    addButtonText,
                    onClick = {
                        sendFormAction(
                            pointer = dataPointer + "/${currentDataValue.size}",
                            action = JsonPointerAction.SetValue(JsonNull),
                        )
                        markAsTouched()
                    },
                    enabled = isEnabled,
                )
            }
        }
        Divider(thickness = 1.5.dp)
    }

    Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        currentDataValue.forEachIndexed { index, _ ->
            WithArrayIndex(index) {
                Card(Modifier.fillMaxWidth(), elevation = 4.dp) {
                    Box(Modifier.padding(8.dp)) {
                        Column {
                            Button(
                                onClick = {
                                    sendFormAction(
                                        pointer = dataPointer + "/$index",
                                        action = JsonPointerAction.RemoveValue
                                    )
                                },
                                enabled = isEnabled,
                            ) {
                                Text("Remove")
                            }

                            val arrayFieldControl by remember {
                                derivedStateOf {
                                    JsonObject(
                                        mapOf(
                                            "type" to JsonPrimitive("Control"),
                                            "scope" to JsonPrimitive((control.schemaScope + "/items").toUriFragment())
                                        )
                                    ).resolveAsControl(vmState.schemaJson)
                                }
                            }
                            UiElement(
                                arrayFieldControl,
                                vmState,
                                postInput,
                            )
                        }
                    }
                }
            }
        }
    }
}
