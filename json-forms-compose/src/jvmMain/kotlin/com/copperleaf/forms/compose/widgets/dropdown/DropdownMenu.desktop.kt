package com.copperleaf.forms.compose.widgets.dropdown

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpOffset
import androidx.compose.material.DropdownMenu as DesktopDropdownMenu
import androidx.compose.material.DropdownMenuItem as DesktopDropdownMenuItem

@Composable
public actual fun DropdownMenuInternal(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    focusable: Boolean,
    modifier: Modifier,
    offset: DpOffset,
    content: @Composable ColumnScope.() -> Unit
) {
    DesktopDropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest,
        focusable = focusable,
        modifier = modifier,
        offset = offset,
        content = content,
    )
}

@Composable
public actual fun DropdownMenuItemInternal(
    onClick: () -> Unit,
    modifier: Modifier,
    enabled: Boolean,
    contentPadding: PaddingValues,
    interactionSource: MutableInteractionSource,
    content: @Composable RowScope.() -> Unit
) {
    DesktopDropdownMenuItem(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        contentPadding = contentPadding,
        interactionSource = interactionSource,
        content = content,
    )
}
