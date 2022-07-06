package com.copperleaf.forms.compose.ui

import com.copperleaf.forms.compose.util.Registered
import com.copperleaf.forms.compose.util.UiElementRenderer
import com.copperleaf.forms.core.Categorization
import com.copperleaf.forms.core.Category
import com.copperleaf.forms.core.Control
import com.copperleaf.forms.core.Group
import com.copperleaf.forms.core.HorizontalLayout
import com.copperleaf.forms.core.Label
import com.copperleaf.forms.core.ListWithDetail
import com.copperleaf.forms.core.VerticalLayout
import com.copperleaf.forms.core.ui.UiElement

public actual fun UiElement.Companion.defaults(): List<Registered<UiElement.ElementWithChildren, UiElementRenderer>> =
    listOf(
        VerticalLayout.element(),
        HorizontalLayout.element(),
        Control.element(),
        Label.element(),
        Group.element(),
        Categorization.element(),
        Category.element(),
        ListWithDetail.element(),
    )
