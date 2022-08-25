package com.copperleaf.forms.compose.rules

import com.copperleaf.forms.core.ui.Rule
import com.copperleaf.forms.core.vm.FormContractLite
import com.copperleaf.forms.core.vm.FormFieldsState
import com.copperleaf.json.pointer.JsonPointer
import kotlinx.serialization.json.JsonElement

public data class RuleScope(
    val vmState: FormFieldsState,
    val postInput: (FormContractLite.Inputs)->Unit,

    val rule: Rule,
    val dataPointer: JsonPointer,
    val schemaPointer: JsonPointer,
    val isValid: Boolean,

    val isEnabled: Boolean,
    val isVisible: Boolean,

    val currentValue: JsonElement,
)
