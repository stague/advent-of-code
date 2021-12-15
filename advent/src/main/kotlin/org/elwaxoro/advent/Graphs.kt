package org.elwaxoro.advent

data class Node(
    val name: String,
    val canVisit: Boolean = true,
    val canLeave: Boolean = true,
    val canRevisit: Boolean = false
) {

    val edges = mutableMapOf<Node, Int>()
    val nodes = mutableListOf<Node>()

    // helpers for dijkstra
    var shortestPathToSource = listOf<Node>()
    var shortestDistance: Int = Int.MAX_VALUE

    override fun toString(): String = name

    fun cost(node: Node): Int = edges[node]!!

    fun addEdge(node: Node, cost: Int = 0) {
        if (!edges.contains(node)) {
            nodes.add(node)
        }
        edges[node] = cost
    }

    /**
     * Dijkstra it up!
     * This call populates this node and all connected nodes with the shortest path from this node to all other connected nodes
     * DANGER: this is a mutating call so will only really work once for all nodes involved
     */
    fun dijkstra() {
        shortestDistance = 0
        val settled: MutableSet<Node> = mutableSetOf()
        val unsettled: MutableSet<Node> = mutableSetOf()
        unsettled.add(this)

        while (unsettled.isNotEmpty()) {
            val current = unsettled.minByOrNull { it.shortestDistance }!!
            unsettled.remove(current)
            current.nodes.filterNot { settled.contains(it) }.forEach { neighbor ->
                neighbor.calculateMinimumDistance(current)
                unsettled.add(neighbor)
            }
            settled.add(current)
        }
    }

    private fun calculateMinimumDistance(source: Node) {
        (source.shortestDistance + cost(source)).takeIf { it < shortestDistance }?.let { newShorterDistance ->
            shortestDistance = newShorterDistance
            shortestPathToSource = source.shortestPathToSource + source
        }
    }
}

/**
 * Insert a new node into a list of existing nodes, updating existing edge/node lists
 */
fun List<Node>.addNode(node: Node): List<Node> = map {
    it.also {
        if (node.edges.contains(it)) {
            it.addEdge(node, node.cost(it))
        }
    }
}.plus(node)

fun minCost(nodes: List<List<Node>>): Int = nodes.minOf { it.cost() }
fun maxCost(nodes: List<List<Node>>): Int = nodes.maxOf { it.cost() }
fun minPath(nodes: List<List<Node>>): List<Node> = nodes.minByOrNull { it.cost() }!!
fun maxPath(nodes: List<List<Node>>): List<Node> = nodes.maxByOrNull { it.cost() }!!
fun List<Node>.cost(): Int = zipWithNext { a, b -> a.cost(b) }.sum()

/**
 * Assumes a fully connected bidirectional graph, find the best path that includes ALL nodes
 */
fun findBestPath(
    nodes: List<Node>,
    connectLoop: Boolean,
    bestPathFinder: (nodes: List<List<Node>>) -> List<Node>,
    costFunction: (nodes: List<List<Node>>) -> Int
): Int =
    costFunction(nodes.map {
        visitAllNodes(listOf(it), nodes.minus(it), bestPathFinder)
    }.map {
        if (connectLoop) {
            it.plus(it.first())
        } else {
            it
        }
    })

/**
 * Assumes a fully connected bidirectional graph
 */
fun visitAllNodes(
    visited: List<Node>,
    remaining: List<Node>,
    bestPathFinder: (nodes: List<List<Node>>) -> List<Node>
): List<Node> =
    if (remaining.isEmpty()) {
        visited
    } else {
        bestPathFinder(remaining.map {
            visitAllNodes(visited.plus(it), remaining.minus(it), bestPathFinder)
        })
    }
