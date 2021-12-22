package org.elwaxoro.advent.y2021

import org.elwaxoro.advent.PuzzleDayTester

/**
 * Dirac Dice
 */
class Dec21 : PuzzleDayTester(21, 2021) {

    override fun puzzle1(): Any = GameV1(listOf(Player(1, 6), Player(2, 8))).let { game ->
        while (!game.hasWinner()) {
            game.players.forEach { player ->
                if (!game.hasWinner()) {
                    player.position = (player.position + List(3) { game.roll() }.sum() - 1) % 10 + 1
                    player.score += player.position
                }
            }
        }
        game.players.minOf { it.score } * game.dice
    }

    override fun puzzle2(): Any = findAllTheGames(mapOf(GameV2(listOf(Player(1, 6), Player(2, 8))) to 1L))
            .entries.groupBy { it.key.getWinner() }
            .map { it.value.sumOf { it.value } }.maxOf { it }

    /**
     * Recursively find all games without a winner by rolling each of 1,2,3 for the current player and expanding the games
     */
    private fun findAllTheGames(games: Map<GameV2, Long>): Map<GameV2, Long> =
        if (games.all { it.key.hasWinner() }) {
            games
        } else {
            findAllTheGames(games.flatMap { (game, count) ->
                if (game.hasWinner()) {
                    listOf(game to count)
                } else {
                    game.roll().map { it to count }
                }
            }.groupBy { it.first }.map {
                it.key to it.value.sumOf { it.second }
            }.toMap())
        }

    private data class Player(val name: Int, var position: Long, var score: Long = 0) {
        fun roll(roll: Long): Player = Player(name, (position + roll - 1) % 10 + 1, score)
        fun score(): Player = Player(name, position, score + position)
        fun rollScore(roll: Long, score: Boolean): Player = roll(roll).takeUnless { score } ?: roll(roll).score()
    }

    private data class GameV1(val players: List<Player>) {
        var dice = 0L
        fun roll(): Long = (dice++ % 100L) + 1
        fun hasWinner(): Boolean = players.any { it.score >= 1000L }
    }

    private data class GameV2(var players: List<Player>) {
        private var rolls = 0L // each player gets 3 rolls before it's the other players turn

        fun hasWinner(): Boolean = getWinner() != null
        fun getWinner(): Int? = players.singleOrNull { it.score >= 21 }?.name
        fun swapPlayers() { players = players.reversed() }

        fun endTurn() {
            if (rolls >= 3) {
                rolls = 0
                swapPlayers()
            }
        }

        fun roll(): List<GameV2> = (1L..3L).map {
            GameV2(listOf(players.first().rollScore(it, rolls + 1 == 3L), players.last()))
        }.onEach {
            it.rolls = rolls + 1
            it.endTurn()
        }
    }
}
