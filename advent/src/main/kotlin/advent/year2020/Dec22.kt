package advent.year2020

import advent.PuzzleDay

class Dec22 : PuzzleDay(22, 2020) {
    // TODO this one got away from me on the refactor after submitting a solution. just give up now and ignore this file
    override fun puzzle1(): Any = "disabled"
//    parse().let { startingDecks ->
//        println("Total Size: ${startingDecks.sumBy { it.size } }}")
//        println("P1: ${startingDecks.first()} P2: ${startingDecks.last()}")
//        combat(startingDecks, 1).let { final ->
//            final.filter { it.isNotEmpty() }.single().reversed().foldIndexed(0L) { index, acc, l -> acc + (l * (index+1))}
//        }
//    }
//
//    override fun puzzle2(): Any = parse().let { startingDecks ->
//        recursiveCombat(GameState(null, 1, 0, startingDecks.first(), null, startingDecks.last(), null, setOf()), listOf())
//    }
//
//    data class GameState(val gameWinner: Int?, val game: Long, val round: Long, val p1Deck: List<Long>, val p1Draw: Long?, val p2Deck: List<Long>, val p2Draw: Long?, val previousDecks: Set<List<List<Long>>>) {
//        override fun toString(): String = "State[winner = $gameWinner game = $game, round = $round, p1Draw = $p1Draw p2Draw = $p2Draw p1Deck = $p1Deck p2Deck = $p2Deck previous: ${previousDecks.size}]"
//        fun hasWinner(): Boolean = gameWinner != null
//        fun hasLoop(): Boolean = previousDecks.contains(previousDeck)
//        fun declareWinner(newWinner: Int) = GameState(newWinner, game, round, p1Deck, null, p2Deck, null, previousDecks)
//        fun drawCard(): GameState = GameState(gameWinner, game, round+1, p1Deck.drop(1), p1Deck.first(), p2Deck.drop(1), p2Deck.first(), previousDecks.plus(setOf(listOf(p1Deck, p2Deck))))
//        fun shouldRecurse(): Boolean = p1Deck.size >= p1Draw!! && p2Deck.size >= p2Draw!!
//        fun resolveRoundWinner(roundWinner: Int): GameState =
//            if(roundWinner == 1) {
//                println("Player 1 wins round $round")
//                val newP1Deck = p1Deck.plus(listOf(p1Draw!!, p2Draw!!))
//                GameState(gameWinner, game, round, newP1Deck, null, p2Deck, null, previousDecks)
//            } else {
//                println("Player 2 wins round $round")
//                val newP2Deck = p2Deck.plus(listOf(p2Draw!!, p1Draw!!))
//                GameState(gameWinner, game, round, p1Deck, null, newP2Deck, null, previousDecks)
//            }
//    }
//
//    val stack = mutableListOf<GameState>()
//    var masterCounter = 1L
//
//    tailrec fun recursiveCombat(state: GameState, previousHands: List<List<Long>>): GameState {
//        println("Init: $state")
//        return if (state.hasWinner()) {
//            if (stack.isEmpty()) {
//                println("Stack is empty! GameState winner is ${state.gameWinner}")
//                state
//            } else {
//                println("Recursive game winner is ${state.gameWinner} stack size is ${stack.size} moving up to previous game")
//                recursiveCombat(stack.removeAt(0).resolveRoundWinner(state.gameWinner!!))
//            }
//        } else if (state.hasLoop() || state.p2Deck.isEmpty()) {
//            println("Loop detected or p2 deck is empty! Time to declare P1 winner")
//            // P1 wins the game if there's a loop or if p2 is out of cards
//            recursiveCombat(state.declareWinner(1), previousHands)
//        } else if (state.p1Deck.isEmpty()) {
//            println("p1 deck is empty! Time to declare P2 winner")
//            recursiveCombat(state.declareWinner(2), previousHands)
//        } else {
//            // draw cards!
//            val draw = state.drawCard()
//            println("DRAW $draw")
//            if (draw.shouldRecurse()) {
//                // COMBAT RECURSIOOOOON
//                println("COMBAT RECURSSSSION stack size: ${stack.size}")
//                stack.add(0, draw)
//                masterCounter++
//                recursiveCombat(GameState(null, masterCounter, 1, draw.p1Deck.take(draw.p1Draw!!.toInt()), null, draw.p2Deck.take(draw.p2Draw!!.toInt()), null, null, setOf()))
//            } else {
//                recursiveCombat(if(draw.p1Draw!! > draw.p2Draw!!) {
//                    draw.resolveRoundWinner(1)
//                } else {
//                    draw.resolveRoundWinner(2)
//                })
//            }
//        }
//    }
//
//    tailrec fun combat(playerDecks: List<List<Long>>, round: Long): List<List<Long>> {
//
//        if(playerDecks.any { it.isEmpty() }) {
//            // base case: LAHOOOZAHERRRR
//            return playerDecks
//        } else {
//            var p1 = playerDecks.first()
//            var p2 = playerDecks.last()
//            val p1Draw = p1.first()
//            val p2Draw = p2.first()
//
//            println("-- Round $round --")
//            println("Player 1's deck: $p1")
//            println("Player 2's deck: $p2")
//            println("Player 1 plays: $p1Draw")
//            println("Player 2 plays: $p2Draw")
//
//            if(p1Draw > p2Draw) {
//                p1 = p1.drop(1).plus(listOf(p1Draw, p2Draw))
//                p2 = p2.drop(1)
//                println("Round $round P1 wins!")
//            } else if(p2Draw > p1Draw) {
//                p2 = p2.drop(1).plus(listOf(p2Draw, p1Draw))
//                p1 = p1.drop(1)
//                println("Round $round P2 wins!")
//            } else {
//                throw IllegalStateException("WTF IS GOING ON")
//            }
//            return combat(listOf(p1, p2), round+1)
//        }
//    }
//
//    fun parse() = load(delimiter = "\n\n").map{ playerLines ->
//        playerLines.split("\n").drop(1).map { it.toLong() }
//    }
}
