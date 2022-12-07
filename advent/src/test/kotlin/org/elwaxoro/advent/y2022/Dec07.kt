package org.elwaxoro.advent.y2022

import org.elwaxoro.advent.PuzzleDayTester
import java.util.UUID

class Dec07 : PuzzleDayTester(7, 2022) {
    override val testRuns = listOf(
            TestRun(1, 95437, 24933642),
            TestRun(null, 1306611, 13210366)
    )

    override fun part1(testFileSuffix: Int?): Any = loadDirectory(testFileSuffix).values.toList().let { dir ->
        dir.filter { it.type == FileType.DIRECTORY }.map { directoryOnly ->
            directoryOnly.name to
            directoryOnly.calculateSizeRecursive(dir)
        }.filter { it.second <= 100000 }.sumOf { it.second }
    }

    override fun part2(testFileSuffix: Int?): Any = loadDirectory(testFileSuffix).values.toList().let { dir ->
        dir.filter { it.type == FileType.DIRECTORY }.map { directoryOnly ->
            directoryOnly.name to
                    directoryOnly.calculateSizeRecursive(dir)
        }.sortedBy { it.second }.toMap().let { nameToSizeMap ->
            println(nameToSizeMap)
            val totalSize = nameToSizeMap["/"]!!
            println("totalSize   $totalSize")
            val unusedSpace = 70000000 - totalSize
            println("unusedSpace $unusedSpace")
            val neededSpace = 30000000 - unusedSpace
            //                10822529
            println("neededSpace $neededSpace")
            nameToSizeMap.filter { it.value >= neededSpace }.also { println(it) }.values.min()
        }
    }

    data class File(val id: UUID, val parentId: UUID?, val type: FileType, val size: Int?, val name: String) {
        fun calculateSizeRecursive(fileStructure: List<File>): Int = fileStructure.filter { it.parentId == this.id }.sumOf { file ->
            when(file.type){
                FileType.FILE -> file.size!!
                FileType.DIRECTORY -> file.calculateSizeRecursive(fileStructure)
            }
        }
    }

    enum class FileType{ DIRECTORY, FILE }

    private fun loadDirectory(testFileSuffix: Int?) = load(testFileSuffix).let { termLines ->
        var currentDirectory: UUID? = null
        val directory = mutableMapOf<UUID, File>()
        termLines.forEach { termLine ->
            termLine.split(" ").let { splitTermLine ->
                when (splitTermLine[0]) {
                    "$" -> when(splitTermLine[1]) {
                        "cd" -> when(splitTermLine[2]){
                            "/" -> File(id = UUID.randomUUID(), parentId = null, type = FileType.DIRECTORY, size = null, name = "/").also {
                                directory[it.id] = it
                                currentDirectory = it.id
                            }
                            ".." -> currentDirectory = directory[currentDirectory]!!.parentId!!
                            else -> currentDirectory =
                                    directory.values.single {
                                        it.parentId == currentDirectory && it.type == FileType.DIRECTORY &&  it.name == splitTermLine[2]
                                    }.id
                        }
                        "ls" -> "" // Do nothing
                        else -> throw Exception("Ermagerrd")
                    }
                    "dir" -> File(id = UUID.randomUUID(), parentId = currentDirectory, type = FileType.DIRECTORY, size = null, name = splitTermLine[1]).also {
                        directory[it.id] = it
                    }
                    else -> File(id = UUID.randomUUID(), parentId = currentDirectory, type = FileType.FILE, size = splitTermLine[0].toInt(), name = splitTermLine[1]).also {
                        directory[it.id] = it
                    }
                }
            }
        }
        directory
    }
}

