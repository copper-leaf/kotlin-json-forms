package com.copperleaf.forms.compose.form

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.copperleaf.forms.compose.ui.LocalArrayIndices

@Composable
public fun WithArrayIndex(
    index: Int,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(LocalArrayIndices provides (LocalArrayIndices.current + index)) {
        content()
    }
}
