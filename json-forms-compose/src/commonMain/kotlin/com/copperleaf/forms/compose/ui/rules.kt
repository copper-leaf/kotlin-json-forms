package com.copperleaf.forms.compose.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.copperleaf.forms.core.ui.Rule
import com.copperleaf.forms.core.ui.UiElement
import com.copperleaf.json.pointer.asPointer
import net.pwall.json.JSONValue
import net.pwall.json.pointer.JSONPointer
import net.pwall.json.schema.JSONSchema

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
        val vm = LocalViewModel.current
        val vmState by vm.observeStates().collectAsState()
        val currentPointer by rulePointer(rule)

        val currentValue: State<JSONValue?> = currentJsonValueAtPointer(vmState, currentPointer)
        val isValid = validateValueForCondition(currentValue.value, rule.conditionSchema)

        LocallyApplyEffect(
            if (isValid.value) rule.effect else rule.effect.inverse(),
            animated,
            content,
        )
    }
}

@Composable
public fun LocallyApplyEffect(
    effect: Rule.Effect,
    animated: Boolean,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocallyEnabled provides effect.isEnabled,
        LocallyVisible provides effect.isVisible
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

@Composable
public fun rulePointer(
    rule: Rule,
): State<JSONPointer> {
    val localArrayIndices = LocalArrayIndices.current
    return remember(rule.dataScope, localArrayIndices) {
        derivedStateOf { rule.dataScope.asPointer(localArrayIndices) }
    }
}

@Composable
public fun validateValueForCondition(
    value: JSONValue?,
    schema: JSONSchema
): State<Boolean> {
    return remember(value, schema) {
        derivedStateOf {
            schema.validate(value)
        }
    }
}
