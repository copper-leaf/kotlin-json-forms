package com.copperleaf.forms.compose.widgets.richtext

import com.copperleaf.json.utils.json
import com.copperleaf.json.utils.parseJson
import com.copperleaf.json.utils.toJsonString
import com.copperleaf.json.values.arrayAt
import com.copperleaf.json.values.int
import com.copperleaf.json.values.optional
import com.copperleaf.json.values.string
import com.darkrockstudios.richtexteditor.utils.RichTextValueSnapshot
import kotlinx.serialization.json.jsonObject

public class RichTextJsonSerializer : RichTextValueSnapshotSerializer {
    override fun encodeToString(snapshot: RichTextValueSnapshot): String {
        return json {
            "text" to snapshot.text
            "spanStyles" to snapshot.spanStyles.map {
                json {
                    "start" to it.start
                    "end" to it.end
                    "tag" to it.tag
                }
            }
            "paragraphStyles" to snapshot.paragraphStyles.map {
                json {
                    "start" to it.start
                    "end" to it.end
                    "tag" to it.tag
                }
            }
            "selectionPosition" to snapshot.selectionPosition
        }.toJsonString()
    }

    override fun decodeFromString(content: String): RichTextValueSnapshot {
        val json = content.parseJson()

        return RichTextValueSnapshot(
            text = json.optional { string("text") } ?: "",
            spanStyles = json.optional {
                arrayAt("spanStyles").map {
                    val tag = it.jsonObject
                    RichTextValueSnapshot.RichTextValueSpanSnapshot(
                        start = tag.int("start"),
                        end = tag.int("end"),
                        tag = tag.string("tag"),
                    )
                }
            } ?: emptyList(),
            paragraphStyles = json.optional {
                arrayAt("paragraphStyles").map {
                    val tag = it.jsonObject
                    RichTextValueSnapshot.RichTextValueSpanSnapshot(
                        start = tag.int("start"),
                        end = tag.int("end"),
                        tag = tag.string("tag"),
                    )
                }
            } ?: emptyList(),
            selectionPosition = json.optional { int("selectionPosition") } ?: 0,
        )
    }
}
