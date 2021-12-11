package org.elwaxoro.advent.y2021

import org.elwaxoro.advent.PuzzleDayTester
import org.elwaxoro.advent.neighborCoords
import org.elwaxoro.advent.splitToInt

/**
 * Dumbo Octopus
 */
class Dec11 : PuzzleDayTester(11, 2021) {

    override fun puzzle1(): Any = load().map { it.splitToInt().toMutableList() }.let { grid ->
        (1..100).sumOf {
            grid.incAndFlash().countAndReset()
        }
    }

    override fun puzzle2(): Any = load().map { it.splitToInt().toMutableList() }.let { grid ->
        seekScore(grid, grid.size * grid[0].size, 1)
    }

    private fun List<MutableList<Int>>.incAndFlash(): List<MutableList<Int>> = this.also {
        forEachIndexed { y, row ->
            row.forEachIndexed { x, _ ->
                incAndFlash(y, x)
            }
        }
    }

    private fun List<MutableList<Int>>.incAndFlash(y: Int, x: Int): List<MutableList<Int>> = this.also {
        if (this[y][x] >= 0) { // octopus has not flashed this round
            this[y][x]++ // MOAR POWER
            if (this[y][x] > 9) { // BOOM
                this[y][x] = -1 // ok need to sleep now till reset
                neighborCoords(y, x, true).forEach { (coord, _) ->
                    incAndFlash(coord.y, coord.x) // LOOK OUT OCTOPUS (OCTOPUSES? OCTOPI?) HERE COMES THE POWER
                }
            }
        }
    }

    private fun List<MutableList<Int>>.countAndReset(): Int =
        mapIndexed { y, row ->
            row.mapIndexedNotNull { x, octopus ->
                1.takeIf { octopus == -1 }?.also { // -1 is temp marker for tired octopus
                    this[y][x] = 0 // BACK IN THE GAME
                }
            }
        }.flatten().sum()

    private tailrec fun seekScore(grid: List<MutableList<Int>>, targetScore: Int, attempts: Int): Int =
        if (grid.incAndFlash().countAndReset() == targetScore) {
            attempts
        } else {
            seekScore(grid, targetScore, attempts + 1)
        }
}
