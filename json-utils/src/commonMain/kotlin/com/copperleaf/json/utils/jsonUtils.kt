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
