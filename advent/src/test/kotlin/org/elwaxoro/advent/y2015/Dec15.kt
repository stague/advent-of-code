package org.elwaxoro.advent.y2015

import org.elwaxoro.advent.PuzzleDayTester
import org.elwaxoro.advent.merge
import org.elwaxoro.advent.plusNull

class Dec15 : PuzzleDayTester(15, 2015) {

    override fun part1(): Any = expandScoops(100L, mapOf(), parse(), null) ?: -1

    override fun part2(): Any = expandScoops(100L, mapOf(), parse(), 500) ?: -1

    private fun expandScoops(
        remainingScoops: Long,
        scooped: Map<Ingredient, Long>,
        unscooped: List<Ingredient>,
        targetCalories: Long?
    ): Long? =
        if (unscooped.isEmpty()) {
            // base case: all scoops are scooped
            scooped.toMap().score().takeIf { targetCalories == null || scooped.calories() == targetCalories }
        } else if (targetCalories != null && scooped.calories() > targetCalories) {
            // base case: over calorie limit
            null
        } else {
            (0..remainingScoops).mapNotNull {
                expandScoops(
                    remainingScoops - it,
                    scooped.plus(unscooped.first() to it),
                    unscooped.drop(1),
                    targetCalories
                )
            }.maxOrNull()
        }

    private fun Map<Ingredient, Long>.calories(): Long = entries.sumOf { (ingredient, scoops) ->
        ingredient.attributes.entries.single { it.key == "calories" }.value * scoops
    }

    private fun Map<Ingredient, Long>.score(): Long = entries.map { (ingredient, scoops) ->
        ingredient.attributes.map { it.key to it.value * scoops }.toMap()
    }.merge(Long::plusNull)
        .filter { it.value > 0 && it.key != "calories" }.values.fold(1L) { acc, score -> acc * score }

    private fun parse() = load().map {
        it.split(": ").let { (name, attributes) ->
            Ingredient(name, attributes.split(", ").associate {
                it.split(" ").let { (attr, value) ->
                    attr to value.toLong()
                }
            })
        }
    }

    data class Ingredient(val name: String, val attributes: Map<String, Long>)
}
