package com.copperleaf.forms.example.web

import com.copperleaf.forms.core.ui.UiSchema
import com.copperleaf.json.schema.JsonSchema
import com.copperleaf.json.utils.parseJson
import com.copperleaf.json.utils.toJsonString
import kotlinx.browser.window
import kotlinx.coroutines.await
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
                val schema = window.fetch("http://localhost:8080/$path/schema.json")
                    .await()
                    .text()
                    .await()
                    .parseJson()
                    .let { JsonSchema.parse(it) }

                val uiSchema = window.fetch("http://localhost:8080/$path/uiSchema.json")
                    .await()
                    .text()
                    .await()
                    .parseJson()
                    .let { UiSchema.parse(schema, it) }

                return schema to uiSchema
            }

            override suspend fun loadInitialData(): JsonElement {
                return allData.getOrPut(path) {
                    window.fetch("http://localhost:8080/$path/data.json")
                        .await()
                        .text()
                        .await()
                }.parseJson()
            }

            override fun saveUpdatedData(data: JsonElement) {
                allData[path] = data.toJsonString()
            }
        }
    }
}
