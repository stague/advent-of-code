package org.elwaxoro.advent.y2019

import org.elwaxoro.advent.splitToInt
import java.lang.IllegalStateException

/**
 * Intercode computer!
 * Created Dec02
 * Expanded Dec05
 */
data class Intercode(val program: List<Int>) {

    /**
     * Dec02 runner
     */
    fun run(noun: Int, verb: Int): Int {
        program.toMutableList().let { codes ->
            // setup
            codes[1] = noun
            codes[2] = verb
            innerRun(listOf(), codes)
            return codes[0]
        }
    }

    /**
     * Dec05 runner
     */
    fun run(input: Int): Int = innerRun(listOf(input), program.toMutableList())

    /**
     * Dec07 runner
     */
    fun run(input: List<Int>): Int = innerRun(input, program.toMutableList())

    private fun innerRun(input: List<Int>, codes: MutableList<Int>): Int {
        var output = 0
        var idx = 0
        var inputIdx = 0
        while (codes[idx] != 99) {
            val codeParts = codes[idx].toString().splitToInt()

            when (codeParts.takeLast(2).joinToString("").toInt()) {
                1 -> { // param 1 + param 2, store in param 3 value's address
                    codes[codes[idx + 3]] = codes.mg(codeParts.mode(1), idx + 1) + codes.mg(codeParts.mode(2), idx + 2)
                    idx += 4
                }
                2 -> { // param 1 * param 2, store in param 3 value's address
                    codes[codes[idx + 3]] = codes.mg(codeParts.mode(1), idx + 1) * codes.mg(codeParts.mode(2), idx + 2)
                    idx += 4
                }
                3 -> { // read input, store in param 1 value's address
                    if(inputIdx >= input.size) {
//                        println("Warn: input read attempted but insufficient inputs provided! $inputIdx")
                    } else {
                        codes[codes[idx + 1]] = input[inputIdx]
                    }
                    inputIdx++
                    idx += 2
                }
                4 -> { // write output to param 1's address or value's address, depending on mode
                    output = codes.mg(codeParts.mode(1), idx + 1)
                    // println("output: $output at idx $idx full code $codes")
                    idx += 2
                }
                5 -> { // jump if true: param 1 nonzero, set idx to value of param 2 (no auto advance idx)
                    if (codes.mg(codeParts.mode(1), idx + 1) != 0) {
                        idx = codes.mg(codeParts.mode(2), idx + 2)
                    } else {
                        idx += 3
                    }
                }
                6 -> { // jump if false: param 1 zero, set idx to value of param 2 (no auto advance idx)
                    if (codes.mg(codeParts.mode(1), idx + 1) == 0) {
                        idx = codes.mg(codeParts.mode(2), idx + 2)
                    } else {
                        idx += 3
                    }
                }
                7 -> { // less than: if param 1 < param 2 store 1 in param 3, else store 0
                    if (codes.mg(codeParts.mode(1), idx + 1) < codes.mg(codeParts.mode(2), idx + 2)) {
                        codes[codes[idx + 3]] = 1
                    } else {
                        codes[codes[idx + 3]] = 0
                    }
                    idx += 4
                }
                8 -> { // equals: if param 1 == param 2, store 1 in param 3, else store 0
                    if (codes.mg(codeParts.mode(1), idx + 1) == codes.mg(codeParts.mode(2), idx + 2)) {
                        codes[codes[idx + 3]] = 1
                    } else {
                        codes[codes[idx + 3]] = 0
                    }
                    idx += 4
                }
                else -> throw IllegalStateException("Unknown opcode ${codes[idx]} at idx $idx! Full codes: $codes")
            }
        }
        return output
    }

    /**
     * Two modes: position mode and immediate mode
     * Position mode: read from codes[idx] as a reference to another position (get value, then go to that idx instead ie: codes[codes[idx]])
     * Immediate mode: read from codes[idx] as a value (get value only)
     */
    private fun List<Int>.mode(argNum: Int): Int =
        when (argNum) {
            1 -> size - 3
            2 -> size - 4
            3 -> size - 5
            4 -> size - 6
            else -> throw IllegalStateException("Tried to get argnum $argNum from $this but that number seems to high?")
        }.let { idx ->
            if (idx < 0) {
                0
            } else {
                this[idx]
            }
        }

    private fun MutableList<Int>.mg(mode: Int, idx: Int): Int =
        when (mode) {
            0 -> this[this[idx]] // position mode
            1 -> this[idx] // immediate mode
            else -> throw IllegalStateException("Unknown mode $mode for idx $idx")
        }
}
