package org.elwaxoro.advent.y2015

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.elwaxoro.advent.PuzzleDayTester

/**
 * JSAbacusFramework.io
 */
class Dec12 : PuzzleDayTester(12, 2015) {

    override fun part1(): Any = load().sumOf { Regex("(-?\\d+)").find(it)?.value?.toInt() ?: 0 }

    override fun part2(): Any = Json.parseToJsonElement(loadText(getPath())).score()

    private fun JsonElement.score(): Int = when (this::class.simpleName) {
        "JsonObject" -> jsonObject.takeIf { it.entries.none { it.value.toString() == "\"red\"" } }?.values?.sumOf { it.score() } ?: 0
        "JsonArray" -> jsonArray.sumOf { it.score() }
        "JsonPrimitive" -> jsonPrimitive.intOrNull ?: 0
        "JsonLiteral" -> jsonPrimitive.intOrNull ?: 0
        else -> TODO("What the hell is this type? ${this::class.java} -> $this")
    }
}
