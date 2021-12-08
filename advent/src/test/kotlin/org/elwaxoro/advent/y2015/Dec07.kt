package org.elwaxoro.advent.y2015

import org.elwaxoro.advent.PuzzleDayTester

/**
 * Some Assembly Required
 */
class Dec07 : PuzzleDayTester(7, 2015) {

    override fun puzzle1(): Any = parse().runTransforms()

    override fun puzzle2(): Any = parse().map {
        if(it.target == "b") {
            Transform("b", puzzle1().toString())
        } else {
            it
        }
    }.toMutableList().runTransforms()

    private fun MutableList<Transform>.runTransforms(): Int {
        val map = mutableMapOf<String, Int>()
        // TODO this would look much nicer as a tail recusive function
        while (isNotEmpty()) {
            val iter = iterator()
            while (iter.hasNext()) {
                val transform = iter.next()
                (transform.extractTwo(" AND ", map)?.let { (left, right) ->
                    left and right
                } ?: transform.extractTwo(" OR ", map)?.let { (left, right) ->
                    left or right
                } ?: transform.extractTwo(" LSHIFT ", map)?.let { (left, right) ->
                    left shl right
                } ?: transform.extractTwo(" RSHIFT ", map)?.let { (left, right) ->
                    left shr right
                } ?: transform.extractOne("NOT ", map)?.inv()
                ?: map.getInt(transform.transform))?.let { output ->
                    map[transform.target] = output
                    iter.remove()
                }
            }
        }
        return map["a"] ?: throw IllegalStateException("IT DIDN'T WORK WHAT AM I EVEN DOING WITH THIS PROBLEM ARGGGHHHH")
    }

    private fun parse(): MutableList<Transform> = load().map {
        it.split(" -> ").let { (transform, target) ->
            Transform(target, transform)
        }
    }.toMutableList()

    private fun Transform.extractTwo(delimiter: String, map: MutableMap<String, Int>): List<Int>? =
        transform.split(delimiter).mapNotNull { map.getInt(it) }.takeIf { it.size == 2 }

    private fun Transform.extractOne(prefix: String, map: MutableMap<String, Int>): Int? =
        takeIf { transform.startsWith(prefix) }?.let {
            map.getInt(it.transform.replace(prefix, ""))
        }

    private fun MutableMap<String, Int>.getInt(key: String): Int? =
        if (key.matches(Regex("\\d+"))) {
            key.toInt()
        } else {
            get(key)
        }

    data class Transform(val target: String, val transform: String)
}
