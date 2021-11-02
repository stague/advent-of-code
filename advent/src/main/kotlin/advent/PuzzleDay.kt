package advent

import java.nio.file.Paths
import kotlin.test.Test
import kotlin.test.assertNotEquals

open class PuzzleDay(val day: Int, val year: Int) {

    private fun getDayFilename(day: Int, year: Int, testNum: Int? = null): String =
        "$year/Dec${day}${if (testNum != null) "-test-$testNum" else ""}.txt"

    private fun loadDay(day: Int, year: Int, testNum: Int? = null, delimiter: String = "\n"): List<String> =
        loadResource(getDayFilename(day, year, testNum), delimiter)

    private fun loadResource(filename: String, delimiter: String = "\n"): List<String> {
        // TODO resources folder acting up. this was working now does not. forcing absolute path instead since this will never be a jar anyway :shrug:
        // javaClass.getResource(filename).readText().split(delimiter)

        val fullFile = Paths.get("src/main/resources/${filename}").toAbsolutePath()
//        println("loading resource: $fullFile")
        return fullFile.toUri().toURL().readText().split(delimiter)
    }

    open fun load(testNum: Int? = null, delimiter: String = "\n") = loadDay(day, year, testNum, delimiter)

    open fun puzzle1(): Any = "TODO"

    open fun puzzle2(): Any = "TODO"

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
