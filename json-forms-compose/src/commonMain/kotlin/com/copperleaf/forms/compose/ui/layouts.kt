package com.copperleaf.forms.compose.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.copperleaf.forms.core.ui.Rule
import com.copperleaf.forms.core.ui.UiElement
import com.copperleaf.forms.core.vm.FormContract
import com.copperleaf.forms.core.vm.FormViewModel
import com.copperleaf.json.pointer.JsonPointerAction
import com.copperleaf.json.pointer.asPointer
import com.copperleaf.json.pointer.asPointerString
import com.copperleaf.json.pointer.toJsonValue
import net.pwall.json.JSONValue
import net.pwall.json.pointer.JSONPointer

public data class ControlScope(
    private val vm: FormViewModel,
    val vmState: FormContract.State,
    val control: UiElement.Control,
    val dataPointer: JSONPointer,
    val schemaPointer: JSONPointer,
    val isValid: Boolean,
    val validationErrors: List<String>,

    val isEnabled: Boolean,

    val currentValue: JSONValue?,
) {
    public fun updateFormState(
        value: Any?,
    ) {
        vm.trySend(
            FormContract.Inputs.UpdateFormState(
                pointer = dataPointer,
                action = JsonPointerAction.SetValue(value.toJsonValue()),
            )
        )
    }

    public fun sendFormAction(
        action: JsonPointerAction,
        pointer: JSONPointer = dataPointer,
    ) {
        vm.trySend(
            FormContract.Inputs.UpdateFormState(
                pointer = pointer,
                action = action,
            )
        )
    }

    public fun <T> getTypedValue(
        defaultValue: T,
        mapper: (JSONValue) -> T?,
    ): T {
        return currentValue?.let(mapper) ?: defaultValue
    }
}

@Composable
public fun ControlLayout(controlElement: UiElement.Control, block: @Composable ControlScope.() -> Unit) {
    Column {
        val localArrayIndices = LocalArrayIndices.current
        val locallyEnabled = LocallyEnabled.current

        val vm = LocalViewModel.current
        val vmState by vm.observeStates().collectAsState()

        val controlScope by remember(controlElement, vmState, localArrayIndices, locallyEnabled) {
            derivedStateOf {
                val currentDataPointer = controlElement.dataScope.asPointer(localArrayIndices)
                val currentSchemaPointer = controlElement.schemaScope.asPointer(localArrayIndices)
                val validationErrors = vmState.errors(currentDataPointer.asPointerString())
                val currentValue = runCatching {
                    currentDataPointer.find(vmState.updatedData)
                }.getOrNull()

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

        controlScope.block()
        if (!controlScope.isValid) {
            controlScope.validationErrors.forEach { Text(it, color = MaterialTheme.colors.error) }
        }
        if (vmState.debug) {
            with(controlScope) {
                Text("schema scope: ${schemaPointer.asPointerString()}")
                Text("data scope: ${dataPointer.asPointerString()}")
                Text("type: ${control.controlType}")
                Text("Required: ${control.required}")
                Text("Has Rule?: ${control.rule != null}")
            }
        }
    }
}

public data class RuleScope(
    private val vm: FormViewModel,
    val vmState: FormContract.State,
    val rule: Rule,
    val dataPointer: JSONPointer,
    val schemaPointer: JSONPointer,
    val isValid: Boolean,

    val isEnabled: Boolean,
    val isVisible: Boolean,

    val currentValue: JSONValue?,
) {
    public fun updateFormState(
        value: Any?,
    ) {
        vm.trySend(
            FormContract.Inputs.UpdateFormState(
                pointer = dataPointer,
                action = JsonPointerAction.SetValue(value.toJsonValue()),
            )
        )
    }

    public fun sendFormAction(
        action: JsonPointerAction,
        pointer: JSONPointer = dataPointer,
    ) {
        vm.trySend(
            FormContract.Inputs.UpdateFormState(
                pointer = pointer,
                action = action,
            )
        )
    }

    public fun <T> getTypedValue(
        defaultValue: T,
        mapper: (JSONValue) -> T?,
    ): T {
        return currentValue?.let(mapper) ?: defaultValue
    }
}

@Composable
public fun RuleLayout(
    uiElement: UiElement,
    animated: Boolean,
    content: @Composable () -> Unit,
) {
    val rule = uiElement.rule
    if (rule == null) {
        // no rule defined, pass directly through
        content()
    } else {
        val localArrayIndices = LocalArrayIndices.current
        val locallyEnabled = LocallyEnabled.current

        val vm = LocalViewModel.current
        val vmState by vm.observeStates().collectAsState()

        val ruleScope by remember(rule, vmState, localArrayIndices, locallyEnabled) {
            derivedStateOf {
                val currentDataPointer = rule.dataScope.asPointer(localArrayIndices)
                val currentSchemaPointer = rule.schemaScope.asPointer(localArrayIndices)
                val currentValue = runCatching {
                    currentDataPointer.find(vmState.updatedData)
                }.getOrNull()

                val isValid = rule.conditionSchema.validate(currentValue)

                val effect = if (isValid) rule.effect else rule.effect.inverse()

                RuleScope(
                    vm = vm,
                    vmState = vmState,

                    rule = rule,
                    dataPointer = currentDataPointer,
                    schemaPointer = currentSchemaPointer,

                    isValid = isValid,

                    isEnabled = effect.isEnabled,
                    isVisible = effect.isVisible,
                    currentValue = currentValue,
                )
            }
        }

        CompositionLocalProvider(
            LocallyEnabled provides ruleScope.isEnabled,
            LocallyVisible provides ruleScope.isVisible
        ) {
            if (animated) {
                AnimatedVisibility(LocallyVisible.current) {
                    content()
                }
            } else {
                if (LocallyVisible.current) {
                    content()
                }
            }
        }
    }
}
