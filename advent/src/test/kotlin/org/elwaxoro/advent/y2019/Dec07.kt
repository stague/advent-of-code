package org.elwaxoro.advent.y2019

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.runBlocking
import org.elwaxoro.advent.PuzzleDayTester

class Dec07: PuzzleDayTester(7, 2019) {

    override fun part1(): Any = Dec7Compy(loadToInt(delimiter = ",")).let { compy ->
        (0..4).toList().permutations().maxOf { phaseSettings->
            phaseSettings.fold(0) { acc, setting ->
                compy.run(listOf(setting, acc))
            }
        }
    } == 79723

    class Dec7Compy(program: List<Int>) : Intercode(program) {
        @OptIn(ExperimentalCoroutinesApi::class)
        fun run(input: List<Int>): Int = runBlocking {
            val output = Channel<Int>(capacity = Channel.UNLIMITED)
            run(input.toChannel(), output, program.toMutableList())
            // compy might output multiple items, just get the last one
            var ret = output.receive()
            while(!output.isClosedForReceive) {
                ret = output.receive()
            }
            ret
        }
    }

//    override fun part2(): Any = Intercode(loadToInt(delimiter = ",")).let { compy ->
//        // TODO compy instances need to have a blocking input queue instead of a list of inputs
////        (5..9).toList().permutations().maxOf { phaseSettings->
//        var counter = 1
//        val phaseSettings = listOf(9,8,7,6,5)
//            var output = 0
//            while(true) {
//                val newOutput = phaseSettings.fold(output) { acc, setting ->
//                    compy.run(listOf(setting, acc))
//                }
//                println("Run $counter = $output")
//                if(newOutput == output) {
//                    output = newOutput
//                    break
//                } else {
//                    output = newOutput
//                }
//                counter++
//            }
//            output
//        }
////    }

    fun <T> List<T>.permutations(): List<List<T>> =
        (0..lastIndex).fold(listOf(Pair(listOf<T>(), this))) { acc, _ ->
            acc.flatMap { (perm, candidates) ->
                candidates.map {
                    val newCans = candidates.minus(it)
                    Pair(perm.plus(it), newCans)
                }
            }
        }.map { it.first }
}
