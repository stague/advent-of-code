package advent.year2020

import advent.PuzzleDay

class Dec17: PuzzleDay(17, 2020) {

    private data class Coord(val w: Int, val z: Int, val y: Int, val x: Int)

    override fun puzzle1(): Any = doDumbShit((0..0))
    override fun puzzle2(): Any = doDumbShit((-1..1))

    private fun doDumbShit(wRange: IntRange): Int =
        (1..6).fold(parse()) { activeCells, iter ->
            // skip coords already tried (2x speedup)
            val alreadyTriedSet = mutableSetOf<Coord>()
            // only care about active cells for the initial set of coordinates
            activeCells.map { activeCellCoord ->
                // from each active coordinate, build a set of ALL its neighbors, these are the coords to test with the rule set
                buildNeighbors(true, activeCellCoord, wRange).filterNot { alreadyTriedSet.contains(it) }.mapNotNull { testCellCord ->
                    alreadyTriedSet.add(testCellCord)
                    val isActive = activeCells.contains(testCellCord)
                    // from each test cell cord, get ALL of its neighbors to use in the nearby active list
                    val activeNearbyCount = buildNeighbors(false, testCellCord, wRange).filter { activeCells.contains(it) }.size
                    if ((isActive && activeNearbyCount in listOf(2, 3)) || (!isActive && activeNearbyCount == 3)) {
                        testCellCord // activate or keep active
                    } else {
                        null
                    }
                }
            }.flatten().toSet().also { println("Cycle $iter had ${it.size} active cells") }
        }.size

    /**
     * go in all directions, make a list of all possible nearby coordinates
     * x,y,z are always checked at (-1..1) range
     * w is added for puzzle2
     */
    private fun buildNeighbors(includeSelf: Boolean, coord: Coord, wRange: IntRange = (0..0), range: IntRange = (-1..1)): Set<Coord> =
            wRange.map { wMod ->
                range.map { zMod ->
                    range.map { yMod ->
                        range.mapNotNull { xMod ->
                            if (!includeSelf && wMod == 0 && zMod == 0 && yMod == 0 && xMod == 0) {
                                null
                            } else {
                                Coord(coord.w + wMod, coord.z + zMod, coord.y + yMod, coord.x + xMod)
                            }
                        }
                    }.flatten()
                }.flatten()
            }.flatten().toSet()

    private fun parse(): Set<Coord> = load().mapIndexed { yIdx, row ->
        row.toCharArray().mapIndexed { xIdx, char ->
            if (char == '#') {
                Coord(0, 0, xIdx, yIdx)
            } else {
                null
            }
        }.filterNotNull()
    }.flatten().toSet()
}

//class Dec17FirstTry: PuzzleDay(17, 2020) {
//
//    override fun puzzle1(): Any = measureTimeMillis {
//        var cube = parse3D()
//
//        (1..6).forEach { iteration ->
//            if (cube.shouldExpand3D()) {
//                cube = cube.expand3D()
//            }
//            cube = cube.iterate3D()
//        }
//
//        cube.countActive3D()
//    }.also {
//        println("Puzzle 1 took ${it}ms")
//    }
//
//    fun List<List<List<Char>>>.countActive3D(): Int = sumBy { it.countActive2D() }
//    fun List<List<Char>>.countActive2D(): Int = sumBy { it.countActive1D() }
//    fun List<Char>.countActive1D(): Int = sumBy { if (it == '#') 1 else 0 }
//
//    fun List<List<List<Char>>>.iterate3D(): List<List<List<Char>>> = mapIndexed { z, layer ->
//        layer.mapIndexed { y, row ->
//            row.mapIndexed { x, cell ->
//                val activeNeighbors = (-1..1).map { zMod ->
//                    (-1..1).map { yMod ->
//                        (-1..1).mapNotNull { xMod ->
//                            if (zMod == 0 && yMod == 0 && xMod == 0) {
//                                null
//                            } else if (value3D(z + zMod, y + yMod, x + xMod) == '#') {
//                                '#'
//                            } else {
//                                null
//                            }
//                        }
//                    }.flatten()
//                }.flatten()
//
//                if (cell == '#' && activeNeighbors.size in listOf(2, 3)) {
//                    cell
//                } else if (cell == '.' && activeNeighbors.size == 3) {
//                    '#'
//                } else {
//                    '.'
//                }
//            }
//        }
//    }
//
//    fun List<List<List<Char>>>.value3D(z: Int, y: Int, x: Int): Char =
//        if (z < 0 || z >= size || y < 0 || y >= this[0].size || x < 0 || x >= this[0][0].size) {
//            '_'
//        } else {
//            this[z][y][x]
//        }
//
//    fun List<List<List<Char>>>.shouldExpand3D(): Boolean = any { layer ->
//        layer.filterIndexed { rowIdx, row ->
//            when {
//                row.first() == '#' -> true//.also { println("Row 1st char is set") }
//                row.last() == '#' -> true//.also { println("Row last char is set") }
//                rowIdx == 0 && row.contains('#') -> true//.also { println("First row is $rowIdx and has a #") }
//                rowIdx + 1 == row.size && row.contains('#') -> true//.also { println("Last row is $rowIdx and has a #") }
//                else -> false
//            }
//        }.isNotEmpty()
//    }
//
//    fun List<List<List<Char>>>.expand3D(): List<List<List<Char>>> {
//        val targetSize = first().first().size + 2
//        val padRow: List<Char> = (1..targetSize).map { '.' }
//        val padLayer: List<List<List<Char>>> = listOf((1..targetSize).map { padRow })
//
//        return padLayer.plus(map { innerLayer ->
//            listOf(padRow).plus(innerLayer.map { innerRow ->
//                listOf('.').plus(innerRow).plus('.')
//            }).plus(listOf(padRow))
//        }).plus(padLayer)
//    }
//
//    fun List<List<List<Char>>>.print3D() {
//        val mid = size / 2
//        forEachIndexed { idx, layer ->
//            println("z:${idx - mid}")
//            println(layer.joinToString("\n") {
//                it.joinToString("")
//            })
//        }
//    }
//
//    fun parse3D(): List<List<List<Char>>> = listOf(loadInputFile().map { xRow ->
//        xRow.toCharArray().toList()
//    })
//
//}
