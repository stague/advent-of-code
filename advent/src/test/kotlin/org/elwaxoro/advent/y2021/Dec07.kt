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

    override fun part1(): Any = loadToInt(delimiter = ",").crabboFuelGuesser5000(
        { it.median() },
        { target, crab -> abs(crab - target) }
    )

    override fun part2(): Any = loadToInt(delimiter = ",").crabboFuelGuesser5000(
        { it.average() },
        { target, crab -> (1..abs(crab - target)).sum() }
    )

    private fun List<Int>.crabboFuelGuesser5000(
        targetingSubsystem: (crabbos: List<Int>) -> Double,
        fuelCalculator: (target: Int, crab: Int) -> Int
    ): Int = targetingSubsystem(this).let { target ->
        listOf(ceil(target).toInt(), floor(target).toInt()).map { opt ->
            map { fuelCalculator(opt, it) }.sum()
        }.minOrNull()!!
    }
}
