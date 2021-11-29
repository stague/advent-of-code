package org.elwaxoro.advent.y2020

import org.elwaxoro.advent.PuzzleDayTester

class Dec03 : PuzzleDayTester(3, 2020) {

    override fun puzzle1(): Any = countTrees(parseInput(), 3, 1)

    override fun puzzle2(): Any = DOTHEDEW(parseInput())

    private fun DOTHEDEW(rows: List<List<Int>>) =
        listOf(
            1 to 1,
            3 to 1,
            5 to 1,
            7 to 1,
            1 to 2
        ).map { countTrees(rows, it.first, it.second) }
            .reduce { total, row -> total * row }

    private fun countTrees(rows: List<List<Int>>, colStep: Int, rowStep: Int): Long =
        rows.filterIndexed { index, _ -> index % rowStep == 0 }
            .fold(0 to 0L) { thingy, row ->
                thingy.first + colStep to thingy.second + row[thingy.first % row.size]
            }.second

    private fun parseInput(): List<List<Int>> = load().map { row ->
        row.trim().toCharArray().map {
            if (it == '#')
                1
            else
                0
        }
    }

    private fun countTreesLoop(rows: List<List<Int>>, colStep: Int, rowStep: Int): Long {
        // god i forgot how many variables you have to use with a for loop wtf
        var col = 0
        var counter = 0L
        for (i in rows.indices step rowStep) {
            val row = rows[i]
            counter += row[col % row.size]
            col += colStep
        }
        return counter
    }
}
