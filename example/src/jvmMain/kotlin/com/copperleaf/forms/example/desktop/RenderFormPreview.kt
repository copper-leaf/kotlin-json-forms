package com.copperleaf.forms.example.desktop

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Checkbox
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.copperleaf.forms.compose.form.Form
import com.copperleaf.forms.compose.ui.LocallyEnabled
import com.copperleaf.forms.core.vm.FormContract
import net.pwall.json.JSONFormat
import org.jetbrains.compose.splitpane.ExperimentalSplitPaneApi
import org.jetbrains.compose.splitpane.VerticalSplitPane
import org.jetbrains.compose.splitpane.rememberSplitPaneState

@OptIn(ExperimentalSplitPaneApi::class)
@Composable
fun RenderFormPreview(
    path: String,
) {
    val coroutineScope = rememberCoroutineScope()
    val vm = remember(coroutineScope, path) { FormViewModelImpl(coroutineScope, path) }
    val vmState by vm.observeStates().collectAsState()

    Box(Modifier.fillMaxSize()) {
        VerticalSplitPane(splitPaneState = rememberSplitPaneState(0.85f)) {
            first(minSize = 64.dp) {
                Column(Modifier.fillMaxSize().padding(16.dp).verticalScroll(rememberScrollState())) {
                    Row(Modifier.fillMaxWidth()) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(
                                checked = vmState.debug,
                                onCheckedChange = {
                                    vm.trySend(FormContract.Inputs.SetDebugMode(it))
                                },
                                enabled = LocallyEnabled.current,
                            )
                            Text("Debug")
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {

                        }
                    }
                    Column(Modifier.fillMaxWidth()) {
                        Form(vm)
                    }
                }
            }
            second(minSize = 64.dp) {
                Column(
                    Modifier
                        .background(MaterialTheme.colors.onSurface.copy(alpha = 0.1f))
                        .fillMaxSize()
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    val jsonFormatter = JSONFormat(0, 4, true)

                    Text(
                        "Original Value",
                        style = MaterialTheme.typography.subtitle1,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    SelectionContainer {
                        Text(jsonFormatter.format(vmState.originalData), fontFamily = FontFamily.Monospace)
                    }
                    Divider(Modifier.padding(bottom = 16.dp))

                    Text(
                        "Updated Value",
                        style = MaterialTheme.typography.subtitle1,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    SelectionContainer {
                        Text(jsonFormatter.format(vmState.updatedData), fontFamily = FontFamily.Monospace)
                    }
                }
            }
        }
    }
}

