package advent.year2020

import java.awt.Point
import advent.PuzzleDay

class Dec20: PuzzleDay(20, 2020) {

    override fun puzzle1(): Any = makeAPrettyPicture().let { pp ->
        // find the corners of the pp and mult them up
        pp[0][0].id * pp[0][pp.size - 1].id * pp[pp.size - 1][0].id * pp[pp.size - 1][pp.size - 1].id
    }

    override fun puzzle2(): Any {
        // start with the pp, but as a single trimmed Orientation
        val combinedPicture: Orientation = makeAPrettyPicture().trimAndBlobItUp()
        val seaMonster: List<Point> = parseMonster()
        // "roughness" = hashes in picture - (sea monster count * 15)
        return combinedPicture.count() - combinedPicture.allPossibleMutations().map { orientation ->
            ((0 until orientation.pixels.size - 1).map { y ->
                (0 until orientation.pixels.size - 19).mapNotNull { x ->
                    // SEA MONSTER MASK! translate the monster by the current point, then see if they're all hashes
                    if (seaMonster.map { Point(x, y).trns(it) }.all { orientation.pixels[it.y][it.x] == "#" }) {
                        15 // sea monsters are 15 pixels each
                    } else {
                        null
                    }
                }
            }.flatten().sum())
        }.maxOrNull()!!
    }

    private fun makeAPrettyPicture(): List<List<Orientation>> {
        val tiles = parse()
        val first = tiles.first()
        val picture = makeAPrettyPictureRecursiveShitshow(
            mapOf(Point(0, 0) to TilePlacement(first, Point(0, 0), first.orentations[0])),
            tiles.drop(1)
        )
        val points = picture.keys
        // uhhhh I don't wanna talk about this
        val maxY = points.map { it.y }.maxOrNull()!!
        val maxX = points.map { it.x }.maxOrNull()!!
        val minY = points.map { it.y }.minOrNull()!!
        val minX = points.map { it.x }.minOrNull()!!

        // reconfigure the map into a 2D array where things actually are
        return (maxY downTo minY).map { y ->
            (minX..maxX).map { x ->
                picture[Point(x, y)]!!.orientation.trim() // leaving this trim here cause it doesn't harm part 1 and I need part 2 to work so I can go drink
            }
        }
    }

    /**
     * recursive shitshow. try and find a valid picture by just trying all tiles with all orientations until something fall out?
     * just going to plop tile 0 down at position 0,0 because you gotta start somewhere
     * assumption: there's just one valid solution and that solution is a square idk wtf is even going on
     * assumption: first tile's orientation doesn't matter? some orientation of another tile will fit it :badpokerface:
     */
    private fun makeAPrettyPictureRecursiveShitshow(
        picture: Map<Point, TilePlacement>,
        remainingTiles: List<Tile>
    ): Map<Point, TilePlacement> =
        if (remainingTiles.isEmpty()) {
            // base case: no more tiles (yay?)
            picture
        } else {
            // figure out what all the open edges are from the picture
            picture.keys.flatMap { it.neighbors() }.filterNot { picture.containsKey(it) }.forEach { point ->
                // for each open edge, get all of the picture edges already touching it
                point.neighbors().mapNotNull { picture[it] }.let { neighbors ->
                    remainingTiles.forEach { tile ->
                        tile.orentations.forEach { orientation ->
                            // available tile for all possible orientations, see if there's one combination that matches all the neighbors
                            if (neighbors.all { orientation.edgeMatches(it.orientation, point.edge(it.point)) }) {
                                // this available tile with this orientation will fit here, probably? keep going
                                // clearly anyone who makes their recursion case THIS deep in for loops deserves whatever happens to them next
                                return makeAPrettyPictureRecursiveShitshow(
                                    picture = picture.plus(point to TilePlacement(tile, point, orientation)),
                                    remainingTiles = remainingTiles.minus(tile)
                                )
                            }
                        }
                    }
                }
            }

            // this never happens
            throw IllegalStateException("THIS NEVER HAPPENS")
        }

    // for part 2, since Orientation already has all the rotate/translate code, just reuse it as an extra big tile orientation
    private fun List<List<Orientation>>.trimAndBlobItUp(): Orientation =
        Orientation(
            -1L,
            map { row ->
                row.first().pixels.indices.map { innerRow ->
                    row.map { orientation ->
                        orientation.pixels[innerRow]
                    }.flatten()
                }
            }.flatten()
        ).rotate() // why a rotate at the end? well this makes my printout look like the example

