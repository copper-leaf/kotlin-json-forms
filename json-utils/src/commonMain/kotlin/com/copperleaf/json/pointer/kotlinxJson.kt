package com.copperleaf.json.pointer

import com.copperleaf.json.utils.takeHead
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

// Find values
// ---------------------------------------------------------------------------------------------------------------------

public fun JsonElement.find(
    pointer: JsonPointer,
): JsonElement {
    return this.findNext(pointer, pointer.tokens)
}

internal fun JsonElement.findNext(
    pointer: JsonPointer,
    tokens: List<String>,
): JsonElement {
    return when (this) {
        is JsonObject -> findNextInJsonObject(pointer, tokens)
        is JsonArray -> findNextInJsonArray(pointer, tokens)
        is JsonPrimitive -> findNextInJsonPrimitive(pointer, tokens)
    }
}

internal fun JsonObject.findNextInJsonObject(
    pointer: JsonPointer,
    tokens: List<String>,
): JsonElement {
    if (tokens.isEmpty()) return this

    val (head, tail) = tokens.takeHead()
    val next = checkNotNull(this[head]) {
        "Property '$head' could not be found in object"
    }

    return next.findNext(pointer, tail)
}

internal fun JsonArray.findNextInJsonArray(
    pointer: JsonPointer,
    tokens: List<String>,
): JsonElement {
    if (tokens.isEmpty()) return this

    val (head, tail) = tokens.takeHead()
    val index = checkNotNull(head.toIntOrNull()) { "$head should represent an index value in array" }
    val next = checkNotNull(this.getOrNull(index)) { "Item at '$index' could not be found in array" }

    return next.findNext(pointer, tail)
}

internal fun JsonPrimitive.findNextInJsonPrimitive(
    pointer: JsonPointer,
    tokens: List<String>,
): JsonElement {
    if (tokens.isEmpty()) return this

    error("value at pointer '${pointer.toUriFragment()}' could not be found")
}

// Mutate
// ---------------------------------------------------------------------------------------------------------------------

public fun JsonElement.mutate(
    pointer: JsonPointer,
    action: JsonPointerAction,
): JsonElement {
    return this.mutateNext(pointer, pointer.tokens, action)
}

internal fun JsonElement.mutateNext(
    pointer: JsonPointer,
    tokens: List<String>,
    action: JsonPointerAction,
): JsonElement {
    return when (this) {
        is JsonObject -> mutateNextInJsonObject(pointer, tokens, action)
        is JsonArray -> mutateNextInJsonArray(pointer, tokens, action)
        is JsonPrimitive -> mutateNextInJsonPrimitive(pointer, tokens, action)
    }
}

internal fun JsonObject.mutateNextInJsonObject(
    pointer: JsonPointer,
    tokens: List<String>,
    action: JsonPointerAction,
): JsonElement {
    val (head, tail) = tokens.takeHead()
    val updatedObject = this
        .toMutableMap()
        .apply {
            if (tail.isEmpty()) {
                // this is the last piece, apply the appropriate action
                when (action) {
                    is JsonPointerAction.RemoveValue -> {
                        this.remove(head)
                        Unit
                    }
                    is JsonPointerAction.SetValue -> {
                        this[head] = (this[head] ?: JsonNull).mutateNext(pointer, tail, action)
                    }
                    is JsonPointerAction.SwapArrayIndices -> {
                        error("cannot swap array indices on an object")
                    }
                }.also {
                    // enforce exhaustiveness
                }
            } else {
                this[head] = (this[head] ?: JsonNull).mutateNext(pointer, tail, action)
            }
        }
        .toMap()

    return JsonObject(updatedObject)
}

private fun MutableList<JsonElement>.expandToFit(index: Int) {
    for (i in (this.size..index)) {
        add(JsonNull) // add null values until the array is of the proper size
    }
}

internal fun JsonArray.mutateNextInJsonArray(
    pointer: JsonPointer,
    tokens: List<String>,
    action: JsonPointerAction,
): JsonElement {
    val (head, tail) = tokens.takeHead()
    val index = checkNotNull(head.toIntOrNull()) { "$head should represent an index value in array" }
    val updatedArray = this
        .toMutableList()
        .apply {
            if (tail.isEmpty()) {
                // this is the last piece, apply the appropriate action
                when (action) {
                    is JsonPointerAction.RemoveValue -> {
                        expandToFit(index)
                        this.removeAt(index)
                        Unit
                    }
                    is JsonPointerAction.SetValue -> {
                        expandToFit(index)
                        this[index] = this[index].mutateNext(pointer, tail, action)
                    }
                    is JsonPointerAction.SwapArrayIndices -> {
                        val fromIndex = index
                        val toIndex = action.to

                        expandToFit(fromIndex)
                        expandToFit(toIndex)

                        val fromValue = this[fromIndex]
                        val toValue = this[action.to]

                        this[fromIndex] = toValue
                        this[toIndex] = fromValue
                    }
                }.also {
                    // enforce exhaustiveness
                }
            } else {
                expandToFit(index)
                this[index] = this[index].mutateNext(pointer, tail, action)
            }
        }
        .toList()

    return JsonArray(updatedArray)
}

internal fun JsonPrimitive.mutateNextInJsonPrimitive(
    pointer: JsonPointer,
    tokens: List<String>,
    action: JsonPointerAction,
): JsonElement {
    return if (tokens.isEmpty()) {
        when (action) {
            is JsonPointerAction.RemoveValue -> error("Cannot remove value on primitive")
            is JsonPointerAction.SetValue -> action.value.toKotlinxJsonValue()
            is JsonPointerAction.SwapArrayIndices -> error("Cannot swap indices value on primitive")
        }
    } else {
        if (this is JsonNull) {
            // are still have pointers left to go, and we're at a null value. Make a best-guess as to whether the next
            // value should be an object or array, and continue applying the pointer with that guess
            val (head, _) = tokens.takeHead()

            val newValue = if (head.toIntOrNull() != null) {
                JsonArray(emptyList())
            } else {
                JsonObject(emptyMap())
            }
            newValue.mutateNext(pointer, tokens, action)
        } else {
            error("value at pointer '${pointer.toUriFragment()}' could not be found")
        }
    }
}

public fun Any?.toKotlinxJsonValue(): JsonElement {
    return when (this) {
        is JsonElement -> this
        is String -> JsonPrimitive(this)
        is Int -> JsonPrimitive(this)
        is Long -> JsonPrimitive(this)
        is Float -> JsonPrimitive(this)
        is Double -> JsonPrimitive(this)
        is Boolean -> JsonPrimitive(this)
        is List<*> -> JsonArray(this.map { it.toKotlinxJsonValue() })
        is Map<*, *> -> JsonObject(this.mapKeys { it.key.toString() }.mapValues { it.value.toKotlinxJsonValue() })
        null -> JsonNull
        else -> error("$this cannot be converted to a valid JsonElement")
    }
}
