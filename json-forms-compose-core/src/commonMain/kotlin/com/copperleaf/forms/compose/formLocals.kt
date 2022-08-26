package com.copperleaf.forms.compose

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf

public val LocalArrayIndices: ProvidableCompositionLocal<List<Int>> = compositionLocalOf { emptyList() }
public val LocallyEnabled: ProvidableCompositionLocal<Boolean> = compositionLocalOf { true }
public val LocallyVisible: ProvidableCompositionLocal<Boolean> = compositionLocalOf { true }
