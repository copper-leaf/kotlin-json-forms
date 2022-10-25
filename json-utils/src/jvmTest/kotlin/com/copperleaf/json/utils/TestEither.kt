package com.copperleaf.json.utils

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

class TestEither : FreeSpec({
    "left" {
        val value: Either<String, Int> = "one".asLeft()
        value.leftValue shouldBe "one"
    }
    "implied left" {
        val value: Either<String, Int> = "one".asEither()
        value.leftValue shouldBe "one"
    }
    "right" {
        val value: Either<String, Int> = 3.asRight()
        value.rightValue shouldBe 3
    }
    "implied right" {
        val value: Either<String, Int> = 3.asEither()
        value.rightValue shouldBe 3
    }
})
