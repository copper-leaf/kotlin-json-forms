package com.copperleaf.forms.core

import com.copperleaf.forms.core.internal.resolveUiSchema
import com.copperleaf.forms.core.ui.UiElement
import com.copperleaf.json.pointer.AbstractJsonPointer
import com.copperleaf.json.pointer.JsonPointer
import com.copperleaf.json.schema.JsonSchema
import com.copperleaf.json.utils.json
import com.copperleaf.json.utils.parseJson
import io.kotest.core.spec.style.FreeSpec
import io.kotest.core.spec.style.scopes.FreeSpecTerminalScope
import io.kotest.matchers.shouldBe

class UiSchemaTests : FreeSpec({
    "tests" - {
        "/fields/boolean" {
            testSchema {
                UiElement.ElementWithChildren(
                    elementType = "VerticalLayout",
                    elements = listOf(
                        UiElement.Control(
                            controlType = "boolean",
                            schemaScope = JsonPointer.parse("#/properties/value"),
                            dataScope = AbstractJsonPointer.parse("#/value"),
                            required = false,
                            label = "Value",
                            uiSchemaConfig = json {
                                "type" to "Control"
                                "scope" to "#/properties/value"
                            },
                            rule = null,
                            schemaConfig = json {
                                "type" to "boolean"
                            },
                        )
                    ),
                    uiSchemaConfig = json {
                        "type" to "VerticalLayout"
                        "elements"[
                            json {
                                "type" to "Control"
                                "scope" to "#/properties/value"
                            }
                        ]
                    },
                    rule = null,
                )
            }
        }
        "/basic" {
            testSchema {
                UiElement.ElementWithChildren(
                    elementType = "VerticalLayout",
                    elements = listOf(
                        UiElement.ElementWithChildren(
                            elementType = "HorizontalLayout",
                            elements = listOf(
                                UiElement.Control(
                                    controlType = "string",
                                    schemaScope = JsonPointer.parse("#/properties/name"),
                                    dataScope = AbstractJsonPointer.parse("#/name"),
                                    required = false,
                                    label = "Name",
                                    rule = null,
                                    schemaConfig = json {
                                        "type" to "string"
                                        "minLength" to 3
                                        "description" to "Please enter your name"
                                    },
                                    uiSchemaConfig = json {
                                        "type" to "Control"
                                        "scope" to "#/properties/name"
                                    },
                                ),
                                UiElement.Control(
                                    controlType = "integer",
                                    schemaScope = JsonPointer.parse("#/properties/personalData/properties/age"),
                                    dataScope = AbstractJsonPointer.parse("#/personalData/age"),
                                    required = true,
                                    label = "Age",
                                    rule = null,
                                    schemaConfig = json {
                                        "type" to "integer"
                                        "description" to "Please enter your age."
                                    },
                                    uiSchemaConfig = json {
                                        "type" to "Control"
                                        "scope" to "#/properties/personalData/properties/age"
                                    },
                                ),
                                UiElement.Control(
                                    controlType = "string",
                                    schemaScope = JsonPointer.parse("#/properties/birthDate"),
                                    dataScope = AbstractJsonPointer.parse("#/birthDate"),
                                    required = false,
                                    label = "Birth Date",
                                    rule = null,
                                    schemaConfig = json {
                                        "type" to "string"
                                        "format" to "date"
                                    },
                                    uiSchemaConfig = json {
                                        "type" to "Control"
                                        "scope" to "#/properties/birthDate"
                                    },
                                ),
                            ),
                            rule = null,
                            uiSchemaConfig = json {
                                "type" to "HorizontalLayout"
                                "elements"[
                                    json {
                                        "type" to "Control"
                                        "scope" to "#/properties/name"
                                    },
                                    json {
                                        "type" to "Control"
                                        "scope" to "#/properties/personalData/properties/age"
                                    },
                                    json {
                                        "type" to "Control"
                                        "scope" to "#/properties/birthDate"
                                    }
                                ]
                            },
                        ),
                        UiElement.ElementWithChildren(
                            elementType = "Label",
                            elements = emptyList(),
                            rule = null,
                            uiSchemaConfig = json {
                                "type" to "Label"
                                "text" to "Additional Information"
                            },
                        ),
                        UiElement.ElementWithChildren(
                            elementType = "HorizontalLayout",
                            elements = listOf(
                                UiElement.Control(
                                    controlType = "number",
                                    schemaScope = JsonPointer.parse("#/properties/personalData/properties/height"),
                                    dataScope = AbstractJsonPointer.parse("#/personalData/height"),
                                    required = true,
                                    label = "Height",
                                    rule = null,
                                    schemaConfig = json {
                                        "type" to "number"
                                    },
                                    uiSchemaConfig = json {
                                        "type" to "Control"
                                        "scope" to "#/properties/personalData/properties/height"
                                    },
                                ),
                                UiElement.Control(
                                    controlType = "string",
                                    schemaScope = JsonPointer.parse("#/properties/nationality"),
                                    dataScope = AbstractJsonPointer.parse("#/nationality"),
                                    required = true,
                                    label = "Nationality",
                                    rule = null,
                                    schemaConfig = json {
                                        "type" to "string"
                                        "enum"[
                                            "DE",
                                            "IT",
                                            "JP",
                                            "US",
                                            "RU",
                                            "Other"
                                        ]
                                    },
                                    uiSchemaConfig = json {
                                        "type" to "Control"
                                        "scope" to "#/properties/nationality"
                                    },
                                ),
                                UiElement.Control(
                                    controlType = "string",
                                    schemaScope = JsonPointer.parse("#/properties/occupation"),
                                    dataScope = AbstractJsonPointer.parse("#/occupation"),
                                    required = true,
                                    label = "Occupation",
                                    rule = null,
                                    schemaConfig = json {
                                        "type" to "string"
                                    },
                                    uiSchemaConfig = json {
                                        "type" to "Control"
                                        "scope" to "#/properties/occupation"
                                        "suggestion"[
                                            "Accountant",
                                            "Engineer",
                                            "Freelancer",
                                            "Journalism",
                                            "Physician",
                                            "Student",
                                            "Teacher",
                                            "Other"
                                        ]
                                    },
                                ),
                            ),
                            rule = null,
                            uiSchemaConfig = json {
                                "type" to "HorizontalLayout"
                                "elements"[
                                    json {
                                        "type" to "Control"
                                        "scope" to "#/properties/personalData/properties/height"
                                    },
                                    json {
                                        "type" to "Control"
                                        "scope" to "#/properties/nationality"
                                    },
                                    json {
                                        "type" to "Control"
                                        "scope" to "#/properties/occupation"
                                        "suggestion"[
                                            "Accountant",
                                            "Engineer",
                                            "Freelancer",
                                            "Journalism",
                                            "Physician",
                                            "Student",
                                            "Teacher",
                                            "Other"
                                        ]
                                    }
                                ]
                            },
                        )
                    ),
                    rule = null,
                    uiSchemaConfig = json {
                        "type" to "VerticalLayout"
                        "elements"[
                            json {
                                "type" to "HorizontalLayout"
                                "elements"[
                                    json {
                                        "type" to "Control"
                                        "scope" to "#/properties/name"
                                    },
                                    json {
                                        "type" to "Control"
                                        "scope" to "#/properties/personalData/properties/age"
                                    },
                                    json {
                                        "type" to "Control"
                                        "scope" to "#/properties/birthDate"
                                    }
                                ]
                            },
                            json {
                                "type" to "Label"
                                "text" to "Additional Information"
                            },
                            json {
                                "type" to "HorizontalLayout"
                                "elements"[
                                    json {
                                        "type" to "Control"
                                        "scope" to "#/properties/personalData/properties/height"
                                    },
                                    json {
                                        "type" to "Control"
                                        "scope" to "#/properties/nationality"
                                    },
                                    json {
                                        "type" to "Control"
                                        "scope" to "#/properties/occupation"
                                        "suggestion"[
                                            "Accountant",
                                            "Engineer",
                                            "Freelancer",
                                            "Journalism",
                                            "Physician",
                                            "Student",
                                            "Teacher",
                                            "Other"
                                        ]
                                    }
                                ]
                            },
                        ]
                    },
                )
            }
        }
        "/array" {
            testSchema {
                UiElement.ElementWithChildren(
                    elementType = "VerticalLayout",
                    elements = listOf(
                        UiElement.Control(
                            controlType = "array",
                            schemaScope = JsonPointer.parse("#/properties/comments"),
                            dataScope = AbstractJsonPointer.parse("#/comments"),
                            required = false,
                            label = "Comments",
                            rule = null,
                            schemaConfig = json {
                                "type" to "array"
                                "items" to json {
                                    "type" to "object"
                                    "properties" to json {
                                        "date" to json {
                                            "type" to "string"
                                            "format" to "date"
                                        }
                                        "message" to json {
                                            "type" to "string"
                                            "maxLength" to 5
                                        }
                                        "enum" to json {
                                            "type" to "string"
                                            "enum"[
                                                "foo",
                                                "bar"
                                            ]
                                        }
                                    }
                                }
                            },
                            uiSchemaConfig = json {
                                "type" to "Control"
                                "scope" to "#/properties/comments"
                            },
                        ),
                    ),
                    rule = null,
                    uiSchemaConfig = json {
                        "type" to "VerticalLayout"
                        "elements"[
                            json {
                                "type" to "Control"
                                "scope" to "#/properties/comments"
                            }
                        ]
                    },
                )
            }
        }
    }

    "exhaustive tests" - {
        listOf(
            "/fields/boolean",
            "/fields/integer",
            "/fields/number",
            "/fields/string",
            "/fields/richText",
            "/fields/object",
            "/fields/array",
            "/form1",
            "/form2",
            "/basic",
            "/control/1",
            "/control/2",
            "/categorization/1",
            "/categorization/2",
            "/categorization/3",
            "/layout/1",
            "/layout/2",
            "/layout/3",
            "/layout/4",
            "/array",
            "/rule",
            "/listWithDetail",
        ).forEach {
            it {
                testSchema { null }
            }
        }
    }
}) {
    companion object {
        fun FreeSpecTerminalScope.testSchema(expectedResolvedSchema: () -> UiElement?) {
            val rootPath = testScope.testCase.name.testName
            val schema = JsonSchema.parse(getJsonFromResources("$rootPath/schema.json").parseJson())
            val uiSchema = getJsonFromResources("$rootPath/uiSchema.json").parseJson()

            val resolvedSchema = uiSchema.resolveUiSchema(schema)
            val expectedSchema = expectedResolvedSchema()

            if (expectedSchema != null) {
                resolvedSchema.rootUiElement shouldBe expectedResolvedSchema()
            }
        }
    }
}
