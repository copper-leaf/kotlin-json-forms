package com.copperleaf.forms.example.android

import android.content.Context
import com.copperleaf.ballast.BallastViewModelConfiguration
import com.copperleaf.ballast.core.BasicViewModel
import com.copperleaf.ballast.core.FifoInputStrategy
import com.copperleaf.ballast.core.LoggingInterceptor
import com.copperleaf.ballast.core.PrintlnLogger
import com.copperleaf.ballast.forViewModel
import com.copperleaf.ballast.plusAssign
import com.copperleaf.ballast.savedstate.BallastSavedStateInterceptor
import com.copperleaf.forms.core.vm.FormContract
import com.copperleaf.forms.core.vm.FormEventHandler
import com.copperleaf.forms.core.vm.FormInputHandler
import com.copperleaf.forms.core.vm.FormSavedStateAdapter
import kotlinx.coroutines.CoroutineScope

class FormViewModelImpl(
    coroutineScope: CoroutineScope,
    context: Context,
    path: String,
) : BasicViewModel<
    FormContract.Inputs,
    FormContract.Events,
    FormContract.State>(
    config = BallastViewModelConfiguration.Builder()
        .apply {
            this += BallastSavedStateInterceptor(
                FormSavedStateAdapter(
                    PreviewFormDataStore.getStoreAt(context, path),
                    FormContract.SaveType.OnCommit,
                )
            )
            this += LoggingInterceptor()
            logger = { PrintlnLogger(it) }
            inputStrategy = FifoInputStrategy()
        }
        .forViewModel(
            initialState = FormContract.State(),
            inputHandler = FormInputHandler(),
            name = path,
        ),
    eventHandler = FormEventHandler(),
    coroutineScope = coroutineScope
)
