package com.copperleaf.forms.example.android

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.ListItem
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

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

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val (selectedForm, setSelectedForm) = remember { mutableStateOf<String?>(null) }

            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { if (selectedForm == null) Text("JSON Forms") else Text(selectedForm) },
                        navigationIcon = if (selectedForm == null) null else {
                            {
                                IconButton(onClick = { setSelectedForm(null) }) {
                                    Icon(Icons.Default.ArrowBack, "Go Back")
                                }
                            }
                        },
                    )
                }
            ) {
                Box(Modifier.padding(it)) {
                    AnimatedContent(selectedForm) { s ->
                        if (s == null) {
                            Column(
                                Modifier.fillMaxSize().padding(16.dp).verticalScroll(rememberScrollState())
                            ) {
                                allForms.forEach {
                                    ListItem(
                                        Modifier
                                            .clickable { setSelectedForm(it) }
                                    ) { Text(it) }
                                }
                            }
                        } else {
                            RenderFormPreview(path = s)
                        }
                    }
                }
            }
        }
    }
}
