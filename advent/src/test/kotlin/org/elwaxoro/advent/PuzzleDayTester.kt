package org.elwaxoro.advent

import kotlin.test.Test
import kotlin.test.assertNotEquals

abstract class PuzzleDayTester(day: Int, year: Int): PuzzleDay(day, year) {

    @Test
    open fun testPuzzle1() = testPuzzle("$year-$day Puzzle 1", ::puzzle1)

    @Test
    open fun testPuzzle2() = testPuzzle("$year-$day Puzzle 2", ::puzzle2)

    private fun testPuzzle(name: String, puzzle: () -> Any) {
        val start = System.currentTimeMillis()
        puzzle().let {
            println("$name: $it [${System.currentTimeMillis()-start}ms]")
            assertNotEquals("TODO", it, "$name not implemented")
        }
    }
}
