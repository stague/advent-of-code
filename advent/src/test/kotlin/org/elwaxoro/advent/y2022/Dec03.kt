package org.elwaxoro.advent.y2022

import org.elwaxoro.advent.PuzzleDayTester

class Dec03 : PuzzleDayTester(3, 2022) {
    override val testRuns = listOf(
            TestRun(1, 157, 70),
            TestRun(null, 8240, 2587)
    )

    override fun part1(testFileSuffix: Int?): Any = load(testFileSuffix).map { row ->
        row.length.div(2).let { half ->
            row.substring(0 until half) to row.substring(half until row.length)
        }
    }.sumOf { (c1, c2) ->
        INPUT_MAP[c1.toSet().intersect(c2.toSet()).single()] ?: 0
    }

    override fun part2(testFileSuffix: Int?): Any = load(testFileSuffix).chunked(3).sumOf { chonk ->
        INPUT_MAP[chonk.fold(initial = chonk.first().toSet()) { acc, s ->
            acc.intersect(s.toSet())
        }.single()] ?: 0
    }

    companion object {
        private val INPUT_MAP: Map<Char, Int> by lazy {
            CharRange('a', 'z').union(CharRange('A', 'Z')).mapIndexed { idx, char ->
                char to idx + 1
            }.toMap()
        }
    }
}

