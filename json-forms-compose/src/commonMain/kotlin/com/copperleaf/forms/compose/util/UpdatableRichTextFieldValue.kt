package com.copperleaf.forms.compose.util

//class UpdatableRichTextFieldValue(
//    private val richTextValue: MutableState<RichTextValue>,
//    private val textString: MutableState<String>,
//    private val onTextChange: (String) -> Unit
//) {
//    private val currentText: String get() = textString.value
//    private val currentValue: State<RichTextValue> = derivedStateOf {
//        richTextValue.value.copy()
//    }
//
//    fun get(): RichTextValue {
//        return currentValue.value
//    }
//    operator fun component1(): RichTextValue {
//        return currentValue.value
//    }
//
//    fun update(newValue: RichTextValue, sendUpdate: Boolean = true) {
//        if (newValue.value.text != currentText && sendUpdate) {
//            onTextChange(newValue.value.text)
//        }
//
//        textString.value = newValue.value.text
//        richTextValue.value = newValue
//    }
//
//    operator fun component2(): (RichTextValue) -> Unit {
//        return { tfv: RichTextValue -> update(tfv) }
//    }
//}
//
//fun UpdatableRichTextFieldValue.updateText(newValue: String, sendUpdate: Boolean = true) {
//    update(get().copy(text = newValue), sendUpdate)
//}
//
//@Composable
//fun rememberUpdatableRichText(
//    initialValue: String,
//    onTextChange: (String) -> Unit
//): UpdatableRichTextFieldValue {
//    return rememberUpdatableRichText(mutableStateOf(initialValue), onTextChange)
//}
//
//@Composable
//fun rememberUpdatableRichText(
//    inputState: State<String>,
//    onTextChange: (String) -> Unit
//): UpdatableRichTextFieldValue {
//    val value by inputState
//    val textPosition = remember { mutableStateOf(TextFieldValue()) }
//    val textString = remember(value) { mutableStateOf(value) }
//
//    return UpdatableRichTextFieldValue(
//        textPosition,
//        textString,
//        onTextChange
//    )
//}
//
//@Composable
//fun <T : Any> rememberUpdatableRichText(
//    inputState: State<T>,
//    mapStateToText: (T) -> String,
//    onTextChange: (String) -> Unit,
//): UpdatableRichTextFieldValue {
//    val value: T by inputState
//    val textPosition: MutableState<TextFieldValue> = remember { mutableStateOf(TextFieldValue()) }
//    val textString: MutableState<String> = remember(value) { mutableStateOf(mapStateToText(value)) }
//
//    return UpdatableRichTextFieldValue(
//        textPosition,
//        textString,
//        onTextChange
//    )
//}
//
