package advent.year2016

import advent.Coord
import advent.Dir
import advent.PuzzleDay

/**
 * --- Day 2: Bathroom Security ---
 */
class Dec2 : PuzzleDay(2, 2016) {

    override fun puzzle1(): Any = codeSolver(
        """
        789
        456
        123
        """.toNumPad(),
        Coord(1,1)
    )

    override fun puzzle2(): Any = codeSolver(
        """
        --D--
        -ABC-
        56789
        -234-
        --1--
        """.toNumPad(),
        Coord(0,2)
    )

    private fun codeSolver(numPad: List<List<Char>>, start: Coord): List<Char> =
        parse().fold(listOf<Coord>()) { codes, line ->
            codes.plus(line.fold(codes.lastOrNull() ?: start) { acc, dir ->
                acc.move(dir).takeIf { numPad.isValid(it) } ?: acc
            })
        }.map { numPad.toNumPad(it) }

    private fun String.toNumPad() = trimIndent().split("\n").map { it.toList() }

    private fun List<List<Char>>.isValid(coord: Coord): Boolean =
        this.toNumPad(coord) != '-'

    private fun List<List<Char>>.toNumPad(coord: Coord): Char =
        try {
            this[coord.y][coord.x]
        } catch (ex: Exception) {
            '-'
        }

    private fun parse(): List<List<Dir>> = load().map { line ->
        line.map { Dir.fromUDLR(it) }
    }
}
