import kotlin.system.measureTimeMillis

fun main() {
    val partOneMillis = measureTimeMillis {
        println("Part One Solution: ${partOne()}")
    }
    println("Part One Solved in: $partOneMillis ms")

    val partTwoMillis = measureTimeMillis {
        println("Part Two Solution: ${partTwo()}")
    }
    println("Part Two Solved in: $partTwoMillis ms")
}

val hexaToBinaryMapping = mapOf(
    '0' to "0000",
    '1' to "0001",
    '2' to "0010",
    '3' to "0011",
    '4' to "0100",
    '5' to "0101",
    '6' to "0110",
    '7' to "0111",
    '8' to "1000",
    '9' to "1001",
    'A' to "1010",
    'B' to "1011",
    'C' to "1100",
    'D' to "1101",
    'E' to "1110",
    'F' to "1111",
)

private fun partOne(): Int {

    val input = decode(readInputLines()[0])
    val (packet, _) = readPacket(input)!!
    return sumPacketVersion(packet)
}

fun sumPacketVersion(packet: Packet): Int {
    return packet.version + packet.subPackets.sumOf { sumPacketVersion(it) }
}

private fun partTwo(): Long {

    val input = decode(readInputLines()[0])
    val (packet, _) = readPacket(input)!!
    return computePacketValue(packet)
}

/**
 * ID 0 are sum packets - their value is the sum of the values of their sub-packets. If they only have a single sub-packet, their value is the value of the sub-packet.
 * ID 1 are product packets - their value is the result of multiplying together the values of their sub-packets. If they only have a single sub-packet, their value is the value of the sub-packet.
 * ID 2 are minimum packets - their value is the minimum of the values of their sub-packets.
 * ID 3 are maximum packets - their value is the maximum of the values of their sub-packets.
 * ID 5 are greater than packets - their value is 1 if the value of the first sub-packet is greater than the value of the second sub-packet; otherwise, their value is 0. These packets always have exactly two sub-packets.
 * ID 6 are less than packets - their value is 1 if the value of the first sub-packet is less than the value of the second sub-packet; otherwise, their value is 0. These packets always have exactly two sub-packets.
 * ID 7 are equal to packets - their value is 1 if the value of the first sub-packet is equal to the value of the second sub-packet; otherwise, their value is 0. These packets always have exactly two sub-packets.
 */
fun computePacketValue(packet: Packet): Long = when (packet.typeId) {
    0 -> packet.subPackets.sumOf { computePacketValue(it) }
    1 -> packet.subPackets.map { computePacketValue(it) }.reduce(Long::times)
    2 -> packet.subPackets.map { computePacketValue(it) }.minOf { it }
    3 -> packet.subPackets.map { computePacketValue(it) }.maxOf { it }
    4 -> packet.value!!
    5 -> {
        val (first, second) = packet.subPackets.map { computePacketValue(it) }
        if (first > second) 1 else 0
    }
    6 -> {
        val (first, second) = packet.subPackets.map { computePacketValue(it) }
        if (first < second) 1 else 0
    }
    7 -> {
        val (first, second) = packet.subPackets.map { computePacketValue(it) }
        if (first == second) 1 else 0
    }
    else -> throw RuntimeException("Impossible state")
}

val regex = Regex("([0-1]{3})([0-1]{3})([0-1]*)")
fun readPacket(input: String): Pair<Packet, String>? {
    if (!regex.matches(input)) {
        return null
    }

    val groups = regex.matchEntire(input)!!.groups

    val version = groups[1]!!.value.toInt(2)
    val typeId = groups[2]!!.value.toInt(2)
    val body = groups[3]!!.value

    return if (typeId == 4) {
        readLiteral(body, version, typeId)
    } else {
        readOperation(body, version, typeId)
    }
}

private fun readOperation(body: String, version: Int, typeId: Int): Pair<Packet, String> {
    var updatedBody = body

    val lengthTypeId = updatedBody.take(1)
    updatedBody = updatedBody.substring(1)

    return when (lengthTypeId) {
        "0" -> {
            readZeroLengthType(updatedBody, version, typeId)
        }
        "1" -> {
            readOneLengthType(updatedBody, version, typeId)
        }
        else -> {
            throw RuntimeException("Impossible State")
        }
    }
}

private fun readOneLengthType(body: String, version: Int, typeId: Int): Pair<Packet, String> {
    var updatedBody = body

    val numberOfSubPackets = updatedBody.take(11).toInt(2)
    updatedBody = updatedBody.substring(11)

    val subs = mutableListOf<Packet>()
    for (i in 1..numberOfSubPackets) {
        readPacket(updatedBody)?.let {
            subs.add(it.first)
            updatedBody = it.second
        }
    }
    val packet = Packet(version = version, typeId = typeId, subPackets = subs)
    return Pair(packet, updatedBody)
}

private fun readZeroLengthType(body: String, version: Int, typeId: Int): Pair<Packet, String> {
    var updatedBody = body
    val length = updatedBody.take(15).toInt(2)
    updatedBody = updatedBody.substring(15)

    var subpackets = updatedBody.take(length)
    updatedBody = updatedBody.substring(length)

    val packet = Packet(version = version, typeId = typeId)
    val subs = mutableListOf<Packet>()
    while (true) {
        val result = readPacket(subpackets)
        if (result == null) {
            break
        } else {
            subpackets = result.second
            subs.add(result.first)
        }
    }
    packet.subPackets.addAll(subs)
    return Pair(packet, updatedBody)
}

private fun readLiteral(body: String, version: Int, typeId: Int): Pair<Packet, String> {
    var updatedBody = body
    var literal = ""
    while (true) {
        val number = updatedBody.take(5)
        updatedBody = updatedBody.substring(5)
        if (number[0] == '1') {
            literal += number.substring(1)
        } else {
            literal += number.substring(1)
            break
        }
    }
    return Pair(Packet(version = version, typeId = typeId, value = literal.toLong(2)), updatedBody)
}

data class Packet(
    val version: Int,
    val typeId: Int,
    val value: Long? = null,
    val subPackets: MutableList<Packet> = mutableListOf(),
)

fun decode(s: String): String {

    return s.map {
        hexaToBinaryMapping[it]
    }.joinToString("")
}

private fun readInputLines(): List<String> {
    return {}::class.java.classLoader.getResource("input.txt")!!
        .readText()
        .split("\n")
        .filter { it.isNotBlank() }
}
