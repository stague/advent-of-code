package org.elwaxoro.advent.y2021

import org.elwaxoro.advent.PuzzleDayTester

/**
 * Arithmetic Logic Unit
 */
class Dec24 : PuzzleDayTester(24, 2021) {

    override fun part1(testFileSuffix: Int?): Any = monaderator(isMax = true)
    override fun part2(testFileSuffix: Int?): Any = monaderator(isMax = false)

    /**
     * Can treat this whole program as a set of smaller sub-programs each marked at the start by "inp w"
     * There are 14 sub-programs and 14 numbers in the monad. Coincidence? I think not!
     * Most of the program is useless, just need to know if the sub-program is a "push" or a "pop" action
     * Pair each push to a pop, pushes always come with a positive number and pops with a negative number
     */
    private fun monaderator(isMax: Boolean): String =
        parse().foldIndexed(mutableMapOf<Int, Int>() to mutableListOf<Pair<Int, Int>>()) { idx, acc, subProgram ->
            acc.also { (monad, stack) ->
                // cmd 5 is always something like "add x -8", if it's positive this is a "push" sub-program, else it's a "pop"
                val mod = subProgram[5][2].toInt()
                // cmd 15 is the only other line that matters, always something like "add y 5"
                // push or pop the stack and compare it to the saved digitMod for this sub-program:
                // this gives both min and max values for the popped idx as well as the current idx
                if (mod > 0) {
                    // push the currently tracked digit in "add y <int>"
                    // this will be paired with the pop action later to decide what high/low values belong to the pair of push/pop digits
                    stack.add(idx to subProgram[15][2].toInt())
                } else {
                    val (popIdx, popMod) = stack.removeLast()
                    val diff = popMod + mod //popMod is always positive, digitMod is always negative
                    if (diff < 0) {
                        if (isMax) {
                            monad[popIdx] = 9
                            monad[idx] = 9 + diff
                        } else {
                            monad[popIdx] = -1 * diff + 1
                            monad[idx] = 1
                        }
                    } else {
                        if (isMax) {
                            monad[popIdx] = 9 - diff
                            monad[idx] = 9
                        } else {
                            monad[popIdx] = 1
                            monad[idx] = 1 + diff
                        }
                    }
                }
            }
        }.let { (monad, _) ->
            monad.keys.sorted().joinToString("") { d ->
                "${monad[d]}"
            }
        }

    /**
     * Ham-jammed the input file to split each sub-program with an extra newline
     * returns a list of subprograms, each subprogram is a list of strings to split "add x -8" into ["add", "x", "-8"]
     */
    private fun parse(): List<List<List<String>>> = load(delimiter = "\n\n").map { it.split("\n").map { it.split(" ") } }
}
