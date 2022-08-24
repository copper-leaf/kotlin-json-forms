package com.copperleaf.forms.compose.bulma.elements

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.copperleaf.forms.compose.elements.UiElementRenderer
import com.copperleaf.forms.compose.form.Registered
import com.copperleaf.forms.compose.form.UiElement
import com.copperleaf.forms.compose.form.uiElement
import com.copperleaf.forms.compose.rules.RuleLayout
import com.copperleaf.forms.core.Categorization
import com.copperleaf.forms.core.Category
import com.copperleaf.forms.core.ui.UiElement
import com.copperleaf.json.values.string
import org.jetbrains.compose.web.dom.A
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H4
import org.jetbrains.compose.web.dom.Hr
import org.jetbrains.compose.web.dom.Li
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.dom.Ul

public fun Categorization.element(): Registered<UiElement.ElementWithChildren, UiElementRenderer> = uiElement {
    column {
        var selectedTab by remember { mutableStateOf(0) }

        Div({ classes("tabs") }) {
            Ul {
                element.elements.forEachIndexed { index, category ->
                    RuleLayout(uiElement = category) {
                        val label = category.uiSchemaConfig.string("label")
                        Li({
                            if (index == selectedTab) {
                                classes("is-active")
                            }
                            onClick { selectedTab = index }
                        }) {
                            A(null, {}) {
                                Text(label)
                            }
                        }
                    }
                }
            }
        }

        UiElement(element.elements[selectedTab])
    }
}

public fun Category.element(): Registered<UiElement.ElementWithChildren, UiElementRenderer> = uiElement {
    val label = element.uiSchemaConfig.string("label")

    column {
        H4 { Text(label) }
        Hr()

        element.elements.forEach { childElement ->
            box {
                UiElement(childElement)
            }
        }
    }
}
