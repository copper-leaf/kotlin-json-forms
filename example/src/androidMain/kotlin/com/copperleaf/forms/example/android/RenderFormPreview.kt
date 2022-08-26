package com.copperleaf.forms.example.android

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.copperleaf.forms.compose.form.MaterialForm
import com.copperleaf.forms.core.ui.UiSchema
import com.copperleaf.json.schema.JsonSchema
import com.copperleaf.json.utils.toJsonString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull

@Composable
fun RenderFormPreview(
    path: String,
) {
    val context = LocalContext.current
    val store = remember(context, path) {
        PreviewFormDataStore.getStoreAt(context, path)
    }
    val schemas by produceState<Pair<JsonSchema, UiSchema>?>(null, store) { value = store.loadSchema() }
    val schema: JsonSchema? = schemas?.first
    val uiSchema: UiSchema? = schemas?.second

    val initialData: JsonElement by produceState<JsonElement>(JsonNull, store) { value = store.loadInitialData() }
    var data: JsonElement by remember(initialData) { mutableStateOf(initialData) }

    Column(Modifier.fillMaxSize()) {
        Column(
            Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            if (schema != null && uiSchema != null) {
                MaterialForm(
                    schema = schema,
                    uiSchema = uiSchema,
                    data = data,
                    onDataChanged = {
                        println("data changed: ${it.toJsonString(true)}")
                        data = it
                        store.saveUpdatedData(it)
                    },
                )
            }
        }
        Column(
            Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colors.onSurface.copy(alpha = 0.1f))
        ) {
            val json = Json { prettyPrint = true }

            Text(
                "Updated Value",
                style = MaterialTheme.typography.subtitle1,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            SelectionContainer {
                Text(
                    data.toJsonString(json),
                    fontFamily = FontFamily.Monospace
                )
            }
        }
    }
}
