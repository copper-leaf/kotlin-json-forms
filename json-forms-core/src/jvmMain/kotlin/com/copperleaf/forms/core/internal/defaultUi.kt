package com.copperleaf.forms.core.internal

import com.copperleaf.forms.core.ui.UiSchema
import net.pwall.json.JSONArray
import net.pwall.json.JSONObject
import net.pwall.json.JSONString
import net.pwall.json.JSONValue

internal fun createDefaultUiSchema(schema: JSONValue): UiSchema {
    return JSONObject(
        mapOf(
            "type" to JSONString("VerticalLayout"),
            "elements" to JSONArray(
                (schema as? JSONObject)
                    ?.getObject("properties")
                    ?.keys
                    ?.map { property ->
                        JSONObject(
                            mapOf(
                                "type" to JSONString("Control"),
                                "scope" to JSONString("#/properties/$property"),
                            )
                        )
                    }
                    ?: emptyList()
            )
        )
    ).resolveUiSchema(schema)
}
