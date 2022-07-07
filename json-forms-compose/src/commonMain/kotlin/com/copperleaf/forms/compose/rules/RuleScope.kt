package com.copperleaf.forms.compose.rules

import com.copperleaf.forms.core.ui.Rule
import com.copperleaf.forms.core.vm.FormContract
import com.copperleaf.forms.core.vm.FormViewModel
import net.pwall.json.JSONValue
import net.pwall.json.pointer.JSONPointer

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
)
