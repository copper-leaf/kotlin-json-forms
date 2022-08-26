package com.copperleaf.forms.compose.form

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Button
import androidx.compose.material.Checkbox
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.copperleaf.forms.compose.design.DesignSystem

public class MaterialDesignSystem(public val animateVisibility: Boolean = true) : DesignSystem {
    @Composable
    override fun text(text: String) {
        Text(text)
    }

    @Composable
    override fun text(text: String, isError: Boolean) {
        Text(text, color = MaterialTheme.colors.error)
    }

    @Composable
    override fun divider() {
        Divider()
    }

    @Composable
    override fun row(content: @Composable () -> Unit) {
        Row(Modifier.fillMaxWidth().wrapContentHeight(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            content()
        }
    }

    @Composable
    override fun column(content: @Composable () -> Unit) {
        Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            content()
        }
    }

    @Composable
    override fun box(content: @Composable () -> Unit) {
        Box { content() }
    }

    @Composable
    override fun button(onClick: () -> Unit, enabled: Boolean, content: @Composable () -> Unit) {
        Button(onClick = onClick) { content() }
    }

    @Composable
    override fun checkbox(checked: Boolean, onCheckedChange: (Boolean) -> Unit, enabled: Boolean, content: @Composable () -> Unit) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
        )
        content()
    }

    @Composable
    override fun visibility(isVisible: Boolean, content: @Composable () -> Unit) {
        if (animateVisibility) {
            AnimatedVisibility(isVisible) {
                content()
            }
        } else {
            if (isVisible) {
                content()
            }
        }
    }
}
