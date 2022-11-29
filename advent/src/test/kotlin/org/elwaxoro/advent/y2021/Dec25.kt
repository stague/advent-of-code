package org.elwaxoro.advent.y2021

import org.elwaxoro.advent.PuzzleDayTester

/**
 * Sea Cucumber
 */
class Dec25 : PuzzleDayTester(25, 2021) {

    override fun part1(): Any = load().map { it.toList() }.let {
        var grid = it
        var rep = 0
        do {
            val newGrid = List(grid.size) { MutableList(grid.first().size) { '.' } }
            var moved = 0

            // move it to the left to the left to the left
            grid.forEachIndexed { y, row ->
                row.forEachIndexed { x, c ->
                    if (c == '>') {
                        val moveX = (x + 1) % row.size
                        // only care about what was there before, since everything moves simultaneously
                        if (row[moveX] == '.') {
                            newGrid[y][moveX] = '>'
                            moved++
                        } else {
                            newGrid[y][x] = '>'
                        }
                    }
                }
            }

            // now get down with it
            grid.forEachIndexed { y, row ->
                row.forEachIndexed { x, c ->
                    if (c == 'v') {
                        val moveY = (y + 1) % grid.size
                        // need to check what was there before for 'v' and that the new space is still open from the left movers
                        if (grid[moveY][x] != 'v' && newGrid[moveY][x] == '.') {
                            newGrid[moveY][x] = 'v'
                            moved++
                        } else {
                            newGrid[y][x] = 'v'
                        }
                    }
                }
            }

            grid = newGrid
            rep++
        } while (moved > 0)
        rep
    }

    override fun part2(): Any = "There is no part 2! Merry Christmas!"
}
