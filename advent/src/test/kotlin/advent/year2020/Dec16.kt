package advent.year2020

import advent.PuzzleDay

class Dec16 : PuzzleDay(16, 2020) {
    override fun puzzle1(): Any = parse().let { shebang ->
        shebang.rules.map { it.range }.flatten().toSet().let { validNums ->
            shebang.otherTickets.map { it.numbers.filterNot { validNums.contains(it) } }.flatten().sum()
        }
    }

    private data class Shebang(val rules: List<Rule>, val yourTicket: Ticket, val otherTickets: List<Ticket>)

    private data class Rule(val name: String, val range: Set<Long>) {
        companion object {
            fun parseRule(input: String): Rule {
                val splitA = input.split(": ")
                val name = splitA[0]
                val ranges = splitA[1].split(" or ")
                val range1 = ranges[0].split("-").map { it.toLong() }.toRangeList()
                val range2 = ranges[1].split("-").map { it.toLong() }.toRangeList()
                return Rule(name, range1.plus(range2).toSet())
            }

            fun List<Long>.toRangeList() = (first()..last()).toList()
        }

        fun contains(input: Long): Boolean = range.contains(input)
    }

    private data class Ticket(val idx: Int, val numbers: List<Long>) {
        companion object {
            fun parseTicket(idx: Int, input: String): Ticket = Ticket(idx, input.split(",").map { it.toLong() })
        }
    }

    private fun parse() = load(delimiter = "\n\n").let { sections ->
        Shebang(
            rules = sections[0].split("\n").map { Rule.parseRule(it) },
            yourTicket = Ticket.parseTicket(-1, sections[1].split("\n")[1]),
            otherTickets = sections[2].split("\n").mapIndexedNotNull { idx, str ->
                if (idx == 0) {
                    null
                } else {
                    Ticket.parseTicket(idx, str)
                }
            }
        )
    }

    /**
     * STOP READING HERE NO SERIOUSLY JUST TURN BACK NOWWWWWW
     */

    override fun puzzle2(): Any {
        val shebang = parse()
        val masterValidSet: Set<Long> = shebang.rules.map { it.range }.flatten().toSet()
        // get rid of all the crap tickets from puzzle 1
        val validTickets: List<Ticket> =
            shebang.otherTickets.filter { it.numbers.all { masterValidSet.contains(it) } }.plus(shebang.yourTicket)

        // map ticket number index to a list of all valid choices for that index
        val sectionIdxToChoiceMap: Map<Int, List<String>> = validTickets.map { ticket ->
            ticket.numbers.mapIndexed { idx, num ->
                idx to shebang.rules.filter { rule ->
                    rule.contains(num)
                }.map { it.name }
            }
        }.flatten().groupBy({ it.first }, { it.second })
            .map { // map ticket number index to all lists of valid choices for that index
                // reduce the list of lists to a single list of common choices
                it.key to it.value.reduce { acc, list -> acc.intersect(list).toList() }
            }.toMap()

        // holy fuck how many different ways can I map this shit
        val sectionAssignments: MutableMap<String, Int> = shebang.rules.map { it.name to -1 }.toMap(mutableMapOf())
        // oh look, a while loop. super original.
        while (sectionAssignments.any { it.value < 0 }) {
            sectionIdxToChoiceMap.forEach { sectionChoices ->
                // just gonna bang thru these things over and over till everything just has one valid match. SUPER EFFICIENT
                sectionChoices.value.filter { sectionAssignments[it] == -1 }.takeIf { it.size == 1 }?.let {
                    sectionAssignments[it.single()] = sectionChoices.key
                }
            }
        }
        // OK NOW LOOK HERE YOU LITTLE SHIT YOU CANT JUST KEEP MAPPING SHIT TO OTHER RANDOM SHIT
        return sectionAssignments.map { assignment -> assignment.key to shebang.yourTicket.numbers[assignment.value] }
            .filter { it.first.contains("departure") }
            .fold(1L) { acc, op -> acc * op.second }
    }
}
