package com.copperleaf.json.pointer

import com.copperleaf.json.utils.takeHead
import net.pwall.json.JSON
import net.pwall.json.JSONArray
import net.pwall.json.JSONBoolean
import net.pwall.json.JSONDouble
import net.pwall.json.JSONFloat
import net.pwall.json.JSONInteger
import net.pwall.json.JSONLong
import net.pwall.json.JSONObject
import net.pwall.json.JSONString
import net.pwall.json.JSONValue
import net.pwall.json.pointer.JSONPointer

public sealed interface JsonPointerAction {
    public data class SetValue(val value: JSONValue?) : JsonPointerAction
    public object RemoveValue : JsonPointerAction
    public data class SwapArrayIndices(val to: Int) : JsonPointerAction
}

internal fun JSONObject.getOrCreateSlot(key: String) {
    this.getOrPut(key) { null }
}

internal fun JSONArray.getOrCreateSlot(index: Int) {
    if (index > lastIndex) {
        val slotsToAdd = index - lastIndex
        repeat(slotsToAdd) {
            add(null)
        }
    }
}

public fun JSONValue?.updateWithPointer(
    pointer: JSONPointer,
    action: JsonPointerAction,
): JSONValue? {
    val tokens = pointer.tokens.toList()

    return if (tokens.isEmpty()) {
        check(action is JsonPointerAction.SetValue) { "Action on the root value must by JsonPointerAction.SetValue" }
        action.value
    } else {
        val objectToActOn: JSONValue? = if (this != null) {
            val jsonString = this.toJSON()
            val copyOfOriginal = jsonString.parseJson()
            copyOfOriginal
        } else {
            val firstPointerIsIntLike = tokens.first().toIntOrNull() != null

            if (firstPointerIsIntLike) {
                JSONArray()
            } else {
                JSONObject()
            }
        }

        objectToActOn?.apply { updateWithPointer(tokens, action) }
    }
}

internal fun JSONValue.updateWithPointer(
    tokens: List<String>,
    action: JsonPointerAction,
) {
    if (tokens.isEmpty()) error("unexpected end of JSONPointer update")

    when (this) {
        is JSONObject -> {
            val (nextPointer, tail) = tokens.takeHead()
            updateObjectWithPointer(nextPointer, tail, action)
        }
        is JSONArray -> {
            val (nextArrayIndex, tail) = tokens.takeHead() {
                checkNotNull(it.toIntOrNull()) {
                    "expected an array index, but got '$it'"
                }
            }

            updateArrayWithPointer(nextArrayIndex, tail, action)
        }
        else -> {
            error("unexpected end of JSONPointer update")
        }
    }
}

internal fun JSONArray.updateArrayWithPointer(
    index: Int,
    remainingTokens: List<String>,
    action: JsonPointerAction,
) {
    // expand array to accept this index
    getOrCreateSlot(index)

    if (remainingTokens.isEmpty()) {
        // if there are no more remaining tokens, apply the value directly
        when (action) {
            is JsonPointerAction.SetValue -> {
                this.set(index, action.value)
            }
            is JsonPointerAction.RemoveValue -> {
                this.removeAt(index)
            }
            is JsonPointerAction.SwapArrayIndices -> {
                getOrCreateSlot(action.to)

                val fromValue = this.get(index)
                val toValue = this.get(action.to)

                this.set(action.to, fromValue)
                this.set(index, toValue)
            }
        }
    } else {
        // otherwise, check if the next token refers to an object key or an array index
        val nextLevelValue = this.get(index) ?: run {
            val (next, _) = remainingTokens.takeHead()
            if (next.toIntOrNull() != null) {
                // the next level looks like an array index, interpret it as that and make this value an array
                JSONArray()
            } else {
                // the next level does not look like an array index, interpret it as an object property and make this
                //  value an object
                JSONObject()
            }
        }
        nextLevelValue.updateWithPointer(remainingTokens, action)

        this.set(index, nextLevelValue)
    }
}

internal fun JSONObject.updateObjectWithPointer(
    property: String,
    remainingTokens: List<String>,
    action: JsonPointerAction,
) {
    // ensure object has the given key
    getOrCreateSlot(property)

    if (remainingTokens.isEmpty()) {
        // if there are no more remaining tokens, apply the value directly
        when (action) {
            is JsonPointerAction.SetValue -> {
                this.putJSON(property, action.value)
            }
            is JsonPointerAction.RemoveValue -> {
                this.remove(property)
            }
            is JsonPointerAction.SwapArrayIndices -> {
                error("Cannot swap array indices on an object")
            }
        }
    } else {
        // otherwise, check if the next token refers to an object key or an array index
        val nextLevelValue = this.get(property) ?: run {
            val (next, _) = remainingTokens.takeHead()
            if (next.toIntOrNull() != null) {
                // the next level looks like an array index, interpret it as that and make this value an array
                JSONArray()
            } else {
                // the next level does not look like an array index, interpret it as an object property and make this
                //  value an object
                JSONObject()
            }
        }
        nextLevelValue.updateWithPointer(remainingTokens, action)

        this.putJSON(property, nextLevelValue)
    }
}

public fun Any?.toJsonValue(): JSONValue? {
    return when (this) {
        is String -> JSONString(this)
        is Int -> JSONInteger(this)
        is Long -> JSONLong(this)
        is Float -> JSONFloat(this)
        is Double -> JSONDouble(this)
        is Boolean -> JSONBoolean(this)
        is List<*> -> JSONArray(this.map { it.toJsonValue() })
        is Map<*, *> -> JSONObject(this.mapKeys { it.key.toString() }.mapValues { it.value.toJsonValue() })
        else -> null
    }
}

public fun String?.parseJson(): JSONValue? {
    return JSON.parse(this ?: "null")
}

public fun String.asPointer(): JSONPointer {
    return JSONPointer.fromURIFragment(this)
}

public fun JSONPointer.asPointerString(): String {
    return "#/" + this.tokens.joinToString(separator = "/")
}
