package com.copperleaf.forms.core.internal

import net.pwall.json.schema.output.BasicOutput
import net.pwall.json.schema.output.DetailedOutput
import net.pwall.json.schema.output.Output

internal fun Output.getValidationErrorMessages(
    pointer: String
): List<String> {
    if (this.valid) return emptyList() // short-circuit for performance

    return buildList {
        getValidationErrorMessages(pointer, this)
    }
}

private fun Output.getValidationErrorMessages(
    pointer: String,
    listBuilder: MutableList<String>,
) {
    when (this) {
        is BasicOutput -> this.getBasicValidationErrorMessages(pointer, listBuilder)
        is DetailedOutput -> this.getDetailedValidationErrorMessages(pointer, listBuilder)
        else -> {}
    }
}

private fun DetailedOutput.getDetailedValidationErrorMessages(
    pointer: String,
    listBuilder: MutableList<String>,
) {
    if (this.valid) return // short-circuit for performance

    if (this.instanceLocation == pointer && this.errors.isNullOrEmpty()) {
        // we are a leaf output node, which has the actual validation message we need
        listBuilder.add(this.error ?: "")
    } else {
        this.errors?.forEach {
            it.getValidationErrorMessages(pointer, listBuilder)
        }
    }
}

private fun BasicOutput.getBasicValidationErrorMessages(
    pointer: String,
    listBuilder: MutableList<String>,
) {
    if (this.valid) return // short-circuit for performance
}

