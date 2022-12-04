package org.elwaxoro.advent.y2019

import org.elwaxoro.advent.PuzzleDayTester

/**
 * Day 5: Sunny with a Chance of Asteroids
 * Note: expanded Intercode with all requirements for today's puzzle
 */
class Dec05 : PuzzleDayTester(5, 2019) {
    override fun part1(): Any = Intercode(loadToInt(delimiter = ",")).run(1) == 11193703
    override fun part2(): Any = Intercode(loadToInt(delimiter = ",")).run(5) == 12410607
}
