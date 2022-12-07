package org.elwaxoro.advent.y2022

import org.elwaxoro.advent.PuzzleDayTester

class Dec01 : PuzzleDayTester(1, 2022) {
    override val testRuns = listOf(
            TestRun(1, 24000, 45000),
            TestRun(null, 72511, 212117)
    )

    override fun part1(testFileSuffix: Int?): Int = loadToInventory(testFileSuffix).maxOf { it.calorieSum() }

    override fun part2(testFileSuffix: Int?): Any = loadToInventory(testFileSuffix).sortedByDescending { it.calorieSum() }.take(3).sumOf { it.calorieSum() }

    private fun loadToInventory(testFileSuffix: Int?): List<ElfInventory> = load(testFileSuffix, "\n\n").map { calStrings ->
        ElfInventory(calStrings.split("\n").map { it.toInt() })
    }

    // Derp but maybe useful for later
    private fun loadToInventory_OLD(testNum: Int? = null): List<ElfInventory> = load(testNum).let { calStrings ->
        val elfList = mutableListOf<ElfInventory>()

        val calIter = calStrings.iterator()
        val calList = mutableListOf<Int>()

        while (calIter.hasNext()) {
            val curVal = calIter.next()
            if (curVal == "") {
                elfList.add(ElfInventory(calList.toList()))
                calList.clear()
            } else {
                calList.add(curVal.toInt())
            }
        }
        elfList.add(ElfInventory(calList.toList())).also {
            val elfCount = calStrings.count { it == "" } + 1
            if (elfList.size != elfCount) {
                throw Exception("Boi you done fucked up")
            }
        }
        elfList
    }

    data class ElfInventory(val cals: List<Int>) {
        fun calorieSum() = cals.sum()
    }
}

