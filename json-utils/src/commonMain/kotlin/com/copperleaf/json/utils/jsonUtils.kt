package com.copperleaf.json.utils

import com.copperleaf.json.pointer.toKotlinxJsonValue
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

// JSON Builder DSL
// ---------------------------------------------------------------------------------------------------------------------

@DslMarker
public annotation class JsonBuilderDsl

@JsonBuilderDsl
public class JsonObjectBuilder {
    private val content = mutableMapOf<String, JsonElement>()

    public infix fun String.to(element: JsonElement) {
        content[this] = element
    }

    public infix fun String.to(objectBuilder: JsonObjectBuilder.() -> Unit) {
        content[this] = json(objectBuilder)
    }

    public operator fun String.get(vararg values: Any?) {
        content[this] = JsonArray(values.map { it.toKotlinxJsonValue() })
    }

    public infix fun String.to(element: String) {
        content[this] = JsonPrimitive(element)
    }

    public infix fun String.to(element: Number) {
        content[this] = JsonPrimitive(element)
    }

    public infix fun String.to(element: Boolean) {
        content[this] = JsonPrimitive(element)
    }

    public infix fun String.to(elements: List<JsonElement>) {
        content[this] = JsonArray(elements)
    }

    public fun build(): JsonObject {
        return JsonObject(content.toMap())
    }
}

public fun json(builder: JsonObjectBuilder.() -> Unit): JsonObject {
    return JsonObjectBuilder().apply(builder).build()
}

public fun jsonArray(vararg values: Any?): JsonArray {
    return JsonArray(values.map { it.toKotlinxJsonValue() })
}

public operator fun JsonArray.Companion.get(vararg values: Any?): JsonArray {
    return JsonArray(values.map { it.toKotlinxJsonValue() })
}

// Parse and serialize JSON strings
// ---------------------------------------------------------------------------------------------------------------------

public fun String.parseJson(json: Json = Json): JsonElement {
    return json.decodeFromString(JsonElement.serializer(), this)
}

public fun JsonElement.toJsonString(json: Json = Json): String {
    return json.encodeToString(JsonElement.serializer(), this)
}

public fun String.parseJson(prettyPrint: Boolean): JsonElement {
    return this.parseJson(Json { this@Json.prettyPrint = prettyPrint })
}

public fun JsonElement.toJsonString(prettyPrint: Boolean): String {
    return this.toJsonString(Json { this@Json.prettyPrint = prettyPrint })
}

// Merge JSON elements
// ---------------------------------------------------------------------------------------------------------------------

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
            error("Cannot merge a primitive into an object")
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
            error("Cannot merge an object into a primitive")
        }

        is JsonArray -> {
            error("Cannot merge an array into a primitive")
        }

        is JsonPrimitive -> {
            error("Cannot merge a primitive into a primitive")
        }
    }
}
