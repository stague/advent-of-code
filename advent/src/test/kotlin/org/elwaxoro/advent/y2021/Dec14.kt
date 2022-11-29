package org.elwaxoro.advent.y2021

import org.elwaxoro.advent.PuzzleDayTester
import org.elwaxoro.advent.merge
import org.elwaxoro.advent.plusNull

/**
 * Extended Polymerization
 */
class Dec14 : PuzzleDayTester(14, 2021) {

    /**
     * HELL YEA LETS MAKE A GIANT STRING WOO!
     */
    override fun part1(): Any = parse().let { (template, rules) ->
        (1..10).fold(template) { acc, _ ->
            acc.zipWithNext { a, b ->
                a + rules.getOrDefault("$a$b", "")
            }.joinToString("") + template.last()
        }.toCharArray().toList().groupingBy { it }.eachCount().let { counts ->
            counts.maxOf { it.value } - counts.minOf { it.value }
        }
    }

    /**
     * Part 1 blows up pretty fast here. CRAAAAAAAP
     * Idea: track the growing string as a list of pairs with counts instead
     * Each entry in the map becomes two new entries based on the rules for splitting
     * EX: AB with rule AB->C becomes AC, CB each with a count of 1
     * C is now included twice, so strip the pairs down at the end to just take the first char from each pair:
     * AC:1,CB:1 -> A:1,C:1 but that leaves out the final original B entirely, so add that in manually at the end (just like part one)
     */
    override fun part2(): Any = parse().let { (template, rules) ->
        val initialMap: Map<String, Long> = template.zipWithNext { a, b -> "$a$b" }.associateWith { 1L }
        (1..40).fold(initialMap) { pairAccumulator, _ ->
            pairAccumulator.entries.map { (pair, count) ->
                mapOf(
                    pair.first() + rules[pair]!! to count,
                    rules[pair]!! + pair.last() to count
                )
            }.merge(Long::plusNull) // Extension to combine a list of maps into a single new map
        }.entries.fold(mutableMapOf<Char, Long>()) { acc, (pair, count) ->
            acc.also { acc[pair.first()] = acc.getOrDefault(pair.first(), 0L) + count }
        }.also { acc ->
            acc[template.last()] = acc.getOrDefault(template.last(), 0L) + 1L
        }.let { counts ->
            counts.maxOf { it.value } - counts.minOf { it.value } // FINALLY!
        }
    }

    fun parse(): Pair<String, Map<String, String>> = load(delimiter = "\n\n").let { (template, pairs) ->
        Pair(template, pairs.split("\n").associate { line ->
            line.split(" -> ").let {
                it.first() to it.last()
            }
        })
    }
}
