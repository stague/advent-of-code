package org.elwaxoro.advent

import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * Cardinal directions
 */
enum class Dir {
    N, S, E, W;

    companion object {
        /**
         * Up, down, left, right to N, S, E, W
         */
        fun fromUDLR(uplr: Char): Dir =
            when (uplr) {
                'U' -> N
                'D' -> S
                'L' -> W
                'R' -> E
                else -> throw UnsupportedOperationException("Can't turn $uplr from UDLR to NSWE!")
            }
    }

    /**
     * Turn left or right, get a new Dir
     */
    fun turn(t: Turn): Dir =
        when (this) {
            N -> when (t) {
                Turn.R -> E
                Turn.L -> W
            }
            S -> when (t) {
                Turn.R -> W
                Turn.L -> E
            }
            E -> when (t) {
                Turn.R -> S
                Turn.L -> N
            }
            W -> when (t) {
                Turn.R -> N
                Turn.L -> S
            }
        }
}

enum class Turn {
    R,
    L
}

/**
 * 2D coordinate
 */
data class Coord(val x: Int = 0, val y: Int = 0, val d: Char? = null) {
    companion object {
        /**
         * "x,y" string to coord
         */
        fun parse(str: String) = str.split(",").let {
            Coord(it[0].toInt(), it[1].toInt())
        }
    }

    override fun toString(): String = "($x,$y)"

    fun move(dir: Dir, distance: Int = 1): Coord =
        when (dir) {
            Dir.N -> Coord(x, y + distance, d)
            Dir.S -> Coord(x, y - distance, d)
            Dir.E -> Coord(x + distance, y, d)
            Dir.W -> Coord(x - distance, y, d)
        }

    fun add(dx: Int, dy: Int): Coord =
        Coord(x + dx, y + dy, d)

    fun add(dxy: Coord): Coord =
        Coord(x + dxy.x, y + dxy.y, d)

    fun subtract(dxy: Coord): Coord =
        Coord(x - dxy.x, y - dxy.y, d)

    fun neighbors(): List<Coord> =
        Dir.values().map { move(it) }

    /**
     * Returns a 3x3 grid with all neighbor coords, including this coord at the center
     */
    fun neighbors9(): List<List<Coord>> =
        listOf(
            listOf(Coord(x - 1, y - 1), Coord(x, y - 1), Coord(x + 1, y - 1)),
            listOf(Coord(x - 1, y), this, Coord(x + 1, y)),
            listOf(Coord(x - 1, y + 1), Coord(x, y + 1), Coord(x + 1, y + 1))
        )

    fun edge(that: Coord): Dir =
        if (x == that.x) {
            if (y == that.y - 1) {
                Dir.N
            } else {
                Dir.S
            }
        } else if (y == that.y) {
            if (x == that.x - 1) {
                Dir.E
            } else {
                Dir.W
            }
        } else {
            throw IllegalStateException("Coord $that is not adjacent to $this")
        }

    fun rotate(rotation: Int): Coord =
        when (rotation) {
            0 -> this
            90 -> Coord(y, x * -1, d)
            180 -> Coord(x * -1, y * -1, d)
            270 -> Coord(y * -1, x, d)
            else -> throw IllegalStateException("Coord $this does not support rotation $rotation")
        }

    /**
     * Return the list of all coords in a rectangle from this coord to the target coord, treating the two coords as corners
     * ex: (0,0).enumerate(2,2) returns
     * [Coord(x=0, y=0), Coord(x=0, y=1), Coord(x=0, y=2), Coord(x=1, y=0), Coord(x=1, y=1), Coord(x=1, y=2), Coord(x=2, y=0), Coord(x=2, y=1), Coord(x=2, y=2)]
     */
    fun enumerateRectangle(toCoord: Coord): List<Coord> {
        val xs = listOf(x, toCoord.x).sorted()
        val ys = listOf(y, toCoord.y).sorted()
        return (xs[0]..xs[1]).map { mx ->
            (ys[0]..ys[1]).map { my ->
                Coord(mx, my, d)
            }
        }.flatten()
    }

    /**
     * Return the list of all coords in a line from this coord to the target coord, includiing both start and end coords
     * Only produces straight lines for vertical, horizontal, and 45deg lines
     */
    fun enumerateLine(toCoord: Coord): List<Coord> =
        (abs(x - toCoord.x) + 1 to abs(y - toCoord.y) + 1).let { (dx, dy) ->
            x.toward(toCoord.x).padTo(dy).zip(y.toward(toCoord.y).padTo(dx)).map { (x, y) ->
                Coord(x, y, d)
            }
        }

