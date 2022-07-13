package com.copperleaf.forms.compose.widgets.dropdown

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.TooltipArea
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalFoundationApi::class)
@Composable
public actual fun IconButtonWithDescriptionInternal(
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier,
    enabled: Boolean,
    interactionSource: MutableInteractionSource,
) {
    TooltipArea(
        tooltip = {
            Surface(elevation = 4.dp) {
                Box(Modifier.padding(vertical = 2.dp, horizontal = 4.dp)) {
                    Text(contentDescription)
                }
            }
        }
    ) {
        IconButton(
            onClick = onClick,
            modifier = modifier,
            enabled = enabled,
            interactionSource = interactionSource,
        ) {
            Icon(icon, contentDescription = contentDescription)
        }
    }
}
