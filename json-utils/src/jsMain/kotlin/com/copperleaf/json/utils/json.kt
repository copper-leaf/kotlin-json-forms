package com.copperleaf.json.utils

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement

public fun JsonElement.asDynamicJson(json: Json = Json) : dynamic {
    return JSON.parse(this.toJsonString(json))
}
