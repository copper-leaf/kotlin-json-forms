package com.copperleaf.json.utils

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

class TestListUtils : FreeSpec({
    "takeHead - mapping" {
        listOf(1, 2, 3, 4, 5).takeHead() shouldBe (1 to listOf(2, 3, 4, 5))
    }
    "takeHead - with mapping" {
        listOf(1, 2, 3, 4, 5).takeHead { "$it" } shouldBe ("1" to listOf(2, 3, 4, 5))
    }
    "takeHead - multiple head elements" {
        listOf(1, 2, 3, 4, 5).takeHead(2) shouldBe (listOf(1, 2) to listOf(3, 4, 5))
    }
})
