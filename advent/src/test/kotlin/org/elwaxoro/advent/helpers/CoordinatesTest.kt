package org.elwaxoro.advent.helpers

import org.elwaxoro.advent.*
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertTrue

/**
 * Not really tests, just a place to experiment
 */
class CoordinatesTest {

    @Test
    fun testEnumerateLine() {
        val a = Coord(0, 0)
        val b = Coord(5, 5)
        val enumerated = a.enumerateLine(b)
        println(enumerated)
        // swap the display char for in-order letters of the alphabet to demonstrate order of coordinates
        println(enumerated.mapIndexed { index, coord -> coord.copyD('A'+index) }.printify(invert = true))
        assertTrue { enumerated.size == 6 }
        assertContains(enumerated, a)
        assertContains(enumerated, b)
    }

    @Test
    fun testMultipleLines() {
        val a = Coord(0, 0)
        val b = Coord(10, 10)
        val c = Coord(10, 0)
        val d = Coord(0, 10) // force an intersection in the path at (5,5)
        val intersection = Coord(5, 5)
        val coords = listOf(a, b, c, d) // don't include the intersection in the list, just let it happen naturally
        println(coords)
        val enumerated = coords.enumerateLines()
        println(enumerated)
        // swap the display char for in-order letters of the alphabet to demonstrate order of coordinates
        println(enumerated.mapIndexed { index, coord -> coord.copyD('A'+index) }.printify(invert = true))
        assertContains(enumerated, a)
        assertContains(enumerated, b)
        assertContains(enumerated, c)
        assertTrue { enumerated.count { it == intersection } == 2 }

        // verify the intersections code
        val segA = listOf(a, b)
        val segB = listOf(c, d)
        val intersections = segA.intersections(segB)
        assertTrue { intersections.size == 1 }
        assertContains(intersections, intersection)


    }
}
