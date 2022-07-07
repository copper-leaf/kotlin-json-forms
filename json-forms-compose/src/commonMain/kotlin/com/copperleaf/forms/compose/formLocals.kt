package com.copperleaf.forms.compose.ui

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.staticCompositionLocalOf
import com.copperleaf.forms.compose.form.ComposeFormConfig
import com.copperleaf.forms.core.vm.FormViewModel

public val LocalFormConfig: ProvidableCompositionLocal<ComposeFormConfig> = staticCompositionLocalOf {
    error("LocalViewModel not defined")
}
public val LocalViewModel: ProvidableCompositionLocal<FormViewModel> = staticCompositionLocalOf {
    error("LocalViewModel not defined")
}

public val LocalArrayIndices: ProvidableCompositionLocal<List<Int>> = compositionLocalOf { emptyList() }

public val LocallyEnabled: ProvidableCompositionLocal<Boolean> = compositionLocalOf { true }
public val LocallyVisible: ProvidableCompositionLocal<Boolean> = compositionLocalOf { true }
