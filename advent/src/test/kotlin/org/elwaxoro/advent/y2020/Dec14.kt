@file:Suppress("EXPERIMENTAL_API_USAGE")

package org.elwaxoro.advent.y2020

import org.elwaxoro.advent.PuzzleDayTester

class Dec14 : PuzzleDayTester(14, 2020) {

    override fun part1(testFileSuffix: Int?): Any {
        val map: MutableMap<Long, Long> = mutableMapOf()
        var mask = ""
        parse().forEach { cmd ->
            if (cmd.first == "mask") {
                mask = cmd.second
            } else {
                // mem[4812] = 133322396 -> mem[4812] = 000000000111111100100101011010011100
                val valuePadded = cmd.second.toULong().toString(radix = 2).padStart(length = 36, padChar = '0')
                val maskedValue = mask.zip(valuePadded) { maskChar, valChar ->
                    when (maskChar) {
                        '1' -> maskChar // mask 1 wins
                        '0' -> maskChar // mask 0 wins
                        else -> valChar // everything else unchanged
                    }
                }.joinToString("")
                map[cmd.first.toLong()] = maskedValue.toLong(radix = 2)
            }
        }
        return map.values.sum()
    }

    override fun part2(testFileSuffix: Int?): Any {
        val map: MutableMap<Long, Long> = mutableMapOf()
        var mask = ""
        parse().forEach { cmd ->
            if (cmd.first == "mask") {
                mask = cmd.second
            } else {
                // mem[4812] = 133322396 -> 000000000000000000000001001011001100 = 133322396
                val addressPadded = cmd.first.toULong().toString(radix = 2).padStart(length = 36, padChar = '0')
                val maskedAddress = mask.zip(addressPadded) { maskChar, addrChar ->
                    when (maskChar) {
                        '1' -> maskChar // mask 1 beats addr
                        '0' -> addrChar // addr whatever wins over mask 0
                        else -> maskChar // Xs get enumerated
                    }
                }.joinToString("")
                exploreMask(maskedAddress).forEach {
                    map[it.toLong(radix = 2)] = cmd.second.toLong()
                }
            }
        }
        return map.values.sum()
    }

    private fun exploreMask(bits: String): List<String> =
        if (bits.contains('X')) {
            // recursion! set it to 1 for one side, 0 for the other, then combine the resulting lists
            exploreMask(bits.replaceFirst('X', '1')).plus(exploreMask(bits.replaceFirst('X', '0')))
        } else {
            listOf(bits)
        }

    private fun parse() = load().map {
        val split = it.split(" = ")
        split[0].replace("mem[", "").replace("]", "") to split[1]
    }
}
