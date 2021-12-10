package org.elwaxoro.advent.y2021

import org.elwaxoro.advent.Coord
import org.elwaxoro.advent.PuzzleDayTester
import org.elwaxoro.advent.neighborCoords
import org.elwaxoro.advent.neighbors
import org.elwaxoro.advent.splitToInt

/**
 * Smoke Basin
 */
class Dec09 : PuzzleDayTester(9, 2021) {

    override fun puzzle1(): Any = parse().let { grid ->
        grid.mapIndexed { rowIdx, row ->
            row.mapIndexedNotNull { colIdx, col ->
                (col + 1).takeIf { grid.neighbors(rowIdx, colIdx).none { it <= col } }
            }
        }.flatten().sum()
    }

    override fun puzzle2(): Any = parse().let { grid ->
        var basinCtr = 0 // idk number basins I guess
        val basinMap = mutableMapOf<Coord, Int>() // each point in the grid, mapped to the basin it belongs to

        grid.forEachIndexed { rowIdx, row ->
            row.forEachIndexed { colIdx, col ->
                Coord(colIdx, rowIdx).takeIf {
                    col != 9 && !basinMap.contains(it)
                }?.let {
                    // New basin! Explore!
                    basinCtr += 1
                    basinMap[it] = basinCtr
                    explore(grid, basinMap, basinCtr, it)
                }
            }
        }
        basinMap.entries.groupingBy { it.value }.eachCount().map { it.value }.sorted().takeLast(3)
            .reduce { acc, basinSize -> acc * basinSize }
    }

    private fun explore(
        grid: List<List<Int>>,
        basinMap: MutableMap<Coord, Int>,
        currentBasin: Int,
        coord: Coord
    ) {
        grid.neighborCoords(coord.y, coord.x)
            .filterNot { (coord, height) -> basinMap.contains(coord) || height == 9 } // ignore neighbors already mapped or too tall
            .map { (coord, _) -> coord.also { basinMap[it] = currentBasin } } // strip out the neighbor height and add neighbor to basin map
            .map { explore(grid, basinMap, currentBasin, it) } // explore!
    }

    private fun parse(): List<List<Int>> = load().map { it.splitToInt() }
}
