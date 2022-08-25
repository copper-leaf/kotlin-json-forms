package com.copperleaf.forms.core.vm

import com.copperleaf.json.pointer.JsonPointer
import com.copperleaf.json.pointer.JsonPointerAction

public object FormContractLite {

    public sealed class Inputs {
        public abstract fun full(): FormContract.Inputs

        public data class UpdateFormState(
            val pointer: JsonPointer,
            val action: JsonPointerAction,
        ) : Inputs() {
            override fun full(): FormContract.Inputs {
                return FormContract.Inputs.UpdateFormState(
                    pointer = pointer,
                    action = action,
                )
            }
        }

        public data class MarkAsTouched(
            val pointer: JsonPointer,
        ) : Inputs() {
            override fun full(): FormContract.Inputs {
                return FormContract.Inputs.MarkAsTouched(
                    pointer = pointer,
                )
            }
        }

        // This may be used by a Submit Button, or anywhere in your UI to manually trigger a "save" of the form data
        public object CommitChanges : Inputs() {
            override fun full(): FormContract.Inputs {
                return FormContract.Inputs.CommitChanges
            }
        }
    }

    public sealed class Events {

    }
}
