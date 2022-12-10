package org.elwaxoro.advent.y2022

import org.elwaxoro.advent.Coord
import org.elwaxoro.advent.Dir
import org.elwaxoro.advent.PuzzleDayTester
import org.elwaxoro.advent.printify
import java.lang.StringBuilder
import java.util.LinkedList
import kotlin.math.abs
import kotlin.math.sign

class Dec10 : PuzzleDayTester(10, 2022) {
    override val testRuns = listOf(
            TestRun(1, 13140, 1),
            TestRun(null, 12460, 1)
    )

    override fun part1(testFileSuffix: Int?): Any = load(testFileSuffix).map {
        it.split(" ").takeIf { it.size == 2 }?.last()?.toInt()
    }.let { instructionSet ->
        var cycle = 0
        var x = 1
        var runningTally = 0
        instructionSet.forEach { instr ->
//            println("cycle: $cycle x $x instr: $instr")
            cycle ++
            if(cycle.mod(40) + 20 == 40) {
                (x * cycle).also {
//                    println("cycle: $cycle x $x instr: $instr - Adding to tally: $it")
                    runningTally += it
                }
            }
            if(instr != null) {
                cycle++
                if(cycle.mod(40) + 20 == 40) {
                    (x * cycle).also {
//                        println("cycle: $cycle x $x instr: $instr - Adding to tally: $it")
                        runningTally += it
                    }
                }
                x += instr
            }
        }
        runningTally
    }

    override fun part2(testFileSuffix: Int?): Any = load(testFileSuffix).map {
        it.split(" ").takeIf { it.size == 2 }?.last()?.toInt()
    }.let { instructionSet ->
        var cycle = 0
        val outputString = StringBuilder()
        instructionSet.fold(1){ acc, instr ->
            cycle ++
            outputString.append( if(acc.equalsWithinOne((cycle-1).mod(40))) '#' else '.')
            if(instr != null) {
                cycle++
                outputString.append( if(acc.equalsWithinOne((cycle-1).mod(40))) '#' else '.')
                acc + instr
            } else acc
        }
        outputString.chunked(40).forEach {
            println(it)
        }
        1
    }

    private fun Int.equalsWithinOne(that: Int) = this == that || this == that -1 || this == that +1


}


