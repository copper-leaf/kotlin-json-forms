package com.copperleaf.json.utils

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement

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
