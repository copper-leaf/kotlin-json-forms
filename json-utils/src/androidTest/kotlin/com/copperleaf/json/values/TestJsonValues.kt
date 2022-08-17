package com.copperleaf.json.values

import com.copperleaf.json.pointer.JsonPointer
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

class TestJsonValues : FreeSpec({
    "objects" - {
        "primitive 3 value" {
            val notObjectMessage = "Element class kotlinx.serialization.json.JsonLiteral is not a JsonObject"
            val notArrayMessage = "Element class kotlinx.serialization.json.JsonLiteral is not a JsonArray"

            val value = JsonPrimitive(3)
            val map = JsonObject(mapOf("name" to value))

            shouldThrow<IllegalArgumentException> { value.string("name") }.message shouldBe notObjectMessage
            shouldThrow<IllegalArgumentException> { value.int("name") }.message shouldBe notObjectMessage
            shouldThrow<IllegalArgumentException> { value.long("name") }.message shouldBe notObjectMessage
            shouldThrow<IllegalArgumentException> { value.float("name") }.message shouldBe notObjectMessage
            shouldThrow<IllegalArgumentException> { value.double("name") }.message shouldBe notObjectMessage
            shouldThrow<IllegalArgumentException> { value.boolean("name") }.message shouldBe notObjectMessage

            map.string("name") shouldBe "3"
            map.int("name") shouldBe 3
            map.long("name") shouldBe 3L
            map.float("name") shouldBe 3f
            map.double("name") shouldBe 3.0
            shouldThrow<IllegalStateException> { map.boolean("name") }.message shouldBe "3 does not represent a Boolean"
            shouldThrow<IllegalArgumentException> { map.objectAt("name") }.message shouldBe notObjectMessage
            shouldThrow<IllegalArgumentException> { map.arrayAt("name") }.message shouldBe notArrayMessage
        }

        "primitive 3.14 value" {
            val notObjectMessage = "Element class kotlinx.serialization.json.JsonLiteral is not a JsonObject"
            val notArrayMessage = "Element class kotlinx.serialization.json.JsonLiteral is not a JsonArray"

            val value = JsonPrimitive(3.14)
            val map = JsonObject(mapOf("name" to value))

            shouldThrow<IllegalArgumentException> { value.string("name") }.message shouldBe notObjectMessage
            shouldThrow<IllegalArgumentException> { value.int("name") }.message shouldBe notObjectMessage
            shouldThrow<IllegalArgumentException> { value.long("name") }.message shouldBe notObjectMessage
            shouldThrow<IllegalArgumentException> { value.float("name") }.message shouldBe notObjectMessage
            shouldThrow<IllegalArgumentException> { value.double("name") }.message shouldBe notObjectMessage
            shouldThrow<IllegalArgumentException> { value.boolean("name") }.message shouldBe notObjectMessage

            map.string("name") shouldBe "3.14"
            shouldThrow<NumberFormatException> { map.int("name") }.message shouldBe "For input string: \"3.14\""
            shouldThrow<NumberFormatException> { map.long("name") }.message shouldBe "For input string: \"3.14\""
            map.float("name") shouldBe 3.14f
            map.double("name") shouldBe 3.14
            shouldThrow<IllegalStateException> { map.boolean("name") }.message shouldBe "3.14 does not represent a Boolean"
            shouldThrow<IllegalArgumentException> { map.objectAt("name") }.message shouldBe notObjectMessage
            shouldThrow<IllegalArgumentException> { map.arrayAt("name") }.message shouldBe notArrayMessage
        }

        "primitive string value" {
            val notObjectMessage = "Element class kotlinx.serialization.json.JsonLiteral is not a JsonObject"
            val notArrayMessage = "Element class kotlinx.serialization.json.JsonLiteral is not a JsonArray"

            val value = JsonPrimitive("something else")
            val map = JsonObject(mapOf("name" to value))

            shouldThrow<IllegalArgumentException> { value.string("name") }.message shouldBe notObjectMessage
            shouldThrow<IllegalArgumentException> { value.int("name") }.message shouldBe notObjectMessage
            shouldThrow<IllegalArgumentException> { value.long("name") }.message shouldBe notObjectMessage
            shouldThrow<IllegalArgumentException> { value.float("name") }.message shouldBe notObjectMessage
            shouldThrow<IllegalArgumentException> { value.double("name") }.message shouldBe notObjectMessage
            shouldThrow<IllegalArgumentException> { value.boolean("name") }.message shouldBe notObjectMessage

            map.string("name") shouldBe "something else"
            shouldThrow<NumberFormatException> { map.int("name") }.message shouldBe "For input string: \"something else\""
            shouldThrow<NumberFormatException> { map.long("name") }.message shouldBe "For input string: \"something else\""
            shouldThrow<NumberFormatException> { map.float("name") }.message shouldBe "For input string: \"something else\""
            shouldThrow<NumberFormatException> { map.double("name") }.message shouldBe "For input string: \"something else\""
            shouldThrow<IllegalStateException> { map.boolean("name") }.message shouldBe "\"something else\" does not represent a Boolean"
            shouldThrow<IllegalArgumentException> { map.objectAt("name") }.message shouldBe notObjectMessage
            shouldThrow<IllegalArgumentException> { map.arrayAt("name") }.message shouldBe notArrayMessage
        }

        "primitive boolean value" {
            val notObjectMessage = "Element class kotlinx.serialization.json.JsonLiteral is not a JsonObject"
            val notArrayMessage = "Element class kotlinx.serialization.json.JsonLiteral is not a JsonArray"

            val value = JsonPrimitive(true)
            val map = JsonObject(mapOf("name" to value))

            shouldThrow<IllegalArgumentException> { value.string("name") }.message shouldBe notObjectMessage
            shouldThrow<IllegalArgumentException> { value.int("name") }.message shouldBe notObjectMessage
            shouldThrow<IllegalArgumentException> { value.long("name") }.message shouldBe notObjectMessage
            shouldThrow<IllegalArgumentException> { value.float("name") }.message shouldBe notObjectMessage
            shouldThrow<IllegalArgumentException> { value.double("name") }.message shouldBe notObjectMessage
            shouldThrow<IllegalArgumentException> { value.boolean("name") }.message shouldBe notObjectMessage

            map.string("name") shouldBe "true"
            shouldThrow<NumberFormatException> { map.int("name") }.message shouldBe "For input string: \"true\""
            shouldThrow<NumberFormatException> { map.long("name") }.message shouldBe "For input string: \"true\""
            shouldThrow<NumberFormatException> { map.float("name") }.message shouldBe "For input string: \"true\""
            shouldThrow<NumberFormatException> { map.double("name") }.message shouldBe "For input string: \"true\""
            map.boolean("name") shouldBe true
            shouldThrow<IllegalArgumentException> { map.objectAt("name") }.message shouldBe notObjectMessage
            shouldThrow<IllegalArgumentException> { map.arrayAt("name") }.message shouldBe notArrayMessage
        }

        "object value" {
            val notPrimitiveMessage = "Element class kotlinx.serialization.json.JsonObject is not a JsonPrimitive"
            val notArrayMessage = "Element class kotlinx.serialization.json.JsonObject is not a JsonArray"

            val value = JsonObject(mapOf("inner" to JsonPrimitive("value")))
            val map = JsonObject(mapOf("name" to value))

            shouldThrow<IllegalArgumentException> { map.string("name") }.message shouldBe notPrimitiveMessage
            shouldThrow<IllegalArgumentException> { map.int("name") }.message shouldBe notPrimitiveMessage
            shouldThrow<IllegalArgumentException> { map.long("name") }.message shouldBe notPrimitiveMessage
            shouldThrow<IllegalArgumentException> { map.float("name") }.message shouldBe notPrimitiveMessage
            shouldThrow<IllegalArgumentException> { map.double("name") }.message shouldBe notPrimitiveMessage
            shouldThrow<IllegalArgumentException> { map.boolean("name") }.message shouldBe notPrimitiveMessage
            map.objectAt("name") shouldBe value
            shouldThrow<IllegalArgumentException> { map.arrayAt("name") }.message shouldBe notArrayMessage
        }

        "array value" {
            val notPrimitiveMessage = "Element class kotlinx.serialization.json.JsonArray is not a JsonPrimitive"
            val notObjectMessage = "Element class kotlinx.serialization.json.JsonArray is not a JsonObject"

            val value = JsonArray(listOf(JsonPrimitive("value")))
            val map = JsonObject(mapOf("name" to value))

            shouldThrow<IllegalArgumentException> { map.string("name") }.message shouldBe notPrimitiveMessage
            shouldThrow<IllegalArgumentException> { map.int("name") }.message shouldBe notPrimitiveMessage
            shouldThrow<IllegalArgumentException> { map.long("name") }.message shouldBe notPrimitiveMessage
            shouldThrow<IllegalArgumentException> { map.float("name") }.message shouldBe notPrimitiveMessage
            shouldThrow<IllegalArgumentException> { map.double("name") }.message shouldBe notPrimitiveMessage
            shouldThrow<IllegalArgumentException> { map.boolean("name") }.message shouldBe notPrimitiveMessage
            shouldThrow<IllegalArgumentException> { map.objectAt("name") }.message shouldBe notObjectMessage
            map.arrayAt("name") shouldBe value
        }
    }

    "arrays" - {
        "primitive 3 value" {
            val notObjectMessage = "Element class kotlinx.serialization.json.JsonLiteral is not a JsonObject"
            val notArrayMessage = "Element class kotlinx.serialization.json.JsonLiteral is not a JsonArray"

            val value = JsonPrimitive(3)
            val array = JsonArray(listOf(JsonNull, value))

            shouldThrow<IllegalArgumentException> { value.string(1) }.message shouldBe notArrayMessage
            shouldThrow<IllegalArgumentException> { value.int(1) }.message shouldBe notArrayMessage
            shouldThrow<IllegalArgumentException> { value.long(1) }.message shouldBe notArrayMessage
            shouldThrow<IllegalArgumentException> { value.float(1) }.message shouldBe notArrayMessage
            shouldThrow<IllegalArgumentException> { value.double(1) }.message shouldBe notArrayMessage
            shouldThrow<IllegalArgumentException> { value.boolean(1) }.message shouldBe notArrayMessage

            array.string(1) shouldBe "3"
            array.int(1) shouldBe 3
            array.long(1) shouldBe 3L
            array.float(1) shouldBe 3f
            array.double(1) shouldBe 3.0
            shouldThrow<IllegalStateException> { array.boolean(1) }.message shouldBe "3 does not represent a Boolean"
            shouldThrow<IllegalArgumentException> { array.objectAt(1) }.message shouldBe notObjectMessage
            shouldThrow<IllegalArgumentException> { array.arrayAt(1) }.message shouldBe notArrayMessage
        }

        "primitive 3.14 value" {
            val notObjectMessage = "Element class kotlinx.serialization.json.JsonLiteral is not a JsonObject"
            val notArrayMessage = "Element class kotlinx.serialization.json.JsonLiteral is not a JsonArray"

            val value = JsonPrimitive(3.14)
            val array = JsonArray(listOf(JsonNull, value))

            shouldThrow<IllegalArgumentException> { value.string(1) }.message shouldBe notArrayMessage
            shouldThrow<IllegalArgumentException> { value.int(1) }.message shouldBe notArrayMessage
            shouldThrow<IllegalArgumentException> { value.long(1) }.message shouldBe notArrayMessage
            shouldThrow<IllegalArgumentException> { value.float(1) }.message shouldBe notArrayMessage
            shouldThrow<IllegalArgumentException> { value.double(1) }.message shouldBe notArrayMessage
            shouldThrow<IllegalArgumentException> { value.boolean(1) }.message shouldBe notArrayMessage

            array.string(1) shouldBe "3.14"
            shouldThrow<NumberFormatException> { array.int(1) }.message shouldBe "For input string: \"3.14\""
            shouldThrow<NumberFormatException> { array.long(1) }.message shouldBe "For input string: \"3.14\""
            array.float(1) shouldBe 3.14f
            array.double(1) shouldBe 3.14
            shouldThrow<IllegalStateException> { array.boolean(1) }.message shouldBe "3.14 does not represent a Boolean"
            shouldThrow<IllegalArgumentException> { array.objectAt(1) }.message shouldBe notObjectMessage
            shouldThrow<IllegalArgumentException> { array.arrayAt(1) }.message shouldBe notArrayMessage
        }

        "primitive string value" {
            val notObjectMessage = "Element class kotlinx.serialization.json.JsonLiteral is not a JsonObject"
            val notArrayMessage = "Element class kotlinx.serialization.json.JsonLiteral is not a JsonArray"

            val value = JsonPrimitive("something else")
            val array = JsonArray(listOf(JsonNull, value))

            shouldThrow<IllegalArgumentException> { value.string(1) }.message shouldBe notArrayMessage
            shouldThrow<IllegalArgumentException> { value.int(1) }.message shouldBe notArrayMessage
            shouldThrow<IllegalArgumentException> { value.long(1) }.message shouldBe notArrayMessage
            shouldThrow<IllegalArgumentException> { value.float(1) }.message shouldBe notArrayMessage
            shouldThrow<IllegalArgumentException> { value.double(1) }.message shouldBe notArrayMessage
            shouldThrow<IllegalArgumentException> { value.boolean(1) }.message shouldBe notArrayMessage

            array.string(1) shouldBe "something else"
            shouldThrow<NumberFormatException> { array.int(1) }.message shouldBe "For input string: \"something else\""
            shouldThrow<NumberFormatException> { array.long(1) }.message shouldBe "For input string: \"something else\""
            shouldThrow<NumberFormatException> { array.float(1) }.message shouldBe "For input string: \"something else\""
            shouldThrow<NumberFormatException> { array.double(1) }.message shouldBe "For input string: \"something else\""
            shouldThrow<IllegalStateException> { array.boolean(1) }.message shouldBe "\"something else\" does not represent a Boolean"
            shouldThrow<IllegalArgumentException> { array.objectAt(1) }.message shouldBe notObjectMessage
            shouldThrow<IllegalArgumentException> { array.arrayAt(1) }.message shouldBe notArrayMessage
        }

        "primitive boolean value" {
            val notObjectMessage = "Element class kotlinx.serialization.json.JsonLiteral is not a JsonObject"
            val notArrayMessage = "Element class kotlinx.serialization.json.JsonLiteral is not a JsonArray"

            val value = JsonPrimitive(true)
            val array = JsonArray(listOf(JsonNull, value))

            shouldThrow<IllegalArgumentException> { value.string(1) }.message shouldBe notArrayMessage
            shouldThrow<IllegalArgumentException> { value.int(1) }.message shouldBe notArrayMessage
            shouldThrow<IllegalArgumentException> { value.long(1) }.message shouldBe notArrayMessage
            shouldThrow<IllegalArgumentException> { value.float(1) }.message shouldBe notArrayMessage
            shouldThrow<IllegalArgumentException> { value.double(1) }.message shouldBe notArrayMessage
            shouldThrow<IllegalArgumentException> { value.boolean(1) }.message shouldBe notArrayMessage

            array.string(1) shouldBe "true"
            shouldThrow<NumberFormatException> { array.int(1) }.message shouldBe "For input string: \"true\""
            shouldThrow<NumberFormatException> { array.long(1) }.message shouldBe "For input string: \"true\""
            shouldThrow<NumberFormatException> { array.float(1) }.message shouldBe "For input string: \"true\""
            shouldThrow<NumberFormatException> { array.double(1) }.message shouldBe "For input string: \"true\""
            array.boolean(1) shouldBe true
            shouldThrow<IllegalArgumentException> { array.objectAt(1) }.message shouldBe notObjectMessage
            shouldThrow<IllegalArgumentException> { array.arrayAt(1) }.message shouldBe notArrayMessage
        }

        "object value" {
            val notPrimitiveMessage = "Element class kotlinx.serialization.json.JsonObject is not a JsonPrimitive"
            val notArrayMessage = "Element class kotlinx.serialization.json.JsonObject is not a JsonArray"

            val value = JsonObject(mapOf("inner" to JsonPrimitive("value")))
            val array = JsonArray(listOf(JsonNull, value))

            shouldThrow<IllegalArgumentException> { array.string(1) }.message shouldBe notPrimitiveMessage
            shouldThrow<IllegalArgumentException> { array.int(1) }.message shouldBe notPrimitiveMessage
            shouldThrow<IllegalArgumentException> { array.long(1) }.message shouldBe notPrimitiveMessage
            shouldThrow<IllegalArgumentException> { array.float(1) }.message shouldBe notPrimitiveMessage
            shouldThrow<IllegalArgumentException> { array.double(1) }.message shouldBe notPrimitiveMessage
            shouldThrow<IllegalArgumentException> { array.boolean(1) }.message shouldBe notPrimitiveMessage
            array.objectAt(1) shouldBe value
            shouldThrow<IllegalArgumentException> { array.arrayAt(1) }.message shouldBe notArrayMessage
        }

        "array value" {
            val notPrimitiveMessage = "Element class kotlinx.serialization.json.JsonArray is not a JsonPrimitive"
            val notObjectMessage = "Element class kotlinx.serialization.json.JsonArray is not a JsonObject"

            val value = JsonArray(listOf(JsonPrimitive("value")))
            val array = JsonArray(listOf(JsonNull, value))

            shouldThrow<IllegalArgumentException> { array.string(1) }.message shouldBe notPrimitiveMessage
            shouldThrow<IllegalArgumentException> { array.int(1) }.message shouldBe notPrimitiveMessage
            shouldThrow<IllegalArgumentException> { array.long(1) }.message shouldBe notPrimitiveMessage
            shouldThrow<IllegalArgumentException> { array.float(1) }.message shouldBe notPrimitiveMessage
            shouldThrow<IllegalArgumentException> { array.double(1) }.message shouldBe notPrimitiveMessage
            shouldThrow<IllegalArgumentException> { array.boolean(1) }.message shouldBe notPrimitiveMessage
            shouldThrow<IllegalArgumentException> { array.objectAt(1) }.message shouldBe notObjectMessage
            array.arrayAt(1) shouldBe value
        }
    }

    "pointer" - {
        "primitive 3 value" {
            val pointerNotFound = "value at pointer '#/name' could not be found"
            val notObjectMessage = "Element class kotlinx.serialization.json.JsonLiteral is not a JsonObject"
            val notArrayMessage = "Element class kotlinx.serialization.json.JsonLiteral is not a JsonArray"

            val pointer = JsonPointer.parse("#/name")
            val value = JsonPrimitive(3)
            val map = JsonObject(mapOf("name" to value))

            shouldThrow<IllegalStateException> { value.string(pointer) }.message shouldBe pointerNotFound
            shouldThrow<IllegalStateException> { value.int(pointer) }.message shouldBe pointerNotFound
            shouldThrow<IllegalStateException> { value.long(pointer) }.message shouldBe pointerNotFound
            shouldThrow<IllegalStateException> { value.float(pointer) }.message shouldBe pointerNotFound
            shouldThrow<IllegalStateException> { value.double(pointer) }.message shouldBe pointerNotFound
            shouldThrow<IllegalStateException> { value.boolean(pointer) }.message shouldBe pointerNotFound

            map.string(pointer) shouldBe "3"
            map.int(pointer) shouldBe 3
            map.long(pointer) shouldBe 3L
            map.float(pointer) shouldBe 3f
            map.double(pointer) shouldBe 3.0
            shouldThrow<IllegalStateException> { map.boolean(pointer) }.message shouldBe "3 does not represent a Boolean"
            shouldThrow<IllegalArgumentException> { map.objectAt(pointer) }.message shouldBe notObjectMessage
            shouldThrow<IllegalArgumentException> { map.arrayAt(pointer) }.message shouldBe notArrayMessage
        }

        "primitive 3.14 value" {
            val pointerNotFound = "value at pointer '#/name' could not be found"
            val notObjectMessage = "Element class kotlinx.serialization.json.JsonLiteral is not a JsonObject"
            val notArrayMessage = "Element class kotlinx.serialization.json.JsonLiteral is not a JsonArray"

            val pointer = JsonPointer.parse("#/name")
            val value = JsonPrimitive(3.14)
            val map = JsonObject(mapOf("name" to value))

            shouldThrow<IllegalStateException> { value.string(pointer) }.message shouldBe pointerNotFound
            shouldThrow<IllegalStateException> { value.int(pointer) }.message shouldBe pointerNotFound
            shouldThrow<IllegalStateException> { value.long(pointer) }.message shouldBe pointerNotFound
            shouldThrow<IllegalStateException> { value.float(pointer) }.message shouldBe pointerNotFound
            shouldThrow<IllegalStateException> { value.double(pointer) }.message shouldBe pointerNotFound
            shouldThrow<IllegalStateException> { value.boolean(pointer) }.message shouldBe pointerNotFound

            map.string(pointer) shouldBe "3.14"
            shouldThrow<NumberFormatException> { map.int(pointer) }.message shouldBe "For input string: \"3.14\""
            shouldThrow<NumberFormatException> { map.long(pointer) }.message shouldBe "For input string: \"3.14\""
            map.float(pointer) shouldBe 3.14f
            map.double(pointer) shouldBe 3.14
            shouldThrow<IllegalStateException> { map.boolean(pointer) }.message shouldBe "3.14 does not represent a Boolean"
            shouldThrow<IllegalArgumentException> { map.objectAt(pointer) }.message shouldBe notObjectMessage
            shouldThrow<IllegalArgumentException> { map.arrayAt(pointer) }.message shouldBe notArrayMessage
        }

        "primitive string value" {
            val pointerNotFound = "value at pointer '#/name' could not be found"
            val notObjectMessage = "Element class kotlinx.serialization.json.JsonLiteral is not a JsonObject"
            val notArrayMessage = "Element class kotlinx.serialization.json.JsonLiteral is not a JsonArray"

            val pointer = JsonPointer.parse("#/name")
            val value = JsonPrimitive("something else")
            val map = JsonObject(mapOf("name" to value))

            shouldThrow<IllegalStateException> { value.string(pointer) }.message shouldBe pointerNotFound
            shouldThrow<IllegalStateException> { value.int(pointer) }.message shouldBe pointerNotFound
            shouldThrow<IllegalStateException> { value.long(pointer) }.message shouldBe pointerNotFound
            shouldThrow<IllegalStateException> { value.float(pointer) }.message shouldBe pointerNotFound
            shouldThrow<IllegalStateException> { value.double(pointer) }.message shouldBe pointerNotFound
            shouldThrow<IllegalStateException> { value.boolean(pointer) }.message shouldBe pointerNotFound

            map.string(pointer) shouldBe "something else"
            shouldThrow<NumberFormatException> { map.int(pointer) }.message shouldBe "For input string: \"something else\""
            shouldThrow<NumberFormatException> { map.long(pointer) }.message shouldBe "For input string: \"something else\""
            shouldThrow<NumberFormatException> { map.float(pointer) }.message shouldBe "For input string: \"something else\""
            shouldThrow<NumberFormatException> { map.double(pointer) }.message shouldBe "For input string: \"something else\""
            shouldThrow<IllegalStateException> { map.boolean(pointer) }.message shouldBe "\"something else\" does not represent a Boolean"
            shouldThrow<IllegalArgumentException> { map.objectAt(pointer) }.message shouldBe notObjectMessage
            shouldThrow<IllegalArgumentException> { map.arrayAt(pointer) }.message shouldBe notArrayMessage
        }

        "primitive boolean value" {
            val pointerNotFound = "value at pointer '#/name' could not be found"
            val notObjectMessage = "Element class kotlinx.serialization.json.JsonLiteral is not a JsonObject"
            val notArrayMessage = "Element class kotlinx.serialization.json.JsonLiteral is not a JsonArray"

            val pointer = JsonPointer.parse("#/name")
            val value = JsonPrimitive(true)
            val map = JsonObject(mapOf("name" to value))

            shouldThrow<IllegalStateException> { value.string(pointer) }.message shouldBe pointerNotFound
            shouldThrow<IllegalStateException> { value.int(pointer) }.message shouldBe pointerNotFound
            shouldThrow<IllegalStateException> { value.long(pointer) }.message shouldBe pointerNotFound
            shouldThrow<IllegalStateException> { value.float(pointer) }.message shouldBe pointerNotFound
            shouldThrow<IllegalStateException> { value.double(pointer) }.message shouldBe pointerNotFound
            shouldThrow<IllegalStateException> { value.boolean(pointer) }.message shouldBe pointerNotFound

            map.string(pointer) shouldBe "true"
            shouldThrow<NumberFormatException> { map.int(pointer) }.message shouldBe "For input string: \"true\""
            shouldThrow<NumberFormatException> { map.long(pointer) }.message shouldBe "For input string: \"true\""
            shouldThrow<NumberFormatException> { map.float(pointer) }.message shouldBe "For input string: \"true\""
            shouldThrow<NumberFormatException> { map.double(pointer) }.message shouldBe "For input string: \"true\""
            map.boolean(pointer) shouldBe true
            shouldThrow<IllegalArgumentException> { map.objectAt(pointer) }.message shouldBe notObjectMessage
            shouldThrow<IllegalArgumentException> { map.arrayAt(pointer) }.message shouldBe notArrayMessage
        }

        "object value" {
            val notPrimitiveMessage = "Element class kotlinx.serialization.json.JsonObject is not a JsonPrimitive"
            val notArrayMessage = "Element class kotlinx.serialization.json.JsonObject is not a JsonArray"

            val pointer = JsonPointer.parse("#/name")
            val value = JsonObject(mapOf("inner" to JsonPrimitive("value")))
            val map = JsonObject(mapOf("name" to value))

            shouldThrow<IllegalArgumentException> { map.string(pointer) }.message shouldBe notPrimitiveMessage
            shouldThrow<IllegalArgumentException> { map.int(pointer) }.message shouldBe notPrimitiveMessage
            shouldThrow<IllegalArgumentException> { map.long(pointer) }.message shouldBe notPrimitiveMessage
            shouldThrow<IllegalArgumentException> { map.float(pointer) }.message shouldBe notPrimitiveMessage
            shouldThrow<IllegalArgumentException> { map.double(pointer) }.message shouldBe notPrimitiveMessage
            shouldThrow<IllegalArgumentException> { map.boolean(pointer) }.message shouldBe notPrimitiveMessage
            map.objectAt(pointer) shouldBe value
            shouldThrow<IllegalArgumentException> { map.arrayAt(pointer) }.message shouldBe notArrayMessage
        }

        "array value" {
            val notPrimitiveMessage = "Element class kotlinx.serialization.json.JsonArray is not a JsonPrimitive"
            val notObjectMessage = "Element class kotlinx.serialization.json.JsonArray is not a JsonObject"

            val pointer = JsonPointer.parse("#/name")
            val value = JsonArray(listOf(JsonPrimitive("value")))
            val map = JsonObject(mapOf("name" to value))

            shouldThrow<IllegalArgumentException> { map.string(pointer) }.message shouldBe notPrimitiveMessage
            shouldThrow<IllegalArgumentException> { map.int(pointer) }.message shouldBe notPrimitiveMessage
            shouldThrow<IllegalArgumentException> { map.long(pointer) }.message shouldBe notPrimitiveMessage
            shouldThrow<IllegalArgumentException> { map.float(pointer) }.message shouldBe notPrimitiveMessage
            shouldThrow<IllegalArgumentException> { map.double(pointer) }.message shouldBe notPrimitiveMessage
            shouldThrow<IllegalArgumentException> { map.boolean(pointer) }.message shouldBe notPrimitiveMessage
            shouldThrow<IllegalArgumentException> { map.objectAt(pointer) }.message shouldBe notObjectMessage
            map.arrayAt(pointer) shouldBe value
        }
    }
})
