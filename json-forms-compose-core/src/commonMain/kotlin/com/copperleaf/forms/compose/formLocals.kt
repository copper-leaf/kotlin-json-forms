package com.copperleaf.forms.compose

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.staticCompositionLocalOf
import com.copperleaf.forms.compose.design.DesignSystem
import com.copperleaf.forms.compose.form.ComposeFormConfig

public val LocalFormConfig: ProvidableCompositionLocal<ComposeFormConfig> = staticCompositionLocalOf {
    error("LocalViewModel not defined")
}
public val LocalDesignSystem: ProvidableCompositionLocal<DesignSystem> = compositionLocalOf {
    error("DesignSystem not provided")
}

public val LocalArrayIndices: ProvidableCompositionLocal<List<Int>> = compositionLocalOf { emptyList() }

public val LocallyEnabled: ProvidableCompositionLocal<Boolean> = compositionLocalOf { true }
public val LocallyVisible: ProvidableCompositionLocal<Boolean> = compositionLocalOf { true }
