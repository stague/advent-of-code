package org.elwaxoro.advent.y2019

import org.elwaxoro.advent.PuzzleDayTester

/**
 * Day 2: 1202 Program Alarm
 * Note: abstracted / refactored the intercode computer after Dec05 expanded it
 */
class Dec02 : PuzzleDayTester(2, 2019) {

    override fun part1(): Any = Intercode(loadToInt(delimiter = ",")).run(12, 2)// == 2782414

    /**
     * Inspection of a few manual runs shows:
     * increasing the noun by 1 doubles the output
     * increasing the verb by one increases the output by 1
     * So keep cranking up the noun till it goes over, subtract one and switch to verb for the difference
     */
    override fun part2(): Any {
        val target = 19690720
        val computer = Intercode(loadToInt(delimiter = ","))
        var noun = 1
        var verb = 1
        while (computer.run(noun, verb) < target) {
            noun++
        }
        noun--
        verb = 1 + target - computer.run(noun, verb)
        return 100 * noun + verb // == 9820
    }
}
