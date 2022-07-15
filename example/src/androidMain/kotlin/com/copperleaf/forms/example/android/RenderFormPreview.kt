package com.copperleaf.forms.example.android

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.copperleaf.forms.compose.form.Form
import com.copperleaf.forms.core.vm.BasicFormViewModel
import com.copperleaf.forms.core.vm.FormSavedStateAdapter
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement

@Composable
fun RenderFormPreview(
    path: String,
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current.applicationContext
    val vm = remember(coroutineScope, context, path) {
        BasicFormViewModel(
            coroutineScope,
            FormSavedStateAdapter(
                PreviewFormDataStore.getStoreAt(context, path),
            )
        )
    }
    val vmState by vm.observeStates().collectAsState()

    Column(Modifier.fillMaxSize()) {
        Column(
            Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            Form(vm)
        }
        Column(
            Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colors.onSurface.copy(alpha = 0.1f))
        ) {
            val json = Json { prettyPrint = true }

            Text(
                "Original Value",
                style = MaterialTheme.typography.subtitle1,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            SelectionContainer {
                Text(
                    json.encodeToString(JsonElement.serializer(), vmState.originalData),
                    fontFamily = FontFamily.Monospace
                )
            }
            Divider(Modifier.padding(bottom = 16.dp))

            Text(
                "Updated Value",
                style = MaterialTheme.typography.subtitle1,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            SelectionContainer {
                Text(
                    json.encodeToString(JsonElement.serializer(), vmState.updatedData),
                    fontFamily = FontFamily.Monospace
                )
            }
        }
    }
}
