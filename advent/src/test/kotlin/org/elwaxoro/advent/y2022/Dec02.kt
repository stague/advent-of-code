package org.elwaxoro.advent.y2022

import org.elwaxoro.advent.PuzzleDayTester
import org.elwaxoro.advent.y2022.Dec02.RPS.Companion.shoot
import org.elwaxoro.advent.y2022.Dec02.RPS.Companion.whatShouldIPlayToGetResponse

class Dec02 : PuzzleDayTester(2, 2022) {
    override val testRuns = listOf(
            TestRun(1, 15, 12),
            TestRun(null, 8890, 10238)
    )

    override fun part1(testFileSuffix: Int?): Any = loadGuide(testFileSuffix).sumOf { it.score() }

    override fun part2(testFileSuffix: Int?): Any = loadGuide2(testFileSuffix).sumOf { (input, wlt) -> wlt.score + input.whatShouldIPlayToGetResponse(wlt).responseValue }

    private fun loadGuide(testFileSuffix: Int?): List<GuideRow> = load(testFileSuffix).map { row ->
        row.split(" ").let {
            GuideRow(
                    input = RPS.fetchByInput(it.first()),
                    response = RPS.fetchByResponse(it.last())
            )
        }
    }

    data class GuideRow(val input: RPS, val response: RPS) {
        fun score() = response.shoot(input) + response.responseValue
    }

    private fun loadGuide2(testFileSuffix: Int?): List<Pair<RPS, WinLoseTie>> = load(testFileSuffix).map { row ->
        row.split(" ").let {
            RPS.fetchByInput(it.first()) to WinLoseTie.fetchByKey(it.last())
        }
    }

    enum class RPS(val inputKey: String, val responseKey: String, val responseValue: Int) {
        ROCK("A", "X", 1),
        PAPER("B", "Y", 2),
        SCISSORS("C", "Z", 3);

        companion object {
            private val INPUT_MAP: Map<String, RPS> by lazy { values().associateBy { it.inputKey } }
            private val RESPONSE_MAP: Map<String, RPS> by lazy { values().associateBy { it.responseKey } }

            fun fetchByInput(input: String) = INPUT_MAP[input] ?: throw Exception("Cant find response [$input]")
            fun fetchByResponse(response: String) = RESPONSE_MAP[response]
                    ?: throw Exception("Cant find response [$response]")

            // LHS score
            fun RPS.shoot(response: RPS): Int = when {
                this.getWinningResponse() == response -> WinLoseTie.WIN.score
                this == response -> WinLoseTie.TIE.score
                else -> WinLoseTie.LOSE.score
            }

            fun RPS.getWinningResponse(): RPS = when (this) {
                ROCK -> SCISSORS
                PAPER -> ROCK
                SCISSORS -> PAPER
            }

            fun RPS.getTieResponse(): RPS = this

            fun RPS.getLosingResponse(): RPS = when (this) {
                ROCK -> PAPER
                PAPER -> SCISSORS
                SCISSORS -> ROCK
            }

            fun RPS.whatShouldIPlayToGetResponse(wlt: WinLoseTie) = when (wlt) {
                WinLoseTie.WIN -> this.getLosingResponse()
                WinLoseTie.LOSE -> this.getWinningResponse()
                WinLoseTie.TIE -> this.getTieResponse()
            }
        }
    }

    enum class WinLoseTie(val inputKey: String, val score: Int) {
        WIN("Z", 6),
        LOSE("X", 0),
        TIE("Y", 3);

        companion object {
            private val INPUT_MAP: Map<String, WinLoseTie> by lazy { WinLoseTie.values().associateBy { it.inputKey } }

            fun fetchByKey(key: String) = INPUT_MAP[key] ?: throw Exception("Cant find WLT [$key]")
        }

    }
}

