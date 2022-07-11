package com.copperleaf.forms.core.vm

import com.copperleaf.ballast.savedstate.RestoreStateScope
import com.copperleaf.ballast.savedstate.SaveStateScope
import com.copperleaf.ballast.savedstate.SavedStateAdapter
import com.copperleaf.forms.core.internal.createDefaultUiSchema
import com.copperleaf.forms.core.internal.resolveUiSchema
import com.copperleaf.forms.core.ui.UiSchema
import com.copperleaf.json.schema.JsonSchema
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull

public class FormSavedStateAdapter(
    private val prefs: Store,
    private val saveType: FormContract.SaveType = FormContract.SaveType.OnValidChange,
    private val json: Json = Json,
) : SavedStateAdapter<
    FormContract.Inputs,
    FormContract.Events,
    FormContract.State> {

    public interface Store {
        public val schema: String
        public val uiSchema: String?

        public suspend fun loadInitialData(): String?
        public suspend fun saveUpdatedData(data: String)
    }

    override suspend fun SaveStateScope<
        FormContract.Inputs,
        FormContract.Events,
        FormContract.State>.save() {
        saveDiff({ originalData }) { updatedData ->
            prefs.saveUpdatedData(json.encodeToString(JsonElement.serializer(), updatedData))
        }
    }

    override suspend fun RestoreStateScope<
        FormContract.Inputs,
        FormContract.Events,
        FormContract.State>.restore(): FormContract.State {
        val schemaJsonString = prefs.schema
        val uiSchemaJsonString = prefs.uiSchema
        val initialDataString = prefs.loadInitialData()

        val schemaJsonElement = json.decodeFromString(JsonElement.serializer(), schemaJsonString)
        val uiSchemaJsonElement = uiSchemaJsonString
            ?.let { json.decodeFromString(JsonElement.serializer(), it) }
            ?: run { createDefaultUiSchema(schemaJsonElement) }
        val initialDataJsonElement = initialDataString?.let { json.decodeFromString(JsonElement.serializer(), it) }

        val schema = JsonSchema.parse(schemaJsonString)
        val uiSchema: UiSchema = uiSchemaJsonElement.resolveUiSchema(schemaJsonElement)

        return FormContract.State(
            saveType = saveType,

            schemaJson = schemaJsonElement,
            schema = schema,
            uiSchemaJson = uiSchemaJsonElement,
            uiSchema = uiSchema,

            originalData = initialDataJsonElement ?: JsonNull,
            updatedData = initialDataJsonElement ?: JsonNull,
        )
    }
}
