package org.elwaxoro.advent.y2021

import org.elwaxoro.advent.Coord
import org.elwaxoro.advent.PuzzleDayTester

class Dec13 : PuzzleDayTester(13, 2021) {

    override fun puzzle1(): Any = parse().let { (coords, folds) ->
        coords.foldPaper(folds.first()).size
    }

    override fun puzzle2(): Any = parse().let { (coords, folds) ->
        "\n\n" + folds.fold(coords) { acc, fold ->
            acc.foldPaper(fold)
        }.printify()
    }

    private fun List<Coord>.foldPaper(fold: Coord): List<Coord> = map { coord ->
        Coord(
            (2 * fold.x - coord.x).takeIf { fold.x > 0 && coord.x > fold.x } ?: coord.x,
            (2 * fold.y - coord.y).takeIf { fold.y > 0 && coord.y > fold.y } ?: coord.y
        )
    }.distinct()

    private fun List<Coord>.printify(): String =
        (0..maxByOrNull { it.y }!!.y).map {
            MutableList(maxByOrNull { it.x }!!.x + 1) { ' ' }
        }.also { screen ->
            forEach { coord ->
                screen[coord.y][coord.x] = '#'
            }
        }.joinToString("\n") {
            it.joinToString("")
        }

    private fun parse(): Pair<List<Coord>, List<Coord>> = load(delimiter = "\n\n").let { (rawCoords, rawFolds) ->
        val coords = rawCoords.split("\n").map(Coord::parse)
        val folds = rawFolds.split("\n").map {
            if (it.contains("x=")) {
                Coord(it.substringAfter('=').toInt(), 0)
            } else {
                Coord(0, it.substringAfter('=').toInt())
            }
        }
        coords to folds
    }
}
