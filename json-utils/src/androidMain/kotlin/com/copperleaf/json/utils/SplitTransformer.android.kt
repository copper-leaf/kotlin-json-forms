package com.copperleaf.json.utils

import org.apache.commons.lang3.StringUtils
import java.net.URLEncoder

public actual val SplitTransformer.SplitterScope.camelCase: SplitTransformer.Splitter
    get() = SplitTransformer.Splitter {
        StringUtils.splitByCharacterTypeCamelCase(it).toList()
    }

public actual val SplitTransformer.SplitterScope.words: SplitTransformer.Splitter
    get() = SplitTransformer.Splitter {
        StringUtils.splitByWholeSeparator(it, null).toList()
    }

public actual val SplitTransformer.TransformerScope.urlEncoded: SplitTransformer.Transformer
    @Suppress("NewApi")
    get() = SplitTransformer.Transformer { _, piece ->
        URLEncoder.encode(piece, "UTF-8")
    }
