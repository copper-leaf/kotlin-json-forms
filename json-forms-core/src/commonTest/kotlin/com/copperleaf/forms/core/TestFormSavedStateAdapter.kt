package com.copperleaf.forms.core

import com.copperleaf.forms.core.vm.FormSavedStateAdapter
import com.copperleaf.json.utils.parseJson
import com.copperleaf.json.utils.toJsonString
import kotlinx.serialization.json.JsonElement

class TestFormSavedStateAdapter(
    private val resourcesPath: String,
) : FormSavedStateAdapter.Store {

    var latestSavedValue: String? = null

    override suspend fun loadSchema(): JsonElement {
        return getJsonFromResources("$resourcesPath/schema.json")
            .parseJson()
    }

    override suspend fun loadUiSchema(): JsonElement {
        return getJsonFromResources("$resourcesPath/uiSchema.json")
            .parseJson()
    }

    override suspend fun loadInitialData(): JsonElement {
        return getJsonFromResources("$resourcesPath/data.json")
            .parseJson()
    }

    override suspend fun saveUpdatedData(data: JsonElement) {
        latestSavedValue = data.toJsonString()
    }
}
