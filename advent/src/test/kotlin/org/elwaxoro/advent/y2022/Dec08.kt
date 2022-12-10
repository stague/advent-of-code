package org.elwaxoro.advent.y2022

import com.google.common.collect.Range
import org.elwaxoro.advent.PuzzleDayTester

class Dec08 : PuzzleDayTester(8, 2022) {
    override val testRuns = listOf(
            TestRun(1, 21L, 8),
            TestRun(null, 1763L, 671160)
    )

    override fun part1(testFileSuffix: Int?): Any = loadIntArray(testFileSuffix).let { plot ->
        (0..plot.maxLongitude()).sumOf { longitude ->
            (0..plot.maxLatitude()).sumOf { latitude ->
                if (plot.isVisible(latitude, longitude))  1L  else  0L
            }
        }
    }

    override fun part2(testFileSuffix: Int?): Any = loadIntArray(testFileSuffix).let { plot ->
        (0..plot.maxLongitude()).map { longitude ->
            (0..plot.maxLatitude()).map { latitude ->
                plot.getScenicScore(latitude, longitude)
            }
        }.flatten().max()
    }

    private fun Array<IntArray>.isVisible(latitude: Int, longitude: Int) = this[longitude][latitude].let { curHeight ->
        when {
            //curHeight == 0 -> false
            latitude == 0 -> true
            longitude == 0 -> true
            latitude == maxLatitude() -> true
            longitude == maxLongitude() -> true
            else -> Heading.values().any { heading ->
                this.listFromCoord(latitude, longitude, heading).all { it < curHeight }
            }
        }
    }

    private fun Array<IntArray>.getScenicScore(latitude: Int, longitude: Int) = this[longitude][latitude].let { curHeight ->
        Heading.values().map { heading ->
            this.listFromCoord(latitude, longitude, heading).let { treeList ->
                when {
                    treeList.isEmpty() -> treeList
                    treeList.max() < curHeight -> treeList
                    else -> treeList.take(treeList.indexOfFirst { it >= curHeight } + 1)
                }
            }.size
        }.reduce { sum, element ->
            sum * element
        }
    }

    private fun Array<IntArray>.listFromCoord(latitude: Int, longitude: Int, heading: Heading) = when (heading) {
        Heading.NORTH -> this.sliceArray(0.until(longitude)).map { it[latitude] }.reversed()
        Heading.SOUTH -> this.sliceArray((longitude + 1)..maxLongitude()).map { it[latitude] }
        Heading.EAST -> this[longitude].let { it.sliceArray((latitude + 1)..maxLatitude()) }.toList()
        Heading.WEST -> this[longitude].let { it.sliceArray(IntRange(0, latitude - 1)) }.toList().reversed()
    }

    enum class Heading { NORTH, EAST, SOUTH, WEST }

    private fun loadIntArray(testFileSuffix: Int?): Array<IntArray> = load(testFileSuffix).map {
        it.toCharArray().map { it.digitToInt() }.toIntArray()
    }.toTypedArray()

    private fun Array<IntArray>.maxLatitude() = this[0].size - 1
    private fun Array<IntArray>.maxLongitude() = this.lastIndex //size - 1
}


