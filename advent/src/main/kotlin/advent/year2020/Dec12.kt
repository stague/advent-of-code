package advent.year2020

import advent.PuzzleDay
import kotlin.math.abs

class Dec12 : PuzzleDay(12, 2020) {

    override fun puzzle1(): Int = parse().sail(1 to 0, true)

    override fun puzzle2(): Int = parse().sail(10 to 1, false)

    private fun List<Pair<Char, Int>>.sail(initialVector: Pair<Int, Int>, compassModsPosition: Boolean): Int {
        var vector = initialVector
        var position = 0 to 0
        forEach {
            when (it.first) {
                'F' -> position =
                    position.first + vector.first * it.second to position.second + vector.second * it.second
                'R' -> vector = vector.rotate(it.second)
                'L' -> vector = vector.rotate(360 - it.second)
                'N' -> if (compassModsPosition) {
                    position = position.north(it.second)
                } else {
                    vector = vector.north(it.second)
                }
                'S' -> if (compassModsPosition) {
                    position = position.south(it.second)
                } else {
                    vector = vector.south(it.second)
                }
                'E' -> if (compassModsPosition) {
                    position = position.east(it.second)
                } else {
                    vector = vector.east(it.second)
                }
                'W' -> if (compassModsPosition) {
                    position = position.west(it.second)
                } else {
                    vector = vector.west(it.second)
                }
            }
        }
        return abs(position.first) + abs(position.second)
    }

    private fun parse() = load().map {
        Pair(it[0], it.substring(1).toInt())
    }

    private fun Pair<Int, Int>.north(mod: Int) = first to second + mod
    private fun Pair<Int, Int>.south(mod: Int) = first to second - mod
    private fun Pair<Int, Int>.east(mod: Int) = first + mod to second
    private fun Pair<Int, Int>.west(mod: Int) = first - mod to second

    private fun Pair<Int, Int>.rotate(rotation: Int) =
        when (rotation) {
            0 -> this
            90 -> second to first * -1
            180 -> first * -1 to second * -1
            270 -> second * -1 to first
            else -> throw IllegalStateException("OMG")
        }
}
