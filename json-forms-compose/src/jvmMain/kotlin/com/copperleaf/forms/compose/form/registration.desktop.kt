package com.copperleaf.forms.compose.form

import com.copperleaf.forms.compose.controls.ControlRenderer
import com.copperleaf.forms.compose.controls.checkbox
import com.copperleaf.forms.compose.controls.checkboxesEnum
import com.copperleaf.forms.compose.controls.checkboxesOneOf
import com.copperleaf.forms.compose.controls.codeEditor
import com.copperleaf.forms.compose.controls.control
import com.copperleaf.forms.compose.controls.dropdownEnum
import com.copperleaf.forms.compose.controls.dropdownOneOf
import com.copperleaf.forms.compose.controls.radioButtonEnum
import com.copperleaf.forms.compose.controls.radioButtonOneOf
import com.copperleaf.forms.compose.controls.richText
import com.copperleaf.forms.compose.controls.switch
import com.copperleaf.forms.compose.elements.UiElementRenderer
import com.copperleaf.forms.compose.elements.element
import com.copperleaf.forms.compose.elements.submit
import com.copperleaf.forms.compose.elements.toggleDebug
import com.copperleaf.forms.core.ArrayControl
import com.copperleaf.forms.core.BooleanControl
import com.copperleaf.forms.core.Button
import com.copperleaf.forms.core.Categorization
import com.copperleaf.forms.core.Category
import com.copperleaf.forms.core.Group
import com.copperleaf.forms.core.HorizontalLayout
import com.copperleaf.forms.core.IntegerControl
import com.copperleaf.forms.core.Label
import com.copperleaf.forms.core.NumberControl
import com.copperleaf.forms.core.ObjectControl
import com.copperleaf.forms.core.StringControl
import com.copperleaf.forms.core.VerticalLayout
import com.copperleaf.forms.core.ui.UiElement

public actual fun UiElement.Control.Companion.defaults(): List<Registered<UiElement.Control, ControlRenderer>> =
    listOf(
        // text fields
        StringControl.control(),
        StringControl.richText(),
        StringControl.codeEditor(),

        // single-select
        StringControl.dropdownEnum(),
        StringControl.dropdownOneOf(),
        StringControl.radioButtonEnum(),
        StringControl.radioButtonOneOf(),

        // multi-select
        ArrayControl.checkboxesEnum(),
        ArrayControl.checkboxesOneOf(),

        // number controls
        IntegerControl.control(),
        NumberControl.control(),

        // boolean controls
        BooleanControl.checkbox(),
        BooleanControl.switch(),

        // composite controls
        ObjectControl.control(),
        ArrayControl.control(),
    )

public actual fun UiElement.Companion.defaults(): List<Registered<UiElement.ElementWithChildren, UiElementRenderer>> =
    listOf(
        VerticalLayout.element(),
        HorizontalLayout.element(),

        Label.element(),

        Group.element(),
        Categorization.element(),
        Category.element(),

        Button.submit(),
        Button.toggleDebug(),
    )
