package org.elwaxoro.advent.y2015

import org.elwaxoro.advent.PuzzleDayTester

/**
 * Matchsticks
 */
class Dec08 : PuzzleDayTester(8, 2015) {
    override fun puzzle1(): Any = load().let { rawLines ->
        rawLines.sumOf { it.length } -
            rawLines.map {
                it.drop(1).dropLast(1)
                    .replace(slash + quote, quote)
                    .replace(slash + slash, slash)
                    .replace(Regex("""\\x[0-9a-f]{2}""")) { match ->
                        match.value.drop(2).toInt(16).toChar().toString()
                    }
            }.sumOf { it.length }
    }

    override fun puzzle2(): Any = load().let { rawLines ->
        rawLines.map {
            it.replace(slash, slash + slash)
                .replace(quote, slash + quote)
        }.sumOf { it.length + 2 } - rawLines.sumOf { it.length }
    }

    private val quote = """
        "
    """.trimIndent()
    private val slash = """
        \
    """.trimIndent()
}
