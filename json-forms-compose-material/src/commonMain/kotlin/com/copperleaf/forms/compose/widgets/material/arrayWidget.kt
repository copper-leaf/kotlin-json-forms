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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.copperleaf.forms.compose.controls.ControlScope
import com.copperleaf.forms.compose.form.UiElement
import com.copperleaf.forms.compose.form.WithArrayIndex
import com.copperleaf.forms.compose.widgets.dropdown.IconButtonWithDescription
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonNull
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
                        addArrayItem(currentDataValue.size, JsonNull)
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
                                    removeArrayItem(index)
                                },
                                enabled = isEnabled,
                            ) {
                                Text("Remove")
                            }

                            val arrayFieldControl = getChildArrayControl()
                            UiElement(arrayFieldControl)
                        }
                    }
                }
            }
        }
    }
}
