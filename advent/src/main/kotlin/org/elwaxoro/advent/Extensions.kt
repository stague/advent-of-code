package org.elwaxoro.advent

fun ByteArray.toHexString() = joinToString("") { (0xFF and it.toInt()).toString(16).padStart(2, '0') }

/**
 * Split a string into a list of integers
 * Ex: "01234" becomes [0, 1, 2, 3, 4]
 */
fun String.splitToInt(): List<Int> = map(Character::getNumericValue)

/**
 * Replace matching values
 */
fun List<Int>.replace(oldInt: Int, newInt: Int): List<Int> = map { it.takeUnless { it == oldInt } ?: newInt }

/**
 * Takes a list of 1s and 0s and turns it into a single integer
 */
fun List<Int>.toBinaryInt(): Int = joinToString("").toInt(2)

/**
 * 0s become 1s, 1s become 0s
 */
fun List<Int>.bitFlip(): List<Int> = map {
    when (it) {
        1 -> 0
        0 -> 1
        else -> throw IllegalStateException("can't bit flip $it")
    }
}
