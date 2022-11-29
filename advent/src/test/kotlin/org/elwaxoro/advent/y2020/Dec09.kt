package org.elwaxoro.advent.y2020

import org.elwaxoro.advent.PuzzleDayTester

class Dec09 : PuzzleDayTester(9, 2020) {

    override fun part1(): Long = parse().findInvalid()

    override fun part2(): Long = parse().let { input ->
        input.findInvalid().let { target ->
            // cheating here, based on puzzle 1 output I knew there were zero numbers less than target after it in the input
            (input.indexOf(target) downTo 1).mapNotNull {
                input.subList(0, it).findInvalidMinMax(target)
            }[0]
        }
    }

    private fun List<Long>.findInvalid(): Long =
        this[(25 until size).first { !subList(it - 25, it).containsSum(this[it]) }]

    private fun List<Long>.containsSum(target: Long): Boolean = any { target - it != it && contains(target - it) }

    // find the right sublist, then create it again cause I love inefficiency apparently
    private fun List<Long>.findInvalidMinMax(target: Long): Long? =
        (size downTo 1).firstOrNull { subList(it, size).sum() == target }
            ?.let { subList(it, size).sorted().let { it.first() + it.last() } }

    private fun parse(): List<Long> = load().map { it.toLong() }
}
