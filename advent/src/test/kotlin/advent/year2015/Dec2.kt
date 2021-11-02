package advent.year2015

import advent.PuzzleDay

/**
 * https://adventofcode.com/2015/day/2
 * part 1: 1606483
 * part 2: 3842356
 */
class Dec2 : PuzzleDay(2, 2015) {

    // TODO write a cartesian product or something for this kind of thing
    override fun puzzle1(): Any = parse().map { box ->
        val sideAreas = listOf(box.first * box.second, box.first * box.third, box.second * box.third)
        val min = sideAreas.minOrNull() ?: 0L
        sideAreas.sum() * 2 + min
    }.sum()

    override fun puzzle2(): Any = parse().map { box ->
        val perimeters = listOf(box.first * 2 + box.second * 2, box.first * 2 + box.third * 2, box.second * 2 + box.third * 2)
        val min = perimeters.minOrNull() ?: 0L
        box.first * box.second * box.third + min
    }.sum()

    private fun parse(): List<Triple<Long, Long, Long>> = load().map { line ->
        line.split("x").map { it.toLong() }.let { Triple(it[0], it[1], it[2]) }
    }
}
