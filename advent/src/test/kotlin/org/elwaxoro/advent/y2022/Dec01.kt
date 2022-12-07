package org.elwaxoro.advent.y2022

import org.elwaxoro.advent.PuzzleDayTester

class Dec01: PuzzleDayTester(1, 2022) {
    override val testRuns = listOf(
            TestRun(1, 1000, 1000),
            TestRun(null, 11223, 11223)
    )

    override fun part1(testFileSuffix: Int?): Int = load(testFileSuffix).first().toInt() // simulate response to show test run

    override fun part2(testFileSuffix: Int?): Any = load(testFileSuffix).first().toInt()

}