package org.elwaxoro.advent.y2021

import org.elwaxoro.advent.PuzzleDayTester
import org.elwaxoro.advent.bitFlip
import org.elwaxoro.advent.replace
import org.elwaxoro.advent.splitToInt
import org.elwaxoro.advent.toBinaryInt

/**
 * Binary Diagnostic
 */
class Dec03 : PuzzleDayTester(3, 2021) {

    /**
     * Swap 0s for -1s then just zip all the lists together into a list of sums
     * If it's negative that's a 0 if its positive that's a 1
     */
    override fun part1(): Any = load().map { it.splitToInt().replace(0, -1) }.let { readings ->
        readings.fold(List(readings.first().size) { 0 }) { acc, reading ->
            acc.zip(reading, Int::plus)
        }
    }.map { num -> 1.takeIf { num > 0 } ?: 0 }.let {
        it.toBinaryInt() * it.bitFlip().toBinaryInt()
    }

    override fun part2(): Any = load().let {
        it.seekReading(true) * it.seekReading(false)
    }

    private fun List<String>.seekReading(keepMostCommon: Boolean): Int = seekReading(keepMostCommon, 0, this).single().toInt(2)

    /**
     * Partition the lists based on 0 or 1 at idx
     * Use keepMostCommon to decide which side to keep for next recursion
     */
    private tailrec fun seekReading(keepMostCommon: Boolean, idx: Int, readings: List<String>): List<String> =
        if (readings.size == 1) {
            readings
        } else {
            seekReading(keepMostCommon, idx + 1,
                readings.partition { it[idx] == '0' }.let { (zeros, ones) ->
                    ones.takeIf { (keepMostCommon && ones.size >= zeros.size) || (!keepMostCommon && ones.size < zeros.size) }
                        ?: zeros
                }
            )
        }
}
