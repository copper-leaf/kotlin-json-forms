package com.copperleaf.json.utils

public class SplitTransformer() {
    private val splitters: MutableList<Splitter> = mutableListOf()
    private val transformers: MutableList<Transformer> = mutableListOf()

    public fun splitEach(getSplitter: SplitterScope.() -> Splitter): SplitTransformer = apply {
        splitters += getSplitter(SplitterScope(this))
    }

    public fun transformEach(getTransformer: TransformerScope.() -> Transformer): SplitTransformer = apply {
        transformers += getTransformer(TransformerScope(this))
    }

    public fun convertTo(original: String, getCombiner: CombinerScope.() -> Combiner): String {
        return getCombiner(CombinerScope(this)).combine(executeSplit(original))
    }

    public fun executeSplit(original: String): List<String> {
        val pieces: List<String> = splitters
            .fold(listOf(original)) { acc: List<String>, nextSplitter: Splitter ->
                acc
                    .flatMap { nextPiece ->
                        nextSplitter
                            .split(nextPiece)
                            .toList()
                    }
            }

        val transformedPieces = pieces.mapIndexed { index: Int, nextPiece: String ->
            transformers.fold(nextPiece) { acc: String, nextTransformer: Transformer ->
                nextTransformer
                    .transform(index, acc)
            }
        }

        return transformedPieces
    }

    public class SplitterScope(public val transformer: SplitTransformer)
    public fun interface Splitter {
        public fun split(input: String): List<String>
    }

    public class TransformerScope(public val transformer: SplitTransformer)
    public fun interface Transformer {
        public fun transform(index: Int, piece: String): String
    }

    public class CombinerScope(public val transformer: SplitTransformer)
    public fun interface Combiner {
        public fun combine(pieces: List<String>): String
    }
}

// "splitFrom" mappers
public expect val SplitTransformer.SplitterScope.camelCase: SplitTransformer.Splitter

public expect val SplitTransformer.SplitterScope.words: SplitTransformer.Splitter

public val SplitTransformer.SplitterScope.snakeCase: SplitTransformer.Splitter
    get() = SplitTransformer.Splitter {
        it.split('_')
    }

public val SplitTransformer.SplitterScope.dashCase: SplitTransformer.Splitter
    get() = SplitTransformer.Splitter {
        it.split('-')
    }

public val SplitTransformer.TransformerScope.camelCase: SplitTransformer.Transformer
    get() = SplitTransformer.Transformer { index, piece ->
        if (index == 0) {
            piece
                .lowercase()
        } else {
            piece
                .lowercase()
                .replaceFirstChar {
                    it.titlecase()
                }
        }
    }

public val SplitTransformer.TransformerScope.pascalCase: SplitTransformer.Transformer
    get() = SplitTransformer.Transformer { _, piece ->
        piece
            .lowercase()
            .replaceFirstChar {
                it.titlecase()
            }
    }

public val SplitTransformer.TransformerScope.uppercase: SplitTransformer.Transformer
    get() = SplitTransformer.Transformer { _, piece ->
        piece.uppercase()
    }

public val SplitTransformer.TransformerScope.lowercase: SplitTransformer.Transformer
    get() = SplitTransformer.Transformer { _, piece ->
        piece.lowercase()
    }

public val SplitTransformer.TransformerScope.trimmed: SplitTransformer.Transformer
    get() = SplitTransformer.Transformer { _, piece ->
        piece.trim()
    }

public expect val SplitTransformer.TransformerScope.urlEncoded: SplitTransformer.Transformer

// "convertTo" mappers
public val SplitTransformer.CombinerScope.camelCase: SplitTransformer.Combiner
    get() = SplitTransformer.Combiner { pieces ->
        pieces.joinToString(separator = "")
    }

public val SplitTransformer.CombinerScope.words: SplitTransformer.Combiner
    get() = SplitTransformer.Combiner { pieces ->
        pieces.joinToString(separator = " ")
    }

public val SplitTransformer.CombinerScope.snakeCase: SplitTransformer.Combiner
    get() = SplitTransformer.Combiner { pieces ->
        pieces.joinToString(separator = "_")
    }

public val SplitTransformer.CombinerScope.dashCase: SplitTransformer.Combiner
    get() = SplitTransformer.Combiner { pieces ->
        pieces.joinToString(separator = "-")
    }
