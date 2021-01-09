package advent.year2020

import advent.PuzzleDay

/**
 * Reverse polish notation, with a twist
 */
class Dec18: PuzzleDay(18, 2020) {

    override fun puzzle1(): Any = reverseElfNotationEvaluationMachination {
        when (it) {
            "+" -> 2
            "*" -> 2
            else -> 0
        }
    }

    override fun puzzle2(): Any = reverseElfNotationEvaluationMachination {
        when (it) {
            "+" -> 2
            "*" -> 1
            else -> 0
        }
    }

    /**
     * Convert Infix to Reverse Elf Notation and evaluate
     */
    private fun reverseElfNotationEvaluationMachination(priority: (function: String?) -> Int): Long =
        load().map { infix ->
            val operationStack = mutableListOf<String>()
            val outputList = mutableListOf<String>()

            infix.split("").filterNot { it.isBlank() }.forEach { char ->
                val longVal = char.toLongOrNull()
                when {
                    longVal != null -> outputList.add(char) // numbers go in the output list
                    char == "(" -> operationStack.add(0, char) // open parens go on the stack
                    char in listOf("+", "*") -> {
                        // eval any higher priority operations
                        while (operationStack.isNotEmpty() && priority(operationStack.firstOrNull()) >= priority(char)) {
                            outputList.poppaCap(operationStack.removeFirst())
                        }
                        // operation goes on the stack
                        operationStack.add(0, char)
                    }
                    char == ")" -> {
                        // eval operations until the first open paren is found
                        while (operationStack.isNotEmpty() && operationStack.first() != "(") {
                            outputList.poppaCap(operationStack.removeFirst())
                        }
                        // remove open paren
                        operationStack.removeFirst()
                    }
                }
            }
            // eval any remaining operations
            while (operationStack.isNotEmpty()) {
                outputList.poppaCap(operationStack.removeFirst())
            }
            outputList.single().toLong()
        }.sum()

    /**
     * Pop 2 off the output list, evaluate, put result back in
     */
    private fun MutableList<String>.poppaCap(operation: String) {
        val right = removeLast().toLong()
        val left = removeLast().toLong()
        add(
            when (operation) {
                "+" -> left + right
                "*" -> left * right
                else -> throw IllegalStateException("WTF IS THIS")
            }.toString()
        )
    }

    /**
     * Test converter: Infix to Reverse Elf Notation
     */
    private fun reverseElfParse(infix: String, priority: (function: String?) -> Int): List<String> {
        val stack = mutableListOf<String>()
        val output = mutableListOf<String>()

        infix.split("").forEach { char ->
            val longVal = char.toLongOrNull()
            when {
                longVal != null -> output.add(char)
                char in listOf("+", "*") -> {
                    while (stack.isNotEmpty() && priority(stack.firstOrNull()) >= priority(char)) {
                        output.add(stack.removeFirst())
                    }
                    stack.add(0, char)
                }
                char == "(" -> stack.add(0, char)
                char == ")" -> {
                    while (stack.isNotEmpty() && stack.first() != "(") {
                        output.add(stack.removeFirst())
                    }
                    stack.removeFirst()
                }
                // ignore the whitespace
            }
        }
        output.addAll(stack)
        println("Infix: $infix ReverseElfNotation: ${output.joinToString(" ")}")
        return output
    }

    /**
     * Test evaluator for Reverse Elf Notation
     */
    private fun reverseElfEval(rpl: List<String>): Long {
        val stack = mutableListOf<String>()
        rpl.forEach { char ->
            val longVal = char.toLongOrNull()
            if (longVal != null) {
                // numbers go onto the stack
                stack.add(0, char)
            } else if (char in listOf("+", "*")) {
                // pop off 2 from stack, run function, put result on stack
                val right = stack.removeFirst().toLong()
                val left = stack.removeFirst().toLong()
                stack.add(
                    0, when (char) {
                        "+" -> left + right
                        "*" -> left * right
                        else -> throw IllegalStateException("WTF IS A $char")
                    }.toString()
                )
            }
        }
        println("Eval result $stack")
        return stack.single().toLong()
    }
}
