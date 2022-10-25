package com.copperleaf.json.pointer

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.datatest.WithDataTestName
import io.kotest.datatest.withData
import io.kotest.matchers.shouldBe

class TestJsonPointerParse : FreeSpec({
    "Non-abstract pointers" - {
        data class TestCase(
            val input: String,
            val expectedFormattedUri: String,
            val expectedTokens: List<String>
        ) : WithDataTestName {
            override fun dataTestName(): String = "a) '$input'"
        }

        withData(
            listOf(
                TestCase("#", "#", emptyList()),
                TestCase("#/one", "#/one", listOf("one")),
                TestCase("#/one/two/1/0", "#/one/two/1/0", listOf("one", "two", "1", "0")),
                TestCase("#/foo/0", "#/foo/0", listOf("foo", "0")),

                TestCase("#/foo", "#/foo", listOf("foo")),
                TestCase("#/", "#/", listOf("")),
                TestCase("#/a~1b", "#/a~1b", listOf("a/b")),
                TestCase("#/c%d", "#/c%d", listOf("c%d")),
                TestCase("#/e^f", "#/e^f", listOf("e^f")),
                TestCase("#/g|h", "#/g|h", listOf("g|h")),
                TestCase("#/i\\j", "#/i\\j", listOf("i\\j")),
                TestCase("#/k\"l", "#/k\"l", listOf("k\"l")),
                TestCase("#/ ", "#/ ", listOf(" ")),
                TestCase("#/m~0n", "#/m~0n", listOf("m~n")),
            )
        ) {
            with(it) {
                val pointer = JsonPointer.parse(input)
                pointer.tokens shouldBe expectedTokens
                pointer.toUriFragment() shouldBe expectedFormattedUri

                val abstractPointer = AbstractJsonPointer.parse(input)
                abstractPointer.tokens shouldBe expectedTokens
                abstractPointer.toUriFragment(emptyList()) shouldBe expectedFormattedUri
            }
        }
    }

    "invalid formats for non-abstract pointer" - {
        data class TestCase(val input: String) : WithDataTestName {
            override fun dataTestName(): String = "b) '$input'"
        }
        withData(
            listOf(
                TestCase("/"),
                TestCase("#/[]"),
                TestCase("#/one/[]/two"),
            )
        ) {
            with(it) {
                shouldThrow<IllegalStateException> { JsonPointer.parse(input) }
            }
        }
    }

    "invalid formats for abstract pointer" - {
        data class TestCase(val input: String, val arrayIndices: List<Int>) : WithDataTestName {
            override fun dataTestName(): String = "c) '$input'"
        }
        withData(
            listOf(
                TestCase("/", emptyList()),
                TestCase("#/one/[]", emptyList()),
                TestCase("#/one/[1, 2]", listOf(1, 2)),
            )
        ) {
            with(it) {
                shouldThrow<IllegalStateException> {
                    AbstractJsonPointer.parse(input).reifyPointer(arrayIndices)
                }
            }
        }
    }

    "parse and reify abstract pointer" - {
        data class TestCase(val input: String, val arrayIndices: List<Int>, val expectedReifiedUri: String) :
            WithDataTestName {
            override fun dataTestName(): String = "d) '$input'"
        }
        withData(
            listOf(
                TestCase("#/[]", listOf(1), "#/1"),
                TestCase("#//[]", listOf(1), "#//1"),
                TestCase("#/ /[]/ /", listOf(1), "#/ /1/ /"),
                TestCase("#/one/[]/two/[]/three/[]", listOf(1, 2, 3), "#/one/1/two/2/three/3"),
            )
        ) {
            with(it) {
                val parsed = AbstractJsonPointer.parse(input)
                parsed.toUriFragment(arrayIndices) shouldBe expectedReifiedUri

                val reifiedPointer = parsed.reifyPointer(arrayIndices)
                reifiedPointer.toUriFragment() shouldBe expectedReifiedUri
            }
        }
    }
})
