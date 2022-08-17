package com.copperleaf.forms.compose.widgets.dropdown

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
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
    Box(modifier = modifier) {
        val localContext = LocalContext.current

        Box(
            modifier = modifier
                .size(48.dp)
                .combinedClickable(
                    onClick = onClick,
                    onLongClick = { Toast.makeText(localContext, contentDescription, Toast.LENGTH_SHORT).show() },
                    enabled = enabled,
                    role = Role.Button,
                    interactionSource = interactionSource,
                    indication = rememberRipple(bounded = false, radius = 24.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            val contentAlpha = if (enabled) LocalContentAlpha.current else ContentAlpha.disabled
            CompositionLocalProvider(LocalContentAlpha provides contentAlpha) {
                Icon(icon, contentDescription)

            }
        }
    }
}
