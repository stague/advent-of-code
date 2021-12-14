package org.elwaxoro.advent

data class Node(val name: String, val canVisit: Boolean = true, val canLeave: Boolean = true, val canRevisit: Boolean = false) {

    val edges = mutableMapOf<Node, Int>()
    val nodes = mutableListOf<Node>()

    fun cost(node: Node): Int = edges[node]!!

    fun addEdge(node: Node, cost: Int = 0) {
        if(!edges.contains(node)) {
            nodes.add(node)
        }
        edges[node] = cost
    }
}

/**
 * Insert a new node into a list of existing nodes, updating existing edge/node lists
 */
fun List<Node>.addNode(node: Node): List<Node> = map {
    it.also {
        if(node.edges.contains(it)) {
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
 * Assumes a fully connected bidirectional graph
 */
fun findBestPath(nodes: List<Node>, connectLoop: Boolean, bestPathFinder: (nodes: List<List<Node>>) -> List<Node>, costFunction: (nodes: List<List<Node>>) -> Int): Int =
    costFunction(nodes.map {
        visitAllNodes(listOf(it), nodes.minus(it), bestPathFinder)
    }.map {
        if(connectLoop) {
            it.plus(it.first())
        } else {
            it
        }
    })

/**
 * Assumes a fully connected bidirectional graph
 */
fun visitAllNodes(visited: List<Node>, remaining: List<Node>, bestPathFinder: (nodes: List<List<Node>>) -> List<Node>): List<Node> =
    if (remaining.isEmpty()) {
        visited
    } else {
        bestPathFinder(remaining.map {
            visitAllNodes(visited.plus(it), remaining.minus(it), bestPathFinder)
        })
    }
