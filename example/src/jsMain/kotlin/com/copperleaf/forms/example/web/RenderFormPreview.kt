package com.copperleaf.forms.example.web

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.copperleaf.forms.compose.bulma.form.BulmaForm
import com.copperleaf.forms.core.ui.UiSchema
import com.copperleaf.json.schema.JsonSchema
import com.copperleaf.json.utils.toJsonString
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import org.jetbrains.compose.web.dom.Code
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Pre
import org.jetbrains.compose.web.dom.Text

@Composable
fun RenderFormPreview(
    path: String,
) {
    val store = remember(path) {
        PreviewFormDataStore.getStoreAt(path)
    }
    val schemas by produceState<Pair<JsonSchema, UiSchema>?>(null, store) { value = store.loadSchema() }
    val schema: JsonSchema? = schemas?.first
    val uiSchema: UiSchema? = schemas?.second

    val initialData: JsonElement by produceState<JsonElement>(JsonNull, store) { value = store.loadInitialData() }
    var data: JsonElement by remember(initialData) { mutableStateOf(initialData) }

    Div({ classes("column", "is-5") }) {
        if (schema != null && uiSchema != null) {
            BulmaForm(
                schema = schema,
                uiSchema = uiSchema,
                data = data,
                onDataChanged = {
                    println("onDataChanged: ${it.toJsonString(true)}")
                    data = it
                    store.saveUpdatedData(it)
                },
            )
        }
    }
    Div({ classes("column", "is-4") }) {
        Pre {
            Code {
                Text(data.toJsonString(true))
            }
        }
    }
}

