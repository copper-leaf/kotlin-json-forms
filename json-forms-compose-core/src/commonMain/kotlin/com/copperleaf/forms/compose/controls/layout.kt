package com.copperleaf.forms.compose.controls

import androidx.compose.runtime.Composable
import com.copperleaf.forms.compose.LocalArrayIndices
import com.copperleaf.forms.compose.LocalDesignSystem
import com.copperleaf.forms.compose.LocalFormConfig
import com.copperleaf.forms.compose.LocallyEnabled
import com.copperleaf.forms.core.ui.UiElement
import com.copperleaf.forms.core.vm.FormContractLite
import com.copperleaf.forms.core.vm.FormFieldsState
import com.copperleaf.json.pointer.find
import com.copperleaf.json.pointer.reifyPointer
import kotlinx.serialization.json.JsonNull

@Composable
public fun ControlLayout(
    controlElement: UiElement.Control,
    vmState: FormFieldsState,
    postInput: (FormContractLite.Inputs)->Unit,
) {
    val designSystem = LocalDesignSystem.current
    designSystem.column {
        val controlRenderer = LocalFormConfig.current.getControl(controlElement)
        if (controlRenderer != null) {
            val localArrayIndices = LocalArrayIndices.current
            val locallyEnabled = LocallyEnabled.current

            val controlScope = run {
                val currentDataPointer = controlElement.dataScope.reifyPointer(localArrayIndices)
                val currentSchemaPointer = controlElement.schemaScope
                val validationErrors = vmState.errors(currentDataPointer)
                val currentValue = runCatching {
                    vmState.data.find(currentDataPointer)
                }.getOrDefault(JsonNull)
                val isTouched = currentDataPointer in vmState.touchedProperties

                ControlScope(
                    vmState = vmState,
                    postInput = postInput,

                    designSystem = designSystem,

                    control = controlElement,
                    dataPointer = currentDataPointer,
                    schemaPointer = currentSchemaPointer,

                    isValid = validationErrors.isEmpty(),
                    validationErrors = validationErrors,

                    isTouched = isTouched,

                    isEnabled = locallyEnabled,
                    currentValue = currentValue,
                )
            }

            controlRenderer(controlScope)
            if (!controlScope.isValid) {
                controlScope.validationErrors.forEach { designSystem.text(it, isError = true) }
            }
        } else {
            designSystem.text("No UI control with type '${controlElement.controlType}' could be found.", isError = true)
        }
    }
}
