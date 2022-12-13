package org.elwaxoro.advent.y2022

import org.elwaxoro.advent.Coord
import org.elwaxoro.advent.Dir
import org.elwaxoro.advent.PuzzleDayTester
import org.elwaxoro.advent.printify
import java.lang.StringBuilder
import java.math.BigInteger
import java.util.LinkedList
import kotlin.concurrent.timer
import kotlin.math.abs
import kotlin.math.sign

class Dec12 : PuzzleDayTester(12, 2022) {
    override val testRuns = listOf(
            TestRun(1, 31, 1),
//            TestRun(null, 72884L, 15310845153L)
    )

    override fun part1(testFileSuffix: Int?): Any = loadMatrix(testFileSuffix).let { matrix ->

        val root = TopoNav(matrix)
        while (!root.noMoreMoves) {
            root.move()
        }
        root.fetchChildrenRecursive().also { println(it.size) }.partition { it.noMoreMoves }.let { (done, undone) ->
            check(undone.isEmpty())
            done.partition { it.valueAtCurPosition == END }.let { (success, _) ->
//                println("Success: ${success.map { it.print() }}")
//                println("Fail: ${fail.map { it.print() }}")
                success.minOf { it.moveCount }
            }
        }
    }

    class TopoNav(
            val matrix: Array<IntArray>,
            var moveCount: Int = 0,
            var curPosition: Coord = Coord(),
            var valueAtCurPosition: Int = 10,
            var noMoreMoves: Boolean = false,
            val children: MutableList<TopoNav> = mutableListOf()
    ) {
        fun fetchChildrenRecursive(): List<TopoNav> = if (children.isEmpty()) {
            listOf(this)
        } else {
            this.children.flatMap { it.fetchChildrenRecursive() } + this
        }

        data class Move(val newPosition: Coord, val dir: Dir, val newValueAtPosition: Int)

        fun minMovesCompletedChild():Int = fetchChildrenRecursive().filter {
            it.noMoreMoves && it.valueAtCurPosition == END
        }.takeIf { it.isNotEmpty() }?.minOf { it.moveCount } ?: Int.MAX_VALUE

        fun move() {
            if (!noMoreMoves) {
                val availableMoves = getLegalMoves()
                if (availableMoves.isNotEmpty()) {
                    availableMoves.drop(1).map { move ->
                        children.add(clone().also { it.applyMove(move) })
                    }
                    availableMoves.take(1).single().also { move ->
                        applyMove(move)
                    }
                } else {
                    noMoreMoves = true
                }
            }
            children.filterNot { it.noMoreMoves }.forEach { it.move() }
            if (!noMoreMoves) {
                move()
            }
        }

        fun clone() =
                TopoNav(
                        matrix = matrix.map { it.clone() }.toTypedArray(),
                        moveCount = moveCount,
                        curPosition = curPosition.copy(),
                        valueAtCurPosition = valueAtCurPosition,
                        noMoreMoves = noMoreMoves,
                        children = mutableListOf()
                )


        fun applyMove(move: Move) {
//            println("-----------------------")
//            print()
//            println("${move.dir.name} ${move.newPosition} ${move.newValueAtPosition}")
            moveCount++
            matrix[curPosition.y][curPosition.x] = when (move.dir) {
                Dir.E -> 100
                Dir.N -> 200
                Dir.S -> 300
                Dir.W -> 400
            }
            curPosition = move.newPosition
            valueAtCurPosition = move.newValueAtPosition
            if (valueAtCurPosition == END) {
                //println("MADE IT TO THE PROMISED LAND")
                noMoreMoves = true
            }
            if(moveCount > minMovesCompletedChild()){
                noMoreMoves = true
                println("detected shorter child. Quitting")
            }
        }

        fun getLegalMoves(): List<Move> = Dir.values().mapNotNull { dir ->
            val newPos = curPosition.move(dir)
            when {
                newPos.x < 0 -> null
                newPos.x > matrix[0].lastIndex -> null
                newPos.y < 0 -> null
                newPos.y > matrix.lastIndex -> null
                else -> {
                    val valueAtNewPos = matrix[newPos.y][newPos.x]
                    if (valueAtNewPos < 100 && (valueAtNewPos == valueAtCurPosition || valueAtNewPos == valueAtCurPosition + 1 || valueAtNewPos == valueAtCurPosition - 1)) {
                        Move(newPos, dir, valueAtNewPos)
                    } else null
                }
            }
        }

        fun print() {
            println("________________")
            println("y:${curPosition.y} x:${curPosition.x} valAtCurrent:$valueAtCurPosition moves:$moveCount")
            matrix.forEach {
                println(it.joinToString {
                    when (it) {
                        100 -> ">"
                        200 -> "v"
                        300 -> "^"
                        400 -> "<"
                        else -> it.toString()
                    }.padStart(3, ' ')
                })
            }.also { println("________________") }
        }
    }

    private fun loadMatrix(testFileSuffix: Int?): Array<IntArray> = load(testFileSuffix).map {
        it.map {
            when (it) {
                'S' -> 0
                'E' -> END
                else -> Character.getNumericValue(it)// case insensitive maps to int. a/A = 10 b/B = 11
            }
        }.toIntArray()
    }.toTypedArray()

    companion object {
        private val END = Character.getNumericValue('z') + 1
    }

    override fun part2(testFileSuffix: Int?): Any = 1
}


