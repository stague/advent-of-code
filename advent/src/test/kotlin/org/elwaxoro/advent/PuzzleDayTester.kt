package org.elwaxoro.advent

import kotlin.test.Test
import kotlin.test.assertNotEquals

abstract class PuzzleDayTester(day: Int, year: Int): PuzzleDay(day, year) {

    @Test
    open fun testPuzzle1() {
        puzzle1().let {
            println("Puzzle 1: $it")
            assertNotEquals("TODO", it, "Puzzle 1 not implemented")
        }
    }

    @Test
    open fun testPuzzle2() {
        puzzle2().let {
            println("Puzzle 2: $it")
            assertNotEquals("TODO", it, "Puzzle 2 not implemented")
        }
    }
}
