package org.elwaxoro.advent.y2021

import org.elwaxoro.advent.PuzzleDayTester

/**
 * Sonar Sweep
 */
class Dec01 : PuzzleDayTester(1, 2021) {

    override fun part1(testFileSuffix: Int?): Any = radarReader().fold(Pair(0, Int.MAX_VALUE)) { acc, next ->
        acc.depthCounter(next)
    }.first

    override fun part2(testFileSuffix: Int?): Any = radarReader().let { input ->
        input.subList(0, input.size - 2).foldIndexed(Pair(0, Int.MAX_VALUE)) { idx, acc, _ ->
            acc.depthCounter(input.subList(idx, idx + 3).sum())
        }.first
    }

    /**
     * if new depth is greater: first + 1 (accumulator), always set new depth to second (previous depth)
     */
    private fun Pair<Int, Int>.depthCounter(newDepth: Int) = Pair(first.takeUnless { second < newDepth } ?: (first + 1), newDepth)

    private fun radarReader() = load().map { it.toInt() }
}
