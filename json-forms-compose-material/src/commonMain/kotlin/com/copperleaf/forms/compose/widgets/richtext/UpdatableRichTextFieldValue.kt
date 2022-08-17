package com.copperleaf.forms.compose.widgets.richtext

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.darkrockstudios.richtexteditor.model.RichTextValue

public class UpdatableRichTextFieldValue(
    private val currentValue: MutableState<RichTextValue>,
    private val onTextChange: (RichTextValue) -> Unit,
) {

    public fun get(): RichTextValue {
        return currentValue.value
    }

    public operator fun component1(): RichTextValue {
        return currentValue.value
    }

    public fun update(newValue: RichTextValue, sendUpdate: Boolean = true) {
        if (newValue.value.annotatedString != currentValue.value.value.annotatedString && sendUpdate) {
            onTextChange(newValue)
        }
        currentValue.value = newValue
    }

    public operator fun component2(): (RichTextValue) -> Unit {
        return { tfv: RichTextValue -> update(tfv) }
    }
}

@Composable
public fun rememberUpdatableRichText(
    initialValue: String,
    serializer: RichTextValueSnapshotSerializer = RichTextJsonSerializer(),
    onTextChange: (String) -> Unit
): UpdatableRichTextFieldValue {
    return rememberUpdatableRichText(mutableStateOf(initialValue), serializer, onTextChange)
}

@Composable
public fun rememberUpdatableRichText(
    inputState: State<String>,
    serializer: RichTextValueSnapshotSerializer = RichTextJsonSerializer(),
    onTextChange: (String) -> Unit
): UpdatableRichTextFieldValue {
    val inputText: String by inputState
    val inputRichTextSnapshot: MutableState<RichTextValue> = remember(inputText) {
        val decodedSnapshot = serializer.decodeFromString(inputText)
        val snapshot = RichTextValue.fromSnapshot(decodedSnapshot)
        mutableStateOf(snapshot)
    }

    return UpdatableRichTextFieldValue(
        inputRichTextSnapshot
    ) {
        val encoded = serializer.encodeToString(it.getLastSnapshot())
        onTextChange(encoded)
    }
}
