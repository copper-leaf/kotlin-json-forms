package com.copperleaf.json.values

import com.copperleaf.json.pointer.JsonPointer
import com.copperleaf.json.pointer.findOrThrow
import com.copperleaf.json.utils.Either
import com.copperleaf.json.utils.asLeft
import com.copperleaf.json.utils.asRight
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.boolean
import kotlinx.serialization.json.double
import kotlinx.serialization.json.float
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.long

// Wrappers around accessor API
// ---------------------------------------------------------------------------------------------------------------------

public fun <T> JsonElement.optional(op: JsonElement.() -> T): T? {
    return runCatching { op() }.getOrNull()
}

public fun <T> JsonElement.optional(defaultValue: T, op: JsonElement.() -> T): T {
    return runCatching { op() }.getOrDefault(defaultValue)
}

public fun <T> JsonElement.optionalWithError(op: JsonElement.() -> T): Either<T, List<String>> {
    return runCatching { op() }
        .fold(
            onSuccess = { it.asLeft() },
            onFailure = { listOf(it.message ?: "").asRight() }
        )
}

// Values from objects
// ---------------------------------------------------------------------------------------------------------------------

public fun JsonElement.string(name: String): String {
    return this.jsonObject.getValue(name).jsonPrimitive.content
}

public fun JsonElement.int(name: String): Int {
    return this.jsonObject.getValue(name).jsonPrimitive.int
}

public fun JsonElement.long(name: String): Long {
    return this.jsonObject.getValue(name).jsonPrimitive.long
}

public fun JsonElement.float(name: String): Float {
    return this.jsonObject.getValue(name).jsonPrimitive.float
}

public fun JsonElement.double(name: String): Double {
    return this.jsonObject.getValue(name).jsonPrimitive.double
}

public fun JsonElement.boolean(name: String): Boolean {
    return this.jsonObject.getValue(name).jsonPrimitive.boolean
}

public fun JsonElement.objectAt(name: String): JsonObject {
    return this.jsonObject.getValue(name).jsonObject
}

public fun JsonElement.arrayAt(name: String): JsonArray {
    return this.jsonObject.getValue(name).jsonArray
}

// Values from arrays
// ---------------------------------------------------------------------------------------------------------------------

public fun JsonElement.string(index: Int): String {
    return this.jsonArray[index].jsonPrimitive.content
}

public fun JsonElement.int(index: Int): Int {
    return this.jsonArray[index].jsonPrimitive.int
}

public fun JsonElement.long(index: Int): Long {
    return this.jsonArray[index].jsonPrimitive.long
}

public fun JsonElement.float(index: Int): Float {
    return this.jsonArray[index].jsonPrimitive.float
}

public fun JsonElement.double(index: Int): Double {
    return this.jsonArray[index].jsonPrimitive.double
}

public fun JsonElement.boolean(index: Int): Boolean {
    return this.jsonArray[index].jsonPrimitive.boolean
}

public fun JsonElement.objectAt(index: Int): JsonObject {
    return this.jsonArray[index].jsonObject
}

public fun JsonElement.arrayAt(index: Int): JsonArray {
    return this.jsonArray[index].jsonArray
}

// Values from arrays
// ---------------------------------------------------------------------------------------------------------------------

public fun JsonElement.string(pointer: JsonPointer): String {
    return this.findOrThrow(pointer).jsonPrimitive.content
}

public fun JsonElement.int(pointer: JsonPointer): Int {
    return this.findOrThrow(pointer).jsonPrimitive.int
}

public fun JsonElement.long(pointer: JsonPointer): Long {
    return this.findOrThrow(pointer).jsonPrimitive.long
}

public fun JsonElement.float(pointer: JsonPointer): Float {
    return this.findOrThrow(pointer).jsonPrimitive.float
}

public fun JsonElement.double(pointer: JsonPointer): Double {
    return this.findOrThrow(pointer).jsonPrimitive.double
}

public fun JsonElement.boolean(pointer: JsonPointer): Boolean {
    return this.findOrThrow(pointer).jsonPrimitive.boolean
}

public fun JsonElement.objectAt(pointer: JsonPointer): JsonObject {
    return this.findOrThrow(pointer).jsonObject
}

public fun JsonElement.arrayAt(pointer: JsonPointer): JsonArray {
    return this.findOrThrow(pointer).jsonArray
}
