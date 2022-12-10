package org.elwaxoro.advent.y2022

import org.elwaxoro.advent.Coord
import org.elwaxoro.advent.Dir
import org.elwaxoro.advent.PuzzleDayTester
import org.elwaxoro.advent.printify
import kotlin.math.abs
import kotlin.math.sign

class Dec09 : PuzzleDayTester(9, 2022) {
    override val testRuns = listOf(
            TestRun(1, 13, 1),
            TestRun(2, 88, 36),
            TestRun(null, 6098, 2597)
    )

    override fun part1(testFileSuffix: Int?): Any = getMoves(testFileSuffix).let { moves ->
        val headMoves: MutableList<Coord> = mutableListOf(Coord())
        val tailMoves: MutableList<Coord> = mutableListOf(Coord())
        moves.forEach { (dir, dist) ->
            repeat(dist) {
                val currentHead = headMoves.last()
                val currentTail = tailMoves.last()
                val newHead = currentHead.move(dir)
                val newTail = if (newHead.neighbors9().flatten().contains(currentTail)) {
                    currentTail
                } else currentHead
                headMoves.add(newHead)
                tailMoves.add(newTail)
            }
        }
        tailMoves.distinct().size
    }

    override fun part2(testFileSuffix: Int?): Any = getMoves(testFileSuffix).let { moves ->
        val moveHist = (0..9).map {
            mutableListOf(Coord())
        }

        moves.forEach { (dir, dist) ->
            repeat(dist) {
                moveHist.zip(mutableListOf(Coord())).let {

                }
                moveHist.forEachIndexed { idx, move ->
                    if (idx == 0) {
                        move.add(move.last().move(dir))
                    } else {
                        val leaderNewestPosition = moveHist[idx - 1].last()
                        val currentPosition = move.last()

                        move.add(
                                when {
                                    leaderNewestPosition.neighbors9().flatten().contains(currentPosition) -> currentPosition
                                    leaderNewestPosition.x == currentPosition.x -> if (leaderNewestPosition.y > currentPosition.y) currentPosition.move(Dir.N) else currentPosition.move(Dir.S)
                                    leaderNewestPosition.y == currentPosition.y -> if (leaderNewestPosition.x > currentPosition.x) currentPosition.move(Dir.E) else currentPosition.move(Dir.W)
                                    else -> currentPosition.add((leaderNewestPosition.x - currentPosition.x).sign, (leaderNewestPosition.y - currentPosition.y).sign)

                                }
                        )
                    }
                }
            }
        }
        moveHist.last().also {
            println( it.printify(invert = true))
        }.distinct().size
    }

    private fun getMoves(testFileSuffix: Int?): List<Pair<Dir, Int>> = load(testFileSuffix).map { row ->
        row.split(" ").let {
            check(it.size == 2)
            Dir.fromUDLR(it.first().first()) to it.last().toInt()
        }
    }

    private fun List<Coord>.followMe(): List<Coord> = mutableListOf<Coord>().also { tailTrack ->
        this.mapTo(mutableListOf<Coord>()) { it }
        fold(Coord(0, 0)) { tail, next ->
            val dx = next.x - tail.x
            val dy = next.y - tail.y
            if (abs(dx) > 1 || abs(dy) > 1) {
                Coord(tail.x + dx.sign, tail.y + dy.sign).also { tailTrack.add(it) }
            } else tail
        }
    }

    public inline fun <T, R> Iterable<T>.fold2(initial: R, operation: (acc: R, T) -> R): R {
        var accumulator = initial
        for (element in this) accumulator = operation(accumulator, element)
        return accumulator
    }
}


