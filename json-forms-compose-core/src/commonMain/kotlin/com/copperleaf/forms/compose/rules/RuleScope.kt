package com.copperleaf.forms.compose.rules

import com.copperleaf.forms.core.ui.Rule
import com.copperleaf.forms.core.vm.FormContract
import com.copperleaf.json.pointer.JsonPointer
import kotlinx.serialization.json.JsonElement

public data class RuleScope(
    val vmState: FormContract.State,
    val postInput: (FormContract.Inputs)->Unit,

    val rule: Rule,
    val dataPointer: JsonPointer,
    val schemaPointer: JsonPointer,
    val isValid: Boolean,

    val isEnabled: Boolean,
    val isVisible: Boolean,

    val currentValue: JsonElement,
)
