package com.copperleaf.forms.compose.widgets.codeeditor.prettify

import com.copperleaf.forms.compose.widgets.codeeditor.parser.ParseResult
import com.copperleaf.forms.compose.widgets.codeeditor.parser.Parser
import com.copperleaf.forms.compose.widgets.codeeditor.prettify.parser.Job
import com.copperleaf.forms.compose.widgets.codeeditor.prettify.parser.Prettify


/**
 * The prettify parser for syntax highlight.
 * @author Chan Wai Shing <cws1989></cws1989>@gmail.com>
 */
internal class PrettifyParser : Parser {

    private var prettify: Prettify = Prettify()

    override fun parse(fileExtension: String, content: String): List<ParseResult> =
        parse(prettify.getLexerForExtension(fileExtension), content)

    override fun parse(provider: Prettify.LangProvider, content: String): List<ParseResult> =
        parse(prettify.getLexerForLanguageProvider(provider), content)

    private fun parse(lexer: Prettify.CreateSimpleLexer, content: String): List<ParseResult> {
        val job = Job(0, content)
        lexer.decorate(job)
        val decorations = job.decorations
        val returnList = ArrayList<ParseResult>()

        // apply style according to the style list
        var i = 0
        val iEnd = decorations.size
        while (i < iEnd) {
            val endPos = if (i + 2 < iEnd) decorations[i + 2] as Int else content.length
            val startPos = decorations[i] as Int
            returnList.add(
                ParseResult(startPos, endPos - startPos, listOf((decorations[i + 1] as String)))
            )
            i += 2
        }
        return returnList
    }
}
