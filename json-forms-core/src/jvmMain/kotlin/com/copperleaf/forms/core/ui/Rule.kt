package com.copperleaf.forms.core.ui

import net.pwall.json.JSONObject
import net.pwall.json.schema.JSONSchema

public class Rule(
    public val schemaScope: String,
    public val dataScope: String,
    public val effect: Effect,

    public val schemaJson: JSONObject,

    public val conditionSchemaJson: JSONObject,
    public val conditionSchema: JSONSchema,
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
