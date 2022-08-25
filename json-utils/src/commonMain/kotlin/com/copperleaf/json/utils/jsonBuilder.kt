package com.copperleaf.json.utils

import com.copperleaf.json.pointer.toKotlinxJsonValue
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
