package org.elwaxoro.advent.y2021

import org.elwaxoro.advent.PuzzleDayTester

/**
 * Syntax Scoring
 */
class Dec10 : PuzzleDayTester(10, 2021) {

    override fun part1(): Any = parse().mapNotNull { line ->
        line.analyze(scoreCorrupt = true)
    }.sum()

    override fun part2(): Any = parse().mapNotNull { line ->
        line.analyze(scoreCorrupt = false)
    }.sorted().let {
        it[it.size / 2]
    }

    private fun parse(): List<List<Syntax>> = load().map { it.map(Syntax::parse) }

    private fun List<Syntax>.analyze(scoreCorrupt: Boolean): Long? = mutableListOf<Syntax>().let { stack ->
        firstOrNull { char ->
            if (char.isClose()) {
                !char.matches(stack.removeLast())
            } else {
                !stack.add(char)
            }
        }.let { corrupt ->
            corrupt?.corruptScore.takeIf { scoreCorrupt } ?: stack.scoreIncomplete().takeIf { corrupt == null && !scoreCorrupt }
        }
    }

    private fun List<Syntax>.scoreIncomplete(): Long = reversed().map { it.getMatch().incompleteScore }.fold(0L) { acc, char -> (acc * 5L) + char }

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
