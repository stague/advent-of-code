package org.elwaxoro.advent.y2021

import org.elwaxoro.advent.Coord3D
import org.elwaxoro.advent.PuzzleDayTester
import org.elwaxoro.advent.Rotation4

/**
 * Beacon Scanner
 */
class Dec19 : PuzzleDayTester(19, 2021) {

    override fun part1(testFileSuffix: Int?): Any = parse().alignTheScanners().map { it.beacons }.flatten().toSet().size

    override fun part2(testFileSuffix: Int?): Any = parse().alignTheScanners().map { it.translation }.let { translations ->
        translations.maxOf { a ->
            translations.minus(a).maxOf { b ->
                a.manhattan(b)
            }
        }
    }

    /**
     * Loop unaligned scanners until everyone is aligned
     * Modifies the scanners so the beacon positions make sense relative to the first scanner
     * Aligned scanners have the translation from 0,0,0 built in
     */
    private fun List<Scanner>.alignTheScanners(): Set<Scanner> = mutableSetOf(first()).also { aligned ->
        val unaligned = drop(1).toMutableList()
        while (unaligned.isNotEmpty()) {
            val iter = unaligned.iterator()
            while (iter.hasNext()) {
                val candidate = iter.next()
                aligned.firstNotNullOfOrNull { candidate.align(it) }?.let {
                    aligned.add(it)
                    iter.remove()
                }
            }
        }
    }

    private fun parse(): List<Scanner> = load(delimiter = "\n\n").mapIndexed { idx, chunk ->
        chunk.split("\n").filterNot { it.isBlank() || it.startsWith("--- scanner") }.map(Coord3D::parse).let {
            Scanner(idx, it)
        }
    }

    private data class Scanner(val name: Int, val beacons: List<Coord3D>, val translation: Coord3D = Coord3D()) {
        private val beaconDistanceMap: Map<Int, Pair<Coord3D, Coord3D>> = beacons.map { a ->
            beacons.minus(a).map { b ->
                a.manhattan(b) to Pair(a, b)
            }
        }.flatten().distinctBy { it.first }.toMap()

        fun align(anchor: Scanner): Scanner? =
            (beaconDistanceMap.keys intersect anchor.beaconDistanceMap.keys).let { potentialBeacons ->
                potentialBeacons.map { distance ->
                    val (a, b) = beaconDistanceMap[distance]!!
                    val (aa, ab) = anchor.beaconDistanceMap[distance]!!
                    // Difference vector everything by everything, take the best matrix if 12 or more beacons line up
                    Rotation4.allTheThings().mapNotNull { matrix ->
                        val ra = matrix.multiply(a)
                        val rb = matrix.multiply(b)

                        if (ra.subtract(aa) == rb.subtract(ab)) {
                            aa.subtract(ra).toMatrix().multiply(matrix)
                        } else if (rb.subtract(aa) == ra.subtract(ab)) {
                            ab.subtract(ra).toMatrix().multiply(matrix)
                        } else {
                            null
                        }
                    }
                }.flatten().groupingBy { it }.eachCount().maxByOrNull { it.value }?.takeIf { it.value >= 12 }
                    ?.let { (matrix, _) ->
                        Scanner(name, beacons.map { (matrix.multiply(it)) }, matrix.multiply(Coord3D()))
                    }
            }
    }
}
