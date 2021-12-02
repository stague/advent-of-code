package org.elwaxoro.advent.y2021

import org.elwaxoro.advent.PuzzleDayTester

/**
 * Dive!
 */
class Dec02 : PuzzleDayTester(2, 2021) {


    override fun puzzle1(): Any = loadMaster().fold(SubCoord(), Instruction::diveDiveDive).whereAmI()
    override fun puzzle2(): Any = loadMaster().fold(SubCoord(), Instruction::diveButAimThisTime).whereAmI()

    private fun loadMaster() = load().map(Instruction::fromString)

    enum class Direction {
        FORWARD,
        DOWN,
        UP
    }

    data class SubCoord(
        val x: Int = 0,
        val y: Int = 0,
        val aim: Int = 0,
    ) {
        fun whereAmI(): Int = x * y
    }

    data class Instruction(
        val direction: Direction,
        val distance: Int,
    ) {
        companion object {
            fun fromString(input: String): Instruction = input.split(" ").let {
                Instruction(Direction.valueOf(it.first().uppercase()), it.last().toInt())
            }

            fun diveDiveDive(start: SubCoord, i: Instruction): SubCoord = when (i.direction) {
                Direction.FORWARD -> SubCoord(start.x + i.distance, start.y)
                Direction.DOWN -> SubCoord(start.x, start.y + i.distance)
                Direction.UP -> SubCoord(start.x, start.y - i.distance)
            }

            fun diveButAimThisTime(start: SubCoord, i: Instruction): SubCoord = when (i.direction) {
                Direction.FORWARD -> SubCoord(start.x + i.distance, start.y + (start.aim * i.distance), start.aim)
                Direction.DOWN -> SubCoord(start.x, start.y, start.aim + i.distance)
                Direction.UP -> SubCoord(start.x, start.y, start.aim - i.distance)
            }
        }
    }
}
