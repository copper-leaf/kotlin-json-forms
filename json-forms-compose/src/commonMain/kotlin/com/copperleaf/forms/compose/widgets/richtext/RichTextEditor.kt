package com.copperleaf.forms.compose.widgets.richtext

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.isMetaPressed
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import com.darkrockstudios.richtexteditor.model.RichTextValue

@Composable
public fun Modifier.richTextToolbarShortcuts(
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
