package com.copperleaf.forms.compose.widgets.richtext

import androidx.compose.foundation.layout.Row
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.darkrockstudios.richtexteditor.model.RichTextValue

@Composable
public fun RichTextToolbar(
    value: RichTextValue,
    onValueChange: (RichTextValue) -> Unit,
    modifier: Modifier = Modifier,
    keys: List<RichTextEditorAction> = defaultRichTextEditorActions,
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

