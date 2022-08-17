package com.copperleaf.json.schema

import com.copperleaf.json.pointer.JsonPointer
import com.copperleaf.json.utils.asDynamicJson
import kotlinx.serialization.json.JsonElement

public actual class JsonSchema actual constructor(input: JsonElement) {

    private val schema: (dynamic) -> dynamic = try {
        val ajv = Ajv()
        ajvFormats(ajv)
        ajv.compile(input.asDynamicJson())
    } catch (t: Throwable) {
        t.printStackTrace()

        val s: (dynamic) -> dynamic = { _ -> }
        s
    }

    public actual fun validate(element: JsonElement): SchemaValidationResult {
        val actualInput = element.asDynamicJson()
        return try {
            if(schema(actualInput)) {
                SchemaValidationResult.Valid
            } else {
                CustomInvalid()
            }
        } catch (e: Throwable) {
            CustomInvalid()
        }
    }

    private class CustomInvalid : SchemaValidationResult.Invalid() {
        override fun issues(pointer: JsonPointer): List<String> {
            return emptyList()
        }
    }

    public actual companion object {
        public actual fun parse(input: JsonElement): JsonSchema {
            return JsonSchema(input)
        }
    }
}
