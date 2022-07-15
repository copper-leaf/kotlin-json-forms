package com.copperleaf.forms.example.desktop

import com.copperleaf.forms.core.vm.FormSavedStateAdapter

object PreviewFormDataStore {

    private val allData = mutableMapOf<String, String>()

    fun getStoreAt(path: String): FormSavedStateAdapter.Store {
        return object : FormSavedStateAdapter.Store {
            override suspend fun loadSchema(): String {
                return PreviewFormDataStore::class.java
                    .getResourceAsStream("$path/schema.json")!!
                    .bufferedReader()
                    .readText()
            }

            override suspend fun loadUiSchema(): String {
                return PreviewFormDataStore::class.java
                    .getResourceAsStream("$path/uiSchema.json")!!
                    .bufferedReader()
                    .readText()
            }

            override suspend fun loadInitialData(): String {
                return allData.getOrPut(path) {
                    PreviewFormDataStore::class.java
                        .getResourceAsStream("$path/data.json")!!
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
