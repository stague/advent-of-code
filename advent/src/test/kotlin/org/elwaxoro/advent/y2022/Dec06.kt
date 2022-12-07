package org.elwaxoro.advent.y2022

import org.elwaxoro.advent.PuzzleDayTester
import org.junit.jupiter.api.Test
import java.util.LinkedList

class Dec06 : PuzzleDayTester(6, 2022) {
    override val testRuns = listOf(
            TestRun(1, 7, 19),
            TestRun(2, 5, 23),
            TestRun(3, 6, 23),
            TestRun(4, 10, 29),
            TestRun(5, 11, 26),
            TestRun(null, 1100, 2421)
    )

    override fun part1(testFileSuffix: Int?): Any = findMessageStart(testFileSuffix, 4)

    override fun part2(testFileSuffix: Int?): Any = findMessageStart(testFileSuffix, 14)

    private fun findMessageStart(testFileSuffix: Int?, markerLength: Int) = load(testFileSuffix).single().let { inputString ->
        inputString.let {
            it.fold("") { acc, char ->
                when {
                    acc.length == markerLength -> return@let acc
                    acc.isEmpty() -> acc
                    acc.contains(char) -> acc.removeRange(0..acc.indexOfLast { it == char })
                    acc.length < markerLength -> acc
                    else -> acc
                }.let { adjAcc ->
                    adjAcc + char
                }
            }
        }.let {
            inputString.indexOf(it) + markerLength
        }
    }
}

