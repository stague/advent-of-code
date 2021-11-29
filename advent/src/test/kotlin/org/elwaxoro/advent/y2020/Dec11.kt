package org.elwaxoro.advent.y2020

import org.elwaxoro.advent.PuzzleDayTester

class Dec11 : PuzzleDayTester(11, 2020) {

    override fun puzzle1(): Int =
        parse().findStableSeating(maxNeighbors = 4, transformFloor = true).countOccupiedSeats()

    override fun puzzle2(): Int =
        parse().findStableSeating(maxNeighbors = 5, transformFloor = false).countOccupiedSeats()

    private fun List<List<Int>>.findStableSeating(
        maxNeighbors: Int,
        transformFloor: Boolean
    ): List<List<Int>> =
        modSeating(maxNeighbors, transformFloor).let { newSeatingChart ->
            newSeatingChart.takeIf { noChange(newSeatingChart) }
                ?: newSeatingChart.findStableSeating(maxNeighbors, transformFloor)
        }

    private fun List<List<Int>>.modSeating(
        maxNeighbors: Int,
        transformFloor: Boolean
    ): List<List<Int>> =
        mapIndexed { y, row ->
            row.mapIndexed { x, spot ->
                countNeighbors(y, x, transformFloor).let { neighbors ->
                    when {
                        spot == 0 && neighbors == 0 -> 1 // nobody nearby! sit down
                        spot == 1 && neighbors >= maxNeighbors -> 0 // too many! stand up
                        else -> spot // no change
                    }
                }
            }
        }

    private fun List<List<Int>>.countNeighbors(y: Int, x: Int, transformFloor: Boolean): Int = listOf(
        findSeat(y, x, -1, -1, transformFloor), // upper left
        findSeat(y, x, -1, 0, transformFloor), // up
        findSeat(y, x, -1, 1, transformFloor), // upper right
        findSeat(y, x, 0, -1, transformFloor), // left
        findSeat(y, x, 0, 1, transformFloor), // right
        findSeat(y, x, 1, -1, transformFloor), // lower left
        findSeat(y, x, 1, 0, transformFloor), // down
        findSeat(y, x, 1, 1, transformFloor), // lower right
    ).sum()

    private fun List<List<Int>>.findSeat(y: Int, x: Int, yMod: Int, xMod: Int, transformFloor: Boolean): Int =
        getSeat(y + yMod, x + xMod, transformFloor).let {
            if (it == 0 || it == 1) // seat or seat-equivalent floor (puzzle 1)
                it
            else // floor section, keep going
                findSeat(y + yMod, x + xMod, yMod, xMod, transformFloor)
        }

    private fun List<List<Int>>.getSeat(y: Int, x: Int, transformFloor: Boolean): Int =
        if (y < 0 || x < 0 || y >= this.size || x >= this[y].size) {
            0 // fell off the side
        } else if (transformFloor && this[y][x] == -1) {
            0 // for puzzle 1, floor counts as an empty seat
        } else {
            this[y][x]
        }

    private fun List<List<Int>>.noChange(that: List<List<Int>>): Boolean = filterIndexed { rowIdx, row ->
        row.filterIndexed { colIdx, _ -> this[rowIdx][colIdx] != that[rowIdx][colIdx] }.isNotEmpty()
    }.isEmpty()

    private fun List<List<Int>>.countOccupiedSeats(): Int = map { row -> row.filter { it == 1 }.sum() }.sum()

    private fun parse(): List<List<Int>> = load().map { row ->
        row.map {
            when (it) {
                '#' -> 1
                'L' -> 0
                '.' -> -1
                else -> throw IllegalStateException("AHHHHHHHHHHHHHHHHHHHHH $it")
            }
        }
    }
}
