package com.copperleaf.forms.compose.rules

import com.copperleaf.forms.core.ui.Rule
import com.copperleaf.forms.core.vm.FormContract
import com.copperleaf.forms.core.vm.FormViewModel
import com.copperleaf.json.pointer.JsonPointer
import kotlinx.serialization.json.JsonElement

public data class RuleScope(
    private val vm: FormViewModel,
    val vmState: FormContract.State,
    val rule: Rule,
    val dataPointer: JsonPointer,
    val schemaPointer: JsonPointer,
    val isValid: Boolean,

    val isEnabled: Boolean,
    val isVisible: Boolean,

    val currentValue: JsonElement,
)
