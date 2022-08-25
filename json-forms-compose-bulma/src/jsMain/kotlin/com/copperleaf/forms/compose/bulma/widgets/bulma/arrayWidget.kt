package com.copperleaf.forms.compose.bulma.widgets.bulma

import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.copperleaf.forms.compose.controls.ControlScope
import com.copperleaf.forms.compose.form.UiElement
import com.copperleaf.forms.compose.form.WithArrayIndex
import com.copperleaf.forms.core.internal.resolveAsControl
import com.copperleaf.json.pointer.JsonPointerAction
import com.copperleaf.json.pointer.plus
import com.copperleaf.json.pointer.toUriFragment
import com.copperleaf.json.values.optional
import com.copperleaf.json.values.string
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonArray
import org.jetbrains.compose.web.attributes.disabled
import org.jetbrains.compose.web.dom.A
import org.jetbrains.compose.web.dom.Aside
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Li
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.dom.Ul

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
    var selectedItem: Int? by remember { mutableStateOf(null) }

    Div({ classes("columns") }) {
        Div({ classes("column", "is-4") }) {
            Aside({ classes("menu") }) {
                P({ classes("menu-list") }) {
                    Ul {
                        currentDataValue.forEachIndexed { index, currentListItemValue ->
                            val listItemLabel: String = when (currentListItemValue) {
                                is JsonPrimitive -> {
                                    currentListItemValue.contentOrNull ?: "Item $index"
                                }

                                is JsonArray -> {
                                    "List #$index (${currentListItemValue.size} items)"
                                }

                                is JsonObject -> {
                                    listOf("name", "title", "label")
                                        .mapNotNull { key -> currentListItemValue.optional { string(key) } }
                                        .firstOrNull()
                                        ?: "Item $index"
                                }
                            }

                            Li({
                                if (index == selectedItem) {
                                    classes("is-active")
                                }
                                onClick { selectedItem = index }
                            }) {
                                A(null, {
                                    if (index == selectedItem) {
                                        classes("is-active")
                                    }
                                    onClick { selectedItem = index }
                                }) { Text(listItemLabel) }
                            }
                        }

                        Li({
                            onClick {
                                selectedItem = currentDataValue.lastIndex + 1
                                sendFormAction(
                                    pointer = dataPointer + "/${currentDataValue.size}",
                                    action = JsonPointerAction.SetValue(JsonNull),
                                )
                                markAsTouched()
                            }
                        }) { A { Text(addButtonText) } }
                    }
                }
            }
        }
        Div({ classes("column", "is-8") }) {
            selectedItem?.let { index ->
                WithArrayIndex(index) {
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

                    Div {
                        Button(
                            {
                                classes("button")
                                onClick {
                                    sendFormAction(
                                        pointer = dataPointer + "/$index",
                                        action = JsonPointerAction.RemoveValue
                                    )
                                }
                                if (!isEnabled) {
                                    disabled()
                                }
                            }
                        ) {
                            Text("Remove")
                        }
                    }
                }
            }
        }
    }
}
