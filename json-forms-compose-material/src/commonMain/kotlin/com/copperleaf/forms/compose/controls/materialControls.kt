package com.copperleaf.forms.compose.controls

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.copperleaf.forms.compose.form.Registered
import com.copperleaf.forms.compose.form.uiControl
import com.copperleaf.forms.compose.widgets.codeeditor.model.CodeLang
import com.copperleaf.forms.compose.widgets.codeeditor.prettify.PrettifyParser
import com.copperleaf.forms.compose.widgets.codeeditor.theme.CodeThemeType
import com.copperleaf.forms.compose.widgets.codeeditor.utils.parseCodeAsAnnotatedString
import com.copperleaf.forms.compose.widgets.material.arrayWidget
import com.copperleaf.forms.compose.widgets.material.checkboxWidget
import com.copperleaf.forms.compose.widgets.material.checkboxesWidget
import com.copperleaf.forms.compose.widgets.material.dropdownWidget
import com.copperleaf.forms.compose.widgets.material.objectWidget
import com.copperleaf.forms.compose.widgets.material.radioButtonsWidget
import com.copperleaf.forms.compose.widgets.material.switchWidget
import com.copperleaf.forms.compose.widgets.material.textFieldWidget
import com.copperleaf.forms.compose.widgets.richtext.RichTextToolbar
import com.copperleaf.forms.compose.widgets.richtext.rememberUpdatableRichText
import com.copperleaf.forms.compose.widgets.richtext.richTextShortcuts
import com.copperleaf.forms.compose.widgets.text.rememberUpdatableAnnotatedText
import com.copperleaf.forms.core.ArrayControl
import com.copperleaf.forms.core.BooleanControl
import com.copperleaf.forms.core.IntegerControl
import com.copperleaf.forms.core.NumberControl
import com.copperleaf.forms.core.ObjectControl
import com.copperleaf.forms.core.StringControl
import com.copperleaf.forms.core.ui.UiElement
import com.copperleaf.json.values.arrayAt
import com.copperleaf.json.values.objectAt
import com.copperleaf.json.values.optional
import com.copperleaf.json.values.string
import com.darkrockstudios.richtexteditor.ui.RichTextEditor
import com.darkrockstudios.richtexteditor.ui.defaultRichTextFieldStyle
import kotlinx.serialization.json.doubleOrNull
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

// Text Field Controls
// ---------------------------------------------------------------------------------------------------------------------

public fun StringControl.control(): Registered<UiElement.Control, ControlRenderer> = uiControl {
    textFieldWidget(
        defaultValue = "",
        mapper = { it.jsonPrimitive.content },
        mapStateToText = { it },
    )
}

public fun StringControl.richText(): Registered<UiElement.Control, ControlRenderer> = uiControl(
    rank = 10,
    tester = { optionIsEnabled("richText") }
) {
    val currentValue = getTypedValue("") {
        it.jsonPrimitive.content
    }
    val (text, updateText) = rememberUpdatableRichText(currentValue) {
        updateFormState(it)
    }

    Text(control.label, style = MaterialTheme.typography.subtitle1)
    RichTextToolbar(
        modifier = Modifier,
        value = text,
        onValueChange = updateText,
    )

    RichTextEditor(
        modifier = Modifier
            .richTextShortcuts(text, updateText)
            .defaultMinSize(minHeight = 120.dp)
            .border(width = 1.dp, color = MaterialTheme.colors.primary, shape = RoundedCornerShape(4.dp)),
        value = text,
        onValueChange = updateText,
        textFieldStyle = defaultRichTextFieldStyle().copy(
            textColor = MaterialTheme.colors.onSurface,
            cursorColor = MaterialTheme.colors.primary
        ),
    )
}

public fun StringControl.codeEditor(): Registered<UiElement.Control, ControlRenderer> = uiControl(
    rank = 20,
    tester = { optionIsEnabled("codeEditor") }
) {
    val language = control.uiSchemaConfig.optional {
        this.objectAt("options").string("lang").let { languageName ->
            CodeLang.values().firstOrNull { language ->
                languageName in language.value || languageName == language.name
            }
        }
    } ?: CodeLang.HTML
    val parser = remember { PrettifyParser() } // try getting from LocalPrettifyParser.current
    val theme = remember { CodeThemeType.Monokai.getTheme() }
    val currentValue = getTypedValue(AnnotatedString("")) {
        parseCodeAsAnnotatedString(
            parser = parser,
            theme = theme,
            lang = language,
            code = it.jsonPrimitive.content
        )
    }
    val (text: TextFieldValue, updateText: (TextFieldValue) -> Unit) = rememberUpdatableAnnotatedText(
        initialValue = currentValue,
        onTextChange = { value ->
            updateFormState(value.text)
        }
    )

    var lineTops by remember { mutableStateOf(emptyArray<Float>()) }
    val density = LocalDensity.current

    Text(control.label, style = MaterialTheme.typography.subtitle1)
    Row(
        modifier = Modifier
            .background(MaterialTheme.colors.onSurface.copy(alpha = 0.5f))
            .border(width = 1.dp, color = MaterialTheme.colors.onSurface.copy(alpha = 0.25f))
    ) {
        if (lineTops.isNotEmpty()) {
            Box(modifier = Modifier.padding(horizontal = 4.dp)) {
                lineTops.forEachIndexed { index, top ->
                    Text(
                        modifier = Modifier.offset(y = with(density) { top.toDp() }),
                        text = index.toString(),
                        color = MaterialTheme.colors.onBackground.copy(.3f)
                    )
                }
            }
        }
        BasicTextField(
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = 120.dp),
            value = text,
            onValueChange = updateText,
            enabled = isEnabled,
            onTextLayout = { result ->
                lineTops = Array(result.lineCount) { result.getLineTop(it) }
            }
        )
    }
}

