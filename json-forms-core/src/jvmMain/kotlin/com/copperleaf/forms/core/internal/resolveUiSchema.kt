package com.copperleaf.forms.core.internal

import com.copperleaf.forms.core.ui.Rule
import com.copperleaf.forms.core.ui.UiElement
import com.copperleaf.forms.core.ui.UiSchema
import com.copperleaf.json.pointer.asPointer
import com.copperleaf.json.pointer.asPointerString
import com.copperleaf.json.pointer.parseJson
import com.copperleaf.json.utils.SplitTransformer
import com.copperleaf.json.utils.camelCase
import com.copperleaf.json.utils.pascalCase
import com.copperleaf.json.utils.words
import net.pwall.json.JSONObject
import net.pwall.json.JSONString
import net.pwall.json.JSONValue
import net.pwall.json.pointer.JSONPointer
import net.pwall.json.schema.JSONSchema
import java.util.Locale

internal fun String.resolveUiSchema(
    schema: JSONValue
): UiSchema {
    val uiSchema = checkNotNull(this.parseJson()) { "UiSchema cannot be null" }
    return UiSchema(
        uiSchema.resolveUiElement(schema)
    )
}

internal fun JSONValue.resolveUiSchema(
    schema: JSONValue
): UiSchema {
    return UiSchema(
        this.resolveUiElement(schema)
    )
}

internal fun JSONValue.resolveUiElement(
    schema: JSONValue
): UiElement {
    check(this is JSONObject) { "UI Element config must be a JSONObject" }
    val type = checkNotNull(this.getString("type")) { "UI element is missing 'type'" }

    return if (type == "Control") {
        resolveAsControl(schema)
    } else {
        resolveAsIntermediateElement(schema)
    }
}

public fun JSONObject.resolveAsControl(
    schema: JSONValue
): UiElement.Control {
    val scope = checkNotNull(this.getString("scope")) { "Control is missing 'scope'" }
    val scopeJsonPointer = scope.asPointer()

    val controlInSchema = scopeJsonPointer.find(schema)
    check(controlInSchema is JSONObject) { "Referenced value at scope '$scope' in schema must be an object" }
    val controlType = checkNotNull(controlInSchema.getString("type")) {
        "Referenced value at scope '$scope' is missing 'type'"
    }

    val schemaConfig = scopeJsonPointer.find(schema)
    val uiSchemaConfig = this
    check(schemaConfig is JSONObject) { "Schema config must be an object" }

    val rule = resolveRule(schema)

    return UiElement.Control(
        schemaConfig = schemaConfig,
        uiSchemaConfig = uiSchemaConfig,
        controlType = controlType,
        schemaScope = scopeJsonPointer.asPointerString(),
        dataScope = staticallyDetermineDataScope(schema, scopeJsonPointer),
        required = isControlRequired(schema, scopeJsonPointer),
        label = label(schema, scopeJsonPointer, uiSchemaConfig),
        rule = rule,
    )
}

internal fun JSONObject.resolveAsIntermediateElement(
    schema: JSONValue
): UiElement.ElementWithChildren {
    val type = checkNotNull(this.getString("type")) { "UI element is missing 'type'" }

    val resolvedChildElements = getArray("elements")?.map { it.resolveUiElement(schema) } ?: emptyList()

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
    schema: JSONValue,
    pointer: JSONPointer,
): String {
    val currentSchemaScope = StringBuilder("#")
    val currentDataScope = StringBuilder("#")

    for (token in pointer.tokens.toList()) {
        currentSchemaScope.append('/')
        currentSchemaScope.append(token)

        if (token != "properties") {
            // get the value at the current schema scope
            val testedPointer = currentSchemaScope.toString().asPointer()
            val referencedProperty = testedPointer.parent().parent().find(schema)
            check(referencedProperty is JSONObject) {
                "Referenced value at $currentSchemaScope must be an object"
            }
            val referencedPropertyType = checkNotNull(referencedProperty.getString("type")) {
                "Referenced value at $currentSchemaScope has no type"
            }

            when (referencedPropertyType) {
                "object" -> {
                    // it's an object, append the token to the data scope
                    currentDataScope.append('/')
                    currentDataScope.append(token)
                }
                "array" -> {
                    // it's an array, append the '[]' placeholder to the data scope
                    currentDataScope.append("/[]")
                }
                else -> {
                    // assume it's a primitive, append the token to the data scope
                    currentDataScope.append('/')
                    currentDataScope.append(token)
                }
            }
        }
    }

    return currentDataScope.toString()
}

/**
 *
 */
internal fun JSONObject.resolveRule(
    schema: JSONValue
): Rule? {
    if (!this.containsKey("rule") || this.get("rule") == null) return null

    val rule = this.getObject("rule")

    // get the effect
    val effectString = checkNotNull(rule.getString("effect")) { "Rule must have an effect" }
    val effect = when (effectString.uppercase(Locale.getDefault())) {
        "HIDE" -> Rule.Effect.Hide
        "SHOW" -> Rule.Effect.Show
        "DISABLE" -> Rule.Effect.Disable
        "ENABLE" -> Rule.Effect.Enable
        else -> error("$effectString is not a valid rule condition")
    }

    val condition = checkNotNull(rule.getObject("condition")) { "Rule must have a condition" }

    val scope = checkNotNull(condition.getString("scope")) { "Control is missing 'scope'" }
    val scopeJsonPointer = scope.asPointer()
    val dataScope = staticallyDetermineDataScope(schema, scopeJsonPointer)

    val schemaJson = scopeJsonPointer.find(schema)
    check(schemaJson is JSONObject) { "Schema config must be an object" }

    val conditionSchemaJson = condition.getObject("schema")
    val conditionSchema = JSONSchema.parse(conditionSchemaJson.toJSON())

    return Rule(
        schemaScope = scopeJsonPointer.asPointerString(),
        dataScope = dataScope,
        effect = effect,
        schemaJson = schemaJson,
        conditionSchemaJson = conditionSchemaJson,
        conditionSchema = conditionSchema,
    )
}

internal fun isControlRequired(
    schema: JSONValue,
    pointer: JSONPointer
): Boolean {
    val parentObject = if (pointer.tokens.size <= 2) {
        // The parent object will refer to the root, but that cannot be resolved directly via JSONPointer lib. Just
        // access the schema directly
        schema
    } else {
        pointer.parent().parent().find(schema)
    }

    check(parentObject is JSONObject) { "Parent must be an object" }
    val requiredPropertyNames: List<String>? = parentObject.getArray("required")?.map {
        check(it is JSONString) { "Value in 'required' list is not a string: $it" }
        it.value
    }

    val isCurrentPointerValueRequired = if (requiredPropertyNames == null) {
        false
    } else {
        pointer.current in requiredPropertyNames
    }

    return isCurrentPointerValueRequired
}

internal fun label(
    schema: JSONValue,
    pointer: JSONPointer,
    uiSchema: JSONObject,
): String {
    return uiSchema
        .getString("label")
        ?: SplitTransformer()
            .splitEach { camelCase }
            .transformEach { pascalCase }
            .convertTo(pointer.current) { words }

}
