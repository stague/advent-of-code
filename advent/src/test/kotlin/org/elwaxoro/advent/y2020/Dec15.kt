package org.elwaxoro.advent.y2020

import org.elwaxoro.advent.PuzzleDayTester

class Dec15 : PuzzleDayTester(15, 2020) {

    override fun puzzle1() = doTheSlightlyFasterThing(2020)
//    override fun puzzle2() = doTheSlowThing(30000000)
    override fun puzzle2(): Any = "OOM"

    // pre-load some arrays of "big enough size" to hold stuff
    private fun doTheSlightlyFasterThing(turns: Int): Any {
        val numbers = load(delimiter = ",").map { it.toInt() }
        val timesSpoken = Array(turns + 1) { 0 }
        val lastSpoken = Array(turns + 1) { 0 }

        // load the initial turns in
        numbers.forEachIndexed { idx, num ->
            timesSpoken[num] = 1
            lastSpoken[num] = idx + 1
        }

        return (numbers.size + 1..turns).fold(numbers.last()) { previousNumber, turn ->
            val speak = if (timesSpoken[previousNumber] == 1) {
                0
            } else {
                turn - 1 - lastSpoken[previousNumber]
            }
            lastSpoken[previousNumber] = turn - 1
            timesSpoken[speak] += 1
            speak
        }
    }

    // HOLY CRAP HASHMAP IS SLOW THIS WAY
    private fun doTheSlowThing(turns: Int): Any {
        val numbers = load(delimiter = ",").map { it.toLong() }
        val timesSpokenMap = numbers.map { it to 1L }.toMap(HashMap())
        val lastSpokenMap = numbers.mapIndexed { idx, num -> num to idx + 1L }.toMap(HashMap())

        return (numbers.size + 1..turns).fold(numbers.last()) { previousNumber, turn ->
            val speak = if (timesSpokenMap[previousNumber] == 1L) {
                0L
            } else {
                turn - 1 - lastSpokenMap[previousNumber]!!
            }
            lastSpokenMap[previousNumber] = turn - 1L
            timesSpokenMap[speak] = timesSpokenMap.getOrPut(speak) { 0L } + 1L
            speak
        }
    }

}
//6,19,0,5,7,13,1
//12,1,16,3,11,0

//object Try2{
//    fun main(args: Array<String>) {
//        println("Advent of Code day 14")
//        // Setup - define some input
//        val testInput1 = arrayListOf(1, 3, 2)
//        val testInput2 = arrayListOf(2, 1, 3)
//        val testInput3 = arrayListOf(1, 2, 3)
//        val testInput4 = arrayListOf(2, 3, 1)
//        val testInput5 = arrayListOf(3, 2, 1)
//        val testInput6 = arrayListOf(3, 1, 2)
//        val testInput7 = arrayListOf(0, 3, 6)
//        val realInput = arrayListOf(12, 1, 16, 3, 11, 0)
//        // Part 1 - Hey, this seems easy.
//        val numbers = realInput.toMutableList()
//        var iterations = 2020
//        var timeMs = measureTimeMillis {
//            while (numbers.size < iterations) {
//                when (val index = numbers.subList(0, numbers.size - 1).lastIndexOf(numbers.last())) {
//                    -1 -> numbers.add(0)
//                    else -> numbers.add(numbers.size - (index + 1))
//                }
//            }
//        }
//        println("Part 1 - The $iterations'th spoken number is ${numbers.last()}")
//        println("Part 1 execution took $timeMs ms")
//        // Part 2 - Oh... better use a map.
//        val spokenMap = HashMap<Int, Int>()
//        realInput.forEachIndexed { index, num -> spokenMap[num] = index }
//        var spokenNumber = 0    // Cheat a bit? We know the number to be spoken is 0 because the starting sequence is all unique
//        var nextSpokenNumber: Int
//        iterations = 30000000
//        timeMs = measureTimeMillis {
//            for (i in numbers.size..iterations - 2) {
//                nextSpokenNumber = i - (spokenMap[spokenNumber] ?: i)
//                spokenMap[spokenNumber] = i
//                spokenNumber = nextSpokenNumber
//            }
//        }
//        println("Part 2 - The $iterations'th spoken number is $spokenNumber")
//        println("Part 2 program execution took $timeMs ms")
//    }
//}
