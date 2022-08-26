package com.copperleaf.forms.example.desktop

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

    fun getStoreAt(path: String): Store {
        return object : Store {
            override suspend fun loadSchema(): Pair<JsonSchema, UiSchema> {
                val schema = PreviewFormDataStore::class.java
                    .getResourceAsStream("$path/schema.json")!!
                    .bufferedReader()
                    .readText()
                    .parseJson()
                    .let { JsonSchema.parse(it) }

                val uiSchema = PreviewFormDataStore::class.java
                    .getResourceAsStream("$path/uiSchema.json")!!
                    .bufferedReader()
                    .readText()
                    .parseJson()
                    .let { UiSchema.parse(schema, it) }

                return schema to uiSchema
            }

            override suspend fun loadInitialData(): JsonElement {
                return allData.getOrPut(path) {
                    PreviewFormDataStore::class.java
                        .getResourceAsStream("$path/data.json")!!
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
