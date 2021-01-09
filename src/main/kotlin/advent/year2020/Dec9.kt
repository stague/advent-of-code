package advent.year2020

import advent.PuzzleDay

class Dec9 : PuzzleDay(9, 2020) {

    override fun puzzle1(): Long = parse().findInvalid()

    override fun puzzle2(): Long = parse().let { input ->
        input.findInvalid().let { target ->
            // cheating here, based on puzzle 1 output I knew there were zero numbers less than target after it in the input :badpokerface:
            (input.indexOf(target) downTo 1).mapNotNull {
                input.subList(0, it).findInvalidMinMax(target)
            }[0]
        }
    }

    private fun List<Long>.findInvalid(): Long = this[(25 until size).first { !subList(it - 25, it).containsSum(this[it]) }]
    private fun List<Long>.containsSum(target: Long): Boolean = any { target - it != it && contains(target - it) }

    // find the right sublist, then create it again cause I'm dumb
    private fun List<Long>.findInvalidMinMax(target: Long): Long? =
        (size downTo 1).firstOrNull { subList(it, size).sum() == target }
            ?.let { subList(it, size).sorted().let { it.first() + it.last() } }

    private fun parse(): List<Long> = load().map { it.toLong() }
}
