package org.elwaxoro.advent.y2021

import org.elwaxoro.advent.PuzzleDayTester
import java.util.UUID
import kotlin.math.ceil

/**
 * Snailfish
 */
class Dec18 : PuzzleDayTester(18, 2021) {

    override fun puzzle1(): Any = load().map { it.parseSnode() }.reduce { acc, snode ->
        acc.add(snode).repeatingExploderSplitterReducer()
    }.repeatingExploderSplitterReducer().magnatude()

    override fun puzzle2(): Any = load().let { lines ->
        lines.maxOf { a ->
            lines.minus(a).maxOf { b ->
                // BRUTE FORCE BAYBEEE
                a.parseSnode().add(b.parseSnode()).repeatingExploderSplitterReducer().magnatude()
            }
        }
    }

    /**
     * Trust me, everything just works better if we drop the leading '[' and pretend like that
     */
    private fun String.parseSnode() = parser(drop(1))

    /**
     * Recursive parsing??? what has the world come to
     */
    private fun parser(string: String, stack: List<SNode> = listOf(SNode(parent = null))): SNode =
        if (string.startsWith("[")) { // open SNode!
            val snode = SNode(parent = stack.last())
            stack.last().insert(snode)
            parser(string.drop(1), stack.plus(snode))
        } else if (string.startsWith("]")) { // close SNode
            if (stack.size == 1) { // Base case, no more snoding
                stack.single()
            } else { // close snode, drop from stack
                parser(string.drop(1), stack.dropLast(1))
            }
        } else if (string[0].isDigit()) { // Literal SNode is literal
            val snode = SNode(parent = stack.last(), num = string[0].toString().toLong())
            stack.last().insert(snode)
            parser(string.drop(1), stack)
        } else if (string[0] == ',') { // No-op just drop this and move on with SNoding
            parser(string.drop(1), stack)
        } else {
            throw IllegalStateException("Unknown char in stream: $string")
        }

    private data class SNode(
        var parent: SNode?,
        var left: SNode? = null,
        var right: SNode? = null,
        var num: Long? = null,
        val id: UUID = UUID.randomUUID(),
    ) {
        fun isLiteral(): Boolean = num != null
        fun isPair(): Boolean = left != null && right != null && left!!.isLiteral() && right!!.isLiteral()
        fun seekRight(): SNode? = takeIf { isLiteral() } ?: right?.seekRight()
        fun seekLeft(): SNode? = takeIf { isLiteral() } ?: left?.seekLeft()
        fun depth(): Int = 0.takeUnless { parent != null } ?: (parent!!.depth() + 1)
        fun magnatude(): Long = num ?: (3 * left!!.magnatude() + 2 * right!!.magnatude())

        fun repeatingExploderSplitterReducer(): SNode = this.also {
            do {
                val reduced = explode() ?: split()
            } while (reduced != null)
        }

        fun explode(): SNode? = if (isPair() && depth() >= 4) {
            traverseLeft()?.let { it.num = it.num!! + left!!.num!! }
            traverseRight()?.let { it.num = it.num!! + right!!.num!! }
            left = null
            right = null
            num = 0
            this
        } else {
            left?.explode() ?: right?.explode()
        }

        fun split(): SNode? = if (isLiteral() && num!! >= 10) {
            val half = num!! / 2.0
            left = SNode(parent = this, num = half.toLong())
            right = SNode(parent = this, num = ceil(half).toLong())
            num = null
            this
        } else {
            left?.split() ?: right?.split()
        }

        /**
         * Combine this with that, both as child snodes under a new parent
         */
        fun add(that: SNode): SNode = SNode(parent = null, left = this, right = that).also { newParent ->
            this.parent = newParent
            that.parent = newParent
        }

        /**
         * Parse helper, fill left then right snodes
         */
        fun insert(snode: SNode) =
            if (left == null) {
                left = snode
            } else if (right == null) {
                right = snode
            } else {
                throw IllegalStateException("Tried to add $snode to $this but left and right are full already")
            }

        fun traverseLeft(): SNode? = parent?.let { parent ->
            if (this == parent.left) {
                parent.traverseLeft()
            } else {
                parent.left?.seekRight()
            }
        }

        fun traverseRight(): SNode? = parent?.let { parent ->
            if (this == parent.right) {
                parent.traverseRight()
            } else {
                parent.right?.seekLeft()
            }
        }

        override fun toString(): String = num?.toString() ?: "[$left,$right]"

        /**
         * I super don't want to talk about this and what a hack it is
         */
        override fun equals(other: Any?): Boolean =
            if (other is SNode) {
                id == other.id
            } else {
                super.equals(other)
            }

        override fun hashCode(): Int = id.hashCode()
    }
}
