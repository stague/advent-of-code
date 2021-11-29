package org.elwaxoro.advent.y2015

import org.elwaxoro.advent.Coord
import org.elwaxoro.advent.Dir
import org.elwaxoro.advent.PuzzleDayTester

/**
 * https://adventofcode.com/2015/day/3
 * part 1: 2081
 * part 2: 2341
 */
class Dec03 : PuzzleDayTester(3, 2015) {

    data class Santa(val visits: List<Coord> = listOf(Coord(0, 0))) {
        fun visit(dir: Dir): Santa = Santa(visits + visits.last().move(dir))
    }

    override fun puzzle1(): Any = parse().fold(Santa()) { santa, dir ->
        santa.visit(dir)
    }.visits.distinct().count()

    // TWO SANTAAAAS?
    override fun puzzle2(): Any = parse().foldIndexed(Pair(Santa(), Santa())) { idx, santas, dir ->
        if (idx % 2 == 0) {
            // santa moves
            Pair(santas.first.visit(dir), santas.second)
        } else {
            // robo santa moves
            Pair(santas.first, santas.second.visit(dir))
        }
    }.let { listOf(it.first.visits, it.second.visits).flatten() }.distinct().count()

    private fun parse(): List<Dir> = load(delimiter = "").mapNotNull {
        when (it) {
            "^" -> Dir.N
            "v" -> Dir.S
            ">" -> Dir.E
            "<" -> Dir.W
            else -> null
        }
    }
}
