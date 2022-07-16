package com.copperleaf.forms.example.desktop

import com.copperleaf.forms.core.vm.FormSavedStateAdapter
import com.copperleaf.json.utils.parseJson
import com.copperleaf.json.utils.toJsonString
import kotlinx.serialization.json.JsonElement

object PreviewFormDataStore {

    private val allData = mutableMapOf<String, String>()

    fun getStoreAt(path: String): FormSavedStateAdapter.Store {
        return object : FormSavedStateAdapter.Store {
            override suspend fun loadSchema(): JsonElement {
                return PreviewFormDataStore::class.java
                    .getResourceAsStream("$path/schema.json")!!
                    .bufferedReader()
                    .readText()
                    .parseJson()
            }

            override suspend fun loadUiSchema(): JsonElement {
                return PreviewFormDataStore::class.java
                    .getResourceAsStream("$path/uiSchema.json")!!
                    .bufferedReader()
                    .readText()
                    .parseJson()
            }

            override suspend fun loadInitialData(): JsonElement {
                return allData.getOrPut(path) {
                    PreviewFormDataStore::class.java
                        .getResourceAsStream("$path/data.json")!!
                        .bufferedReader()
                        .readText()
                }.parseJson()
            }

            override suspend fun saveUpdatedData(data: JsonElement) {
                allData[path] = data.toJsonString()
            }
        }
    }
}
