package org.elwaxoro.advent.y2019

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.runBlocking
import org.elwaxoro.advent.PuzzleDayTester
import org.elwaxoro.advent.permutations

/**
 * Day 7: Amplification Circuit
 */
class Dec07: PuzzleDayTester(7, 2019) {

    /**
     * Create all permutations of the list [0,1,2,3,4], then find the max of running each list
     * Running a list by looping each item, feeding output of the previous into the next
     */
    override fun part1(): Any = Dec7Compy(loadToInt(delimiter = ",")).let { compy ->
        (0..4).toList().permutations().maxOf { phaseSettings->
            phaseSettings.fold(0) { acc, setting ->
                compy.run(listOf(setting, acc))
            }
        }
    }// == 79723

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

    override fun part2(): Any {
        return "WIP"
    }

//    override fun part2(): Any = runBlocking {
//        val input = listOf(9,8,7,6,5)
//        val code = loadToInt(delimiter = ",")
//
//        var ampInput = listOf(input[0], 0).toChannel(close = false)
//        val firstInput = ampInput
//        listOf("A","B","C","D","E").mapIndexed { idx, name ->
//            val localInput = ampInput
//            ampInput = Channel(capacity = Channel.UNLIMITED)
//            if(idx+1 < input.size) {
//                localInput.send(input[idx+1])
//            } else {
//                ampInput = firstInput
//            }
//
//            async {
//                Intercode(code, name = name).run(localInput, ampInput)
//            }
//        }.map { it.join() }
//        println("everone stopped???")
//        var output = firstInput.receive()
//        while(!firstInput.isClosedForReceive) {
//            output = firstInput.receive()
//        }
//
//        println("Last amp should have a final output? $output")
//    }
}
