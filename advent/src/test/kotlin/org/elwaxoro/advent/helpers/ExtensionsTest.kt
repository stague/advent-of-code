package org.elwaxoro.advent.helpers

import org.elwaxoro.advent.permutations
import kotlin.test.Test
import kotlin.test.assertEquals

class ExtensionsTest {

    @Test
    fun testPermutations() {
        val abc = listOf('A', 'B', 'C').permutations()
        println(abc)
        assertEquals(6, abc.size)
    }
}
