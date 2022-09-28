package org.elwaxoro.advent.y2021

import org.elwaxoro.advent.PuzzleDayTester

/**
 * Reactor Reboot
 */
class Dec22: PuzzleDayTester(22, 2021) {
    override fun puzzle1() = "skip"
    override fun puzzle2() = "skip"

// TODO lost the plot here, working solve turned into refactor that doesn't work
// Now it's 2022 so I guess lets call it a day :(
//    override fun puzzle1(): Any = parse().filter { it.isInit() }.fold(setOf<Coord3D>()) { acc, cube ->
//        if(cube.isOn) {
//            acc.plus(cube.enumerate())
//        } else {
//            acc.minus(cube.enumerate())
//        }
//    }.size
//
//    override fun puzzle2(): Any = parse().fold(listOf<Cube>()) { acc, cube ->
//        val newCubes = mutableListOf<Cube>()
//        acc.forEach {
//            if (it.intersects(cube)) {
//                val intersection = it.intersection(cube)
//                println("$it intersects $cube: $intersection")
//                newCubes.add(intersection)
//            }
//        }
//        if(cube.isOn) {
//            newCubes.add(cube)
//        }
//        acc.plus(newCubes)
//    }.sumOf { it.score() }
//
//    fun parse() = load(1).map { line ->
//        val bounds = line.replace("on ", "").replace("off ", "").split(",").let { bounds ->
//            val xs = bounds[0].replace("x=", "").split("..").map { it.toInt() }.sorted()
//            val ys = bounds[1].replace("y=", "").split("..").map { it.toInt() }.sorted()
//            val zs = bounds[2].replace("z=", "").split("..").map { it.toInt() }.sorted()
//            Bounds3D(Coord3D(xs[0], ys[0], zs[0]), Coord3D(xs[1], ys[1], zs[1]))
//        }
//        Cube(isOn = line.startsWith("on"), bounds)
//    }
//
//    data class Cube(val isOn: Boolean, val bounds: Bounds3D, val score: Int = 1) {
//        fun isInit(): Boolean = listOf(bounds.min.x, bounds.min.y, bounds.min.z, bounds.max.x, bounds.max.y, bounds.max.z).none { it < -50 || it > 50 }
//        fun enumerate(): List<Coord3D> = bounds.enumerateCube()
//        fun score(): Long = bounds.size() * score
//        fun intersects(that: Cube): Boolean = bounds.intersects(that.bounds)
//        fun intersection(that: Cube): Cube = Cube(false, bounds.intersection(that.bounds), -1)
//    }
}
