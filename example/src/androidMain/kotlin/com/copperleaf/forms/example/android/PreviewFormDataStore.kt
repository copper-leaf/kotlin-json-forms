package com.copperleaf.forms.example.android

import android.content.Context
import com.copperleaf.forms.core.vm.FormSavedStateAdapter
import com.copperleaf.json.utils.parseJson
import com.copperleaf.json.utils.toJsonString
import kotlinx.serialization.json.JsonElement

object PreviewFormDataStore {

    private val allData = mutableMapOf<String, String>()

    fun getStoreAt(context: Context, path: String): FormSavedStateAdapter.Store {
        return object : FormSavedStateAdapter.Store {
            override suspend fun loadSchema(): JsonElement {
                return context.assets.open("$path/schema.json".trimStart('/'))
                    .bufferedReader()
                    .readText()
                    .parseJson()
            }

            override suspend fun loadUiSchema(): JsonElement {
                return context.assets.open("$path/uiSchema.json".trimStart('/'))
                    .bufferedReader()
                    .readText()
                    .parseJson()
            }

            override suspend fun loadInitialData(): JsonElement {
                return allData.getOrPut(path) {
                    context.assets.open("$path/data.json".trimStart('/'))
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
