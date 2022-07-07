package com.copperleaf.forms.compose.ui

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import net.pwall.json.JSONArray
import net.pwall.json.JSONBoolean
import net.pwall.json.JSONDecimal
import net.pwall.json.JSONDouble
import net.pwall.json.JSONFloat
import net.pwall.json.JSONInteger
import net.pwall.json.JSONLong
import net.pwall.json.JSONObject
import net.pwall.json.JSONString
import net.pwall.json.JSONValue

@Composable
public fun JSONValue.requireElement(propertyName: String): JSONValue? {
    return if (this is JSONObject && this.containsKey(propertyName)) {
        val maybeElementValue = this.getValue(propertyName)
        maybeElementValue
    } else {
        Text("Required element '$propertyName' was missing")
        null
    }
}

@Composable
public fun JSONValue.requireObject(propertyName: String): JSONObject {
    return if (this is JSONObject && this.containsKey(propertyName)) {
        val maybeObjectValue = this.getValue(propertyName)

        if (maybeObjectValue is JSONObject) {
            maybeObjectValue
        } else {
            Text("Required value '$propertyName' must be an object, but was $maybeObjectValue")
            JSONObject(emptyMap())
        }
    } else {
        Text("Required object value '$propertyName' was missing")
        JSONObject(emptyMap())
    }
}

@Composable
public fun JSONValue.requireArray(propertyName: String): JSONArray {
    return if (this is JSONObject && this.containsKey(propertyName)) {
        val maybeArrayValue = this.getValue(propertyName)

        if (maybeArrayValue is JSONArray) {
            maybeArrayValue
        } else {
            Text("Required value '$propertyName' must be an array, but was $maybeArrayValue")
            JSONArray(emptyList())
        }
    } else {
        Text("Required array value '$propertyName' was missing")
        JSONArray(emptyList())
    }
}

@Composable
public fun JSONValue.requireString(propertyName: String): String {
    return requirePrimitiveValueOfType(propertyName, "string", "") { (this as? JSONString)?.value }
}

public fun JSONValue.optionalString(propertyName: String): String? {
    return (this as? JSONObject)?.getString(propertyName)?.takeIf { v: String? -> !v.isNullOrBlank() }
}

@Composable
public fun JSONValue.requireBoolean(propertyName: String): Boolean {
    return requirePrimitiveValueOfType(propertyName, "boolean", false) { (this as? JSONBoolean)?.value }
}

@Composable
public fun JSONValue.requireInt(propertyName: String): Int {
    return requirePrimitiveValueOfType(propertyName, "int", 0) { (this as? JSONInteger)?.value }
}

@Composable
public fun JSONValue.requireDouble(propertyName: String): Double {
    return requirePrimitiveValueOfType(propertyName, "double", 0.0) { (this as? JSONDouble)?.value }
}

@Composable
private inline fun <T> JSONValue.requirePrimitiveValueOfType(
    propertyName: String,
    typeName: String,
    defaultValue: T,
    getTypedValue: JSONValue.() -> T?,
): T {
    return if (this is JSONObject && this.containsKey(propertyName)) {
        val maybePrimitiveValue = this.getValue(propertyName)

        if (maybePrimitiveValue is JSONString || maybePrimitiveValue is JSONInteger || maybePrimitiveValue is JSONDouble || maybePrimitiveValue is JSONDecimal || maybePrimitiveValue is JSONBoolean || maybePrimitiveValue is JSONFloat || maybePrimitiveValue is JSONLong) {
            val value = getTypedValue(maybePrimitiveValue)

            if (value != null) {
                value
            } else {
                Text("Required value '$propertyName' must be a $typeName, but was ${maybePrimitiveValue}")
                defaultValue
            }
        } else {
            Text("Required value '$propertyName' must be a $typeName, but was $maybePrimitiveValue")
            defaultValue
        }
    } else {
        Text("Required $typeName value '$propertyName' was missing")
        defaultValue
    }
}
