package org.elwaxoro.advent.y2022

import org.elwaxoro.advent.PuzzleDayTester
import java.util.LinkedList

class Dec05 : PuzzleDayTester(5, 2022) {
    override val testRuns = listOf(
            TestRun(1, "CMZ", "MCD"),
            TestRun(null, "FWSHSPJWM", "PWPWHGFZS")
    )

    override fun part1(testFileSuffix: Int?): Any = loadManifest(testFileSuffix)
            .let { (manifestState, moves) ->
                moves.forEach { move ->
                    repeat(move.itemCountToMove) {
                        manifestState[move.destinationStack]!!.addFirst(
                                manifestState[move.sourceStack]!!.removeFirst())
                    }
                }
                manifestState.map { it.value.first() }.joinToString(separator = "")
            }

    private fun loadManifest(testFileSuffix: Int?) = load(testFileSuffix, "\n\n").let { combinedManifest ->
        check(combinedManifest.size == 2)
        combinedManifest.first().split("\n").let { stacksWithLabels ->
            stacksWithLabels.partition { it.contains('[') }.let { (stackInventory, stackLabel) ->
                val stackLabels = stackLabel.single()
                        .replace("   ", ",")
                        .replace(" ", "")
                        .split(",").map { it.toInt() }

                stackInventory.map { stackRow ->
                    stackRow.replace("    ", " []")
                            .replace(" ", ",")
                            .split(",").map { stackRowCsv ->
                                stackRowCsv.replace("[", "")
                                        .replace("]", "")
                            }
                }.let { stackRows ->
                    stackLabels.map { stackLabel ->
                        stackLabel to
                                LinkedList<String>().also {
                                    it.addAll(stackRows.mapNotNull { it.getOrNull(stackLabel - 1)?.takeUnless { it == "" } })
                                }
                    }.toMap()
                }

            }
        } to combinedManifest.last().split("\n").map { move ->
            move.replace("move ", "")
                    .replace(" from ", ",")
                    .replace(" to ", ",")
                    .split(",").let { csvMove ->
                        check(csvMove.size == 3)
                        Move(
                                itemCountToMove = csvMove[0].toInt(),
                                sourceStack = csvMove[1].toInt(),
                                destinationStack = csvMove[2].toInt()
                        )
                    }
        }
    }

    private data class Move(val itemCountToMove: Int, val sourceStack: Int, val destinationStack: Int)

    override fun part2(testFileSuffix: Int?): Any = loadManifest(testFileSuffix)
            .let { (manifestState, moves) ->
                moves.forEach { move ->
                    val moveSet = mutableListOf<String>()
                    repeat(move.itemCountToMove) {
                        moveSet.add(manifestState[move.sourceStack]!!.removeFirst())
                    }
                    moveSet.reversed().forEach {
                        manifestState[move.destinationStack]!!.addFirst(it)
                    }
                }
                manifestState.map { it.value.first() }.joinToString(separator = "")
            }
}

