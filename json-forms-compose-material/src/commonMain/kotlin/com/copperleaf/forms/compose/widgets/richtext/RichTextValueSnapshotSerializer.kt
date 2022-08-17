package com.copperleaf.forms.compose.widgets.richtext

import com.darkrockstudios.richtexteditor.utils.RichTextValueSnapshot

public interface RichTextValueSnapshotSerializer {

    public fun encodeToString(snapshot: RichTextValueSnapshot): String
    public fun decodeFromString(content: String): RichTextValueSnapshot
}
