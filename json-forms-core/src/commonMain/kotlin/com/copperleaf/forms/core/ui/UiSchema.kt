package com.copperleaf.forms.core.ui

import kotlinx.serialization.json.JsonElement

public class UiSchema(
    public val rootUiElement: UiElement,
    public val json: JsonElement
)
