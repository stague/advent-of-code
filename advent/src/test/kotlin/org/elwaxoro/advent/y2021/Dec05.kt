package org.elwaxoro.advent.y2021

import org.elwaxoro.advent.Coord
import org.elwaxoro.advent.PuzzleDayTester

/**
 * Hydrothermal Venture
 */
class Dec05 : PuzzleDayTester(5, 2021) {

    override fun puzzle1(): Any = parse().filter { it.first().x == it.last().x || it.first().y == it.last().y }.enumerateAndCount()

    override fun puzzle2(): Any = parse().enumerateAndCount()

    private fun List<List<Coord>>.enumerateAndCount() = flatMap { it.first().enumerateLine(it.last()) }.groupingBy { it }.eachCount().filter { it.value > 1 }.size

    private fun parse(): List<List<Coord>> = load().map { line -> line.split(" -> ").map(Coord::parse) }

// Copied from Extensions.kt and Coordinates.kt
//    fun Coord.enumerateLine(toCoord: Coord): List<Coord> =
//        (abs(x -toCoord.x) + 1 to abs(y - toCoord.y) + 1).let { (dx, dy) ->
//            x.toward(toCoord.x).padTo(dy).zip(y.toward(toCoord.y).padTo(dx)).map { (x, y) ->
//                Coord(x, y)
//            }
//        }
//
//    fun Int.toward(to: Int): IntProgression = IntProgression.fromClosedRange(this, to, 1.takeIf { this <= to } ?: -1)
//
//    fun IntProgression.padTo(newSize: Int): List<Int> = toList().padTo(newSize)
//
//    fun <T> List<T>.padTo(newSize: Int): List<T> = takeIf { size >= newSize } ?: plus(List(newSize - size) { last() })
}
