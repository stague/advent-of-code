package org.elwaxoro.advent.y2019

import org.elwaxoro.advent.PuzzleDayTester
import kotlin.math.max

/**
 * Day 1: The Tyranny of the Rocket Equation
 */
class Dec01: PuzzleDayTester(1, 2019) {

    /**
     * Fuel Counter-Upper!
     * Fuel required to launch a given module is based on its mass. Specifically, to find the fuel required for a module, take its mass, divide by three, round down, and subtract 2
     */
    override fun part1(): Any = loadToLong().sumOf { it.fuelCost() }

    /**
     * Rocket Equation Double-Checker!
     * Fuel itself requires fuel just like a module - take its mass, divide by three, round down, and subtract 2. However, that fuel also requires fuel, and that fuel requires fuel, and so on. Any mass that would require negative fuel should instead be treated as if it requires zero fuel; the remaining mass, if any, is instead handled by wishing really hard, which has no mass and is outside the scope of this calculation.
     * Calculate the fuel requirements for each module separately, then add them all up at the end.
     */
    override fun part2(): Any = loadToLong().sumOf { recursiveFuelCost(it.fuelCost()) }

    private tailrec fun recursiveFuelCost(runningCost: Long, newFuel: Long = runningCost): Long =
        if (newFuel == 0L) {
            runningCost
        } else {
            recursiveFuelCost(runningCost + newFuel, newFuel.fuelCost())
        }

    /**
     * Fuel function, with a minimum of zero
     * Ex: 2 mass would be 0 fuel, not -2 fuel
     */
    private fun Long.fuelCost(): Long = max(0, this / 3 - 2)
}
