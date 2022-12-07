package org.elwaxoro.advent.y2021

import org.elwaxoro.advent.PuzzleDayTester

/**
 * Lanternfish
 */
class Dec06 : PuzzleDayTester(6, 2021) {

    override fun part1(testFileSuffix: Int?): Any = countTheFishes(80)
    override fun part2(testFileSuffix: Int?): Any = countTheFishes(256)

    private fun countTheFishes(days: Int) =
        (0 until days).map { it % 8 } // ignore last bucket for iteration, that's the nursery and it never spawns
            .fold(loadTheFishes()) { fishes, day ->
                fishes.also {
                    fishes[day] = fishes[8].also { // dump out the nursery into today so they can spawn next time it comes around
                        fishes[8] = fishes[day] // babies go to the nursery
                        fishes[(day + 7) % 8] += fishes[day] // move the fish that just spawned to their new spawning day
                    }
                }
            }.sum()  // there are plenty more fish in the sea now!

    /**
     * Load the starting fish into their spawning-day slots
     * Lanternfish spawn on an 8 day cycle plus one extra bucket for newbies
     */
    private fun loadTheFishes(): MutableList<Long> = MutableList(9) { 0L }.also { fishes ->
        load(delimiter = ",").map { it.toInt() }.groupingBy { it }.eachCount()
            .map { (idx, count) -> fishes[idx] = count.toLong() }
    }
}
