package advent.year2020

import advent.PuzzleDay

class Dec25: PuzzleDay(25, 2020) {

    override fun puzzle1(): Any {
        val keys = listOf(1965712L, 19072108L)
        val firstKeyLoop = hACKtHEpLANET(keys)
        val otherKey = keys.filterNot { it == firstKeyLoop.first }.single()
        return hACKtHEpLANET(subject = otherKey, stop = firstKeyLoop.second).first
    }

    private fun hACKtHEpLANET(keys: List<Long> = listOf(), subject: Long = 7L, stop: Int = Int.MAX_VALUE): Pair<Long, Int> =
        (1..stop).fold(1L) { acc, idx ->
            ((acc * subject) % 20201227).also {
                // phase 1: bail as soon as the accumulator matches any of the keys
                if (keys.contains(acc)) {
                    return acc to idx - 1
                }
            }
        // phase 2: return the final accumulator
        }.let { it to stop }
}
