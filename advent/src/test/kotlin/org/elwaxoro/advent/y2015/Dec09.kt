package org.elwaxoro.advent.y2015

import org.elwaxoro.advent.Node
import org.elwaxoro.advent.PuzzleDayTester
import org.elwaxoro.advent.findBestPath
import org.elwaxoro.advent.maxCost
import org.elwaxoro.advent.maxPath
import org.elwaxoro.advent.minCost
import org.elwaxoro.advent.minPath

/**
 * All in a Single Night
 */
class Dec09 : PuzzleDayTester(9, 2015) {

    override fun puzzle1(): Any = findBestPath(parse(), connectLoop = false, ::minPath, ::minCost)

    override fun puzzle2(): Any = findBestPath(parse(), connectLoop = false, ::maxPath, ::maxCost)

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
}
