package org.elwaxoro.advent.y2021

import org.elwaxoro.advent.Node
import org.elwaxoro.advent.PuzzleDayTester
import org.elwaxoro.advent.cost
import org.elwaxoro.advent.neighborCoords
import org.elwaxoro.advent.splitToInt

/**
 * Chiton (oh look its Dijkstra)
 */
class Dec15 : PuzzleDayTester(15, 2021) {

    override fun part1(): Any = parse().cornerToCornerCost()

    // victim of incomplete refactors to optimize and clean up code, now goes OOM but it's a year later so :shrug:
    override fun part2(): Any = "OOM"//parse(reps = 5).cornerToCornerCost()

    private fun List<List<Node>>.cornerToCornerCost(): Int {
        this[0][0].dijkstra()
        // because I'm dumb and insisted on reusing existing code that wasn't a perfect fit,
        // the shortest path (and cost) includes the start node but not the end
        // it works, so I'm just leaving it - but the true cost must be recalculated:
        val end = this[size - 1][size - 1]
        return (end.shortestPath + end).cost()
    }

    fun parse(reps: Int = 1): List<List<Node>> = load().map { it.splitToInt() }.getSwole(reps).let { grid ->
        val refGraph = mutableMapOf<String, Node>()
        grid.mapIndexed { rowIdx, row ->
            row.mapIndexed { colIdx, i ->
                // LOTS of debugging here, so went with a key that could be parsed back out in various ways
                val nodeKey = "$colIdx,$rowIdx:$i"
                val node = refGraph.getOrDefault(nodeKey, Node(nodeKey))
                refGraph[node.name] = node
                grid.neighborCoords(rowIdx, colIdx).forEach { (coord, cost) ->
                    val neighborKey = "${coord.x},${coord.y}:$cost"
                    val neighbor = refGraph.getOrDefault(neighborKey, Node(neighborKey))
                    refGraph[neighbor.name] = neighbor
                    node.addEdge(neighbor, cost)
                    neighbor.addEdge(node, i)
                }
                node
            }
        }
    }

    /**
     * Never skip leg day
     */
    private fun List<List<Int>>.getSwole(reps: Int): List<List<Int>> =
        (0 until size * reps).map { y ->
            (0 until size * reps).map { x ->
                // All truly great code is completely unreadable
                ((this[y % size][x % size] + x / size + y / size - 1) % 9) + 1
            }
        }
}
