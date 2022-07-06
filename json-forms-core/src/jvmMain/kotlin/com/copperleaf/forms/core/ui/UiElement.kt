package com.copperleaf.forms.core.ui

import net.pwall.json.JSONObject

public sealed interface UiElement {
    public companion object {}

    public val uiSchemaConfig: JSONObject
    public val rule: Rule?

    public data class ElementWithChildren(
        override val uiSchemaConfig: JSONObject = JSONObject(),
        override val rule: Rule? = null,

        val elementType: String,
        val elements: List<UiElement>,
    ) : UiElement {
        override fun toString(): String {
            return """
                |{
                |   type: $elementType,
                |   elements: ${elements.joinToString(separator = "\n")}
                |}
            """.trimMargin()
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is ElementWithChildren) return false

            if (elementType != other.elementType) return false
            if (elements != other.elements) return false

            return true
        }

        override fun hashCode(): Int {
            var result = elementType.hashCode()
            result = 31 * result + elements.hashCode()
            return result
        }
    }

    public data class Control(
        override val uiSchemaConfig: JSONObject = JSONObject(),
        override val rule: Rule? = null,

        val controlType: String,
        val schemaScope: String,
        val schemaConfig: JSONObject = JSONObject(),

        val dataScope: String,

        val required: Boolean,
        val label: String,
    ) : UiElement {
        public companion object {}

        override fun toString(): String {
            return """
                |{
                |   type: $controlType${if (required) "" else "?"} ($label) 
                |   controlType: $controlType
                |   schemaScope: $schemaScope
                |   dataScope: $dataScope
                |}
            """.trimMargin()
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is Control) return false

            if (controlType != other.controlType) return false
            if (required != other.required) return false
            if (schemaScope != other.schemaScope) return false
            if (dataScope != other.dataScope) return false
            if (label != other.label) return false

            return true
        }

        override fun hashCode(): Int {
            var result = controlType.hashCode()
            result = 31 * result + required.hashCode()
            result = 31 * result + schemaScope.hashCode()
            result = 31 * result + dataScope.hashCode()
            result = 31 * result + label.hashCode()
            return result
        }
    }
}
