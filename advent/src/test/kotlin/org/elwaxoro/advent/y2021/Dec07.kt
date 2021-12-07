package org.elwaxoro.advent.y2021

import org.elwaxoro.advent.PuzzleDayTester
import org.elwaxoro.advent.median
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.roundToInt

/**
 * The Treachery of Whales
 */
class Dec07 : PuzzleDayTester(7, 2021) {

    override fun puzzle1(): Any = load( delimiter = ",").map { it.toInt() }.let { crabbos ->
        val median = crabbos.median().roundToInt()
        crabbos.map { abs(it - median) }.sum()
    }

    override fun puzzle2(): Any = load(delimiter = ",").map { it.toInt() }.let { crabbos ->
        // try ceiling and floor of the average and go with the cheaper one
        crabbos.average().let { avg ->
            listOf(ceil(avg).toInt(), floor(avg).toInt()).map { opt ->
                crabbos.map { (1..abs(it - opt)).sum() }.sum()
            }.minOrNull()!!
        }
    }
}
