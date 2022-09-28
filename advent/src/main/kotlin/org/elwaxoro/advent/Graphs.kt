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
    var shortestPath = listOf<Node>()
    var shortestDistance: Int = Int.MAX_VALUE

    override fun toString(): String = name

    /**
     * Edge cost of an adjacent node
     */
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
            // always work on the shortest path first
            unsettled.minByOrNull { it.shortestDistance }!!.let { node ->
                // node goes from unsettled to settled
                settled.add(unsettled.delete(node))
                // all neighbors not already settled update their distances and go in the unsettled pile
                unsettled.addAll(node.nodes.filterNot { settled.contains(it) }.map { it.calculateMinimumDistance(node) })
            }
        }
    }

    private fun MutableSet<Node>.delete(node: Node): Node = node.also { remove(node) }

    /**
     * If (other's shortest path + cost there to here) is better than this node's shortest path,
     * replace this node's shortest path stuff
     */
    private fun calculateMinimumDistance(other: Node): Node = this.also {
        (other.shortestDistance + cost(other)).takeIf { it < shortestDistance }?.let { newShorterDistance ->
            shortestDistance = newShorterDistance
            shortestPath = other.shortestPath + other
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

/**
 * Minimum cost path from a list of paths (path: list of connected nodes)
 */
fun minCost(nodes: List<List<Node>>): Int = nodes.minOf { it.cost() }

/**
 * Maximum cost path from a list of paths (path: list of connected nodes)
 */
fun maxCost(nodes: List<List<Node>>): Int = nodes.maxOf { it.cost() }

/**
 * Minimum cost path, get the path
 */
fun minPath(nodes: List<List<Node>>): List<Node> = nodes.minByOrNull { it.cost() }!!

/**
 * Maximum cost path, get the path
 */
fun maxPath(nodes: List<List<Node>>): List<Node> = nodes.maxByOrNull { it.cost() }!!

/**
 * Total cost of path described by list of nodes. Assumes in-order traversal and that nodes are actually connected
 */
fun List<Node>.cost(): Int = zipWithNext { a, b -> a.cost(b) }.sum()

/**
 * Assumes a fully connected bidirectional graph, find the best path that includes ALL nodes
 * pathfinder and cost function must be provided
 * connectLoop will add an edge between start and end nodes
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
 * Visits all nodes in a list, using the provided pathfinder to decide what to visit next
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
