package com.copperleaf.json.pointer

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldNotBeSameInstanceAs
import net.pwall.json.JSONValue

class TestJsonPointerUpdate : FreeSpec({
    "Update Primitives In Object" - {
        "string" {
            "null".testSetValue("#/one", "one") {
                """
                    {"one":"one"}
                """
            }
        }
        "int" {
            "null".testSetValue("#/one", 2) {
                """
                    {"one":2}
                """
            }
        }
        "double" {
            "null".testSetValue("#/one", 3.4) {
                """
                    {"one":3.4}
                """
            }
        }
        "boolean" {
            "null".testSetValue("#/one", true) {
                """
                    {"one":true}
                """
            }
        }
        "null" {
            "null".testSetValue("#/one", null) {
                """
                    {"one":null}
                """
            }
        }
        "array" {
            "null".testSetValue("#/one", listOf(1, 2, 3)) {
                """
                    {"one":[1,2,3]}
                """
            }
        }
        "object" {
            "null".testSetValue("#/one", mapOf("one" to 1, "two" to 2)) {
                """
                    {"one":{"one": 1, "two": 2}}
                """
            }
        }
    }
    "Update Primitives In Array" - {
        "string" {
            "null".testSetValue("#/1", "one") {
                """
                    [null,"one"]
                """
            }
        }
        "int" {
            "null".testSetValue("#/1", 2) {
                """
                    [null,2]
                """
            }
        }
        "double" {
            "null".testSetValue("#/1", 3.4) {
                """
                    [null,3.4]
                """
            }
        }
        "boolean" {
            "null".testSetValue("#/1", true) {
                """
                    [null,true]
                """
            }
        }
        "null" {
            "null".testSetValue("#/1", null) {
                """
                    [null,null]
                """
            }
        }
        "array" {
            "null".testSetValue("#/1", listOf(1, 2, 3)) {
                """
                    [null,[1,2,3]]
                """
            }
        }
        "object" {
            "null".testSetValue("#/1", mapOf("one" to 1, "two" to 2)) {
                """
                    [null,{"one": 1, "two": 2}]
                """
            }
        }
    }
    "Deep Pointers" - {
        "object, 2 deep" {
            "null".testSetValue("#/one/two", "value") {
                """
                    { 
                       "one": { 
                            "two": "value"
                        }
                    }
                """
            }
        }
        "object, 3 deep" {
            "null".testSetValue("#/one/two/three", "value") {
                """
                    { 
                       "one": { 
                            "two": { 
                                "three": "value"
                            }
                        }
                    }
                """
            }
        }
        "array, 2 deep" {
            "null".testSetValue("#/1/2", "value") {
                """
                    [
                        null,
                        [
                            null,
                            null,
                            "value"
                        ]
                    ]
                """
            }
        }
        "array, 3 deep" {
            "null".testSetValue("#/1/2/3", "value") {
                """
                    [
                        null,
                        [
                            null,
                            null,
                            [
                                null,
                                null,
                                null,
                                "value"
                            ]
                        ]
                    ]
                """
            }
        }
        "really, really deep" {
            "null".testSetValue("#/a/b/1/c/2/d", "one") {
                """
                    { 
                       "a": { 
                            "b": [
                                null,
                                { 
                                    "c": [
                                        null,
                                        null,
                                        { 
                                            "d": "one"
                                        }
                                    ]
                                }
                            ]
                        }
                    }
                """
            }
        }
    }
}) {
    companion object {
        fun String.testSetValue(
            jsonPointer: String,
            value: Any?,
            resultingJson: () -> String
        ) {
            val json: JSONValue? = this.parseJson()

            val jsonValue = value.toJsonValue()

            val pointer = jsonPointer.asPointer(emptyList())
            val action = JsonPointerAction.SetValue(jsonValue)

            val updatedValue = json.updateWithPointer(pointer, action)
            val expectedResultingJson = resultingJson().trimIndent().trim()
            val expectedResultingJsonAsObject = expectedResultingJson.parseJson()

            val updatedValueJsonString = updatedValue?.toJSON()
            val expectedResultingJsonString = expectedResultingJsonAsObject?.toJSON()

            updatedValue shouldNotBeSameInstanceAs json
            updatedValueJsonString shouldBe expectedResultingJsonString

            val valueFromPointer = pointer.find(updatedValue)
            valueFromPointer shouldBe jsonValue
        }
    }
}
