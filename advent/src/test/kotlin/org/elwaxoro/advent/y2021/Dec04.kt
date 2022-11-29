package org.elwaxoro.advent.y2021

import org.elwaxoro.advent.PuzzleDayTester
import org.elwaxoro.advent.rowColSwap

/**
 * Giant Squid
 * (in which I abuse for loops)
 */
class Dec04 : PuzzleDayTester(4, 2021) {

    override fun part1(): Any = loadBoards().let { (calls, boards) ->
        calls.forEach { call ->
            boards.forEach { board ->
                if (board.callNumber(call)) {
                    return board.score()
                }
            }
        }
    }

    override fun part2(): Any = loadBoards().let { (calls, boards) ->
        val remainingBoards = boards.toMutableList()
        val winningBoards = mutableListOf<Board>()
        calls.forEach { call ->
            val iter = remainingBoards.iterator()
            while (iter.hasNext()) {
                val board = iter.next()
                if (board.callNumber(call)) {
                    iter.remove()
                    winningBoards.add(board)
                }
            }
        }
        winningBoards.last().score()
    }

    data class Board(
        val rows: MutableList<MutableList<Pair<Int, Boolean>>>
    ) {
        var lastCall: Int = -1
        val cols: MutableList<MutableList<Pair<Int, Boolean>>> = rows.rowColSwap()

        fun callNumber(call: Int): Boolean {
            rows.forEachIndexed { rowIdx, row ->
                row.forEachIndexed { colIdx, col ->
                    if (col.first == call) {
                        rows[rowIdx][colIdx] = Pair(call, true)
                        cols[colIdx][rowIdx] = Pair(call, true)
                    }
                }
            }
            lastCall = call
            return checkWin()
        }

        fun checkWin(): Boolean =
            rows.any { row ->
                row.all { it.second }
            } || cols.any { col ->
                col.all { it.second }
            }

        fun score(): Int =
            lastCall * rows.flatMap { row ->
                row.filterNot { it.second }
            }.sumOf { it.first }
    }

    private fun loadBoards(): Pair<List<Int>, List<Board>> = load(delimiter = "\n\n").let { chunks ->
        val calls = chunks[0].split(",").map { it.toInt() }
        val boards = chunks.drop(1).map { raw ->
            Board(raw.split("\n").map { row -> row.trim().split(Regex("\\W+")).map { Pair(it.toInt(), false) }.toMutableList() }.toMutableList())
        }
        Pair(calls, boards)
    }
}
