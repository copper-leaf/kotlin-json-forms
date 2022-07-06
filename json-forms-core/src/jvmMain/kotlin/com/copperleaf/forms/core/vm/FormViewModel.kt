package com.copperleaf.forms.core.vm

import com.copperleaf.ballast.BallastViewModel

public typealias FormViewModel = BallastViewModel<
    FormContract.Inputs,
    FormContract.Events,
    FormContract.State,
    >
