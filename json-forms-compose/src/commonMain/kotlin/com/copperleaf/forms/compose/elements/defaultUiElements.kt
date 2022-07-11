package com.copperleaf.forms.compose.elements

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
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
import com.copperleaf.forms.core.Group
import com.copperleaf.forms.core.HorizontalLayout
import com.copperleaf.forms.core.Label
import com.copperleaf.forms.core.ListWithDetail
import com.copperleaf.forms.core.VerticalLayout
import com.copperleaf.forms.core.ui.UiElement
import com.copperleaf.json.values.optional
import com.copperleaf.json.values.string

public fun VerticalLayout.element(): Registered<UiElement.ElementWithChildren, UiElementRenderer> = uiElement {
    Column(Modifier.fillMaxWidth()) {
        element.elements.forEach {
            Box {
                UiElement(it)
            }
        }
    }
}

public fun HorizontalLayout.element(): Registered<UiElement.ElementWithChildren, UiElementRenderer> = uiElement {
    Row(Modifier.fillMaxWidth().wrapContentHeight()) {
        element.elements.forEach {
            Box(Modifier.weight(1f)) {
                UiElement(it)
            }
        }
    }
}

public fun Label.element(): Registered<UiElement.ElementWithChildren, UiElementRenderer> = uiElement {
    val text = element.uiSchemaConfig.string("text")

    Column(Modifier.fillMaxWidth().padding(16.dp)) {
        Text(text)
    }
}

public fun Group.element(): Registered<UiElement.ElementWithChildren, UiElementRenderer> = uiElement {
    val label = element.uiSchemaConfig.optional { string("label") }

    Column(Modifier.padding(16.dp)) {
        if (label != null) {
            Text(label)
        }
        element.elements.forEach {
            Box {
                UiElement(it)
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
public fun Categorization.element(): Registered<UiElement.ElementWithChildren, UiElementRenderer> =
    uiElement {
        Column(Modifier.fillMaxWidth()) {
            var selectedTab by remember { mutableStateOf(0) }
            ScrollableTabRow(
                selectedTabIndex = selectedTab,
            ) {
                element.elements.forEachIndexed { index, category ->
                    RuleLayout(uiElement = category, animated = false) {
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
        Column(Modifier.padding(16.dp)) {
            Text(label)
            element.elements.forEach { childElement ->
                Box {
                    UiElement(childElement)
                }
            }
        }
    }
}

public fun ListWithDetail.element(): Registered<UiElement.ElementWithChildren, UiElementRenderer> = uiElement {
}
