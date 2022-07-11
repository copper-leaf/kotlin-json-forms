package com.copperleaf.forms.core.vm

import com.copperleaf.ballast.EventHandler
import com.copperleaf.ballast.EventHandlerScope

public class FormEventHandler : EventHandler<
    FormContract.Inputs,
    FormContract.Events,
    FormContract.State> {
    override suspend fun EventHandlerScope<
        FormContract.Inputs,
        FormContract.Events,
        FormContract.State>.handleEvent(
        event: FormContract.Events
    ) {
    }
}
