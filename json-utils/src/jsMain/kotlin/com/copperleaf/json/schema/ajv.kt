package com.copperleaf.json.schema

@JsModule("ajv")
@JsNonModule
public external class Ajv() {
    public fun validate(schema: dynamic, data: dynamic): Boolean
    public fun validateSchema(schema: dynamic): Boolean
    public fun addKeyword(keyword: String)
    public fun compile(keyword: dynamic): (dynamic)->Boolean

    public val errors: dynamic
}

@JsModule("ajv-formats")
@JsNonModule
public external fun ajvFormats(ajv: Ajv)
