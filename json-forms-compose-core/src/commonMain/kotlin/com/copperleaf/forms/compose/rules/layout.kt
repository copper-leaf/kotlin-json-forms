package com.copperleaf.forms.compose.rules

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.copperleaf.forms.compose.LocalArrayIndices
import com.copperleaf.forms.compose.LocalDesignSystem
import com.copperleaf.forms.compose.LocallyEnabled
import com.copperleaf.forms.compose.LocallyVisible
import com.copperleaf.forms.core.ui.UiElement
import com.copperleaf.forms.core.vm.FormContract
import com.copperleaf.json.pointer.find
import com.copperleaf.json.pointer.reifyPointer
import kotlinx.serialization.json.JsonNull

@Composable
public fun RuleLayout(
    uiElement: UiElement,
    vmState: FormContract.State,
    postInput: (FormContract.Inputs)->Unit,
    content: @Composable () -> Unit,
) {
    val rule = uiElement.rule
    if (rule == null) {
        // no rule defined, pass directly through
        content()
    } else {
        val localArrayIndices = LocalArrayIndices.current
        val locallyEnabled = LocallyEnabled.current

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
                    vmState = vmState,
                    postInput = postInput,

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
            LocalDesignSystem.current.visibility(LocallyVisible.current) {
                content()
            }
        }
    }
}
