package org.elwaxoro.advent.y2021

import org.elwaxoro.advent.Node
import org.elwaxoro.advent.PuzzleDayTester

/**
 * Passage Pathing
 */
class Dec12 : PuzzleDayTester(12, 2021) {

    override fun puzzle1(): Any = parse().let { nodes ->
        val start = nodes["start"]!!
        // reserve the special small cave ahead of time, preventing any small node re-visits (part 2 only)
        flail(start, specialSmallCave = start, listOf(start)).size
    }

    override fun puzzle2(): Any = parse().let { nodes ->
        val start = nodes["start"]!!
        flail(start, specialSmallCave = null, listOf(start)).size
    }

    private fun flail(current: Node, specialSmallCave: Node?, path: List<Node>): List<List<Node>> =
        if (!current.canLeave) {
            listOf(path) // exit found, nowhere else to go!
        } else {
            current.nodes.mapNotNull {
                if (!it.canVisit) {
                    null // start can never be re-visited
                } else if (it.canRevisit || !it.canLeave || !path.contains(it)) {
                    flail(it, specialSmallCave, path.plus(it)) // big caves, the exit, and un-visited small caves
                } else if (specialSmallCave == null) {
                    flail(it, it, path.plus(it)) // already visited small cave, but the special small cave is available for a second visit
                } else {
                    null // small cave that has already been visited (or twice for special small cave)
                }
            }.flatten().map {
                path.plus(it) // create the entire path by adding the existing path to this new section
            }
        }

    private fun parse(): MutableMap<String, Node> = mutableMapOf<String, Node>().also { map ->
        load().map { it.split("-") }.map { (a, b) ->
            // bi-directional graph
            val nodeA = map.getOrDefaultNode(a)
            val nodeB = map.getOrDefaultNode(b)
            nodeA.addEdge(nodeB)
            nodeB.addEdge(nodeA)
            map[a] = nodeA
            map[b] = nodeB
        }
    }

    private fun Map<String, Node>.getOrDefaultNode(name: String): Node = getOrDefault(name, Node(name, canVisit = name != "start", canLeave = name != "end", canRevisit = name.matches(Regex("[A-Z]+"))))
}
