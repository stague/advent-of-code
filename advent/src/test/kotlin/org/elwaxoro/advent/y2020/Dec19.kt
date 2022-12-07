package org.elwaxoro.advent.y2020

import org.elwaxoro.advent.PuzzleDayTester

class Dec19 : PuzzleDayTester(19, 2020) {

    override fun part1(testFileSuffix: Int?): Any = parse().let { rulesToMessages ->
        // first arg is the rule map, second is the list of messages
        rulesToMessages.second.filter {
            val maxIdxMatches = rulesToMessages.first["0"]!!.find(it, 0, rulesToMessages.first)
            //println("Puzzle1: $it len: ${it.length} matches: $maxIdxMatches ${if (maxIdxMatches.contains(it.length)) "SUCCESS" else "failed"}")
            // if there's a max idx equal to the message length, we did it!
            maxIdxMatches.contains(it.length)
        }.size
    }

    override fun part2(testFileSuffix: Int?): Any = parse().let { rulesToMessages ->
        // first arg is the rule map, second is the list of messages
        val rules = rulesToMessages.first.toMutableMap()
        // add the 2 custom rules
        rules["8"] = Rule("8", listOf(listOf("42"), listOf("42", "8")))
        rules["11"] = Rule("11", listOf(listOf("42", "31"), listOf("42", "11", "31")))

        rulesToMessages.second.filter {
            val maxIdxMatches = rules["0"]!!.find(it, 0, rules)
            //println("Puzzle2: $it len: ${it.length} matches: $maxIdxMatches ${if (maxIdxMatches.contains(it.length)) "SUCCESS" else "failed"}")
            // if there's a max idx equal to the message length, we did it!
            maxIdxMatches.contains(it.length)
        }.size
    }

    private data class Rule(val name: String, var subRules: List<List<String>> = listOf(), var value: Char? = null) {
        /**
         * Recursive thingy!
         * Move thru the message, one char at a time and see how many possible matches there are (even partial)
         * Little nasty since the rule carries the rule map along with it into the recursive steps
         */
        fun find(message: String, messageIdx: Int, ruleMap: Map<String, Rule>): List<Int> =
            if (value == null) {
                // recurse case: check all of the subRule lists and generate a list of max matching index from that
                subRules.fold(listOf()) { subRuleIdxAcc, subRule ->
                    // note: this running orMessageIdxList accumulator is almost ALWAYS empty or size one
                    // adding empty list to empty list just results in an empty list, so that's nice
                    subRule.fold(listOf(messageIdx)) { messageIdxList, ruleName ->
                        // messageIdxList is the list of index that have perfectly matched so far
                        // recurse into each rule and try to get more matches
                        // flatten deals with multiple empty lists naturally
                        // ex: [[], [32], []] becomes [32]
                        messageIdxList.flatMap { testIdx ->
                            ruleMap[ruleName]!!.find(message, testIdx, ruleMap)
                        }
                    }.plus(subRuleIdxAcc)
                }
            } else if (messageIdx < message.length && message[messageIdx] == value) {
                // base case: success! increase the running index
                // note: offset can run off the end of the message (ask me how I know)
                listOf(messageIdx + 1) // total message match so far, increase the index by 1
            } else {
                // base case: failure
                listOf()
            }
    }

    // parse to pair: first is the map of rule name to rule, second is the list of messages
    private fun parse(): Pair<Map<String, Rule>, List<String>> = load(delimiter = "\n\n").let { initial ->
        val rules = initial[0].split("\n").map { ruleLine ->
            ruleLine.split(": ").let {
                it[0] to Rule(it[0]).also { rule ->
                    if (it[1].contains("\"")) {
                        rule.value = it[1].replace("\"", "").single()
                    } else {
                        rule.subRules = it[1].split(" | ").map { it.split(" ") }
                    }
                }
            }
        }.toMap()
        val messages = initial[1].split("\n")
        rules to messages
    }
}
