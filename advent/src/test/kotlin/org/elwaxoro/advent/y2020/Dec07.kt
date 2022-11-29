package org.elwaxoro.advent.y2020

import org.elwaxoro.advent.PuzzleDayTester

class Dec07 : PuzzleDayTester(7, 2020) {
    override fun part1(): Int {
        val tree = Node("shiny gold")
        val bags = parse()
        var oldTreeSize: Int
        // TODO filter the bags or something shit man have some pride
        do {
            oldTreeSize = tree.puzzle1().size
            bags.forEach { bag ->
                tree.find(bag.first)?.addBag(bag.second)
            }
        } while (oldTreeSize != tree.puzzle1().size)
        return (tree.puzzle1().size - 1)
    }

    override fun part2(): Int {
        val tree = Node("shiny gold")
        val bags = parse2()
        var inserts: Int
        do {
            inserts = 0
            bags.forEach { bag ->
                tree.findAll(bag.first).forEach { found ->
                    if (!found.values.map { it.key }.contains(bag.second.first)) {
                        found.addBag(bag.second.first, bag.second.second)
                        // oh fuck yea count inserts that's way better dumbass
                        inserts++
                    }
                }
            }
        } while (inserts > 0)
        return (tree.puzzle2() - 1)
    }

    // Inverted pair: inner bag to outer bag, ignore the shiny gold outer bag
    private fun parse(): List<Pair<String, String>> = load().filter { !it.startsWith("shiny gold") }.map { raw ->
        raw.split(" bags contain ").let { outer ->
            val outerBag = outer[0].trim()
            val innerBags =
                outer[1].replace("bags", "").replace("bag", "").replace(".", "").split(", ").map { it.drop(2).trim() }
            innerBags.map { it to outerBag }
        }
    }.flatten().toMutableList()

    // Lets do a whole crap ton of processing here for no good reason and return something incomprehensible
    // Pairs are outer bag to inner bag + inner bag count
    // Multiple Pairs if outer bag has multiple inner bags
    // Ignore empty bags
    private fun parse2(): List<Pair<String, Pair<String, Int>>> =
        load().filter { !it.contains("no other bags") }.map { raw ->
            raw.split(" bags contain ").let { outer ->
                val outerBag = outer[0].trim()
                val innerBags = outer[1].replace("bags", "").replace("bag", "").replace(".", "").split(", ").map {
                    it.drop(2).trim() to Character.getNumericValue(it[0])
                }
                innerBags.map { outerBag to it }
            }
        }.flatten().toMutableList()

    /**
     * LETS MAKE A TREE WHAT A DUMB IDEA AHHH
     */
    private data class Node(val key: String, val count: Int = 1, val values: MutableList<Node> = mutableListOf()) {
        // for puzzle 1: find the single key matching
        fun find(searchKey: String): Node? =
            if (searchKey == key) {
                this
            } else {
                values.mapNotNull { it.find(searchKey) }.firstOrNull()
            }

        // for puzzle 2: find ALL matching keys
        fun findAll(searchKey: String): List<Node> =
            if (searchKey == key) {
                listOf(this)
            } else {
                values.map { it.findAll(searchKey) }.flatten()
            }

        fun addBag(newBag: String, count: Int = 1) {
            values.add(Node(newBag, count))
        }

        fun puzzle1(): Set<String> = values.map { it.puzzle1() }.flatten().plus(key).toSet()

        fun puzzle2(): Int =
            if (values.isEmpty()) {
                count
            } else {
                values.map { it.puzzle2() }.sum() * count + count
            }
    }
}
