package org.elwaxoro.advent.y2021

import org.elwaxoro.advent.PuzzleDayTester
import kotlin.math.abs

/**
 * Beacon Scanner
 */
class Dec19 : PuzzleDayTester(19, 2021) {

    override fun puzzle1(): Any = parse().alignTheScanners().map { it.beacons }.flatten().toSet().size == 512

    override fun puzzle2(): Any = parse().alignTheScanners().map { it.translation }.let { translations ->
        translations.maxOf { a ->
            translations.minus(a).maxOf { b ->
                a.manhattan(b)
            }
        }
    } == 16802

    /**
     * Loop unaligned scanners until everyone is aligned
     * Modifies the scanners so the beacon positions make sense relative to the first scanner
     * Aligned scanners have the translation from 0,0,0 built in
     */
    private fun List<Scanner>.alignTheScanners(): Set<Scanner> = mutableSetOf(first()).also { aligned ->
        val unaligned = drop(1).toMutableList()
        while (unaligned.isNotEmpty()) {
            val iter = unaligned.iterator()
            while (iter.hasNext()) {
                val candidate = iter.next()
                aligned.firstNotNullOfOrNull { candidate.align(it) }?.let {
                    aligned.add(it)
                    iter.remove()
                }
            }
        }
    }

    private fun parse(): List<Scanner> = load(delimiter = "\n\n").mapIndexed { idx, chunk ->
        chunk.split("\n").filterNot { it.isBlank() || it.startsWith("--- scanner") }.map(Coord3D::parse).let {
            Scanner(idx, it)
        }
    }
}

data class Scanner(val name: Int, val beacons: List<Coord3D>, val translation: Coord3D = Coord3D()) {
    private val beaconDistanceMap: Map<Int, Pair<Coord3D, Coord3D>> = beacons.map { a ->
        beacons.minus(a).map { b ->
            a.manhattan(b) to Pair(a, b)
        }
    }.flatten().distinctBy { it.first }.toMap()

    fun align(anchor: Scanner): Scanner? =
        (beaconDistanceMap.keys intersect anchor.beaconDistanceMap.keys).let { potentialBeacons ->
            potentialBeacons.map { distance ->
                val (a, b) = beaconDistanceMap[distance]!!
                val (aa, ab) = anchor.beaconDistanceMap[distance]!!
                // Difference vector everything by everything, take the best matrix if 12 or more beacons line up
                Rotation4.allTheThings().mapNotNull { matrix ->
                    val ra = matrix.multiply(a)
                    val rb = matrix.multiply(b)

                    if (ra.subtract(aa) == rb.subtract(ab)) {
                        aa.subtract(ra).toMatrix().multiply(matrix)
                    } else if (rb.subtract(aa) == ra.subtract(ab)) {
                        ab.subtract(ra).toMatrix().multiply(matrix)
                    } else {
                        null
                    }
                }
            }.flatten().groupingBy { it }.eachCount().maxByOrNull { it.value }?.takeIf { it.value >= 12 }
                ?.let { (matrix, _) ->
                    Scanner(name, beacons.map { (matrix.multiply(it)) }, matrix.multiply(Coord3D()))
                }
        }
}

/**
 * The 'w' is for WTF
 * Based on javax.vecmath.Tuple4d
 */
data class Coord3D(val x: Int = 0, val y: Int = 0, val z: Int = 0, val w: Int = 1) {
    companion object {
        fun parse(string: String): Coord3D = string.split(",").let { (a, b, c) ->
            Coord3D(a.toInt(), b.toInt(), c.toInt())
        }
    }
    fun subtract(that: Coord3D): Coord3D = Coord3D(x - that.x, y - that.y, z - that.z)
    fun manhattan(that: Coord3D): Int = abs(x - that.x) + abs(y - that.y) + abs(z - that.z)
    fun toMatrix(): Matrix4 = Matrix4(
        1, 0, 0, x,
        0, 1, 0, y,
        0, 0, 1, z,
        0, 0, 0, w
    )
}

