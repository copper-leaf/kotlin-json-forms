package com.copperleaf.forms.compose.widgets.codeeditor.utils

import androidx.compose.ui.text.AnnotatedString
import com.copperleaf.forms.compose.widgets.codeeditor.model.CodeLang
import com.copperleaf.forms.compose.widgets.codeeditor.parser.ParseResult
import com.copperleaf.forms.compose.widgets.codeeditor.prettify.PrettifyParser
import com.copperleaf.forms.compose.widgets.codeeditor.theme.CodeTheme

internal fun List<ParseResult>.toAnnotatedString(theme: CodeTheme, source: String): AnnotatedString = AnnotatedString(
    text = source,
    spanStyles = map {
        AnnotatedString.Range(theme toSpanStyle it, it.offset, it.offset + it.length)
    }
)

internal fun parseCodeAsAnnotatedString(
    parser: PrettifyParser,
    theme: CodeTheme,
    lang: String,
    code: String
): AnnotatedString = parser.parse(lang, code).toAnnotatedString(theme, code)

internal fun parseCodeAsAnnotatedString(
    parser: PrettifyParser,
    theme: CodeTheme,
    lang: CodeLang,
    code: String
): AnnotatedString = lang.langProvider?.let {
    parser.parse(it, code).toAnnotatedString(theme, code)
} ?: parseCodeAsAnnotatedString(
    parser = parser,
    theme = theme,
    lang = lang.value.first(),
    code = code,
)
