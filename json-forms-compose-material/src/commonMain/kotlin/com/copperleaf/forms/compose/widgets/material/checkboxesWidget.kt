package com.copperleaf.forms.compose.widgets.material

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Checkbox
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.copperleaf.forms.compose.controls.ControlScope
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonPrimitive

@Composable
public fun ControlScope.checkboxWidget() {
    val currentValue = getTypedValue(false) {
        it.jsonPrimitive.booleanOrNull
    }

    Row(verticalAlignment = Alignment.CenterVertically) {
        Checkbox(
            checked = currentValue,
            onCheckedChange = {
                updateFormState(it)
            },
            enabled = isEnabled,
        )
        Text(control.label)
    }
}

@Composable
public fun ControlScope.switchWidget() {
    val currentValue = getTypedValue(false) {
        it.jsonPrimitive.booleanOrNull
    }

    Row(verticalAlignment = Alignment.CenterVertically) {
        Switch(
            checked = currentValue,
            onCheckedChange = {
                updateFormState(it)
            },
            enabled = isEnabled,
        )
        Text(control.label)
    }
}

@Composable
public fun ControlScope.checkboxesWidget(
    getOptions: JsonElement.()->List<Pair<String, String>>
) {
    val selectedValues: List<String> = getTypedValue(emptyList()) {
        if (it == JsonNull) {
            emptyList()
        } else {
            it.jsonArray.map { it.jsonPrimitive.content }
        }
    }
    val allOptions: List<Pair<String, String>> = remember {
        control.schemaConfig.getOptions()
    }

    Row(Modifier.fillMaxSize(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        allOptions.forEach { (const, title) ->
            Row(Modifier.clickable {
                if (const in selectedValues) {
                    removeArrayItem(selectedValues.indexOf(const))
                } else {
                    addArrayItem(selectedValues.size, const)
                }
            }) {
                Checkbox(
                    checked = const in selectedValues,
                    onCheckedChange = null,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(title)
            }
        }
    }
}
