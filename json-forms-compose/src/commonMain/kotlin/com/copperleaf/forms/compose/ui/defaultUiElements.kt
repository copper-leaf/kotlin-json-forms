package com.copperleaf.forms.compose.ui

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
import com.copperleaf.forms.compose.util.Registered
import com.copperleaf.forms.compose.util.UiElementRenderer
import com.copperleaf.forms.core.Categorization
import com.copperleaf.forms.core.Category
import com.copperleaf.forms.core.Control
import com.copperleaf.forms.core.Group
import com.copperleaf.forms.core.HorizontalLayout
import com.copperleaf.forms.core.Label
import com.copperleaf.forms.core.ListWithDetail
import com.copperleaf.forms.core.UiElementType
import com.copperleaf.forms.core.VerticalLayout
import com.copperleaf.forms.core.ui.UiElement

public fun UiElementType.uiElement(
    rank: Int = 0,
    tester: (UiElement.ElementWithChildren) -> Boolean = { it.elementType == this@uiElement.type },
    renderer: UiElementRenderer,
): Registered<UiElement.ElementWithChildren, UiElementRenderer> {
    return Registered(
        rank = rank,
        tester = tester,
        renderer = renderer,
    )
}

expect public fun UiElement.Companion.defaults(): List<Registered<UiElement.ElementWithChildren, UiElementRenderer>>

public fun VerticalLayout.element(): Registered<UiElement.ElementWithChildren, UiElementRenderer> =
    uiElement { element ->
        Column(Modifier.fillMaxWidth()) {
            element.elements.forEach {
                Box {
                    RenderGenericUiElement(it)
                }
            }
        }
    }

public fun HorizontalLayout.element(): Registered<UiElement.ElementWithChildren, UiElementRenderer> =
    uiElement { element ->
        Row(Modifier.fillMaxWidth().wrapContentHeight()) {
            element.elements.forEach {
                Box(Modifier.weight(1f)) {
                    RenderGenericUiElement(it)
                }
            }
        }
    }

public fun Control.element(): Registered<UiElement.ElementWithChildren, UiElementRenderer> = uiElement { element ->
    Column(Modifier.fillMaxWidth().padding(16.dp)) {
        RenderUiControl(element as UiElement.Control)
    }
}

public fun Label.element(): Registered<UiElement.ElementWithChildren, UiElementRenderer> = uiElement { element ->
    val text = element.uiSchemaConfig.requireString("text")

    Column(Modifier.fillMaxWidth().padding(16.dp)) {
        Text(text)
    }
}

public fun Group.element(): Registered<UiElement.ElementWithChildren, UiElementRenderer> = uiElement { element ->
    val label = element.uiSchemaConfig.optionalString("label")

    Column(Modifier.padding(16.dp)) {
        if (label != null) {
            Text(label)
        }
        element.elements.forEach {
            Box {
                RenderGenericUiElement(it)
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
public fun Categorization.element(): Registered<UiElement.ElementWithChildren, UiElementRenderer> =
    uiElement { element ->
        Column(Modifier.fillMaxWidth()) {
            var selectedTab by remember { mutableStateOf(0) }
            ScrollableTabRow(
                selectedTabIndex = selectedTab,
            ) {
                element.elements.forEachIndexed { index, category ->
                    RuleLayout(uiElement = category, animated = false) {
                        val label = category.uiSchemaConfig.requireString("label")
                        Tab(
                            selected = index == selectedTab,
                            onClick = { selectedTab = index },
                            text = { Text(label) },
                        )
                    }
                }
            }
            AnimatedContent(selectedTab) { selectedIndex ->
                RenderGenericUiElement(element.elements[selectedIndex])
            }
        }
    }

public fun Category.element(): Registered<UiElement.ElementWithChildren, UiElementRenderer> = uiElement { element ->
    val label = element.uiSchemaConfig.requireString("label")

    Column(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp)) {
            Text(label)
            element.elements.forEach { childElement ->
                Box {
                    RenderGenericUiElement(childElement)
                }
            }
        }
    }
}

public fun ListWithDetail.element(): Registered<UiElement.ElementWithChildren, UiElementRenderer> =
    uiElement { element ->
    }
