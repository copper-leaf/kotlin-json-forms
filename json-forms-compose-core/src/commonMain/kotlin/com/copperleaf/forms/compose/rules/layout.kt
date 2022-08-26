package com.copperleaf.forms.compose.rules

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.copperleaf.forms.compose.LocalArrayIndices
import com.copperleaf.forms.compose.LocallyEnabled
import com.copperleaf.forms.compose.LocallyVisible
import com.copperleaf.forms.compose.form.FormScope
import com.copperleaf.forms.core.ui.UiElement

@Composable
public fun FormScope.RuleLayout(
    uiElement: UiElement,
    content: @Composable () -> Unit,
) {
    val rule = uiElement.rule
    if (rule == null) {
        // no rule defined, pass directly through
        content()
    } else {
        val ruleScope = getRuleScope(
            uiElement = uiElement,
            rule = rule,
            localArrayIndices = LocalArrayIndices.current,
        )

        CompositionLocalProvider(
            LocallyEnabled provides ruleScope.isEnabled,
            LocallyVisible provides ruleScope.isVisible
        ) {
            visibility(LocallyVisible.current) {
                content()
            }
        }
    }
}
