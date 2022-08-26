package com.copperleaf.forms.core.vm

import com.copperleaf.json.pointer.JsonPointer
import com.copperleaf.json.pointer.JsonPointerAction
import com.copperleaf.json.pointer.mutate
import kotlinx.serialization.json.JsonElement

public object FormFieldsContract {
    public sealed class Inputs {
        public abstract fun applyToState(
            data: JsonElement,
            touchedProperties: Set<JsonPointer>
        ): Pair<JsonElement, Set<JsonPointer>>

        public data class UpdateFormState(
            val pointer: JsonPointer,
            val action: JsonPointerAction,
        ) : Inputs() {
            override fun applyToState(
                data: JsonElement,
                touchedProperties: Set<JsonPointer>
            ): Pair<JsonElement, Set<JsonPointer>> {
                val updatedData = data.mutate(pointer, action)
                val updatedTouchedProperties = touchedProperties + pointer
                return updatedData to updatedTouchedProperties
            }
        }

        public data class MarkAsTouched(
            val pointer: JsonPointer,
        ) : Inputs() {
            override fun applyToState(
                data: JsonElement,
                touchedProperties: Set<JsonPointer>
            ): Pair<JsonElement, Set<JsonPointer>> {
                val updatedTouchedProperties = touchedProperties + pointer
                return data to updatedTouchedProperties
            }
        }
    }

    public sealed class Events {

    }
}
