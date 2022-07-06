package com.copperleaf.forms.core

public abstract class ControlType(public val type: String)

public object StringControl : ControlType("string")
public object IntegerControl : ControlType("integer")
public object NumberControl : ControlType("number")
public object BooleanControl : ControlType("boolean")
public object ObjectControl : ControlType("object")
public object ArrayControl : ControlType("array")
