package com.copperleaf.forms.compose.rules

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.copperleaf.forms.compose.ui.LocalArrayIndices
import com.copperleaf.forms.compose.ui.LocalViewModel
import com.copperleaf.forms.compose.ui.LocallyEnabled
import com.copperleaf.forms.compose.ui.LocallyVisible
import com.copperleaf.forms.core.ui.UiElement
import com.copperleaf.json.pointer.find
import com.copperleaf.json.pointer.reifyPointer
import kotlinx.serialization.json.JsonNull

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
                val currentDataPointer = rule.dataScope.reifyPointer(localArrayIndices)
                val currentSchemaPointer = rule.schemaScope
                val currentValue = runCatching {
                    vmState.updatedData.find(currentDataPointer)
                }.getOrDefault(JsonNull)

                val result = rule.conditionSchema.validate(currentValue)

                val effect = if (result.isValid) rule.effect else rule.effect.inverse()

                RuleScope(
                    vm = vm,
                    vmState = vmState,

                    rule = rule,
                    dataPointer = currentDataPointer,
                    schemaPointer = currentSchemaPointer,

                    isValid = result.isValid,

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
