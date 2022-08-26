package com.copperleaf.forms.example.web

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import org.jetbrains.compose.web.dom.A
import org.jetbrains.compose.web.dom.Aside
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Li
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.dom.Ul
import org.jetbrains.compose.web.renderComposable

val allForms = listOf(
    "/kitchenSink",
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
    "/nestedArray",
    "/rule",
    "/listWithDetail",
)

fun main() {
    renderComposable("root") {
        var selectedForm by remember { mutableStateOf("/kitchenSink") }

        Div({ classes("columns") }) {
            Div({ classes("column", "is-3") }) {
                Aside({ classes("menu") }) {
                    P({ classes("menu-label") }) {
                        Text("Examples")
                    }
                    Ul({ classes("menu-list") }) {
                        allForms.forEach { form ->
                            Li() {
                                A(null, {
                                    if (selectedForm == form) {
                                        classes("is-active")
                                    }

                                    onClick {
                                        selectedForm = form
                                        console.log("setting form to $form")
                                    }
                                }) { Text(form) }
                            }
                        }
                    }
                }
            }

            RenderFormPreview(selectedForm)
        }
    }
}

