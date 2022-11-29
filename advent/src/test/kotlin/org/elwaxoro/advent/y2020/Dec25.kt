package org.elwaxoro.advent.y2020

import org.elwaxoro.advent.PuzzleDayTester

class Dec25 : PuzzleDayTester(25, 2020) {

    override fun part1(): Any {
        val keys = listOf(19241437L, 17346587L)
        val firstKeyLoop = hACKtHEpLANET(keys)
        val otherKey = keys.filterNot { it == firstKeyLoop.first }.single()
        return hACKtHEpLANET(subject = otherKey, stop = firstKeyLoop.second).first
    }

    override fun part2(): Any = "There is no puzzle 2"

    private fun hACKtHEpLANET(
        keys: List<Long> = listOf(),
        subject: Long = 7L,
        stop: Int = Int.MAX_VALUE
    ): Pair<Long, Int> =
        (1..stop).fold(1L) { acc, idx ->
            ((acc * subject) % 20201227).also {
                // phase 1: bail as soon as the accumulator matches any of the keys
                if (keys.contains(acc)) {
                    return acc to idx - 1
                }
            }
            // phase 2: return the final accumulator
        }.let { it to stop }
}
