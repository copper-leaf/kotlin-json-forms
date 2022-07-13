package com.copperleaf.forms.compose.widgets.richtext

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.copperleaf.forms.compose.widgets.dropdown.IconButtonWithDescription
import com.darkrockstudios.richtexteditor.model.RichTextValue

@Composable
public fun RichTextToolbar(
    value: RichTextValue,
    onValueChange: (RichTextValue) -> Unit,
    modifier: Modifier = Modifier,
    toolbar: RichTextToolbar = RichTextEditorDefaults.defaultToolbar,
) {
    Row(modifier, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        for (group in toolbar.groups) {
            Row(Modifier.border(width = Dp.Hairline, color = MaterialTheme.colors.onSurface)) {
                for (action in group.actions) {
                    IconButtonWithDescription(
                        icon = action.icon,
                        contentDescription = action.name,
                        onClick = { onValueChange(value.insertStyle(action.style)) }
                    )
                }
            }
        }
    }
}

