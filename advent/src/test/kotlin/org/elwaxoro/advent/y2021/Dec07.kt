package org.elwaxoro.advent.y2021

import org.elwaxoro.advent.PuzzleDayTester
import org.elwaxoro.advent.median
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.floor

/**
 * The Treachery of Whales
 */
class Dec07 : PuzzleDayTester(7, 2021) {

    override fun puzzle1(): Any = loadToInt(delimiter = ",").crabboFuelGuesser5000(
        { it.median() },
        { opt, crab -> abs(crab - opt) }
    )

    override fun puzzle2(): Any = loadToInt(delimiter = ",").crabboFuelGuesser5000(
        { it.average() },
        { opt, crab -> (1..abs(crab - opt)).sum() }
    )

    private fun List<Int>.crabboFuelGuesser5000(
        guessProvider: (crabbos: List<Int>) -> Double,
        adderUpper: (opt: Int, crab: Int) -> Int
    ): Int = guessProvider(this).let { guess ->
        listOf(ceil(guess).toInt(), floor(guess).toInt()).map { opt ->
            map { adderUpper(opt, it) }.sum()
        }.minOrNull()!!
    }
}
