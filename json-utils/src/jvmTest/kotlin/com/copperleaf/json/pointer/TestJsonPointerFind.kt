package com.copperleaf.json.pointer

import io.kotest.core.spec.style.FreeSpec
import io.kotest.datatest.WithDataTestName
import io.kotest.datatest.withData
import io.kotest.matchers.shouldBe
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

class TestJsonPointerFind : FreeSpec({
    val json = Json { prettyPrint = true }

    "Basic 1-level document with strange keys" - {
        val document = """
            {
              "foo": ["bar", "baz"],
              "": 0,
              "a/b": 1,
              "c%d": 2,
              "e^f": 3,
              "g|h": 4,
              "i\\j": 5,
              "k\"l": 6,
              " ": 7,
              "m~n": 8
            }
        """.trimIndent()
            .let { json.decodeFromString(JsonElement.serializer(), it) }

        data class TestCase(val input: String, val expectedValue: JsonElement) : WithDataTestName {
            override fun dataTestName(): String = "a) '$input'"
        }

        withData(
            listOf(
                TestCase("#", document),

                TestCase("#/foo", JsonArray(listOf(JsonPrimitive("bar"), JsonPrimitive("baz")))),
                TestCase("#/", JsonPrimitive(0)),
                TestCase("#/a~1b", JsonPrimitive(1)),
                TestCase("#/c%d", JsonPrimitive(2)),
                TestCase("#/e^f", JsonPrimitive(3)),
                TestCase("#/g|h", JsonPrimitive(4)),
                TestCase("#/i\\j", JsonPrimitive(5)),
                TestCase("#/k\"l", JsonPrimitive(6)),
                TestCase("#/ ", JsonPrimitive(7)),
                TestCase("#/m~0n", JsonPrimitive(8)),
            )
        ) {
            with(it) {
                val pointer = JsonPointer.parse(input)
                val result = document.find(pointer)
                result shouldBe expectedValue
            }
        }
    }

    "Simple nested object document" - {
        val document = """
            {
              "a": {
                "b": {
                  "c": true
                }
              }
            }
        """.trimIndent()
            .let { json.decodeFromString(JsonElement.serializer(), it) }

        data class TestCase(val input: String, val expectedValue: JsonElement) : WithDataTestName {
            override fun dataTestName(): String = "b) '$input'"
        }

        withData(
            listOf(
                TestCase("#", document),

                TestCase("#/a/b/c", JsonPrimitive(true)),
                TestCase("#/a/b", JsonObject(mapOf("c" to JsonPrimitive(true)))),
                TestCase("#/a", JsonObject(mapOf("b" to JsonObject(mapOf("c" to JsonPrimitive(true)))))),
            )
        ) {
            with(it) {
                val pointer = JsonPointer.parse(input)
                val result = document.find(pointer)
                result shouldBe expectedValue
            }
        }
    }

    "Simple nested array document" - {
        val document = """
            [
              [
                [
                  true
                ]
              ]
            ]
        """.trimIndent()
            .let { json.decodeFromString(JsonElement.serializer(), it) }

        data class TestCase(val input: String, val expectedValue: JsonElement) : WithDataTestName {
            override fun dataTestName(): String = "c) '$input'"
        }

        withData(
            listOf(
                TestCase("#", document),

                TestCase("#/0/0/0", JsonPrimitive(true)),
                TestCase("#/0/0", JsonArray(listOf(JsonPrimitive(true)))),
                TestCase("#/0", JsonArray(listOf(JsonArray(listOf(JsonPrimitive(true)))))),
            )
        ) {
            with(it) {
                val pointer = JsonPointer.parse(input)
                val result = document.find(pointer)
                result shouldBe expectedValue
            }
        }
    }
})
