package org.elwaxoro.advent.y2020

import org.elwaxoro.advent.PuzzleDayTester

class Dec13 : PuzzleDayTester(13, 2020) {

    override fun puzzle1(): Any {

        val input = load()
        val startTime = input[0].toLong()
        var earliest = input[0].toLong()
        val schedule = input[1].split(",").filter { it != "x" }.map { it.toLong() }

        while (true) {
            val bus = schedule.firstOrNull { earliest % it == 0L }
            if (bus != null) {
                return (earliest - startTime) * bus
            } else {
                earliest++
            }
        }
    }

    override fun puzzle2(): Any {
        val input = load()
        val busSchedule = input[1].split(",").map {
            if (it == "x")
                0L
            else
                it.toLong()
        }

        var hop = 1L
        var time = hop

        busSchedule.forEachIndexed { idx, bus ->
            if (bus != 0L) { // skip the 'x' busses
                while ((time + idx) % bus != 0L) {
                    time += hop // find the next common time
                }
//                println("Common time for bus $bus at time $time offset $idx [${time + idx}] hop $hop")
                hop *= bus // these are all primes, so just keep a running multiple of all of them as the hop size
            }
        }

        return time
    }
}
