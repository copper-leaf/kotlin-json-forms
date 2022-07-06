package com.copperleaf.forms.compose.util

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FormatBold
import androidx.compose.material.icons.filled.FormatItalic
import androidx.compose.material.icons.filled.FormatStrikethrough
import androidx.compose.material.icons.filled.FormatUnderlined
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.isMetaPressed
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import com.darkrockstudios.richtexteditor.model.RichTextValue
import com.darkrockstudios.richtexteditor.model.Style

public data class EditorToolbarAction(
    val name: String,
    val icon: ImageVector,
    val style: Style,
    val shortcutKey: Key,
)

@OptIn(ExperimentalComposeUiApi::class)
public val editorButtons: List<EditorToolbarAction> = listOf(
    EditorToolbarAction("Bold", Icons.Default.FormatBold, Style.Bold, Key.B),
    EditorToolbarAction("Italic", Icons.Default.FormatItalic, Style.Italic, Key.I),
    EditorToolbarAction("Underlined", Icons.Default.FormatUnderlined, Style.Underline, Key.U),
    EditorToolbarAction("Strikethrough", Icons.Default.FormatStrikethrough, Style.Strikethrough, Key.S),
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
public fun RichTextToolbar(
    value: RichTextValue,
    onValueChange: (RichTextValue) -> Unit,
    modifier: Modifier = Modifier,
    keys: List<EditorToolbarAction> = editorButtons,
) {
    Row(modifier) {
        for (key in keys) {
            IconButton(
                onClick = { onValueChange(value.insertStyle(key.style)) }
            ) {
                Icon(key.icon, key.name)
            }
//            TooltipArea(
//                tooltip = {
//                    Surface(elevation = 4.dp) {
//                        Box(Modifier.padding(vertical = 2.dp, horizontal = 4.dp)) {
//                            Text("⌘ + ${key.shortcutKey.nativeKeyCode.toChar()}")
//                        }
//                    }
//                }
//            ) {
//            }
        }
    }
}

@Composable
public fun Modifier.richTextToolbarShortcuts(
    value: RichTextValue,
    onValueChange: (RichTextValue) -> Unit,
    keys: List<EditorToolbarAction> = editorButtons,
): Modifier = composed {
    onPreviewKeyEvent { pressedKey ->
        if (pressedKey.isMetaPressed && pressedKey.type == KeyEventType.KeyUp) {
            keys
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
