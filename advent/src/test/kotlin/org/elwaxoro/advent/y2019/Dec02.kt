package org.elwaxoro.advent.y2019

import org.elwaxoro.advent.PuzzleDayTester
import java.lang.IllegalStateException

/**
 * Day 2: 1202 Program Alarm
 */
class Dec02 : PuzzleDayTester(2, 2019) {

    override fun part1(): Any = goComputerGo(12, 2, loadToInt(delimiter = ","))// == 2782414

    override fun part2(): Any = findComputerFind(19690720, loadToInt(delimiter = ","))// == 9820

    /**
     * Inspection of a few manual runs shows:
     * increasing the noun by 1 doubles the output
     * increasing the verb by one increases the output by 1
     * So keep cranking up the noun till it goes over, subtract one and switch to verb for the difference
     */
    private fun findComputerFind(target: Int, input: List<Int>): Int {
        var noun = 1
        var verb = 1
        while (goComputerGo(noun, verb, input) < target) {
            noun++
        }
        noun--
        verb = 1 + target - goComputerGo(noun, verb, input)
        return 100 * noun + verb
    }

    private fun goComputerGo(noun: Int, verb: Int, input: List<Int>): Int =
        input.toMutableList().let { codes ->
            // setup
            codes.ws(1, noun)
            codes.ws(2, verb)
            var idx = 0
            while (codes.wg(idx) != 99) {
                when (codes.wg(idx)) {
                    1 -> codes.ws(codes.wg(idx + 3), codes.wgg(idx + 1) + codes.wgg(idx + 2))
                    2 -> codes.ws(codes.wg(idx + 3), codes.wgg(idx + 1) * codes.wgg(idx + 2))
                    else -> throw IllegalStateException("Unknown opcode ${codes.wg(idx)} at idx $idx! Full codes: $codes")
                }
                idx = (idx + 4) % codes.size
            }
            codes[0]
        }

    // safe getter that wraps the list
    private fun <T> List<T>.wg(idx: Int): T = this[idx % this.size]
    // safe getter that wraps the list, then fetches the value marked by the first value
    private fun MutableList<Int>.wgg(idx: Int): Int = this[wg(idx)]
    // safe setter that wraps the list
    private fun <T> MutableList<T>.ws(idx: Int, value: T) {
        this[idx % this.size] = value
    }
}
