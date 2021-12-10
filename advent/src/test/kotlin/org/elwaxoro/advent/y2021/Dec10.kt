package org.elwaxoro.advent.y2021

import org.elwaxoro.advent.PuzzleDayTester

/**
 * Syntax Scoring
 */
class Dec10 : PuzzleDayTester(10, 2021) {

    override fun puzzle1(): Any = parse().mapNotNull { line ->
        line.analyze(scoreCorrupt = true)
    }.sum() == 296535L

    override fun puzzle2(): Any = parse().mapNotNull { line ->
        line.analyze(scoreCorrupt = false)
    }.sorted().let {
        it[it.size / 2]
    } == 4245130838L

    private fun parse(): List<List<Syntax>> = load().map { it.map(Syntax::parse) }

    fun List<Syntax>.analyze(scoreCorrupt: Boolean): Long? {
        val stack = mutableListOf<Syntax>()
        forEach { char ->
            if (char.isClose()) {
                val pop = stack.removeLast()
                if (char.getMatch() != pop) {
                    // part 1: only score first corrupt char
                    return char.corruptScore.takeIf { scoreCorrupt }
                }
            } else {
                stack.add(char)
            }
        }
        // part 2: score the missing chars needed to fix the incomplete line
        return stack.reversed().map {
            it.getMatch().incompleteScore
        }.fold(0L) { acc, closer ->
            (acc * 5L) + closer
        }.takeUnless { scoreCorrupt }
    }

    enum class Syntax(val char: Char, val corruptScore: Long = 0, val incompleteScore: Long = 0) {
        OPEN_PAREN('('),
        CLOSE_PAREN(')', 3L, 1L),
        OPEN_BRACKET('['),
        CLOSE_BRACKET(']', 57L, 2L),
        OPEN_CURLY('{'),
        CLOSE_CURLY('}', 1197L, 3L),
        OPEN_ANGLE('<'),
        CLOSE_ANGLE('>', 25137L, 4L);

        companion object {
            fun parse(char: Char): Syntax = values().single { it.char == char }
        }

        fun isClose(): Boolean = corruptScore > 0

        fun matches(that: Syntax): Boolean = getMatch() == that

        fun getMatch(): Syntax = when (this) {
            OPEN_PAREN -> CLOSE_PAREN
            CLOSE_PAREN -> OPEN_PAREN
            OPEN_BRACKET -> CLOSE_BRACKET
            CLOSE_BRACKET -> OPEN_BRACKET
            OPEN_CURLY -> CLOSE_CURLY
            CLOSE_CURLY -> OPEN_CURLY
            OPEN_ANGLE -> CLOSE_ANGLE
            CLOSE_ANGLE -> OPEN_ANGLE
        }
    }
}
