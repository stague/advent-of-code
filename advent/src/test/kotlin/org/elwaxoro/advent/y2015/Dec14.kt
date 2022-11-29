package org.elwaxoro.advent.y2015

import org.elwaxoro.advent.PuzzleDayTester
import kotlin.math.min

/**
 * Reindeer Olympics
 */
class Dec14 : PuzzleDayTester(14, 2015) {

    override fun part1(): Any = parse().let { reindeer ->
        reindeer.maxOf {
            it.distanceTraveled(2503)
        }
    }

    /**
     * yikes nobody had ever read this wtf were you thinking doing this instead of a fold or something else non-stupid
     */
    override fun part2(): Any = parse().let { reindeer ->
        (1..2503L).map { seconds ->
            reindeer.map { hurr ->
                hurr to hurr.distanceTraveled(seconds)
            }.groupBy { it.second }.maxByOrNull { it.key }!!.value.map { (durr, _) ->
                durr.score++
            }
        }
        reindeer.maxOf { it.score }
    }

    private fun parse() = load().map(Reindeer::parse)

    data class Reindeer(val name: String, val speed: Long, val flightTime: Long, val restTime: Long, var score: Long = 0) {
        companion object {
            fun parse(string: String): Reindeer = string.replace(" can fly ", " ").replace(" km/s for ", " ")
                .replace(" seconds, but then must rest for ", " ").replace(" seconds.", " ").split(" ")
                .let { (name, speed, flightTime, restTime) ->
                    Reindeer(name, speed.toLong(), flightTime.toLong(), restTime.toLong())
                }
        }
        private val totalTime = flightTime + restTime

        fun distanceTraveled(time: Long): Long {
            val wholeChunks = (time / totalTime)
            val wholeChunkDistance = wholeChunks * (speed * flightTime)
            val remainingChunk = time - (wholeChunks * totalTime)
            val remainingFlyable = min(remainingChunk, flightTime)
            val remainingDistance = remainingFlyable * speed
            return wholeChunkDistance + remainingDistance
        }
    }
}
