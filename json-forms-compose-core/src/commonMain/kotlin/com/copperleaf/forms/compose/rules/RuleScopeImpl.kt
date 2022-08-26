package com.copperleaf.forms.compose.rules

import com.copperleaf.forms.compose.form.FormScope
import com.copperleaf.forms.core.ui.Rule
import com.copperleaf.json.pointer.JsonPointer
import kotlinx.serialization.json.JsonElement

public data class RuleScopeImpl(
    private val formScope: FormScope,
    public override val rule: Rule,
    public override val dataPointer: JsonPointer,
    public override val schemaPointer: JsonPointer,
    public override val isRuleValid: Boolean,

    public override val isEnabled: Boolean,
    public override val isVisible: Boolean,

    public override val currentValue: JsonElement,
) : RuleScope, FormScope by formScope
