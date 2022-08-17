package com.copperleaf.json.utils

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import kotlinx.serialization.json.JsonArray

class TestJsonBuilder : FreeSpec({
    "object builder" {
        val result = json {
            "objectValue" to {
                "one" to 1
                "two" to 2.3
                "three" to true
            }
            "arrayValue"[
                "one",
                2,
                3.4,
                true
            ]
        }

        result.toJsonString(true) shouldBe """
            {
                "objectValue": {
                    "one": 1,
                    "two": 2.3,
                    "three": true
                },
                "arrayValue": [
                    "one",
                    2,
                    3.4,
                    true
                ]
            }
        """.trimIndent()
    }
    "array builder" {
        val result = JsonArray[
            "one",
            2,
            3.4,
            true,
            json {

            }
        ]

        result.toJsonString(true) shouldBe """
            [
                "one",
                2,
                3.4,
                true,
                {
                }
            ]
        """.trimIndent()
    }
})
