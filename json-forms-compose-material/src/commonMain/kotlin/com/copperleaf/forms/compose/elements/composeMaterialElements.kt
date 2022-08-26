package com.copperleaf.forms.compose.elements

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScrollableTabRow
import androidx.compose.material.Tab
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.copperleaf.forms.compose.form.Registered
import com.copperleaf.forms.compose.form.UiElement
import com.copperleaf.forms.compose.form.uiElement
import com.copperleaf.forms.compose.rules.RuleLayout
import com.copperleaf.forms.core.Categorization
import com.copperleaf.forms.core.Category
import com.copperleaf.forms.core.ui.UiElement
import com.copperleaf.json.values.string

@OptIn(ExperimentalAnimationApi::class)
public fun Categorization.element(): Registered<UiElement.ElementWithChildren, UiElementRenderer> =
    uiElement {
        Column(Modifier.fillMaxWidth()) {
            var selectedTab by remember { mutableStateOf(0) }
            ScrollableTabRow(
                selectedTabIndex = selectedTab,
            ) {
                element.elements.forEachIndexed { index, category ->
                    RuleLayout(category) {
                        val label = category.uiSchemaConfig.string("label")
                        Tab(
                            selected = index == selectedTab,
                            onClick = { selectedTab = index },
                            text = { Text(label) },
                        )
                    }
                }
            }
            AnimatedContent(selectedTab) { selectedIndex ->
                UiElement(element.elements[selectedIndex])
            }
        }
    }

public fun Category.element(): Registered<UiElement.ElementWithChildren, UiElementRenderer> = uiElement {
    val label = element.uiSchemaConfig.string("label")

    Column(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Text(label, style = MaterialTheme.typography.h4)
            Divider()

            element.elements.forEach { childElement ->
                Box {
                    UiElement(childElement)
                }
            }
        }
    }
}
