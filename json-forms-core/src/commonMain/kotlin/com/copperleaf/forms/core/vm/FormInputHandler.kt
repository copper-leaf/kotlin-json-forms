package com.copperleaf.forms.core.vm

import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import com.copperleaf.json.pointer.mutate

public class FormInputHandler : InputHandler<
    FormContract.Inputs,
    FormContract.Events,
    FormContract.State> {
    override suspend fun InputHandlerScope<
        FormContract.Inputs,
        FormContract.Events,
        FormContract.State>.handleInput(
        input: FormContract.Inputs
    ): Unit = when (input) {
        is FormContract.Inputs.SetDebugMode -> {
            updateState { it.copy(debug = input.isDebug) }
        }
        is FormContract.Inputs.SetSaveType -> {
            updateState { it.copy(saveType = input.saveType) }
        }
        is FormContract.Inputs.UpdateFormState -> {
            val currentState = getCurrentState()

            val updatedTemporaryState = currentState.copy(
                updatedData = currentState.updatedData.mutate(input.pointer, input.action),
            )

            val updatedState = when (currentState.saveType) {
                FormContract.SaveType.OnAnyChange -> {
                    updatedTemporaryState.copy(
                        originalData = updatedTemporaryState.updatedData
                    )
                }
                FormContract.SaveType.OnValidChange -> {
                    if (updatedTemporaryState.isValid) {
                        updatedTemporaryState.copy(
                            originalData = updatedTemporaryState.updatedData
                        )
                    } else {
                        updatedTemporaryState
                    }
                }
                FormContract.SaveType.OnCommit -> {
                    updatedTemporaryState
                }
            }

            updateState { updatedState }
        }
        is FormContract.Inputs.CommitChanges -> {
            updateState {
                if (it.isValid) {
                    it.copy(
                        originalData = it.updatedData,
                    )
                } else {
                    it
                }
            }
        }
    }
}
