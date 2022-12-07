package org.elwaxoro.advent.y2022

import org.elwaxoro.advent.PuzzleDayTester

class Dec04 : PuzzleDayTester(4, 2022) {
    override val testRuns = listOf(
            TestRun(1, 2, 4),
            TestRun(null, 453, 919)
    )

    override fun part1(testFileSuffix: Int?): Any = loadAssignments(testFileSuffix).count {
        it.first().fullyContains(it.last()) || it.last().fullyContains(it.first())
    }

    private fun Pair<Int, Int>.fullyContains(that: Pair<Int, Int>): Boolean = this.first <= that.first && this.second >= that.second

    private fun loadAssignments(testFileSuffix: Int?) = load(testFileSuffix).map { row ->
        row.split(',').map { assignedSections ->
            assignedSections.split('-').let { sections ->
                Pair(sections.first().toInt(), sections.last().toInt())
            }
        }
    }

    override fun part2(testFileSuffix: Int?): Any = loadAssignments(testFileSuffix).count {
        it.first().overlaps(it.last()) || it.last().fullyContains(it.first())
    }

    private fun Pair<Int, Int>.overlaps(that: Pair<Int, Int>): Boolean = this.first <= that.second && this.second >= that.first
}

