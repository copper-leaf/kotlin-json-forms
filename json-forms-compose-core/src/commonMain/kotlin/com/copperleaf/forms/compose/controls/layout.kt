package com.copperleaf.forms.compose.controls

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.copperleaf.forms.compose.LocalArrayIndices
import com.copperleaf.forms.compose.LocalDesignSystem
import com.copperleaf.forms.compose.LocalFormConfig
import com.copperleaf.forms.compose.LocalViewModel
import com.copperleaf.forms.compose.LocallyEnabled
import com.copperleaf.forms.core.ui.UiElement
import com.copperleaf.json.pointer.find
import com.copperleaf.json.pointer.reifyPointer
import com.copperleaf.json.pointer.toUriFragment
import kotlinx.serialization.json.JsonNull

@Composable
public fun ControlLayout(controlElement: UiElement.Control) {
    val designSystem = LocalDesignSystem.current
    designSystem.column {
        val controlRenderer = LocalFormConfig.current.getControl(controlElement)
        if (controlRenderer != null) {
            val localArrayIndices = LocalArrayIndices.current
            val locallyEnabled = LocallyEnabled.current

            val vm = LocalViewModel.current
            val vmState by vm.observeStates().collectAsState()

            val controlScope = remember(controlElement, vmState, localArrayIndices, locallyEnabled) {
                val currentDataPointer = controlElement.dataScope.reifyPointer(localArrayIndices)
                val currentSchemaPointer = controlElement.schemaScope
                val validationErrors = vmState.errors(currentDataPointer)
                val originalValue = runCatching {
                    vmState.originalData.find(currentDataPointer)
                }.getOrDefault(JsonNull)
                val currentValue = runCatching {
                    vmState.updatedData.find(currentDataPointer)
                }.getOrDefault(JsonNull)
                val isTouched = currentDataPointer in vmState.touchedProperties
                val isChanged = originalValue != currentValue

                ControlScope(
                    vm = vm,
                    designSystem = designSystem,
                    vmState = vmState,

                    control = controlElement,
                    dataPointer = currentDataPointer,
                    schemaPointer = currentSchemaPointer,

                    isValid = validationErrors.isEmpty(),
                    validationErrors = validationErrors,

                    isTouched = isTouched,
                    isChanged = isChanged,

                    isEnabled = locallyEnabled,
                    currentValue = currentValue,
                )
            }



            controlRenderer(controlScope)
            if (!controlScope.isValid) {
                controlScope.validationErrors.forEach { designSystem.text(it, isError = true) }
            }
            if (vmState.debug) {
                with(controlScope) {
                    text("schema scope: ${schemaPointer.toUriFragment()}")
                    text("data scope: ${dataPointer.toUriFragment()}")
                    text("type: ${control.controlType}")
                    text("Required: ${control.required}")
                    text("Has Rule?: ${control.rule != null}")
                    text("touched?: $isTouched")
                    text("changed?: $isChanged")
                }
            }
        } else {
            designSystem.text("No UI control with type '${controlElement.controlType}' could be found.", isError = true)
        }
    }
}
