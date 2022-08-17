package com.copperleaf.json.utils

public actual val SplitTransformer.SplitterScope.camelCase: SplitTransformer.Splitter
    get() = SplitTransformer.Splitter {
        val intermediateSeparator = "🤬"
        val intermediate = it.replace("([A-Z]+)".toRegex(), "${intermediateSeparator}$1")
            .replace("^${intermediateSeparator}".toRegex(), "")

        intermediate.split(intermediateSeparator)
    }

public actual val SplitTransformer.SplitterScope.words: SplitTransformer.Splitter
    get() = SplitTransformer.Splitter {
        it.split("\\s+".toRegex())
    }

public actual val SplitTransformer.TransformerScope.urlEncoded: SplitTransformer.Transformer
    @Suppress("NewApi")
    get() = SplitTransformer.Transformer { _, piece ->
        piece.urlEncode()
    }
