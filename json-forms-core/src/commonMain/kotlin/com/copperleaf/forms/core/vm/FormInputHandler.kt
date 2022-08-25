package com.copperleaf.forms.core.vm

import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import com.copperleaf.forms.core.internal.resolveUiSchema
import com.copperleaf.json.pointer.mutate
import com.copperleaf.json.schema.JsonSchema

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

        is FormContract.Inputs.SetValidationMode -> {
            updateState { it.copy(validationMode = input.validationMode) }
        }

        is FormContract.Inputs.SetSaveType -> {
            updateState { it.copy(saveType = input.saveType) }
        }

        is FormContract.Inputs.SetReadOnly -> {
            updateState { it.copy(readOnly = input.readOnly) }
        }

        is FormContract.Inputs.UpdateSchema -> {
            updateState {
                it.copy(
                    schema = JsonSchema.parse(input.schemaJson)
                )
            }
        }

        is FormContract.Inputs.UpdateUiSchema -> {
            updateState { state ->
                state.copy(
                    uiSchema = state.schema?.let { input.uiSchemaJson.resolveUiSchema(it) }
                )
            }
        }

        is FormContract.Inputs.FormDataChangedExternally -> {
            updateState {
                it.copy(
                    originalData = input.newFormData,
                    updatedData = input.newFormData,
                    touchedProperties = emptySet(),
                )
            }
        }

        is FormContract.Inputs.UpdateFormState -> {
            val currentState = getCurrentState()

            val updatedTemporaryState = currentState.copy(
                updatedData = currentState.updatedData.mutate(input.pointer, input.action),
                touchedProperties = currentState.touchedProperties + input.pointer,
            )

            val updatedState = when (currentState.saveType) {
                FormContract.SaveType.OnAnyChange -> {
                    updatedTemporaryState.copy(
                        originalData = updatedTemporaryState.updatedData,
                        touchedProperties = emptySet()
                    )
                }

                FormContract.SaveType.OnValidChange -> {
                    if (updatedTemporaryState.isValid) {
                        updatedTemporaryState.copy(
                            originalData = updatedTemporaryState.updatedData,
                            touchedProperties = emptySet()
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

        is FormContract.Inputs.MarkAsTouched -> {
            updateState {
                it.copy(touchedProperties = it.touchedProperties + input.pointer)
            }
        }

        is FormContract.Inputs.CommitChanges -> {
            updateState {
                if (it.isValid) {
                    it.copy(
                        originalData = it.updatedData,
                        touchedProperties = emptySet(),
                    )
                } else {
                    it
                }
            }
        }
    }
}
