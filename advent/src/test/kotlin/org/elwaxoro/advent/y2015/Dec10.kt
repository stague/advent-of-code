package org.elwaxoro.advent.y2015

import org.elwaxoro.advent.PuzzleDayTester

/**
 * Elves Look, Elves Say
 */
class Dec10 : PuzzleDayTester(10, 2015) {

    override fun puzzle1(): Any = (1..40).fold("3113322113") { acc, _ ->
        acc.lookAndSay()
    }.length

    override fun puzzle2(): Any = (1..50).fold("3113322113") { acc, _ ->
        acc.lookAndSay()
    }.length

    private fun String.lookAndSay(): String {
        var current = this[0]
        var counter = 0
        val newString = mutableListOf<Any>()
        forEach { i ->
            if (i == current) {
                counter++
            } else {
                newString.add(counter)
                newString.add(current)
                current = i
                counter = 1
            }
        }
        newString.add(counter)
        newString.add(current)
        return newString.joinToString("")
    }
}
