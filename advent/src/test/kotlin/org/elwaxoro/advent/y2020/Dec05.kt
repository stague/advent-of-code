package org.elwaxoro.advent.y2020

import org.elwaxoro.advent.PuzzleDayTester

class Dec05 : PuzzleDayTester(5, 2020) {

    override fun part1(): Any = findMaxPass()

    override fun part2(): Any = findMissingPass()

    /**
     * Missing pass = sum of all pass numbers - sum of actual pass numbers
     */
    private fun findMissingPass(): Int = buildPassNumbers().sorted().let { (it.first()..it.last()).sum() - it.sum() }
    private fun findMaxPass(): Int = buildPassNumbers().maxOrNull() ?: -1

    private fun buildPassNumbers(): List<Int> = load().map { buildPassNumber(it) }
    private fun buildPassNumber(pass: String): Int = findRow(pass) * 8 + findSeat(pass)

    private fun findSeat(pass: String): Int = boop(pass.slice(7 until 10), 'L', (0 until 8).toList())
    private fun findRow(pass: String): Int = boop(pass.slice(0 until 7), 'F', (0 until 128).toList())

    /**
     * Tail recursion!
     * base: choice size of 1, return that choice
     * recurse: shorten pass, split the list (left or right), go again!
     */
    private tailrec fun boop(pass: String, left: Char, choices: List<Int>): Int =
        when {
            choices.size == 1 -> choices[0]
            pass[0] == left -> boop(pass.drop(1), left, choices.slice(0 until choices.size / 2))
            else -> boop(pass.drop(1), left, choices.slice(choices.size / 2 until choices.size))
        }

}
