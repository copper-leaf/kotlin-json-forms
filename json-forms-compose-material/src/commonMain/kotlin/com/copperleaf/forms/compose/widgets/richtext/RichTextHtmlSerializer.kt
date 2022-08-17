package com.copperleaf.forms.compose.widgets.richtext

import com.darkrockstudios.richtexteditor.utils.RichTextValueSnapshot

public class RichTextHtmlSerializer : RichTextValueSnapshotSerializer {
    override fun encodeToString(snapshot: RichTextValueSnapshot): String {
        return snapshot.text
    }

    override fun decodeFromString(content: String): RichTextValueSnapshot {
        return RichTextValueSnapshot(
            text = content,
            spanStyles = emptyList(),
            paragraphStyles = emptyList(),
            selectionPosition = 0,
        )
    }
}
