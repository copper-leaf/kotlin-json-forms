package com.copperleaf.forms.core

public abstract class UiElementType(public val type: String)

public object VerticalLayout : UiElementType("VerticalLayout")
public object HorizontalLayout : UiElementType("HorizontalLayout")
public object Control : UiElementType("Control")
public object Label : UiElementType("Label")
public object Group : UiElementType("Group")
public object Categorization : UiElementType("Categorization")
public object Category : UiElementType("Category")
public object ListWithDetail : UiElementType("ListWithDetail")
