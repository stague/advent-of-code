package org.elwaxoro.advent

import java.nio.file.Path
import java.nio.file.Paths
import kotlin.test.Test
import kotlin.test.assertNotEquals

abstract class PuzzleDayTester(val day: Int, val year: Int) {

    /**
     * Extend me!
     */
    open fun puzzle1(): Any = "TODO"

    /**
     * Extend me!
     */
    open fun puzzle2(): Any = "TODO"

    /**
     * Default loader, gets the input file and returns a list of strings
     */
    open fun load(testNum: Int? = null, delimiter: String = "\n"): List<String> = loadSplit(getPath(testNum), delimiter)

    /**
     * Default loader, gets the input file and returns a list of integers
     */
    open fun loadToInt(testNum: Int? = null, delimiter: String = "\n"): List<Int> = load(testNum, delimiter).map { it.toInt() }

    /**
     * Default loader: Loads whatever the path points to into a single string
     * Generally just call "load" instead, but this can be useful for loading multi-line JSON or something
     */
    fun loadText(path: Path): String = path.toUri().toURL().readText()

    /**
     * Helper: gets the absolute path for the given puzzle day
     */
    fun getPath(testNum: Int? = null): Path = Paths.get("src/test/resources/${getDayFilename(day, year, testNum)}").toAbsolutePath()

    /**
     * Unit test for part one of puzzles
     * Shouldn't need to touch this
     */
    @Test
    open fun testPuzzle1() = testPuzzle("$year-$day Puzzle 1", ::puzzle1)

    /**
     * Unit test for part two of puzzles
     * Shouldn't need to touch this either!
     */
    @Test
    open fun testPuzzle2() = testPuzzle("$year-$day Puzzle 2", ::puzzle2)

    /**
     * Runs the test, prints its output and how long it took
     */
    private fun testPuzzle(name: String, puzzle: () -> Any) {
        val start = System.currentTimeMillis()
        puzzle().let {
            println("$name: $it [${System.currentTimeMillis()-start}ms]")
            assertNotEquals("TODO", it, "$name not implemented")
        }
    }

    /**
     * Helper: Converts day/year and optional test number into the expected resources path and filename
     */
    private fun getDayFilename(day: Int, year: Int, testNum: Int? = null): String =
        "$year/Dec${if (day < 10) "0$day" else day}${if (testNum != null) "-test-$testNum" else ""}.txt"

    /**
     * Helper: Loads the path into a list of strings, based on provided delimiter or newline
     * Generally just call "load" instead
     */
    private fun loadSplit(path: Path, delimiter: String = "\n"): List<String> = loadText(path).split(delimiter)
}
