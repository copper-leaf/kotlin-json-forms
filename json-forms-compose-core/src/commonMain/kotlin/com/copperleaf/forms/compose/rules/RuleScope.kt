package com.copperleaf.forms.compose.rules

import com.copperleaf.forms.compose.form.FormScope
import com.copperleaf.forms.core.ui.Rule
import com.copperleaf.json.pointer.JsonPointer
import kotlinx.serialization.json.JsonElement

public interface RuleScope : FormScope {
    public val rule: Rule
    public val dataPointer: JsonPointer
    public val schemaPointer: JsonPointer
    public val isRuleValid: Boolean

    public val isEnabled: Boolean
    public val isVisible: Boolean

    public val currentValue: JsonElement
}
