package com.copperleaf.forms.core.ui

import com.copperleaf.json.pointer.AbstractJsonPointer
import com.copperleaf.json.pointer.JsonPointer
import kotlinx.serialization.json.JsonElement

public sealed interface UiElement {
    public val uiSchemaConfig: JsonElement
    public val rule: Rule?

    public data class ElementWithChildren(
        override val uiSchemaConfig: JsonElement,
        override val rule: Rule?,

        val elementType: String,
        val elements: List<UiElement>,
    ) : UiElement

    public data class Control(
        val controlType: String,
        val schemaScope: JsonPointer,
        val schemaConfig: JsonElement,
        override val uiSchemaConfig: JsonElement,

        val dataScope: AbstractJsonPointer,
        override val rule: Rule?,

        val required: Boolean,
        val label: String,
    ) : UiElement {
        public companion object {}
    }

    public companion object {}
}
