package com.copperleaf.json.pointer

public sealed interface JsonPointerAction {
    public data class SetValue(val value: Any?) : JsonPointerAction
    public object RemoveValue : JsonPointerAction
    public data class SwapArrayIndices(val to: Int) : JsonPointerAction
}
