package com.copperleaf.forms.compose.widgets.richtext

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FormatBold
import androidx.compose.material.icons.filled.FormatItalic
import androidx.compose.material.icons.filled.FormatStrikethrough
import androidx.compose.material.icons.filled.FormatUnderlined
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.key.Key
import com.darkrockstudios.richtexteditor.model.Style

public data class RichTextEditorAction(
    val name: String,
    val icon: ImageVector,
    val style: Style,
    val shortcutKey: Key,
)

@OptIn(ExperimentalComposeUiApi::class)
public val defaultRichTextEditorActions: List<RichTextEditorAction> = listOf(
    RichTextEditorAction("Bold", Icons.Default.FormatBold, Style.Bold, Key.B),
    RichTextEditorAction("Italic", Icons.Default.FormatItalic, Style.Italic, Key.I),
    RichTextEditorAction("Underlined", Icons.Default.FormatUnderlined, Style.Underline, Key.U),
    RichTextEditorAction("Strikethrough", Icons.Default.FormatStrikethrough, Style.Strikethrough, Key.S),
)
