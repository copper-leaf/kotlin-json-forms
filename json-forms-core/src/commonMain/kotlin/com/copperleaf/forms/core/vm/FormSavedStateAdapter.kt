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
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull

public class FormSavedStateAdapter(
    private val store: Store,
    private val initialState: () -> FormContract.State = { FormContract.State() },
) : SavedStateAdapter<
    FormContract.Inputs,
    FormContract.Events,
    FormContract.State> {

    public interface Store {
        public suspend fun loadSchema(): JsonElement
        public suspend fun loadUiSchema(): JsonElement?

        public suspend fun loadInitialData(): JsonElement?
        public suspend fun saveUpdatedData(data: JsonElement)
    }

    override suspend fun SaveStateScope<
        FormContract.Inputs,
        FormContract.Events,
        FormContract.State>.save() {
        saveDiff({ originalData }) {
            store.saveUpdatedData(it)
        }
    }

    override suspend fun RestoreStateScope<
        FormContract.Inputs,
        FormContract.Events,
        FormContract.State>.restore(): FormContract.State = coroutineScope {
        println("restoring form state")
        val schemaJsonStringAsync = async { store.loadSchema() }
        val uiSchemaJsonStringAsync = async { store.loadUiSchema() }
        val initialDataStringAsync = async { store.loadInitialData() }

        val schemaJsonElement = schemaJsonStringAsync.await()
        val uiSchemaJsonElement = uiSchemaJsonStringAsync.await() ?: createDefaultUiSchema(schemaJsonElement)
        val initialDataJsonElement = initialDataStringAsync.await()

        val schema = JsonSchema.parse(schemaJsonElement)
        val uiSchema: UiSchema = uiSchemaJsonElement.resolveUiSchema(schema)

        initialState().copy(
            schema = schema,
            uiSchema = uiSchema,

            originalData = initialDataJsonElement ?: JsonNull,
            updatedData = initialDataJsonElement ?: JsonNull,
        )
    }
}
