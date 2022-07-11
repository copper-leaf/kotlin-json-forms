package com.copperleaf.forms.compose.controls

import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.copperleaf.forms.compose.ui.LocalArrayIndices
import com.copperleaf.forms.compose.ui.LocalFormConfig
import com.copperleaf.forms.compose.ui.LocalViewModel
import com.copperleaf.forms.compose.ui.LocallyEnabled
import com.copperleaf.forms.core.ui.UiElement
import com.copperleaf.json.pointer.find
import com.copperleaf.json.pointer.reifyPointer
import com.copperleaf.json.pointer.toUriFragment
import kotlinx.serialization.json.JsonNull

@Composable
public fun ControlLayout(controlElement: UiElement.Control) {
    Column {
        val controlRenderer = LocalFormConfig.current.getControl(controlElement)

        if (controlRenderer != null) {
            val localArrayIndices = LocalArrayIndices.current
            val locallyEnabled = LocallyEnabled.current

            val vm = LocalViewModel.current
            val vmState by vm.observeStates().collectAsState()

            val controlScope by remember(controlElement, vmState, localArrayIndices, locallyEnabled) {
                derivedStateOf {
                    val currentDataPointer = controlElement.dataScope.reifyPointer(localArrayIndices)
                    val currentSchemaPointer = controlElement.schemaScope
                    val validationErrors = vmState.errors(currentDataPointer)
                    val currentValue = runCatching {
                        vmState.updatedData.find(currentDataPointer)
                    }.getOrDefault(JsonNull)

                    ControlScope(
                        vm = vm,
                        vmState = vmState,

                        control = controlElement,
                        dataPointer = currentDataPointer,
                        schemaPointer = currentSchemaPointer,

                        isValid = validationErrors.isEmpty(),
                        validationErrors = validationErrors,

                        isEnabled = locallyEnabled,
                        currentValue = currentValue,
                    )
                }
            }

            controlRenderer(controlScope)
            if (!controlScope.isValid) {
                controlScope.validationErrors.forEach { Text(it, color = MaterialTheme.colors.error) }
            }
            if (vmState.debug) {
                with(controlScope) {
                    Text("schema scope: ${schemaPointer.toUriFragment()}")
                    Text("data scope: ${dataPointer.toUriFragment()}")
                    Text("type: ${control.controlType}")
                    Text("Required: ${control.required}")
                    Text("Has Rule?: ${control.rule != null}")
                }
            }
        } else {
            Text("No UI control with type '${controlElement.controlType}' could be found.")
        }
    }
}
