package com.copperleaf.json.utils

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

class TestJsonMerge : FreeSpec({
    "shallow merge object" {
        val a = json {
            "one" to 1
        }
        val b = json {
            "two" to 2
        }

        (a.merge(b).toJsonString(true)) shouldBe """
            {
                "one": 1,
                "two": 2
            }
        """.trimIndent()
    }
    "2-level merge object" {
        val a = json {
            "1" to {
                "one" to 1
            }
        }
        val b = json {
            "1" to {
                "two" to 2
            }
        }

        (a.merge(b).toJsonString(true)) shouldBe """
            {
                "1": {
                    "one": 1,
                    "two": 2
                }
            }
        """.trimIndent()
    }
    "3-level merge object" {
        val a = json {
            "1" to {
                "1a" to {
                    "one" to 1
                }
                "1b" to {
                    "three" to 3
                }
            }
            "2" to {
                "2a" to {
                    "five" to 5
                }
                "2b" to {
                    "seven" to 7
                }
            }
        }
        val b = json {
            "1" to {
                "1a" to {
                    "two" to 2
                }
                "1b" to {
                    "four" to 4
                }
            }
            "2" to {
                "2a" to {
                    "six" to 6
                }
                "2b" to {
                    "eight" to 8
                }
            }
        }

        (a.merge(b).toJsonString(true)) shouldBe """
            {
                "1": {
                    "1a": {
                        "one": 1,
                        "two": 2
                    },
                    "1b": {
                        "three": 3,
                        "four": 4
                    }
                },
                "2": {
                    "2a": {
                        "five": 5,
                        "six": 6
                    },
                    "2b": {
                        "seven": 7,
                        "eight": 8
                    }
                }
            }
        """.trimIndent()
    }

    "shallow merge array" {
        val a = json {
            "arr"[
                1,
                2
            ]
        }
        val b = json {
            "arr"[
                3,
                4
            ]
        }

        (a.merge(b).toJsonString(true)) shouldBe """
            {
                "arr": [
                    1,
                    2,
                    3,
                    4
                ]
            }
        """.trimIndent()
    }
    "2-level merge array" {
        val a = json {
            "arr"[
                jsonArray(1, 11),
                jsonArray(2, 22),
            ]
        }
        val b = json {
            "arr"[
                jsonArray(3, 33),
                jsonArray(4, 44),
            ]
        }

        (a.merge(b).toJsonString(true)) shouldBe """
            {
                "arr": [
                    [
                        1,
                        11
                    ],
                    [
                        2,
                        22
                    ],
                    [
                        3,
                        33
                    ],
                    [
                        4,
                        44
                    ]
                ]
            }
        """.trimIndent()
    }
})
