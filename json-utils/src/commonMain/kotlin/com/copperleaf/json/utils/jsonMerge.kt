package com.copperleaf.json.utils

import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

public fun JsonElement.merge(other: JsonElement): JsonElement {
    return when (this) {
        is JsonObject -> this.mergeIntoObject(other)
        is JsonArray -> this.mergeIntoArray(other)
        is JsonPrimitive -> this.mergeIntoPrimitive(other)
    }
}

private fun JsonObject.mergeIntoObject(other: JsonElement): JsonObject {
    val thisObject = this

    return when (other) {
        is JsonObject -> {
            JsonObject(
                buildMap {
                    val otherObject = other

                    (thisObject.keys + otherObject.keys).forEach { key ->
                        val thisObjectValue = thisObject[key]
                        val otherObjectValue = otherObject[key]

                        if (thisObjectValue != null && otherObjectValue != null) {
                            put(key, thisObjectValue.merge(otherObjectValue))
                        } else if (thisObjectValue != null && otherObjectValue == null) {
                            put(key, thisObjectValue)
                        } else if (thisObjectValue == null && otherObjectValue != null) {
                            put(key, otherObjectValue)
                        }
                    }
                }
            )
        }

        is JsonArray -> {
            error("Cannot merge an array into an object")
        }

        is JsonPrimitive -> {
            error("Cannot merge a primitive into an object (${other.toJsonString(true)} -> ${this.toJsonString(true)})")
        }
    }
}

private fun JsonArray.mergeIntoArray(other: JsonElement): JsonArray {
    val thisArray = this
    return when (other) {
        is JsonObject -> {
            error("Cannot merge an object into an array")
        }

        is JsonArray -> {
            val otherArray = other
            JsonArray(thisArray + otherArray)
        }

        is JsonPrimitive -> {
            error("Cannot merge a primitive into an array")
        }
    }
}

private fun JsonPrimitive.mergeIntoPrimitive(other: JsonElement): JsonPrimitive {
    when (other) {
        is JsonObject -> {
            error("Cannot merge an object into a primitive (${other.toJsonString(true)} -> ${this.toJsonString(true)})")
        }

        is JsonArray -> {
            error("Cannot merge an array into a primitive")
        }

        is JsonPrimitive -> {
            error("Cannot merge a primitive into a primitive")
        }
    }
}
