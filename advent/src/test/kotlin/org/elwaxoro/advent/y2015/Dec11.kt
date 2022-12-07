package org.elwaxoro.advent.y2015

import org.elwaxoro.advent.PuzzleDayTester

/**
 * Corporate Policy
 */
class Dec11 : PuzzleDayTester(11, 2015) {

    override fun part1(testFileSuffix: Int?): Any {
        val password = "hepxcrrq".toMutableList()
        findNextPassword(password)
        return password.joinToString("")
    }

    override fun part2(testFileSuffix: Int?): Any {
        val password = "hepxcrrq".toMutableList()
        findNextPassword(password)
        findNextPassword(password)
        return password.joinToString("")
    }

    private fun findNextPassword(input: MutableList<Char>) {
        var ctr = 0L
        input.increment()
        while(!input.isValid()) {
            ctr++
            input.increment()
        }
    }

    private fun MutableList<Char>.isValid(): Boolean =
        noIllegalChars() && containsTwoPairs() && containsStraight()

    private fun MutableList<Char>.containsTwoPairs(): Boolean {
        var lastPairIdx = -1
        var pairs = 0
        (0..size-2).forEach { idx ->
            if(lastPairIdx < idx && this[idx] == this[idx+1]) {
                pairs++
                lastPairIdx = idx+1
            }
        }
        return pairs > 1
    }

    private fun MutableList<Char>.noIllegalChars(): Boolean =
        none { it == 'i' || it == 'o' || it == 'l' }

    private fun MutableList<Char>.containsStraight(): Boolean =
        (0..size - 3).any { this[it] + 1 == this[it + 1] && this[it] + 2 == this[it + 2] }

    private fun MutableList<Char>.increment() {
        this[size - 1] = this[size - 1] + 1
        (size - 1).downTo(0).forEach { idx ->
            if (this[idx] > 'z' && idx > 0) {
                this[idx - 1] = this[idx - 1] + 1
            }
        }
        this.replaceAll { c -> 'a'.takeIf { c > 'z' } ?: c }
    }
}
