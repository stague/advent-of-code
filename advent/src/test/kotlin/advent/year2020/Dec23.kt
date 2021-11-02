package advent.year2020

import advent.PuzzleDay

class Dec23 : PuzzleDay(23, 2020) {

    override fun puzzle1(): Any = playCupGameWithCrabbo(parse(), 100).let { cups ->
        cups.listify(1, cups[1]!!, listOf()).joinToString("")
    }

    override fun puzzle2(): Any = playCupGameWithCrabbo(parse().plus((10..1000000)), 10000000).let { cups ->
        cups[1]!! * cups[cups[1]]!!.toLong()
    }

    /**
     * Starting from an initial key, fetch value, then value's value as a pair
     */
    private fun Map<Int, Int>.extract(start: Int): Pair<Int, Int> = this[start]!! to this[this[start]]!!

    /**
     * Explore the map until the starting key is found again, return as a list
     * Initially this was to print the list, then it turned out this was the answer for part 1 also
     */
    private tailrec fun Map<Int, Int>.listify(start: Int, last: Int = start, thingy: List<Int> = listOf()): List<Int> {
        return if (this[last] == start) {
            // end of the loop!
            thingy.plus(last)
        } else {
            this.listify(start, this[last]!!, thingy.plus(last))
        }
    }

    private fun playCupGameWithCrabbo(initial: List<Int>, rounds: Int): Map<Int, Int> {
        val maxVal = initial.maxOrNull()!!

        // shitty linked list time! key = cup, value = ptr to next cup
        val cups = initial.zipWithNext { a, b ->
            a to b
        }.toMap(mutableMapOf())

        // close the loop
        cups[initial.last()] = initial.first()

        // start with first cup
        var current = initial.first()

        var a: Pair<Int, Int> = 1 to 1
        var b: Pair<Int, Int> = 2 to 2
        var c: Pair<Int, Int> = 3 to 3
        var finder = -1
        var destination: Int? = null
        (1..rounds).forEach {

            a = cups.extract(current)
            b = cups.extract(a.first)
            c = cups.extract(b.first)

            // instead of removing these 3, then searching the other cups
            // lets just look at the ones we pulled out instead
            finder = current - 1
            destination = null
            do {
                if (finder == a.first || finder == b.first || finder == c.first) {
                    finder--
                } else if (finder < 1) {
                    finder = maxVal
                } else {
                    destination = finder
                }
            } while (destination == null)

            // current cup points to what 3rd cup used to point to (fake remove)
            cups[current] = c.second
            // 3rd cup points to what destination used to point to
            cups[c.first] = cups[destination]!!
            // destination points to what current used to point to
            cups[destination!!] = a.first
            // advance to the next cup
            current = cups[current]!!
        }
        return cups
    }

    private fun parse(): MutableList<Int> =
        load().single().split("").filter { it.isNotEmpty() }.map { it.toInt() }.toMutableList()
}
