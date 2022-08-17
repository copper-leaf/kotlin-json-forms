package com.copperleaf.forms.compose.design

import androidx.compose.runtime.Composable

public interface DesignSystem {
    @Composable
    public fun text(text: String)

    @Composable
    public fun text(text: String, isError: Boolean)

    @Composable
    public fun divider()

    @Composable
    public fun row(content: @Composable () -> Unit)

    @Composable
    public fun column(content: @Composable () -> Unit)

    @Composable
    public fun box(content: @Composable () -> Unit)

    @Composable
    public fun button(onClick: ()->Unit, enabled: Boolean, content: @Composable () -> Unit)

    @Composable
    public fun checkbox(checked: Boolean, onCheckedChange: (Boolean)->Unit, enabled: Boolean, content: @Composable () -> Unit)

    @Composable
    public fun visibility(isVisible: Boolean, content: @Composable () -> Unit)
}
