package com.copperleaf.forms.example.android

import android.content.Context
import com.copperleaf.forms.core.vm.FormSavedStateAdapter

object PreviewFormDataStore {

    private val allData = mutableMapOf<String, String>()

    fun getStoreAt(context: Context, path: String): FormSavedStateAdapter.Store {
        return object : FormSavedStateAdapter.Store {
            override val schema: String
                get() {
                    return context.assets.open("$path/schema.json".trimStart('/'))
                        .bufferedReader()
                        .readText()
                }

            override val uiSchema: String
                get() {
                    return context.assets.open("$path/uiSchema.json".trimStart('/'))
                        .bufferedReader()
                        .readText()
                }

            override suspend fun loadInitialData(): String {
                return allData.getOrPut(path) {
                    context.assets.open("$path/data.json".trimStart('/'))
                        .bufferedReader()
                        .readText()
                }
            }

            override suspend fun saveUpdatedData(data: String) {
                allData[path] = data
            }
        }
    }
}
