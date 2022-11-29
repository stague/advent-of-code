package org.elwaxoro.advent.y2021

import org.elwaxoro.advent.PuzzleDayTester

/**
 * Packet Decoder
 */
class Dec16 : PuzzleDayTester(16, 2021) {

    override fun part1(): Any = parse().parsePacket().first!!.calcVersion()
    override fun part2(): Any = parse().parsePacket().first!!.calcValue()

    /**
     * Reads the first entire packet off a string
     * Returns the packet (or null if no packet was created) paired with the un-parsed remaining string
     */
    private fun String.parsePacket(): Pair<Packet?, String> =
        if (length < 11) {
            // picked an arbitrary size that can't possibly be a packet
            null to this
        } else {
            val packet = Packet(substring(0, 3).toInt(2), substring(3, 6).toInt(2))
            if (packet.type == 4) {
                // I literally can't even this packet literal
                var remainingString = substring(6)
                val chunks = mutableListOf<String>()
                while (remainingString.startsWith("1")) {
                    chunks.add(remainingString.take(5)) // only the finest, most efficient string manipulation here
                    remainingString = remainingString.drop(5)
                }
                chunks.add(remainingString.substring(0, 5)) // add in the straggler who broke the loop
                packet.value = chunks.joinToString("") { it.drop(1) }.toLong(2)
                packet.lengthBits += chunks.size * 5
            } else {
                // OPERATOR!
                // 911 what's your emergency?
                // My friend and I were walking through the woods when he just collapsed. I think he died!
                // Calm down. First make sure he's dead
                // *gunshot*
                // Ok, now what?
                if (this[6] == '0') {
                    // Sub-packets of a known length, but an unknown count
                    packet.lengthBits += 16 // +1 for the sub-packet type, +15 for the length
                    val length = substring(7, packet.lengthBits).toInt(2)
                    var subPacket = substring(packet.lengthBits, packet.lengthBits + length).parsePacket()
                    while (subPacket.first != null) {
                        packet.subpackets.add(subPacket.first!!)
                        subPacket = subPacket.second.parsePacket()
                    }
                } else {
                    // Sub-packets of an unknown length in bits, but a known count
                    packet.lengthBits += 12 // +1 for the sub-packet type, +11 for the count
                    val count = substring(7, packet.lengthBits).toInt(2)
                    var subPacket = substring(packet.lengthBits).parsePacket()
                    packet.subpackets.add(subPacket.first!!)
                    (1 until count).map {
                        subPacket = subPacket.second.parsePacket()
                        packet.subpackets.add(subPacket.first!!)
                    }
                }
            }
            packet to substring(packet.calcLength())
        }

    private data class Packet(val version: Int, val type: Int) {
        var value: Long = -1 // if packet is a literal, this is set
        var lengthBits: Int = 6 // how long is this thing anyway (not including sub-packets)
        var subpackets = mutableListOf<Packet>()

        fun calcLength(): Int = lengthBits + subpackets.sumOf { it.calcLength() }

        fun calcVersion(): Int = subpackets.fold(version) { acc, packet -> acc + packet.calcVersion() }

        fun calcValue(): Long = when (type) {
            4 -> value
            0 -> subpackets.sumOf { it.calcValue() }
            1 -> subpackets.fold(1) { acc, packet -> acc * packet.calcValue() }
            2 -> subpackets.minOf(Packet::calcValue)
            3 -> subpackets.maxOf(Packet::calcValue)
            5 -> (1L).takeIf { subpackets[0].calcValue() > subpackets[1].calcValue() } ?: 0L
            6 -> (1L).takeIf { subpackets[0].calcValue() < subpackets[1].calcValue() } ?: 0L
            7 -> (1L).takeIf { subpackets[0].calcValue() == subpackets[1].calcValue() } ?: 0L
            else -> throw IllegalStateException("WHAT THE HELL IS $this???")
        }
    }

    private fun parse(): String = load().single().toCharArray().joinToString("") {
        Integer.toBinaryString(Integer.parseInt(it.toString(), 16)).padStart(4, '0')
    }
}
