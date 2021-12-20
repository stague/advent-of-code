package org.elwaxoro.advent.y2021

import org.elwaxoro.advent.Coord
import org.elwaxoro.advent.PuzzleDayTester
import org.elwaxoro.advent.bounds

/**
 * Trench Map
 */
class Dec20 : PuzzleDayTester(20, 2021) {

    override fun puzzle1(): Any = glowUpLikeALot(2).size
    override fun puzzle2(): Any = glowUpLikeALot(50).size

    /**
     * OOB coords swap from all '.' to all '#' every other iteration
     */
    private fun glowUpLikeALot(reps: Int): Set<Coord> = parse().let { (algorithm, litCoords) ->
        (0 until reps).fold(litCoords) { acc, i ->
            glowUp(algorithm, acc, isOobLit = i % 2 != 0)
        }
    }

    /**
     * Grow the bounds by 1 in every direction to consider new coords that might be touching the currently lit ones
     * Use isOobLit to decide if coords outside the (non-expanded) bounds are lit or not
     */
    private fun glowUp(algorithm: String, litCoords: Set<Coord>, isOobLit: Boolean): Set<Coord> =
        litCoords.bounds().let { bounds ->
            (bounds.first.y - 1..bounds.second.y + 1).map { y ->
                (bounds.first.x - 1..bounds.second.x + 1).mapNotNull { x ->
                    Coord(x, y).takeIf { algorithm[it.calcNumber(bounds, litCoords, isOobLit)] == '#'}
                }
            }.flatten().toSet()
        }

    private fun Coord.calcNumber(bounds: Pair<Coord, Coord>, litCoords: Set<Coord>, isOobLit: Boolean): Int =
        neighbors9().flatten().joinToString("") { neighbor ->
            "1".takeIf { litCoords.contains(neighbor) || (isOobLit && bounds.isOob(neighbor)) } ?: "0"
        }.toInt(2)

    private fun Pair<Coord, Coord>.isOob(c: Coord): Boolean = c.x < first.x || c.x > second.x || c.y < first.y || c.y > second.y

    private fun parse(): Pair<String, Set<Coord>> = load(delimiter = "\n\n").let { (algorithm, image) ->
        algorithm.replace("\n", "") to
            image.split("\n").flatMapIndexed { y, row ->
                row.mapIndexedNotNull { x, c ->
                    Coord(x, y).takeIf { c == '#' }
                }
            }.toSet()
    }
}
