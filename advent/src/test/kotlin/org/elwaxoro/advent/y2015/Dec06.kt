package org.elwaxoro.advent.y2015

import org.elwaxoro.advent.Coord
import org.elwaxoro.advent.PuzzleDayTester
import kotlin.math.max

/**
 * https://adventofcode.com/2015/day/6
 * Part 1: 569999
 * Part 2: 17836115
 */
class Dec06 : PuzzleDayTester(6, 2015) {

    override fun part1(testFileSuffix: Int?): Any = doTheThing(::cmdPuzzle1)
    override fun part2(testFileSuffix: Int?): Any = doTheThing(::cmdPuzzle2)

    private enum class CMD(val parse: String) {
        ON("turn on "),
        OFF("turn off "),
        TOGGLE("toggle ")
    }

    private fun doTheThing(mod: (initial: Int, cmd: CMD) -> Int): Int =
        // make the 1000x1000 grid
        MutableList(1000) { MutableList(1000) { 0 } }.also { grid ->
            load().map { line ->
                // parse the command and the coords
                val cmd = CMD.values().single { line.startsWith(it.parse) }
                val coords = line.replace("through ", "").replace(cmd.parse, "").split(" ").map { Coord.parse(it) }

                // build the list of all impacted coords, then run the command on that pixel
                coords[0].enumerateRectangle(coords[1]).map {
                    grid[it.y][it.x] = mod(grid[it.y][it.x], cmd)
                }
            }
        }.sumOf{ it.sum() }

    private fun cmdPuzzle1(initial: Int, cmd: CMD): Int =
        when (cmd) {
            CMD.ON -> 1
            CMD.OFF -> 0
            CMD.TOGGLE -> (initial + 1) % 2
        }

    private fun cmdPuzzle2(initial: Int, cmd: CMD): Int =
        when (cmd) {
            CMD.ON -> initial + 1
            CMD.OFF -> max(0, initial - 1)
            CMD.TOGGLE -> initial + 2
        }
}
