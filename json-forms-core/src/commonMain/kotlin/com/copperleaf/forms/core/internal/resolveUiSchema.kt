package com.copperleaf.forms.core.internal

import com.copperleaf.forms.core.ui.Rule
import com.copperleaf.forms.core.ui.UiElement
import com.copperleaf.forms.core.ui.UiSchema
import com.copperleaf.json.pointer.AbstractJsonPointer
import com.copperleaf.json.pointer.JsonPointer
import com.copperleaf.json.pointer.current
import com.copperleaf.json.pointer.find
import com.copperleaf.json.pointer.parent
import com.copperleaf.json.schema.JsonSchema
import com.copperleaf.json.utils.SplitTransformer
import com.copperleaf.json.utils.camelCase
import com.copperleaf.json.utils.pascalCase
import com.copperleaf.json.utils.words
import com.copperleaf.json.values.arrayAt
import com.copperleaf.json.values.objectAt
import com.copperleaf.json.values.optional
import com.copperleaf.json.values.string
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

internal fun JsonElement.resolveUiSchema(
    schema: JsonElement
): UiSchema {
    return UiSchema(
        this.resolveUiElement(schema)
    )
}

internal fun JsonElement.resolveUiElement(
    schema: JsonElement
): UiElement {
    val type = string("type")

    return if (type == "Control") {
        resolveAsControl(schema)
    } else {
        resolveAsIntermediateElement(schema)
    }
}

public fun JsonElement.resolveAsControl(
    schema: JsonElement
): UiElement.Control {
    val scope = string("scope")
    val scopeJsonPointer = JsonPointer.parse(scope)

    val schemaConfig = schema.objectAt(scopeJsonPointer)
    val controlType = schemaConfig.string("type")
    val uiSchemaConfig = this

    val rule = resolveRule(schema)

    return UiElement.Control(
        schemaConfig = schemaConfig,
        uiSchemaConfig = uiSchemaConfig,
        controlType = controlType,
        schemaScope = scopeJsonPointer,
        dataScope = staticallyDetermineDataScope(schema, scopeJsonPointer),
        required = isControlRequired(schema, scopeJsonPointer),
        label = label(schema, scopeJsonPointer, uiSchemaConfig, schemaConfig),
        rule = rule,
    )
}

internal fun JsonElement.resolveAsIntermediateElement(
    schema: JsonElement
): UiElement.ElementWithChildren {
    val type = string("type")

    val resolvedChildElements = this.optional { arrayAt("elements") }
        ?.map { it.resolveUiElement(schema) }
        ?: emptyList()

    val rule = resolveRule(schema)

    return UiElement.ElementWithChildren(
        elementType = type,
        uiSchemaConfig = this,
        elements = resolvedChildElements,
        rule = rule
    )
}

/**
 * Returns something that is close to a JSON Pointer, but any array values are replaced by '[]' placeholders, since we
 * cannot know the specific index of array until we bind the form to data at runtime. Those array indices must be
 * replaced by actual index values when rendering the form's `array` controls.
 */
internal fun staticallyDetermineDataScope(
    schema: JsonElement,
    pointer: JsonPointer,
): AbstractJsonPointer {
    val currentSchemaScope = StringBuilder("#")
    val currentDataScope = mutableListOf<String>()

    for (token in pointer.tokens) {
        currentSchemaScope.append('/')
        currentSchemaScope.append(token)

        if (token == "properties") {
            // just skip it
        } else if (token == "items") {
            // get the value at the current schema scope
            val testedPointer = JsonPointer.parse(currentSchemaScope.toString())
            val referencedProperty = schema.objectAt(testedPointer.parent())
            val referencedPropertyType = referencedProperty.string("type")

            when (referencedPropertyType) {
                "object" -> {
                    // it's an object, append the token to the data scope
                    currentDataScope.add(token)
                }

                "array" -> {
                    // it's an array, append the '[]' placeholder to the data scope
                    currentDataScope.add("[]")
                }

                else -> {
                    // assume it's a primitive, append the token to the data scope
                    currentDataScope.add(token)
                }
            }
        } else {
            // get the value at the current schema scope
            val testedPointer = JsonPointer.parse(currentSchemaScope.toString())
            val referencedProperty = schema.objectAt(testedPointer.parent().parent())
            val referencedPropertyType = referencedProperty.string("type")

            when (referencedPropertyType) {
                "object" -> {
                    // it's an object, append the token to the data scope
                    currentDataScope.add(token)
                }

                "array" -> {
                    // it's an array, append the '[]' placeholder to the data scope
                    currentDataScope.add("[]")
                }

                else -> {
                    // assume it's a primitive, append the token to the data scope
                    currentDataScope.add(token)
                }
            }
        }
    }

    return AbstractJsonPointer(currentDataScope)
}

/**
 *
 */
internal fun JsonElement.resolveRule(
    schema: JsonElement
): Rule? {
    check(this is JsonObject) { "Rule definition must be an object" }
    if (!this.containsKey("rule") || this["rule"] == null) return null

    val rule = this.objectAt("rule")

    // get the effect
    val effectString = rule.string("effect")
    val effect = when (effectString.uppercase()) {
        "HIDE" -> Rule.Effect.Hide
        "SHOW" -> Rule.Effect.Show
        "DISABLE" -> Rule.Effect.Disable
        "ENABLE" -> Rule.Effect.Enable
        else -> error("$effectString is not a valid rule condition")
    }

    val condition = rule.objectAt("condition")

    val scope = condition.string("scope")
    val scopeJsonPointer = JsonPointer.parse(scope)
    val dataScope = staticallyDetermineDataScope(schema, scopeJsonPointer)

    val schemaJson = schema.objectAt(scopeJsonPointer)
    val conditionSchemaJson = condition.objectAt("schema")
    val conditionSchema = JsonSchema.parse(conditionSchemaJson)

    return Rule(
        schemaScope = scopeJsonPointer,
        dataScope = dataScope,
        effect = effect,
        schemaJson = schemaJson,
        conditionSchemaJson = conditionSchemaJson,
        conditionSchema = conditionSchema,
    )
}

internal fun isControlRequired(
    schema: JsonElement,
    pointer: JsonPointer
): Boolean {
    val parentObject = if (pointer.tokens.size <= 2) {
        // The parent object will refer to the root, but that cannot be resolved directly via JsonPointer lib. Just
        // access the schema directly
        schema
    } else {
        schema.find(pointer.parent().parent())
    }

    check(parentObject is JsonObject) { "Parent must be an object" }
    val requiredPropertyNames: List<String>? = parentObject
        .optional { arrayAt("required") }
        ?.map { it.jsonPrimitive.content }

    val isCurrentPointerValueRequired = if (requiredPropertyNames == null) {
        false
    } else {
        pointer.current in requiredPropertyNames
    }

    return isCurrentPointerValueRequired
}

@Suppress("UNUSED_PARAMETER")
internal fun label(
    schema: JsonElement,
    pointer: JsonPointer,
    uiSchemaObject: JsonElement,
    schemaObject: JsonObject,
): String {
    return uiSchemaObject
        .jsonObject
        .optional { string("label") }
        ?: schemaObject
            .optional { string("title") }
        ?: SplitTransformer()
            .splitEach { camelCase }
            .transformEach { pascalCase }
            .convertTo(pointer.current ?: "") { words }
}
