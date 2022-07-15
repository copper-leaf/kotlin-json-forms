package com.copperleaf.forms.compose.widgets.codeeditor.prettify.lang

import com.copperleaf.forms.compose.widgets.codeeditor.prettify.parser.Prettify
import com.copperleaf.forms.compose.widgets.codeeditor.prettify.parser.StylePattern
import com.copperleaf.forms.compose.widgets.codeeditor.utils.new

/**
 * Registers a language handler for markdown.
 *
 * @author Kirill Biakov (kbiakov@gmail.com)
 */
internal class LangMd : Lang() {
    companion object {
        val fileExtensions: List<String>
            get() = listOf("md", "markdown")
    }

    override val fallthroughStylePatterns = ArrayList<StylePattern>()
    override val shortcutStylePatterns = ArrayList<StylePattern>()

    init {
        fallthroughStylePatterns.new(
            Prettify.PR_DECLARATION,
            Regex("^#.*?[\\n\\r]")
        )
        fallthroughStylePatterns.new(
            Prettify.PR_STRING,
            Regex("^```[\\s\\S]*?(?:```|$)")
        )


    }

    override fun getFileExtensions(): List<String> {
        return fileExtensions
    }
}
