package com.copperleaf.forms.core.vm

import com.copperleaf.ballast.BallastViewModel
import com.copperleaf.ballast.BallastViewModelConfiguration
import com.copperleaf.ballast.core.AndroidViewModel
import com.copperleaf.ballast.core.FifoInputStrategy
import com.copperleaf.ballast.core.LoggingInterceptor
import com.copperleaf.ballast.core.PrintlnLogger
import com.copperleaf.ballast.forViewModel
import com.copperleaf.ballast.plusAssign
import com.copperleaf.ballast.savedstate.BallastSavedStateInterceptor
import kotlinx.coroutines.CoroutineScope
import kotlin.time.ExperimentalTime

public class AndroidFormViewModel(
    config: BallastViewModelConfiguration<
        FormContract.Inputs,
        FormContract.Events,
        FormContract.State>,
) : AndroidViewModel<
    FormContract.Inputs,
    FormContract.Events,
    FormContract.State>(
    config = config,
) {
    public constructor(
        savedStateAdapter: FormSavedStateAdapter,
        name: String? = null,
    ) : this(
        config = BallastViewModelConfiguration.Builder()
            .withJsonForms(
                savedStateAdapter = savedStateAdapter,
                name = name,
            )
            .apply {
                this += LoggingInterceptor()
                logger = { PrintlnLogger(it) }
            }
            .build()
    )
}
