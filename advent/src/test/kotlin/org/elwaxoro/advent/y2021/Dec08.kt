package org.elwaxoro.advent.y2021

import org.elwaxoro.advent.PuzzleDayTester

/**
 * Seven Segment Search
 */
class Dec08 : PuzzleDayTester(8, 2021) {

    /**
     * Only care about the right side of the pipe
     * Only care about unique length digits
     */
    override fun part1(testFileSuffix: Int?): Any = load().map { it.split(" | ").let { it[0] to it[1] } }
        .sumOf { it.second.split(" ").filter { it.length in listOf(2, 3, 4, 7) }.size }

    /**
     * 1, 4, 7, 8 all have unique segment counts
     * masking 1, 4, 7 against other numbers and counting the remaining segments yields a unique digit:
     * Ex on 2 (5 segments)
     * 2 masked by 1 = 4 segments remaining
     * 2 masked by 4 = 3 segments remaining
     * 2 masked by 7 = 3 segments remaining
     * sum of remaining for each mask = 10
     * Each 5 segment digit masks to a different sum, same with 6 segment digits (note 5 and 6 overlap so doing separately)
     */
    override fun part2(testFileSuffix: Int?): Any =
        load().map { it.replace(" |", "").split(" ") }.map { it to it.buildMasks() }.map { (digits, masks) ->
            digits.map { digit ->
                when (digit.length) {
                    2 -> 1
                    3 -> 7
                    4 -> 4
                    7 -> 8
                    5 -> masks.apply(digit).to5SegmentDigit() //2, 3, 5
                    6 -> masks.apply(digit).to6SegmentDigit() //6, 9, 0
                    else -> throw IllegalStateException("Unknown digit with unsupported lit segments: $digit")
                }
            }.takeLast(4).joinToString("").toInt()
        }.sum()

    private fun List<String>.buildMasks(): List<Regex> = listOf(
        Regex("[${first { it.length == 2 }}]+"), // 1
        Regex("[${first { it.length == 4 }}]+"), // 4
        Regex("[${first { it.length == 3 }}]+") // 7
    )

    private fun List<Regex>.apply(digit: String) = fold(0) { acc, mask -> acc + digit.replace(mask, "").length }

    private fun Int.to5SegmentDigit(): Int =
        when (this) {
            10 -> 2
            7 -> 3
            9 -> 5
            else -> throw IllegalStateException("Unknown 5 segment digit: $this")
        }

    private fun Int.to6SegmentDigit(): Int =
        when (this) {
            12 -> 6
            9 -> 9
            10 -> 0
            else -> throw IllegalStateException("Unknown 6 segment digit: $this")
        }
}