/**
 * javax.vecmath.Matrix4f / javax.media.j3d.Transform3D
 */
data class Matrix4(
    val m00: Int, val m10: Int, val m20: Int, val m30: Int,
    val m01: Int, val m11: Int, val m21: Int, val m31: Int,
    val m02: Int, val m12: Int, val m22: Int, val m32: Int,
    val m03: Int, val m13: Int, val m23: Int, val m33: Int,
) {
    fun multiply(m: Matrix4): Matrix4 = Matrix4(
        m00 * m.m00 + m10 * m.m01 + m20 * m.m02 + m30 * m.m03,
        m00 * m.m10 + m10 * m.m11 + m20 * m.m12 + m30 * m.m13,
        m00 * m.m20 + m10 * m.m21 + m20 * m.m22 + m30 * m.m23,
        m00 * m.m30 + m10 * m.m31 + m20 * m.m32 + m30 * m.m33,

        m01 * m.m00 + m11 * m.m01 + m21 * m.m02 + m31 * m.m03,
        m01 * m.m10 + m11 * m.m11 + m21 * m.m12 + m31 * m.m13,
        m01 * m.m20 + m11 * m.m21 + m21 * m.m22 + m31 * m.m23,
        m01 * m.m30 + m11 * m.m31 + m21 * m.m32 + m31 * m.m33,

        m02 * m.m00 + m12 * m.m01 + m22 * m.m02 + m32 * m.m03,
        m02 * m.m10 + m12 * m.m11 + m22 * m.m12 + m32 * m.m13,
        m02 * m.m20 + m12 * m.m21 + m22 * m.m22 + m32 * m.m23,
        m02 * m.m30 + m12 * m.m31 + m22 * m.m32 + m32 * m.m33,

        m03 * m.m00 + m13 * m.m01 + m23 * m.m02 + m33 * m.m03,
        m03 * m.m10 + m13 * m.m11 + m23 * m.m12 + m33 * m.m13,
        m03 * m.m20 + m13 * m.m21 + m23 * m.m22 + m33 * m.m23,
        m03 * m.m30 + m13 * m.m31 + m23 * m.m32 + m33 * m.m33,
    )

    fun multiply(c: Coord3D): Coord3D = Coord3D(
        m00 * c.x + m10 * c.y + m20 * c.z + m30 * 1,
        m01 * c.x + m11 * c.y + m21 * c.z + m31 * 1,
        m02 * c.x + m12 * c.y + m22 * c.z + m32 * 1,
        m03 * c.x + m13 * c.y + m23 * c.z + m33 * 1,
    )

    override fun toString(): String =
        """
            $m00,$m10,$m20,$m30
            $m01,$m11,$m21,$m31
            $m02,$m12,$m22,$m32
            $m03,$m13,$m23,$m33
        """.trimIndent()
}

enum class Rotation4(val matrix: Matrix4) {
    IDENTITY(Matrix4(
    1, 0, 0, 0,
    0, 1, 0, 0,
    0, 0, 1, 0,
    0, 0, 0, 1)),
    X90(Matrix4(
    1, 0, 0, 0,
    0, 0, -1, 0,
    0, 1, 0, 0,
    0, 0, 0, 1)),
    Y90(Matrix4(
    0, 0, 1, 0,
    0, 1, 0, 0,
    -1, 0, 0, 0,
    0, 0, 0, 1)),
    Z90(Matrix4(
    0, -1, 0, 0,
    1, 0, 0, 0,
    0, 0, 1, 0,
    0, 0, 0, 1));

    companion object {
        // This makes 64 rotations but only 24 of them are unique. That's called efficiency
        fun allTheThings(): Set<Matrix4> =
            (0..3).map { x ->
                (0..3).map { y ->
                    (0..3).map { z ->
                        List(x) { X90.matrix } + List(y) { Y90.matrix } + List(z) { Z90.matrix }
                    }
                }.flatten()
            }.flatten().filter { it.isNotEmpty() }.map {
                it.reduce { acc, rotation4 ->
                    acc.multiply(rotation4)
                }
            }.toSet()
    }
}
