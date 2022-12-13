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

class Dec11 : PuzzleDayTester(11, 2022) {
    override val testRuns = listOf(
            TestRun(1, 10605L, 2713310158L),
            TestRun(null, 72884L, 15310845153L)
    )

    override fun part1(testFileSuffix: Int?): Any = monkeyLoader(testFileSuffix).let { monkeyMap ->
        repeat(20) {
            monkeyMap.values.forEach { monkey ->
                val iter = monkey.items.listIterator()
                while (iter.hasNext()) {
                    val item = iter.next()
                    monkey.processItem(item).also { (newValue, destination) ->
                        monkeyMap[destination]!!.items.add(newValue)
                    }
                    iter.remove()
                }
            }
        }
        monkeyMap.values.map { it.inspectionCount }.sorted().takeLast(2).reduce { acc, i -> acc * i }

    }

    override fun part2(testFileSuffix: Int?): Any = monkeyLoader(testFileSuffix).let { monkeyMap ->
        val maxMod = monkeyMap.values.map { it.testMod }.reduce { acc, mod ->  acc * mod }


        repeat(10000) {
            monkeyMap.values.forEach { monkey ->
                val iter = monkey.items.listIterator()
                while (iter.hasNext()) {
                    val item = iter.next()
                    monkey.processItem2(item, maxMod).also { (newValue, destination) ->
                        monkeyMap[destination]!!.items.add(newValue)
                    }
                    iter.remove()
                }
            }
        }
        monkeyMap.values.forEach {
                println("Monkey ${it.nameIdx} (${it.inspectionCount}): ${it.items} ")
            }
        monkeyMap.values.map { it.inspectionCount }.sorted().takeLast(2).reduce { acc, i -> acc * i }

    }

    private fun monkeyLoader(testFileSuffix: Int?) = load(testFileSuffix, "\n\n").map { monkeyChonk ->
        monkeyChonk.split("\n").let { monkey ->
            Monkey(
                    nameIdx = monkey[0].split(" ")[1].replace(":", "").toInt(),
                    operation = monkey[2].replace("Operation: new = old ", "").trim().let {
                        it.split(" ").let {
                            when {
                                it.first() == "*" && it.last() == "old" -> { a: BigInteger -> a * a }
                                it.first() == "*" -> { a: BigInteger -> a * it.last().toBigInteger() }
                                it.first() == "+" -> { a: BigInteger -> a + it.last().toBigInteger() }
                                else -> throw Exception("ONOES")
                            }
                        }
                    },
                    testMod = monkey[3].replace("Test: divisible by ", "").trim().toBigInteger(),
                    testPassDestination = monkey[4].replace("If true: throw to monkey ", "").trim().toInt(),
                    testFailDestination = monkey[5].replace("If false: throw to monkey ", "").trim().toInt()

            ).also {
                it.items.addAll(
                        monkey[1].replace("Starting items: ", "").trim().split(",").map { it.trim().toBigInteger() }
                )
            }
        }
    }.associateBy { it.nameIdx }


    class Monkey(
            val nameIdx: Int,
            val operation: (BigInteger) -> BigInteger,
            val testMod: BigInteger,
            val testPassDestination: Int,
            val testFailDestination: Int
    ) {
        val items = LinkedList<BigInteger>()
        var inspectionCount = 0L

        fun processItem(item: BigInteger): Pair<BigInteger, Int> =
            operation(item).div(BigInteger.valueOf(3L)).let{
                inspectionCount++
                it to if(it.mod(testMod) == BigInteger.ZERO) testPassDestination else testFailDestination
        }

        fun processItem2(item: BigInteger, maxMod: BigInteger): Pair<BigInteger, Int> =
                operation(item).mod(maxMod).let{
                    inspectionCount++
                    it to if(it.mod(testMod) == BigInteger.ZERO) testPassDestination else testFailDestination
                }
    }
}


