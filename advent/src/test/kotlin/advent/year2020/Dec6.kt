package advent.year2020

import advent.PuzzleDay

class Dec6 : PuzzleDay(6, 2020) {

    override fun puzzle1() = boop().map { beep(it).toSet() }.sumBy { it.size }

    override fun puzzle2() = boop().map { group ->
        group.split("\n").size.let { groupSize ->
            beep(group).groupBy { it }.filter { it.value.size == groupSize }.size
        }
    }.sum()

    private fun beep(group: String): List<Char> = group.toCharArray().filterNot { it.isWhitespace() }
    private fun boop(): List<String> = load(delimiter = "\n\n")
}
