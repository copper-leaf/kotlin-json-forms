package com.copperleaf.json.schema

import com.copperleaf.json.pointer.JsonPointer
import com.copperleaf.json.pointer.toUriFragment
import com.copperleaf.json.utils.toJsonString
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.boolean
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.double
import kotlinx.serialization.json.doubleOrNull
import kotlinx.serialization.json.int
import kotlinx.serialization.json.intOrNull
import net.pwall.json.JSONArray
import net.pwall.json.JSONBoolean
import net.pwall.json.JSONDecimal
import net.pwall.json.JSONInteger
import net.pwall.json.JSONObject
import net.pwall.json.JSONString
import net.pwall.json.JSONValue
import net.pwall.json.schema.JSONSchema
import net.pwall.json.schema.output.BasicOutput
import net.pwall.json.schema.output.DetailedOutput
import net.pwall.json.schema.output.Output

public actual class JsonSchema actual constructor(input: JsonElement) {

    private val schema = JSONSchema.parse(input.toJsonString())

    public actual fun validate(element: JsonElement): SchemaValidationResult {
        return schema
            .validateDetailed(element.toJsonSchemaValue())
            .evaluateResults()
    }

    private fun DetailedOutput.evaluateResults(): SchemaValidationResult {
        return if (this.valid) {
            SchemaValidationResult.Valid
        } else {
            CustomInvalid(this)
        }
    }

    private class CustomInvalid(private val output: DetailedOutput) : SchemaValidationResult.Invalid() {
        override fun issues(pointer: JsonPointer): List<String> {
            if (output.valid) return emptyList() // short-circuit for performance

            return buildList {
                output.getValidationErrorMessages(pointer.toUriFragment(), this)
            }
        }

        private fun Output.getValidationErrorMessages(
            pointer: String,
            listBuilder: MutableList<String>,
        ) {
            when (this) {
                is BasicOutput -> this.getBasicValidationErrorMessages(pointer, listBuilder)
                is DetailedOutput -> this.getDetailedValidationErrorMessages(pointer, listBuilder)
                else -> {}
            }
        }

        private fun DetailedOutput.getDetailedValidationErrorMessages(
            pointer: String,
            listBuilder: MutableList<String>,
        ) {
            if (this.valid) return // short-circuit for performance

            if (this.instanceLocation == pointer && this.errors.isNullOrEmpty()) {
                // we are a leaf output node, which has the actual validation message we need
                listBuilder.add(this.error ?: "")
            } else {
                this.errors?.forEach {
                    it.getValidationErrorMessages(pointer, listBuilder)
                }
            }
        }

        @Suppress("UNUSED_PARAMETER")
        private fun BasicOutput.getBasicValidationErrorMessages(
            pointer: String,
            listBuilder: MutableList<String>,
        ) {
            if (this.valid) return // short-circuit for performance
        }
    }

    private fun JsonElement.toJsonSchemaValue(): JSONValue? {
        return when (this) {
            is JsonPrimitive -> {
                if (this.isString) {
                    JSONString(content)
                } else if (this.booleanOrNull != null) {
                    JSONBoolean(boolean)
                } else if (this.intOrNull != null) {
                    JSONInteger(int)
                } else if (this.doubleOrNull != null) {
                    JSONDecimal(double.toBigDecimal())
                } else {
                    JSONString(content)
                }
            }
            is JsonNull -> null
            is JsonObject -> {
                JSONObject(this.mapValues { it.value.toJsonSchemaValue() })
            }
            is JsonArray -> {
                JSONArray(this.map { it.toJsonSchemaValue() })
            }
        }
    }

    public actual companion object {
        public actual fun parse(input: JsonElement): JsonSchema {
            return JsonSchema(input)
        }
    }
}
