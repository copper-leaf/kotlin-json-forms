package com.copperleaf.forms.core.ui

import com.copperleaf.json.pointer.AbstractJsonPointer
import com.copperleaf.json.pointer.JsonPointer
import com.copperleaf.json.schema.JsonSchema
import kotlinx.serialization.json.JsonElement

public class Rule(
    public val schemaScope: JsonPointer,
    public val dataScope: AbstractJsonPointer,
    public val effect: Effect,

    public val schemaJson: JsonElement,

    public val conditionSchemaJson: JsonElement,
    public val conditionSchema: JsonSchema,
) {
    public enum class Effect(public val isVisible: Boolean, public val isEnabled: Boolean) {
        Show(true, true),
        Hide(false, true),
        Enable(true, true),
        Disable(true, false);

        public fun inverse(): Effect {
            return when (this) {
                Show -> Hide
                Hide -> Show
                Enable -> Disable
                Disable -> Enable
            }
        }
    }
}
