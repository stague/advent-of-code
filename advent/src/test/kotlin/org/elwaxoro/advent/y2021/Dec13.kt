package org.elwaxoro.advent.y2021

import org.elwaxoro.advent.Coord
import org.elwaxoro.advent.PuzzleDayTester
import org.elwaxoro.advent.printify

/**
 * Transparent Origami
 */
class Dec13 : PuzzleDayTester(13, 2021) {

    override fun part1(): Any = parse().let { (coords, folds) ->
        coords.foldPaper(folds.first()).size
    }

    override fun part2(): Any = parse().let { (coords, folds) ->
        "\n\n" + folds.fold(coords) { acc, fold ->
            acc.foldPaper(fold)
        }.printify(empty = ' ')
    }

    private fun List<Coord>.foldPaper(fold: Coord): List<Coord> = map { coord ->
        Coord(
            (2 * fold.x - coord.x).takeIf { fold.x > 0 && coord.x > fold.x } ?: coord.x,
            (2 * fold.y - coord.y).takeIf { fold.y > 0 && coord.y > fold.y } ?: coord.y
        )
    }.distinct()

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
