package com.copperleaf.forms.example.web

import com.copperleaf.forms.core.vm.FormSavedStateAdapter
import com.copperleaf.json.utils.parseJson
import com.copperleaf.json.utils.toJsonString
import kotlinx.browser.window
import kotlinx.coroutines.await
import kotlinx.serialization.json.JsonElement

object PreviewFormDataStore {

    private val allData = mutableMapOf<String, String>()

    fun getStoreAt(path: String): FormSavedStateAdapter.Store {
        return object : FormSavedStateAdapter.Store {
            override suspend fun loadSchema(): JsonElement {
                return window.fetch("http://localhost:8080/$path/schema.json")
                    .await()
                    .text()
                    .await()
                    .parseJson()
            }

            override suspend fun loadUiSchema(): JsonElement {
                return window.fetch("http://localhost:8080/$path/uiSchema.json")
                    .await()
                    .text()
                    .await()
                    .parseJson()
            }

            override suspend fun loadInitialData(): JsonElement {
                return allData.getOrPut(path) {
                    window.fetch("http://localhost:8080/$path/data.json")
                        .await()
                        .text()
                        .await()
                }.parseJson()
            }

            override suspend fun saveUpdatedData(data: JsonElement) {
                allData[path] = data.toJsonString()
            }
        }
    }
}
