package com.copperleaf.forms.compose.bulma.form

import androidx.compose.runtime.Composable
import com.copperleaf.forms.compose.bulma.controls.BulmaField
import com.copperleaf.forms.compose.design.DesignSystem
import org.jetbrains.compose.web.attributes.disabled
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.CheckboxInput
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Hr
import org.jetbrains.compose.web.dom.Text

public class BulmaDesignSystem : DesignSystem {
    @Composable
    override fun text(text: String) {
        Text(text)
    }

    @Composable
    override fun text(text: String, isError: Boolean) {
        Text(text)
    }

    @Composable
    override fun divider() {
        Hr()
    }

    @Composable
    override fun row(content: @Composable () -> Unit) {
        Div({ classes("columns") }) {
            content()
        }
    }

    @Composable
    override fun column(content: @Composable () -> Unit) {
        Div({ classes("column") }) {
            content()
        }
    }

    @Composable
    override fun box(content: @Composable () -> Unit) {
        Div {
            content()
        }
    }

    @Composable
    override fun button(onClick: () -> Unit, enabled: Boolean, content: @Composable () -> Unit) {
        Button({ onClick { onClick() } }) { content() }
    }

    @Composable
    override fun checkbox(
        checked: Boolean,
        onCheckedChange: (Boolean) -> Unit,
        enabled: Boolean,
        content: @Composable () -> Unit
    ) {
        BulmaField {
            CheckboxInput(
                checked = checked,
            ) {
                onClick { onCheckedChange(!checked) }
                if (!enabled) {
                    disabled()
                }
            }
            content()
        }
    }

    @Composable
    override fun visibility(isVisible: Boolean, content: @Composable () -> Unit) {
        if (isVisible) {
            content()
        }
    }
}
