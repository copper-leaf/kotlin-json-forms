package com.copperleaf.forms.core

import com.copperleaf.forms.core.vm.FormSavedStateAdapter

class TestFormSavedStateAdapter(
    private val resourcesPath: String,
) : FormSavedStateAdapter.Store {

    var latestSavedValue: String? = null

    override suspend fun loadSchema(): String {
        return getJsonFromResources("$resourcesPath/schema.json")
    }

    override suspend fun loadUiSchema(): String {
        return getJsonFromResources("$resourcesPath/uiSchema.json")
    }

    override suspend fun loadInitialData(): String {
        return getJsonFromResources("$resourcesPath/data.json")
    }

    override suspend fun saveUpdatedData(data: String) {
        latestSavedValue = data
    }
}
