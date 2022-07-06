package com.copperleaf.forms.core.vm

import com.copperleaf.ballast.savedstate.RestoreStateScope
import com.copperleaf.ballast.savedstate.SaveStateScope
import com.copperleaf.ballast.savedstate.SavedStateAdapter
import com.copperleaf.forms.core.internal.createDefaultUiSchema
import com.copperleaf.forms.core.internal.resolveUiSchema
import com.copperleaf.forms.core.ui.UiSchema
import com.copperleaf.json.pointer.parseJson
import net.pwall.json.schema.JSONSchema

public class FormSavedStateAdapter(
    private val prefs: Store,
    private val saveType: FormContract.SaveType = FormContract.SaveType.OnValidChange,
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
            prefs.saveUpdatedData(updatedData?.toJSON() ?: "")
        }
    }

    override suspend fun RestoreStateScope<
        FormContract.Inputs,
        FormContract.Events,
        FormContract.State>.restore(): FormContract.State {
        val schemaJsonString = prefs.schema
        val schemaJson = schemaJsonString.parseJson()!!
        val schema = JSONSchema.parse(schemaJsonString)

        val uiSchemaJson = prefs
            .uiSchema
            .parseJson()

        val uiSchema: UiSchema = try {
            uiSchemaJson
                ?.resolveUiSchema(schemaJson)!!
        } catch (e: Exception) {
            e.printStackTrace()
            createDefaultUiSchema(schemaJson)
        }

        val initialData = prefs.loadInitialData().parseJson()

        return FormContract.State(
            saveType = saveType,

            schemaJson = schemaJson,
            schema = schema,
            uiSchemaJson = uiSchemaJson,
            uiSchema = uiSchema,

            originalData = initialData,
            updatedData = initialData,
        )
    }
}
