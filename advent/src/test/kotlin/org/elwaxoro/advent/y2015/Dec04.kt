package org.elwaxoro.advent.y2015

import org.elwaxoro.advent.PuzzleDayTester
import org.elwaxoro.advent.toHexString
import java.security.MessageDigest

/**
 * https://adventofcode.com/2015/day/4
 * 1: 282749 (~500ms)
 * 2: 9962624 (~8000ms)
 */
class Dec04 : PuzzleDayTester(4, 2015) {

    private val input = "yzbqklnj"

    override fun puzzle1(): Any = hashHaxxor("00000")
    override fun puzzle2(): Any = hashHaxxor("000000")

    fun hashHaxxor(goal: String): Long = MessageDigest.getInstance("MD5").let { md2020 ->
        generateSequence(0L) { it + 1 }.first { seed ->
            md2020.update("$input$seed".encodeToByteArray())
            md2020.digest().toHexString().startsWith(goal)
        }
    }
}
