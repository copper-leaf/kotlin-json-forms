package com.copperleaf.json.pointer

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

class TestJsonPointerData : FreeSpec({
    "Test pointer parent and current" {
        val pointerFour = JsonPointer.parse("#/one/two/three/four")
        pointerFour.current shouldBe "four"

        val pointerThree = pointerFour.parent()
        pointerThree shouldBe JsonPointer.parse("#/one/two/three")
        pointerThree.current shouldBe "three"

        val pointerTwo = pointerThree.parent()
        pointerTwo shouldBe JsonPointer.parse("#/one/two")
        pointerTwo.current shouldBe "two"

        val pointerOne = pointerTwo.parent()
        pointerOne shouldBe JsonPointer.parse("#/one")
        pointerOne.current shouldBe "one"

        val pointerRoot = pointerOne.parent()
        pointerRoot shouldBe JsonPointer.parse("#")
        pointerRoot.parent() shouldBe JsonPointer.parse("#")
        pointerRoot.current shouldBe null
    }
})
