package org.elwaxoro.advent.y2021

import org.elwaxoro.advent.PuzzleDayTester

/**
 * Lanternfish
 */
class Dec06 : PuzzleDayTester(6, 2021) {

    override fun puzzle1(): Any = countTheFishies(80)
    override fun puzzle2(): Any = countTheFishies(256)

    private fun countTheFishies(days: Int) =
        // Lanternfish spawn on an 8 day cycle plus one extra bucket for newbies
        MutableList(9) { 0L }.also { fishes ->
            // load the starting fish in there
            load(delimiter = ",").map { it.toInt() }.groupingBy { it }.eachCount().map { (idx, count) -> fishes[idx] = count.toLong() }
        }.let { fishes ->
            (0 until days).forEach { day ->
                val spawnIdx = day % 8 // ignore last bucket, that's the nursery and it never spawns
                val spawnCount = fishes[spawnIdx] // fish havin babies today
                fishes[(spawnIdx + 7) % 8] += spawnCount // move the fish that just spawned to their new spawning day
                fishes[spawnIdx] = fishes[8] // dump out the nursery into today so they can spawn next time it comes around
                fishes[8] = spawnCount // babies go to the nursery
            }
            fishes.sum() // there are plenty more fish in the sea now!
        }
}