    /**
     * Pythagoras figured this out so we don't have to
     */
    fun distance(to: Coord): Double = sqrt((to.x - x).toDouble().pow(2) + (to.y - y).toDouble().pow(2))

    /**
     * https://en.wikipedia.org/wiki/Taxicab_geometry
     */
    fun taxiDistance(to: Coord): Int = abs(to.x - x) + abs(to.y - y)
}

/**
 * Extension of getOrNull that returns the input row and col as a Coord, along with the item if found
 * IMPORTANT! if thinking of the list of lists as a coordinate grid, row = y and col = x
 */
fun <T> List<List<T>>.getCoordOrNull(row: Int, col: Int): Pair<Coord, T>? =
    getOrNull(row, col)?.let { Pair(Coord(col, row), it) }

/**
 * Extension of neighbors that returns the neighbors as a Coord along with the item if found
 * IMPORTANT! if thinking of the list of lists as a coordinate grid, row = y and col = x
 */
fun <T> List<List<T>>.neighborCoords(row: Int, col: Int, includeDiagonal: Boolean = false): List<Pair<Coord, T>> =
    listOfNotNull(
        getCoordOrNull(row - 1, col),
        getCoordOrNull(row + 1, col),
        getCoordOrNull(row, col - 1),
        getCoordOrNull(row, col + 1),
        getCoordOrNull(row - 1, col - 1).takeIf { includeDiagonal },
        getCoordOrNull(row - 1, col + 1).takeIf { includeDiagonal },
        getCoordOrNull(row + 1, col - 1).takeIf { includeDiagonal },
        getCoordOrNull(row + 1, col + 1).takeIf { includeDiagonal },
    )

/**
 * Returns true if given coord is contained by the rectangle formed by the pair of coordinates
 */
fun Pair<Coord, Coord>.contains(c: Coord): Boolean {
    val xs = listOf(first.x, second.x).sorted()
    val ys = listOf(first.y, second.y).sorted()
    return c.x >= xs.first() && c.x <= xs.last() && c.y >= ys.first() && c.y <= ys.last()
}

fun Collection<Coord>.bounds(): Pair<Coord, Coord> {
    val xs = map { it.x }.sorted()
    val ys = map { it.y }.sorted()
    return (Coord(xs.first(), ys.first()) to Coord(xs.last(), ys.last()))
}

fun Collection<Coord>.printify(full: Char = '#', empty: Char = '.', invert: Boolean = false): String {
    val xs = map { it.x }.sorted()
    val ys = map { it.y }.sorted()
    val xtranslate = 0 - xs.first()
    val ytranslate = 0 - ys.first()

    return "[${xs.first()},${ys.first()}] to [${xs.last()},${ys.last()}]\n" +
        (0..(ys.last() - ys.first())).map {
            MutableList(xs.last() - xs.first() + 1) { empty }
        }.also { screen ->
            forEach { coord ->
                screen[coord.y + ytranslate][coord.x + xtranslate] = coord.d ?: full
            }
        }.let {
            if (invert) {
                it.reversed()
            } else {
                it
            }
        }.joinToString("\n") { it.joinToString("") }
}

enum class HexDir { E, W, NE, NW, SE, SW }

/**
 * Hex coordinates based on https://www.redblobgames.com/grids/hexagons/
 * Uses cube coordinates
 */
data class Hex(val x: Int, val y: Int, val z: Int) {

    fun move(dir: HexDir): Hex =
        when (dir) {
            HexDir.E -> Hex(x + 1, y - 1, z)
            HexDir.W -> Hex(x - 1, y + 1, z)
            HexDir.NE -> Hex(x + 1, y, z - 1)
            HexDir.NW -> Hex(x, y + 1, z - 1)
            HexDir.SE -> Hex(x, y - 1, z + 1)
            HexDir.SW -> Hex(x - 1, y, z + 1)
        }

    fun neighbors(): List<Hex> = HexDir.values().map { move(it) }
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
    IDENTITY(
        Matrix4(
            1, 0, 0, 0,
            0, 1, 0, 0,
            0, 0, 1, 0,
            0, 0, 0, 1
        )
    ),
    X90(
        Matrix4(
            1, 0, 0, 0,
            0, 0, -1, 0,
            0, 1, 0, 0,
            0, 0, 0, 1
        )
    ),
    Y90(
        Matrix4(
            0, 0, 1, 0,
            0, 1, 0, 0,
            -1, 0, 0, 0,
            0, 0, 0, 1
        )
    ),
    Z90(
        Matrix4(
            0, -1, 0, 0,
            1, 0, 0, 0,
            0, 0, 1, 0,
            0, 0, 0, 1
        )
    );

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
