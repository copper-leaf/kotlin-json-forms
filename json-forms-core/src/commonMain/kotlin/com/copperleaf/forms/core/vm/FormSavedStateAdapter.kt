package com.copperleaf.forms.core.vm

import com.copperleaf.ballast.savedstate.RestoreStateScope
import com.copperleaf.ballast.savedstate.SaveStateScope
import com.copperleaf.ballast.savedstate.SavedStateAdapter
import com.copperleaf.forms.core.internal.createDefaultUiSchema
import com.copperleaf.forms.core.internal.resolveUiSchema
import com.copperleaf.forms.core.ui.UiSchema
import com.copperleaf.json.schema.JsonSchema
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull

public class FormSavedStateAdapter(
    private val store: Store,
    private val initialState: () -> FormContract.State = { FormContract.State() },
    private val json: Json = Json,
) : SavedStateAdapter<
    FormContract.Inputs,
    FormContract.Events,
    FormContract.State> {

    public interface Store {
        public suspend fun loadSchema(): String
        public suspend fun loadUiSchema(): String?

        public suspend fun loadInitialData(): String?
        public suspend fun saveUpdatedData(data: String)
    }

    override suspend fun SaveStateScope<
        FormContract.Inputs,
        FormContract.Events,
        FormContract.State>.save() {
        saveDiff({ originalData }) { updatedData ->
            store.saveUpdatedData(json.encodeToString(JsonElement.serializer(), updatedData))
        }
    }

    override suspend fun RestoreStateScope<
        FormContract.Inputs,
        FormContract.Events,
        FormContract.State>.restore(): FormContract.State = coroutineScope {
        val schemaJsonStringAsync = async { store.loadSchema() }
        val uiSchemaJsonStringAsync = async { store.loadUiSchema() }
        val initialDataStringAsync = async { store.loadInitialData() }

        val schemaJsonString = schemaJsonStringAsync.await()
        val uiSchemaJsonString = uiSchemaJsonStringAsync.await()
        val initialDataString = initialDataStringAsync.await()

        val schemaJsonElement = json.decodeFromString(JsonElement.serializer(), schemaJsonString)
        val uiSchemaJsonElement = uiSchemaJsonString
            ?.let { json.decodeFromString(JsonElement.serializer(), it) }
            ?: run { createDefaultUiSchema(schemaJsonElement) }
        val initialDataJsonElement = initialDataString?.let { json.decodeFromString(JsonElement.serializer(), it) }

        val schema = JsonSchema.parse(schemaJsonString)
        val uiSchema: UiSchema = uiSchemaJsonElement.resolveUiSchema(schemaJsonElement)

        initialState().copy(
            schemaJson = schemaJsonElement,
            schema = schema,
            uiSchemaJson = uiSchemaJsonElement,
            uiSchema = uiSchema,

            originalData = initialDataJsonElement ?: JsonNull,
            updatedData = initialDataJsonElement ?: JsonNull,
        )
    }
}
