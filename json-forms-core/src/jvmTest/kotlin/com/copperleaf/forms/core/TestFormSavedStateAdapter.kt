package com.copperleaf.forms.core

import com.copperleaf.forms.core.vm.FormSavedStateAdapter

class TestFormSavedStateAdapter(
    private val resourcesPath: String,
) : FormSavedStateAdapter.Store {

    var latestSavedValue: String? = null

    override val schema: String by lazy {
        getJsonFromResources("$resourcesPath/schema.json")
    }

    override val uiSchema: String by lazy {
        getJsonFromResources("$resourcesPath/uiSchema.json")
    }

    override suspend fun loadInitialData(): String {
        return getJsonFromResources("$resourcesPath/data.json")
    }

    override suspend fun saveUpdatedData(data: String) {
        latestSavedValue = data
    }
}
