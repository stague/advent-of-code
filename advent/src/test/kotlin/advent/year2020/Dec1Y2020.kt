package advent.year2020

import advent.PuzzleDay

/**
 * no idea why Dec1 is freaking out intellij but naming the file something more specific is making it shut up so whatever this is just for fun anyway
 */
class Dec1 : PuzzleDay(1, 2020) {

    override fun puzzle1(): Any {
        val array = load().map { it.toInt() }.sorted()
        (1..2020).forEach { first ->
            if (array.contains(first)) {
                // yay found one that might work for this problem
                (first + 1..2020).forEach { second ->
                    if (array.contains(second) && first + second == 2020) {
                        // yay! we're done!
                        return "$first, $second, ${first * second}"
                    }
                }
            }
        }
        throw IllegalStateException("well that didn't work ya dummy")
    }

    override fun puzzle2(): Any {
        val list = load().map { it.toInt() }.sorted()
        list.forEachIndexed { idx1, first ->
            // if first + second + min already too big even for 2020, just filter
            val sublist = list.drop(idx1 + 1).filter { first + it + list[0] <= 2020 }
            sublist.forEachIndexed { idx2, second ->
                sublist.drop(idx2 + 1).firstOrNull { first + second + it == 2020 }?.let { third ->
                    return "$first, $second, $third, ${first * second * third}"
                }
            }
        }
        throw IllegalStateException("well that didn't work ya dummy")
    }
}
