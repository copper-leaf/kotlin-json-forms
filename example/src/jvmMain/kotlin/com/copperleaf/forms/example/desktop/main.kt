package com.copperleaf.forms.example.desktop

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.singleWindowApplication

val allForms = listOf(
    "/fields/boolean",
    "/fields/integer",
    "/fields/number",
    "/fields/string",
    "/fields/richText",
    "/fields/object",
    "/fields/array",
    "/form1",
    "/form2",
    "/basic",
    "/control/1",
    "/control/2",
    "/categorization/1",
    "/categorization/2",
    "/categorization/3",
    "/layout/1",
    "/layout/2",
    "/layout/3",
    "/layout/4",
    "/array",
    "/rule",
    "/listWithDetail",
)

@OptIn(ExperimentalMaterialApi::class, ExperimentalAnimationApi::class)
fun main() = singleWindowApplication {
    Row(Modifier.fillMaxSize()) {
        val (selectedForm, setSelectedForm) = remember { mutableStateOf("/fields/boolean") }
        Column(Modifier.width(240.dp).verticalScroll(rememberScrollState())) {
            allForms.forEach {
                ListItem(
                    Modifier
                        .clickable { setSelectedForm(it) }
                        .then(
                            if (selectedForm == it) {
                                Modifier.background(MaterialTheme.colors.onSurface.copy(alpha = 0.1f))
                            } else {
                                Modifier
                            }
                        )
                ) { Text(it) }
            }
        }
        Column(Modifier.weight(1f)) {
            AnimatedContent(selectedForm) { f: String ->
                RenderFormPreview(path = f)
            }
        }
    }
}

