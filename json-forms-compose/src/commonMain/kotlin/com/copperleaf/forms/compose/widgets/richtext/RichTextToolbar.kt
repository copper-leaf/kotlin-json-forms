package com.copperleaf.forms.compose.widgets.richtext

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.isMetaPressed
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
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

@Composable
public fun Modifier.richTextShortcuts(
    value: RichTextValue,
    onValueChange: (RichTextValue) -> Unit,
    toolbar: RichTextToolbar = RichTextEditorDefaults.defaultToolbar,
): Modifier = composed {
    onPreviewKeyEvent { pressedKey ->
        if (pressedKey.isMetaPressed && pressedKey.type == KeyEventType.KeyUp) {
            toolbar
                .groups.flatMap { it.actions }
                .firstOrNull { it.shortcutKey == pressedKey.key }
                ?.run {
                    onValueChange(value.insertStyle(style))
                    true
                }
                ?: false
        } else {
            false
        }
    }
}