    // because Point.translate is mutating and I wanted copies
    private fun Point.trns(dxy: Point): Point = Point(x + dxy.x, y + dxy.y)

    private fun Point.neighbors(): List<Point> = listOf(
        Point(x, y + 1),// up
        Point(x, y - 1),// down
        Point(x - 1, y),// left?
        Point(x + 1, y),// right? idk I swapped these to get it working and forgot my own brain at this point
    )

    private fun Point.edge(that: Point): Edge =
        if (x == that.x) {
            if (y == that.y - 1) {
                Edge.TOP
            } else {
                Edge.BOTTOM
            }
        } else if (y == that.y) {
            if (x == that.x - 1) {
                Edge.RIGHT
            } else {
                Edge.LEFT
            }
        } else {
            throw IllegalStateException("Point $that is not adjacent to $this")
        }

    private enum class Edge {
        TOP,
        BOTTOM,
        LEFT,
        RIGHT
    }

    // TilePlacement is a tile + coordinate + orientation
    private data class TilePlacement(val tile: Tile, val point: Point, val orientation: Orientation) {
        override fun toString(): String = "Tile $tile at $point orientation\n$orientation\n"
    }

    // Tile is id + all possible orientations
    private data class Tile(val id: Long, val orentations: List<Orientation>)

    // Orientation is a specific rotation / transformation of a tile
    private data class Orientation(val id: Long, val pixels: List<List<String>>) {
        // edges of this square of pixels
        val top = pixels.first()
        val left = pixels.map { it.first() }
        val right = pixels.map { it.last() }
        val bottom = pixels.last()

        override fun toString(): String = pixels.joinToString("\n") { it.joinToString("") }
        fun flipVert(): Orientation = Orientation(id, pixels.map { it.reversed() })
        fun flipHoriz(): Orientation = Orientation(id, pixels.reversed())

        /**
         * OMG I CANT BELIVE THIS WORKED FIRST TRY WTFFF
         * Rotates right 90deg because that's how it turned out
         */
        fun rotate(): Orientation = Orientation(id, pixels.mapIndexed { yIdx, row ->
            row.mapIndexed { xIdx, char ->
                pixels[pixels.size - xIdx - 1][yIdx]
            }
        })

        /**
         * trial and error: there's just 8 ways to muss the pixels!
         */
        fun allPossibleMutations(): List<Orientation> = listOf(
            this,
            flipHoriz(),
            flipVert(),
            flipVert().rotate(),
            flipHoriz().rotate(),
            rotate(),
            rotate().rotate(),
            rotate().rotate().rotate(),
        )

        /**
         * Is "that" on top of "this"?
         * Is "that" on the bottom of "this"?
         * Is "that" to the left of "this"?
         * Is "that" to the right of "this"?
         */
        fun edgeMatches(that: Orientation, edge: Edge): Boolean =
            when (edge) {
                Edge.TOP -> top == that.bottom
                Edge.BOTTOM -> bottom == that.top
                Edge.LEFT -> left == that.right
                Edge.RIGHT -> right == that.left
            }

        fun trim(): Orientation = Orientation(id, pixels.drop(1).dropLast(1).map {
            it.subList(1, it.size - 1)
        })

        fun count(): Int = pixels.map { it.filter { it == "#" } }.flatten().count()
    }

    private fun parse() = load(delimiter = "\n\n").map { rawTile ->
        val tileLines = rawTile.split("\n")
        val name = tileLines[0].substring(5, 9).toLong()
        val rows = tileLines.drop(1).map { it.toCharArray().map { "$it" }.toList() }
        Tile(name, Orientation(name, rows).allPossibleMutations())
    }

    private fun parseMonster(): List<Point> = monster.split("\n").mapIndexed { y, row ->
        row.split("").mapIndexedNotNull { x, char ->
            if (char == "#") {
                Point(x, y)
            } else {
                null
            }
        }
    }.flatten()

    // no indentation because nothing was working today
    val monster = """
                  #
#    ##    ##    ###
 #  #  #  #  #  #
    """.trimIndent()
}
