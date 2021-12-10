package org.elwaxoro.advent.y2015

import org.elwaxoro.advent.PuzzleDayTester

/**
 * All in a Single Night
 */
class Dec09 : PuzzleDayTester(9, 2015) {

    override fun puzzle1(): Any = explore(::minPath, ::minCost)

    override fun puzzle2(): Any = explore(::maxPath, ::maxCost)

    private fun minCost(nodes: List<List<Node>>): Int = nodes.minOf { it.cost() }
    private fun maxCost(nodes: List<List<Node>>): Int = nodes.maxOf { it.cost() }
    private fun minPath(nodes: List<List<Node>>): List<Node> = nodes.minByOrNull { it.cost() }!!
    private fun maxPath(nodes: List<List<Node>>): List<Node> = nodes.maxByOrNull { it.cost() }!!

    private fun explore(bestPathFinder: (nodes: List<List<Node>>) -> List<Node>, costFunction: (nodes: List<List<Node>>) -> Int): Int = parse().let { nodes ->
        costFunction(nodes.map {
            flail(listOf(it), nodes.minus(it), bestPathFinder)
        })
    }

    private fun flail(visited: List<Node>, remaining: List<Node>, bestPathFinder: (nodes: List<List<Node>>) -> List<Node>): List<Node> =
        if (remaining.isEmpty()) {
            visited
        } else {
            bestPathFinder(remaining.map {
                flail(visited.plus(it), remaining.minus(it), bestPathFinder)
            })
        }

    private fun parse(): List<Node> = mutableMapOf<String, Node>().also { map ->
        load().let { lines ->
            // THIS IS NOT A DIRECTED GRAPH BOTH DIRECTIONS ARE ENABLED BY A SINGLE LINK
            lines.forEach { line ->
                line.split(" to ").map { it.split(" = ") }.flatten().let { (source, destination, cost) ->
                    val sourceNode = map.getOrDefault(source, Node(source))
                    val destNode = map.getOrDefault(destination, Node(destination))
                    sourceNode.addEdge(destNode, cost.toInt())
                    destNode.addEdge(sourceNode, cost.toInt())
                    map[source] = sourceNode
                    map[destination] = destNode
                }
            }
        }
    }.values.toList()

    private fun List<Node>.cost(): Int = zipWithNext { a, b -> a.cost(b) }.sum()

    data class Node(val name: String) {
        val edges = mutableMapOf<Node, Int>()

        fun cost(node: Node): Int = edges[node]!!

        fun addEdge(node: Node, cost: Int) {
            edges[node] = cost
        }
    }
}

