package com.copperleaf.forms.compose.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.TextFieldValue

public class UpdatableAnnotatedTextFieldValue(
    private val textPosition: MutableState<TextFieldValue>,
    private val annotatedTextString: MutableState<AnnotatedString>,
    private val onTextChange: (AnnotatedString) -> Unit
) {
    private val currentText: AnnotatedString get() = annotatedTextString.value
    private val currentValue = derivedStateOf {
        textPosition.value.copy(
            annotatedString = annotatedTextString.value
        )
    }

    public fun get(): TextFieldValue {
        return currentValue.value
    }

    public operator fun component1(): TextFieldValue {
        return currentValue.value
    }

    public fun update(newValue: TextFieldValue, sendUpdate: Boolean = true) {
        if (newValue.annotatedString != currentText && sendUpdate) {
            onTextChange(newValue.annotatedString)
        }

        annotatedTextString.value = newValue.annotatedString
        textPosition.value = newValue
    }

    public operator fun component2(): (TextFieldValue) -> Unit {
        return { tfv: TextFieldValue -> update(tfv) }
    }
}

@Composable
public fun rememberUpdatableAnnotatedText(
    initialValue: AnnotatedString,
    onTextChange: (AnnotatedString) -> Unit
): UpdatableAnnotatedTextFieldValue {
    return rememberUpdatableAnnotatedText(mutableStateOf(initialValue), onTextChange)
}

@Composable
public fun rememberUpdatableAnnotatedText(
    inputState: State<AnnotatedString>,
    onTextChange: (AnnotatedString) -> Unit
): UpdatableAnnotatedTextFieldValue {
    val value by inputState
    val textPosition = remember { mutableStateOf(TextFieldValue()) }
    val textString = remember(value) { mutableStateOf(value) }

    return UpdatableAnnotatedTextFieldValue(
        textPosition,
        textString,
        onTextChange
    )
}
