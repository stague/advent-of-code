package org.elwaxoro.advent

import java.nio.file.Path
import java.nio.file.Paths

/**
 * Base class for solving puzzles, only used by PuzzleDayTester
 * WHY IS THIS HERE INSTEAD OF WITH PuzzleDayTester???
 * Because loading resources turned into a huge pain in the ass.
 * Eventually I gave up, left this here, and moved on with my life.
 */
abstract class PuzzleDay(val day: Int, val year: Int) {

    /**
     * Converts day/year and optional test number into the expected resources path and filename
     */
    fun getDayFilename(day: Int, year: Int, testNum: Int? = null): String =
        "$year/Dec${if (day < 10) "0$day" else day}${if (testNum != null) "-test-$testNum" else ""}.txt"

    /**
     * Loads whatever the path points to into a single string
     * Generally just call "load" instead
     */
    fun loadText(path: Path): String = path.toUri().toURL().readText()

    /**
     * Loads the path into a list of strings, based on provided delimiter or newline
     * Generally just call "load" instead
     */
    fun loadSplit(path: Path, delimiter: String = "\n"): List<String> = loadText(path).split(delimiter)

    /**
     * Helper to get the absolute path for the given puzzle day
     */
    fun getPath(testNum: Int? = null): Path = Paths.get("src/main/resources/${getDayFilename(day, year, testNum)}").toAbsolutePath()

    /**
     * Default loader, gets the input file and returns a list of strings
     */
    open fun load(testNum: Int? = null, delimiter: String = "\n"): List<String> = loadSplit(getPath(testNum), delimiter)

    /**
     * Default loader, gets the input file and returns a list of integers
     */
    open fun loadToInt(testNum: Int? = null, delimiter: String = "\n"): List<Int> = load(testNum, delimiter).map { it.toInt() }

    /**
     * Extend me!
     */
    open fun puzzle1(): Any = "TODO"

    /**
     * Extend me!
     */
    open fun puzzle2(): Any = "TODO"
}
