package com.copperleaf.forms.core

import com.copperleaf.forms.core.internal.resolveUiSchema
import com.copperleaf.forms.core.ui.UiElement
import io.kotest.core.spec.style.FreeSpec
import io.kotest.core.spec.style.scopes.FreeSpecTerminalScope
import io.kotest.matchers.shouldBe
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement

class UiSchemaTests : FreeSpec({
    "tests" - {
        "/fields/boolean" {
            testSchema {
                UiElement.ElementWithChildren(
                    elementType = "VerticalLayout",
                    elements = listOf(
                        UiElement.Control(
                            controlType = "boolean",
                            schemaScope = "#/properties/value",
                            dataScope = "#/value",
                            required = false,
                            label = "Value",
                        )
                    )
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
                                    schemaScope = "#/properties/name",
                                    dataScope = "#/name",
                                    required = false,
                                    label = "Name",
                                ),
                                UiElement.Control(
                                    controlType = "integer",
                                    schemaScope = "#/properties/personalData/properties/age",
                                    dataScope = "#/personalData/age",
                                    required = true,
                                    label = "Age",
                                ),
                                UiElement.Control(
                                    controlType = "string",
                                    schemaScope = "#/properties/birthDate",
                                    dataScope = "#/birthDate",
                                    required = false,
                                    label = "Birth Date",
                                ),
                            )
                        ),
                        UiElement.ElementWithChildren(
                            elementType = "Label",
                            elements = emptyList(),
                        ),
                        UiElement.ElementWithChildren(
                            elementType = "HorizontalLayout",
                            elements = listOf(
                                UiElement.Control(
                                    controlType = "number",
                                    schemaScope = "#/properties/personalData/properties/height",
                                    dataScope = "#/personalData/height",
                                    required = true,
                                    label = "Height",
                                ),
                                UiElement.Control(
                                    controlType = "string",
                                    schemaScope = "#/properties/nationality",
                                    dataScope = "#/nationality",
                                    required = true,
                                    label = "Nationality",
                                ),
                                UiElement.Control(
                                    controlType = "string",
                                    schemaScope = "#/properties/occupation",
                                    dataScope = "#/occupation",
                                    required = true,
                                    label = "Occupation",
                                ),
                            )
                        )
                    )
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
                            schemaScope = "#/properties/comments",
                            dataScope = "#/comments",
                            required = false,
                            label = "Comments",
                        ),
                    )
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
            val schema = Json.decodeFromString(JsonElement.serializer(), getJsonFromResources("$rootPath/schema.json"))
            val uiSchema =  Json.decodeFromString(JsonElement.serializer(), getJsonFromResources("$rootPath/uiSchema.json"))

            val resolvedSchema = uiSchema.resolveUiSchema(schema)
            val expectedSchema = expectedResolvedSchema()

            if (expectedSchema != null) {
                resolvedSchema.rootUiElement shouldBe expectedResolvedSchema()
            }
        }
    }
}
