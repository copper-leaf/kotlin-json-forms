package com.copperleaf.forms.core.vm

import com.copperleaf.ballast.BallastViewModel
import com.copperleaf.ballast.BallastViewModelConfiguration
import com.copperleaf.ballast.build
import com.copperleaf.ballast.core.BasicViewModel
import com.copperleaf.ballast.core.FifoInputStrategy
import com.copperleaf.ballast.core.LoggingInterceptor
import com.copperleaf.ballast.core.PrintlnLogger
import com.copperleaf.ballast.eventHandler
import com.copperleaf.ballast.plusAssign
import com.copperleaf.ballast.savedstate.BallastSavedStateInterceptor
import kotlinx.coroutines.CoroutineScope
import kotlin.time.ExperimentalTime

public typealias FormViewModel = BallastViewModel<
    FormContract.Inputs,
    FormContract.Events,
    FormContract.State,
    >

@OptIn(ExperimentalTime::class)
public fun BallastViewModelConfiguration.Builder.withJsonForms(
    savedStateAdapter: FormSavedStateAdapter,
    name: String? = null,
): BallastViewModelConfiguration.Builder = apply {
    this += BallastSavedStateInterceptor(
        savedStateAdapter
    )
    this.inputStrategy = FifoInputStrategy()
    this.initialState = FormContract.State()
    this.inputHandler = FormInputHandler()
    this.name = name ?: "Json Form"
}

public class BasicFormViewModel(
    coroutineScope: CoroutineScope,
    config: BallastViewModelConfiguration<
        FormContract.Inputs,
        FormContract.Events,
        FormContract.State>,
) : BasicViewModel<
    FormContract.Inputs,
    FormContract.Events,
    FormContract.State>(
    config = config,
    eventHandler = eventHandler { },
    coroutineScope = coroutineScope
) {
    public constructor(
        coroutineScope: CoroutineScope,
        savedStateAdapter: FormSavedStateAdapter,
        name: String? = null,
    ) : this(
        coroutineScope = coroutineScope,
        config = BallastViewModelConfiguration.Builder()
            .withJsonForms(
                savedStateAdapter = savedStateAdapter,
                name = name,
            )
            .apply {
//                this += LoggingInterceptor()
                logger = { PrintlnLogger(it) }
            }
            .build()
    )
}
