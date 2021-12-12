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
