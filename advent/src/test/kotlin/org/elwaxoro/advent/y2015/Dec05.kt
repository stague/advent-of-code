package org.elwaxoro.advent.y2015

import org.elwaxoro.advent.PuzzleDayTester

/**
 * https://adventofcode.com/2015/day/5
 * Part 1: 258
 * Part 2: 53
 */
class Dec05 : PuzzleDayTester(5, 2015) {

    private val vowels = listOf('a', 'e', 'i', 'o', 'u')
    private val bad = listOf("ab", "cd", "pq", "xy")

    override fun puzzle1(): Any = load().filter { line ->
        line.count { it in vowels } > 2 // at least three vowels
            && line.zipWithNext().any { it.first == it.second } // at least one letter that appears twice in a row
            && bad.none { line.contains(it) } // does not contain the strings ab, cd, pq, or xy
    }.count()

    override fun puzzle2(): Any = load().filter { line ->
        // blerg. this got messy. loop every single char
        line.mapIndexedNotNull { index, c ->
            // save next char for the pair, then substring the rest. if the rest contains the pair, its a match
            if (line.length > index + 3 && line.substring(index + 2).contains("$c${line[index + 1]}")) {
                c
            } else {
                null
            }
        }.isNotEmpty() // count the pairs
            &&
            // loop every single char
            line.mapIndexedNotNull { index, c ->
                // if idx + 2 == char, match
                if (line.length > index + 2 && c == line[index + 2]) {
                    c
                } else {
                    null
                }
            }.isNotEmpty() // count the matches
    }.count()
}