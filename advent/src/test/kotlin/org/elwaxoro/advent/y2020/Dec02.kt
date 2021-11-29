package org.elwaxoro.advent.y2020

import org.elwaxoro.advent.PuzzleDayTester

class Dec02 : PuzzleDayTester(2, 2020) {

    override fun puzzle1(): Any = parseInput().count { it.isValid() }
    override fun puzzle2(): Any = parseInput().count { it.isValid2() }

    private data class Password(
        val min: Int,
        val max: Int,
        val char: Char,
        val pass: String
    ) {
        fun isValid(): Boolean = pass.count { it == char } in min..max
        fun isValid2(): Boolean = (pass[min - 1] == char) xor (pass[max - 1] == char)
    }

    private fun parseInput() = load().map {
        val split = it.replace(":", "").split(" ")
        val range = split[0].split("-")
        Password(
            min = range[0].toInt(),
            max = range[1].toInt(),
            char = split[1].first(),
            pass = split[2]
        )
    }
}
