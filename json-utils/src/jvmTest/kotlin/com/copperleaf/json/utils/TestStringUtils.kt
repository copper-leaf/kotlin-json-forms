package com.copperleaf.json.utils

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

class TestStringUtils : FreeSpec({
    "splits" - {
        "camelCase" {
            SplitTransformer()
                .splitEach { camelCase }
                .executeSplit("oneTwoThree") shouldBe listOf("one", "Two", "Three")
        }
        "words" {
            SplitTransformer()
                .splitEach { words }
                .executeSplit("one two three") shouldBe listOf("one", "two", "three")
        }
        "snakeCase" {
            SplitTransformer()
                .splitEach { snakeCase }
                .executeSplit("one_two_three") shouldBe listOf("one", "two", "three")
        }
        "dashCase" {
            SplitTransformer()
                .splitEach { dashCase }
                .executeSplit("one-two-three") shouldBe listOf("one", "two", "three")
        }
        "untrimmed by default" {
            SplitTransformer()
                .splitEach { dashCase }
                .executeSplit("one - two - three") shouldBe listOf("one ", " two ", " three")
        }
    }
    "transforms" - {
        "camelCase" {
            SplitTransformer()
                .splitEach { camelCase }
                .transformEach { camelCase }
                .executeSplit("oneTwoThree") shouldBe listOf("one", "Two", "Three")
        }
        "pascalCase" {
            SplitTransformer()
                .splitEach { camelCase }
                .transformEach { pascalCase }
                .executeSplit("oneTwoThree") shouldBe listOf("One", "Two", "Three")
        }
        "upperCase" {
            SplitTransformer()
                .splitEach { camelCase }
                .transformEach { uppercase }
                .executeSplit("oneTwoThree") shouldBe listOf("ONE", "TWO", "THREE")
        }
        "lowerCase" {
            SplitTransformer()
                .splitEach { camelCase }
                .transformEach { lowercase }
                .executeSplit("oneTwoThree") shouldBe listOf("one", "two", "three")
        }
        "trimmed" {
            SplitTransformer()
                .splitEach { dashCase }
                .transformEach { trimmed }
                .executeSplit("one - two - three") shouldBe listOf("one", "two", "three")
        }
        "urlEncoded" {
            SplitTransformer()
                .splitEach { dashCase }
                .transformEach { urlEncoded }
                .executeSplit("one - two - three") shouldBe listOf("one+", "+two+", "+three")
        }
    }
    "combines" - {
        "camelCase" {
            SplitTransformer()
                .splitEach { camelCase }
                .transformEach { camelCase }
                .convertTo("oneTwoThree") { camelCase } shouldBe "oneTwoThree"
        }
        "pascalCase" {
            SplitTransformer()
                .splitEach { camelCase }
                .transformEach { pascalCase }
                .convertTo("oneTwoThree") { camelCase } shouldBe "OneTwoThree"
        }
        "dashCase" {
            SplitTransformer()
                .splitEach { camelCase }
                .transformEach { uppercase }
                .convertTo("oneTwoThree") { dashCase } shouldBe "ONE-TWO-THREE"
        }
        "snakeCase" {
            SplitTransformer()
                .splitEach { camelCase }
                .transformEach { uppercase }
                .convertTo("oneTwoThree") { snakeCase } shouldBe "ONE_TWO_THREE"
        }
        "words" {
            SplitTransformer()
                .splitEach { camelCase }
                .transformEach { uppercase }
                .convertTo("oneTwoThree") { words } shouldBe "ONE TWO THREE"
        }
    }
})
