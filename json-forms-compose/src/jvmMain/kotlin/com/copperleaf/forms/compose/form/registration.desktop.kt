package com.copperleaf.forms.compose.form

import com.copperleaf.forms.compose.controls.control
import com.copperleaf.forms.compose.controls.dropdownEnum
import com.copperleaf.forms.compose.controls.richText
import com.copperleaf.forms.compose.elements.element
import com.copperleaf.forms.core.ArrayControl
import com.copperleaf.forms.core.BooleanControl
import com.copperleaf.forms.core.Categorization
import com.copperleaf.forms.core.Category
import com.copperleaf.forms.core.Control
import com.copperleaf.forms.core.Group
import com.copperleaf.forms.core.HorizontalLayout
import com.copperleaf.forms.core.IntegerControl
import com.copperleaf.forms.core.Label
import com.copperleaf.forms.core.ListWithDetail
import com.copperleaf.forms.core.NumberControl
import com.copperleaf.forms.core.ObjectControl
import com.copperleaf.forms.core.StringControl
import com.copperleaf.forms.core.VerticalLayout
import com.copperleaf.forms.core.ui.UiElement

public actual fun UiElement.Control.Companion.defaults(): List<Registered<UiElement.Control, ControlRenderer>> =
    listOf(
        StringControl.control(),
        StringControl.richText(),
        StringControl.dropdownEnum(),

        IntegerControl.control(),
        NumberControl.control(),
        BooleanControl.control(),
        ObjectControl.control(),
        ArrayControl.control(),
    )

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
