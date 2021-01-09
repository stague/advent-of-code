package advent.year2020

import advent.PuzzleDay

class Dec8: PuzzleDay(8, 2020) {

    override fun puzzle1(): String {
        val ops = parse()
        val opStack = mutableListOf(ops[0])

        // keep going until the top of the stack has already been executed
        while (!opStack.last().isExecuted) {
            opStack.last().execute()?.let { nextIdx ->
                // don't add an op to the stack if its already been run (the infinite loop)
                ops[nextIdx].takeUnless { it.isExecuted }?.let { nextOp ->
                    opStack.add(nextOp)
                }
            }
        }

        // too lazy to manage a running total apparently
        return "${opStack.sumBy { it.accumulate() }}"
    }

    override fun puzzle2(): String {
        val ops = parse()
        var idx = 0
        val idxStack = mutableListOf<Int>()
        var modIdx = -1
        while (idx < ops.size) {
            val op = ops[idx]
            idxStack.add(idx)
            op.execute()?.let { nextIdx ->
                idx = nextIdx
            } ?: run {
                // INFINITE LOOP DETECTED AHHHHHHHHHHH

                // Uhhhh pop stack until we're back to the one that we modified?
                while (true) {
                    idx = idxStack.removeLast()
                    ops[idx].reset()
                    if (modIdx == idx || modIdx == -1) {
                        break
                    }
                }

                // pop MORE stack till we get to another alteration candidate we didn't try yet
                // yea lets just have more while loops jesus
                while (true) {
                    idx = idxStack.removeLast()
                    ops[idx].reset()
                    if (ops[idx].alter()) {
                        modIdx = idx
                        break
                    }
                }
            }
        }
        // wtf man why can't you be normal
        return "${idxStack.filter { ops[it].cmd == "acc" }.sumBy { ops[it].mod }}"
    }

    private fun parse(): List<Op> = load().mapIndexed { idx, line ->
        line.split(" ").let {
            Op(it[0], it[1].toInt(), idx)
        }
    }

    private data class Op(
        var cmd: String,
        val mod: Int,
        var idx: Int,
        var isExecuted: Boolean = false,
        var isAltered: Boolean = false,
        var wasAlreadyTried: Boolean = false
    ) {
        // adder upper
        fun accumulate(): Int =
            if (cmd == "acc" && isExecuted) {
                mod
            } else {
                0
            }

        // knows who is next, but only if it hasn't been executed before because reasons
        fun execute(): Int? =
            if (isExecuted) {
                null
            } else {
                isExecuted = true
                when (cmd) {
                    "jmp" -> idx + mod
                    else -> idx + 1
                }
            }

        // unset execution flag, undo alteration
        fun reset() {
            isExecuted = false
            if (isAltered) {
                when (cmd) {
                    "nop" -> cmd = "jmp"
                    "jmp" -> cmd = "nop"
                }
                isAltered = false
            }
        }

        // jack shit up if possible, return true if shit is jacked
        fun alter(): Boolean =
            if (wasAlreadyTried) {
                false
            } else {
                when (cmd) {
                    "acc" -> false
                    "nop" -> true.also {
                        cmd = "jmp"
                        wasAlreadyTried = true
                        isAltered = true
                    }
                    "jmp" -> true.also {
                        cmd = "nop"
                        wasAlreadyTried = true
                        isAltered = true
                    }
                    else -> throw IllegalStateException("wtf man")
                }
            }
    }
}
