package org.elwaxoro.advent

import java.nio.file.Path
import java.nio.file.Paths

abstract class PuzzleDay(val day: Int, val year: Int) {

    fun getDayFilename(day: Int, year: Int, testNum: Int? = null): String =
        "$year/Dec${if (day < 10) "0$day" else day}${if (testNum != null) "-test-$testNum" else ""}.txt"

    fun loadText(path: Path): String = path.toUri().toURL().readText()

    fun loadSplit(path: Path, delimiter: String = "\n"): List<String> = loadText(path).split(delimiter)

    fun getPath(testNum: Int? = null): Path = Paths.get("src/main/resources/${getDayFilename(day, year, testNum)}").toAbsolutePath()

    open fun load(testNum: Int? = null, delimiter: String = "\n") = loadSplit(getPath(testNum), delimiter)

    open fun loadToInt(testNum: Int? = null, delimiter: String = "\n"): List<Int> = load(testNum, delimiter).map { it.toInt() }

    open fun puzzle1(): Any = "TODO"

    open fun puzzle2(): Any = "TODO"
}
