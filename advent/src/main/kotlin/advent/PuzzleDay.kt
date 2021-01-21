package advent

open class PuzzleDay(val day: Int, val year: Int) {

    private fun <R> measureTime(item: String, fn: () -> R): R =
        System.currentTimeMillis().let { start ->
            println("--------------------------------------------------------------------------------")
            println("START $item")
            fn().also {
                println("COMPLETE $item took ${System.currentTimeMillis() - start}ms")
            }
        }

    private fun getDayFilename(day: Int, year: Int, testNum: Int? = null): String =
        "$year/Dec${day}${if(testNum != null) "-test-$testNum" else ""}.txt"

    private fun loadDay(day: Int, year: Int, testNum: Int? = null, delimiter: String = "\n"): List<String> =
        loadResource(getDayFilename(day, year, testNum), delimiter)

    private fun loadResource(filename: String, delimiter: String = "\n"): List<String> =
        javaClass.getResource(filename).readText().split(delimiter)

    open fun load(testNum: Int? = null, delimiter: String = "\n") = loadDay(day, year, testNum, delimiter)

    open fun puzzle1(): Any = "TODO"

    open fun puzzle2(): Any = "TODO"

    open fun runSingle(isOne: Boolean): Any = measureTime("Puzzle ${if (isOne) "1" else "2"}") {
        if(isOne) {
            puzzle1()
        } else {
            puzzle2()
        }
    }

    open fun run(): Any = "Puzzle 1: ${runSingle(true)} Puzzle 2: ${runSingle(false)}"
}
