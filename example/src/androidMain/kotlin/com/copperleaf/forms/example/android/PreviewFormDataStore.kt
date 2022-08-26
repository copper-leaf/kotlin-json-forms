package com.copperleaf.forms.example.android

import android.content.Context
import com.copperleaf.forms.core.ui.UiSchema
import com.copperleaf.json.schema.JsonSchema
import com.copperleaf.json.utils.parseJson
import com.copperleaf.json.utils.toJsonString
import kotlinx.serialization.json.JsonElement

object PreviewFormDataStore {

    interface Store {
        public suspend fun loadSchema(): Pair<JsonSchema, UiSchema>
        public suspend fun loadInitialData(): JsonElement
        public fun saveUpdatedData(data: JsonElement)
    }

    private val allData = mutableMapOf<String, String>()

    fun getStoreAt(context: Context, path: String): Store {
        return object : Store {
            override suspend fun loadSchema(): Pair<JsonSchema, UiSchema> {
                val schema = context.assets.open("$path/schema.json".trimStart('/'))
                    .bufferedReader()
                    .readText()
                    .parseJson()
                    .let { JsonSchema.parse(it) }

                val uiSchema = context.assets.open("$path/uiSchema.json".trimStart('/'))
                    .bufferedReader()
                    .readText()
                    .parseJson()
                    .let { UiSchema.parse(schema, it) }

                return schema to uiSchema
            }

            override suspend fun loadInitialData(): JsonElement {
                return allData.getOrPut(path) {
                    context.assets.open("$path/data.json".trimStart('/'))
                        .bufferedReader()
                        .readText()
                }.parseJson()
            }

            override fun saveUpdatedData(data: JsonElement) {
                allData[path] = data.toJsonString()
            }
        }
    }
}
