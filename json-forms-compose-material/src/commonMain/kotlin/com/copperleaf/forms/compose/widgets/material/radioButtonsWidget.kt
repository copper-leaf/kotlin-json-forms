package com.copperleaf.forms.compose.widgets.material

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.copperleaf.forms.compose.controls.ControlScope
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonPrimitive

@Composable
public fun ControlScope.radioButtonsWidget(
    getOptions: JsonElement.()->List<Pair<String, String>>
) {
    val currentValue = getTypedValue("") {
        it.jsonPrimitive.content
    }

    val allOptions: List<Pair<String, String>> = remember {
        control.schemaConfig.getOptions()
    }

    Row(Modifier.fillMaxSize(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        allOptions.forEach { (const, title) ->
            Row(Modifier.clickable { updateFormState(const) }) {
                RadioButton(
                    selected = currentValue == const,
                    onClick = null,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(title)
            }
        }
    }
}
