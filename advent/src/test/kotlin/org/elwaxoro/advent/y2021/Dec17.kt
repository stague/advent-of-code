package org.elwaxoro.advent.y2021

import org.elwaxoro.advent.Coord
import org.elwaxoro.advent.PuzzleDayTester
import org.elwaxoro.advent.contains

/**
 * Trick Shot
 */
class Dec17 : PuzzleDayTester(17, 2021) {

    override fun puzzle1(): Any = parse().sprayAndPray().flatten().maxOf { it.pos.y }
    override fun puzzle2(): Any = parse().sprayAndPray().size

    /**
     * Shoot a shit ton of probes out there and see what hits the target
     * Returns each successful flight
     */
    private fun Pair<Coord, Coord>.sprayAndPray(): List<List<Probe>> =
        (1..second.x).mapNotNull { xVel ->
            (first.y..100).mapNotNull { yVel ->
                Probe(Coord(), xVel, yVel).probulate(this)
            }
        }.flatten()

    data class Probe(val pos: Coord, val velX: Int, val velY: Int) {
        /**
         * Move the probe and update the velocity
         */
        fun iterate(): Probe = Probe(pos.add(velX, velY), (velX - 1).takeUnless { velX <= 0 } ?: 0, velY - 1)

        /**
         * Track the probe through all iterations until it either hits the target (return path) or falls out of bounds (return null)
         */
        tailrec fun probulate(target: Pair<Coord, Coord>, track: List<Probe> = listOf(this)): List<Probe>? =
            if (target.contains(track.last().pos)) {
                track
            } else if (track.last().pos.x >= target.second.x || track.last().pos.y <= target.first.y) {
                null
            } else {
                probulate(target, track.plus(track.last().iterate()))
            }
    }

    private fun parse() = load().single().replace("target area: x=", "").replace(" y=", "").split(",").let { (xs, ys) ->
        val xs2 = xs.split("..")
        val ys2 = ys.split("..")
        Coord(xs2[0].toInt(), ys2[0].toInt(), 'T') to Coord(xs2[1].toInt(), ys2[1].toInt(), 'T')
    }
}
