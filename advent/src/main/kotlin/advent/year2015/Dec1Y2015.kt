package advent.year2015

import advent.PuzzleDay

/**
 * https://adventofcode.com/2015/day/1
 * part 1: 280
 * part 2: 1797
 */
class Dec1Y2015 : PuzzleDay(1, 2015) {

    override fun puzzle1(): Any = parseInput().sum()

    // accumulator is a pair: first is running sum, second is basement idx (-1 until found)
    override fun puzzle2(): Any = parseInput().foldIndexed(0 to -1) { idx, acc, item ->
        if (acc.first + item == -1 && acc.second == -1) {
            acc.first + item to idx + 1 // puzzle idx starts at 1 not zero
        } else {
            acc.first + item to acc.second
        }
    }.second

    private fun parseInput(): List<Int> = load(delimiter = "").map {
        when (it) {
            "(" -> 1
            ")" -> -1
            else -> 0
        }
    }.filterNot { it == 0 } // lazy delimiter "" means first and last char both show up as empty strings
}
