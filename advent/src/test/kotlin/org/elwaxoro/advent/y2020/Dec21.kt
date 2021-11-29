package org.elwaxoro.advent.y2020

import org.elwaxoro.advent.PuzzleDayTester

class Dec21 : PuzzleDayTester(21, 2020) {

    override fun puzzle1(): Any = parse().let { inputLines ->
        foodPoisioningMap(inputLines).let { poisoning ->
            val badIngredients = poisoning.values
            // remove all the bad ingredients, count up what's left (leave in dupes)
            inputLines.map { it.second.filterNot { badIngredients.contains(it) } }.flatten().size
        }
    }

    // sort by allergen, then print list of ingredients
    override fun puzzle2(): Any =
        foodPoisioningMap(parse()).toList().sortedBy { it.first }.joinToString(",") { it.second }

    // parse input into a list of pairs: first is the set of allergens, second is the set of ingredients
    private fun parse(): List<Pair<Set<String>, Set<String>>> = load().map { line ->
        line.split(" (contains ").let { split ->
            split[1].replace(')', ' ').split(", ").map { it.trim() }.toSet() to split[0].split(" ").toSet()
        }
    }

    // returns map of 1 allergen to 1 ingredient
    private fun foodPoisioningMap(inputLines: List<Pair<Set<String>, Set<String>>>): Map<String, String> {

        // set of all the allergens exactly once
        val uniqueAllergens = inputLines.map { it.first }.flatten().toSet()

        // map allergen to the set of common ingredients across all lines
        val allergenMap: Map<String, Set<String>> = uniqueAllergens.map { allergen ->
            // for just lines with the allergen, intersect their ingredient sets together, so only common ingredients remain
            allergen to inputLines.filter { it.first.contains(allergen) }.map { it.second }
                .reduce { acc, set -> acc.intersect(set) }
        }.toMap()

        // split the allergens into known (1 allergen to 1 ingredient) and unknown (1 allergen to multiple ingredients)
        val partition = allergenMap.toList().partition { it.second.size == 1 }
        val known = partition.first.map { it.first to it.second.single() }.toMap(mutableMapOf())
        val unknown: MutableMap<String, Set<String>> = partition.second.toMap(mutableMapOf())

        // INTO THE UNKNOOOOOWWWWWWNNNNNNNN
        while (unknown.isNotEmpty()) {
            // for every unknown allergen, subtract out all the known ingredients, then see if anything is down to ingredient size 1
            unknown.map { it.key to it.value - known.values }.filter { it.second.size == 1 }.let { freshFinds ->
                // TODO this turned out nasty. better way?
                known.putAll(freshFinds.map { it.first to it.second.single() }.toMap())
                freshFinds.forEach { unknown.remove(it.first) }
            }
        }
        return known
    }
}