// Single-Select Controls
// ---------------------------------------------------------------------------------------------------------------------

public fun StringControl.dropdownEnum(): Registered<UiElement.Control, ControlRenderer> = uiControl(
    rank = 20,
    tester = { hasSchemaProperty("enum") }
) {
    dropdownWidget {
        arrayAt("enum").map { it.jsonPrimitive.content to it.jsonPrimitive.content }
    }
}

public fun StringControl.dropdownOneOf(): Registered<UiElement.Control, ControlRenderer> = uiControl(
    rank = 21,
    tester = { hasSchemaProperty("oneOf") }
) {
    dropdownWidget {
        arrayAt("oneOf").map {
            it.jsonObject.string("const") to it.jsonObject.string("title")
        }
    }
}

public fun StringControl.radioButtonEnum(): Registered<UiElement.Control, ControlRenderer> = uiControl(
    rank = 30,
    tester = { hasSchemaProperty("enum") && optionFieldIs("format", "radio") }
) {
    radioButtonsWidget {
        arrayAt("enum").map { it.jsonPrimitive.content to it.jsonPrimitive.content }
    }
}

public fun StringControl.radioButtonOneOf(): Registered<UiElement.Control, ControlRenderer> = uiControl(
    rank = 31,
    tester = { hasSchemaProperty("oneOf") && optionFieldIs("format", "radio") }
) {
    radioButtonsWidget {
        arrayAt("oneOf").map {
            it.jsonObject.string("const") to it.jsonObject.string("title")
        }
    }
}

// Multi-Select Controls
// ---------------------------------------------------------------------------------------------------------------------

public fun ArrayControl.checkboxesEnum(): Registered<UiElement.Control, ControlRenderer> = uiControl(
    rank = 20,
    tester = { hasSchemaProperty("uniqueItems") && hasArrayItemType(StringControl) && hasArrayItemProperty("enum") }
) {
    checkboxesWidget {
        objectAt("items")
            .arrayAt("enum")
            .map { it.jsonPrimitive.content to it.jsonPrimitive.content }
    }
}

public fun ArrayControl.checkboxesOneOf(): Registered<UiElement.Control, ControlRenderer> = uiControl(
    rank = 21,
    tester = { hasSchemaProperty("uniqueItems") && hasArrayItemProperty("oneOf") }
) {
    checkboxesWidget {
        objectAt("items")
            .arrayAt("oneOf")
            .map { it.string("const") to it.string("title") }
    }
}

// Number Controls
// ---------------------------------------------------------------------------------------------------------------------

public fun IntegerControl.control(): Registered<UiElement.Control, ControlRenderer> = uiControl {
    textFieldWidget(
        defaultValue = 0,
        mapper = { it.jsonPrimitive.intOrNull },
        mapStateToText = { it.toString() },
        nudgeUp = { it + 1 },
        nudgeDown = { it - 1 },
    )
}

public fun NumberControl.control(): Registered<UiElement.Control, ControlRenderer> = uiControl {
    textFieldWidget(
        defaultValue = 0.0,
        mapper = { it.jsonPrimitive.doubleOrNull },
        mapStateToText = { it.toString() },
        nudgeUp = { it + 1.0 },
        nudgeDown = { it - 1.0 },
    )
}

// Boolean Controls
// ---------------------------------------------------------------------------------------------------------------------

public fun BooleanControl.checkbox(): Registered<UiElement.Control, ControlRenderer> = uiControl {
    checkboxWidget()
}

public fun BooleanControl.switch(): Registered<UiElement.Control, ControlRenderer> = uiControl(
    rank = 10,
    tester = { optionIsEnabled("toggle") }
) {
    switchWidget()
}

// Composite Controls
// ---------------------------------------------------------------------------------------------------------------------

public fun ObjectControl.control(): Registered<UiElement.Control, ControlRenderer> = uiControl {
    objectWidget()
}

public fun ArrayControl.control(): Registered<UiElement.Control, ControlRenderer> = uiControl {
    arrayWidget("Add new ${control.label}")
}

public fun ArrayControl.arrayOfObjects(): Registered<UiElement.Control, ControlRenderer> = uiControl(
    rank = 10,
    tester = { hasArrayItemType(ObjectControl) }
) {
    arrayWidget("Add to ${control.label}")
}
