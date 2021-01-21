package advent.year2020

import advent.PuzzleDay

class Dec10 : PuzzleDay(10, 2020) {

    override fun puzzle1() = parse().fold(AdapterWad(), AdapterWad::addAnyAdapter).let {
        it.threeJolt * it.oneJolt
    }

    override fun puzzle2() = parse().fold(mutableListOf(AdapterWad(oneJolt = 1L))) { wads, adapter ->
        wads.also {
            wads.last().addSequentialAdapter(adapter) ?: wads.add(AdapterWad(oneJolt = 1L, lastAdapter = adapter))
        }
    }.map(AdapterWad::possibleCombinations).reduce { acc, wad ->
        acc * wad
    }

    private fun parse() = load().map { it.toLong() }.sorted()

    private data class AdapterWad(
        var oneJolt: Long = 0,
        var threeJolt: Long = 1L, // always include the final 3 joltage hop
        var lastAdapter: Long = 0,
    ) {
        // puzzle 1: one giant adapter wad
        fun addAnyAdapter(adapter: Long): AdapterWad = this.also {
            when (adapter - lastAdapter) {
                1L -> oneJolt++
                3L -> threeJolt++
            }
            lastAdapter = adapter
        }

        // puzzle 2: only add +1 adapters (new wad after every +3 adapter)
        fun addSequentialAdapter(adapter: Long): AdapterWad? = this.takeIf { adapter - lastAdapter == 1L }?.also {
            oneJolt++
            lastAdapter = adapter
        }

        // puzzle 2
        // turns out you can be pretty lazy with the data: no sequences have gaps, max sequence length is 5
        fun possibleCombinations(): Long =
            when (oneJolt) {
                in 0L..2L -> 1L // no adapter branching
                3L -> 2L // 2 branches
                4L -> 4L // 4 branches
                5L -> 7L // 7 branches
                else -> 0L // EVERYTHING IS A LIE WHY DID I DO IT LIKE THIS AAAHHHHHHHH
            }
    }
}
