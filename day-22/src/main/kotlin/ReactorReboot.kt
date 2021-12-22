import kotlin.math.max
import kotlin.math.min
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

private fun partOne(): Int {

    val instructions = readInstructions()

    val on = mutableSetOf<Triple<Int, Int, Int>>()
    val validRange = -50..50
    for (i in instructions) {
        for (x in i.xRange) {
            if (x !in validRange) {
                continue
            }
            for (y in i.yRange) {
                if (y !in validRange) {
                    continue
                }
                for (z in i.zRange) {
                    if (z !in validRange) {
                        continue
                    }
                    val cuboid = Triple(x, y, z)
                    if (i.switch == "on") on.add(cuboid) else on.remove(cuboid)
                }
            }
        }
    }

    return on.size
}

private fun partTwo(): Long {

    val instructions = readInstructions()

    val on = mutableSetOf<Triple<IntRange, IntRange, IntRange>>()
    for (i in instructions) {
        if (i.switch == "on") {
            on.add(Triple(i.xRange, i.yRange, i.zRange))
        } else {
            handleOffWithOnCollision(on, i.toTriple())
        }
    }

    val wraps = findWraps(on)
    on.removeAll(wraps)

    correctOnCollisions(on)

    return on.sumOf {
        it.first.count().toLong() * it.second.count() * it.third.count()
    }
}

fun findWraps(on: MutableSet<Triple<IntRange, IntRange, IntRange>>): Set<Triple<IntRange, IntRange, IntRange>> {
    val wraps = mutableSetOf<Triple<IntRange, IntRange, IntRange>>()
    for (i in on) {
        for (j in on) {
            if (i == j) continue
            if (i.wraps(j)) {
                wraps.add(j)
            } else if (j.wraps(i)) {
                wraps.add(i)
            }
        }
    }
    return wraps
}

fun correctOnCollisions(on: MutableSet<Triple<IntRange, IntRange, IntRange>>) {
    val alreadyCorrected = mutableSetOf<Triple<IntRange, IntRange, IntRange>>()
    out@ while (true) {
        for (i in on.iterator()) {
            if (i in alreadyCorrected) {
                continue
            }
            for (j in on.iterator()) {
                if (i == j) continue
                if (i.collidesWith(j)) {
                    handleOnWithOnCollision(i, j, on)
                    continue@out
                }
            }
            alreadyCorrected.add(i)
        }
        break@out
    }
}

fun handleOnWithOnCollision(
    i: Triple<IntRange, IntRange, IntRange>,
    j: Triple<IntRange, IntRange, IntRange>,
    on: MutableSet<Triple<IntRange, IntRange, IntRange>>
) {
    if (i.wraps(j)) {
        on.remove(j)
        return
    }

    if (j.wraps(i)) {
        on.remove(i)
        return
    }

    on.remove(i)
    on.remove(j)

    mergeOnWithOn(i, j)?.let {
        on.add(it)
        return
    }

    val xRanges = getNonCollidingRanges(i.first, j.first)
    val yRanges = getNonCollidingRanges(i.second, j.second)
    val zRanges = getNonCollidingRanges(i.third, j.third)

    addRangeCombinations(
        on,
        xRanges.filter { it.inRange(i.first) && it.count() > 0 },
        yRanges.filter { it.inRange(i.second) && it.count() > 0 },
        zRanges.filter { it.inRange(i.third) && it.count() > 0 },
    )

    addRangeCombinations(
        on,
        xRanges.filter { it.inRange(j.first) && it.count() > 0 },
        yRanges.filter { it.inRange(j.second) && it.count() > 0 },
        zRanges.filter { it.inRange(j.third) && it.count() > 0 },
    )
}

private fun getNonCollidingRanges(i: IntRange, j: IntRange): Set<IntRange> {
    val common = i.intersect(j).toSortedSet()
    if (common.isEmpty()) return emptySet()
    val commonRange = common.first()..common.last()
    val minFirst = min(i.first, j.first)
    val maxLast = max(i.last, j.last)
    val firstRange = minFirst until common.first()
    val lastRange = common.last() + 1..maxLast
    return setOf(commonRange, firstRange, lastRange)
}

private fun mergeOnWithOn(
    i: Triple<IntRange, IntRange, IntRange>,
    j: Triple<IntRange, IntRange, IntRange>,
): Triple<IntRange, IntRange, IntRange>? {
    return if (i.first == j.first && i.second == j.second) {
        i.copy(third = min(i.third.first, j.third.first)..max(i.third.last, j.third.last))
    } else if (i.first == j.first && i.third == j.third) {
        i.copy(second = min(i.second.first, j.second.first)..max(i.second.last, j.second.last))
    } else if (i.second == j.second && i.third == j.third) {
        i.copy(first = min(i.first.first, j.first.first)..max(i.first.last, j.first.last))
    } else {
        null
    }
}

fun addRangeCombinations(
    on: MutableSet<Triple<IntRange, IntRange, IntRange>>,
    xRanges: List<IntRange>,
    yRanges: List<IntRange>,
    zRanges: List<IntRange>,
) {
    for (x in xRanges) {
        for (y in yRanges) {
            for (z in zRanges) {
                on.add(Triple(x, y, z))
            }
        }
    }
}

private fun handleOffWithOnCollision(
    on: MutableSet<Triple<IntRange, IntRange, IntRange>>,
    i: Triple<IntRange, IntRange, IntRange>,
) {
    val onCopy = on.toSet()
    for (triple in onCopy) {
        if (i.collidesWith(triple)) {
            on.remove(triple)

            if (i.wraps(triple)) {
                continue
            }

            val xRanges = getNonCollidingRanges(i.first, triple.first)
            val yRanges = getNonCollidingRanges(i.second, triple.second)
            val zRanges = getNonCollidingRanges(i.third, triple.third)

            addRangeCombinationsOff(
                on,
                xRanges.filter { it.count() > 0 && triple.first.wraps(it) },
                yRanges.filter { it.count() > 0 && triple.second.wraps(it) },
                zRanges.filter { it.count() > 0 && triple.third.wraps(it) },
                i,
            )
        }
    }
}

fun addRangeCombinationsOff(
    on: MutableSet<Triple<IntRange, IntRange, IntRange>>,
    xRanges: List<IntRange>,
    yRanges: List<IntRange>,
    zRanges: List<IntRange>,
    offTriple: Triple<IntRange, IntRange, IntRange>,
) {
    for (x in xRanges) {
        for (y in yRanges) {
            for (z in zRanges) {
                if (x.inRange(offTriple.first) && y.inRange(offTriple.second) && z.inRange(offTriple.third)) continue
                on.add(Triple(x, y, z))
            }
        }
    }
}

fun IntRange.inRange(other: IntRange) =
    this.first in other || this.last in other || other.first in this || other.last in this

fun IntRange.wraps(other: IntRange) = this.first <= other.first && this.last >= other.last

fun Triple<IntRange, IntRange, IntRange>.wraps(other: Triple<IntRange, IntRange, IntRange>): Boolean {
    return this.first.wraps(other.first) && this.second.wraps(other.second) && this.third.wraps(other.third)
}

fun Triple<IntRange, IntRange, IntRange>.collidesWith(other: Triple<IntRange, IntRange, IntRange>): Boolean {
    return this.first.inRange(other.first) && this.second.inRange(other.second) && this.third.inRange(other.third)
}

fun readInstructions(): List<Instruction> {
    val regex =
        Regex("([a-z]+)\\sx=(-?[0-9]+)\\.\\.(-?[0-9]+),y=(-?[0-9]+)\\.\\.(-?[0-9]+),z=(-?[0-9]+)\\.\\.(-?[0-9]+)")
    return readInputLines().map {
        val result = regex.matchEntire(it)!!
        val switch = result.groups[1]!!.value
        val xFrom = result.groups[2]!!.value.toInt()
        val xTo = result.groups[3]!!.value.toInt()
        val yFrom = result.groups[4]!!.value.toInt()
        val yTo = result.groups[5]!!.value.toInt()
        val zFrom = result.groups[6]!!.value.toInt()
        val zTo = result.groups[7]!!.value.toInt()
        Instruction(switch, xFrom..xTo, yFrom..yTo, zFrom..zTo)
    }
}

data class Instruction(
    val switch: String,
    val xRange: IntRange,
    val yRange: IntRange,
    val zRange: IntRange,
) {
    fun toTriple() = Triple(xRange, yRange, zRange)
}

private fun readInputLines(): List<String> {
    return {}::class.java.classLoader.getResource("input.txt")!!
        .readText()
        .split("\n")
        .filter { it.isNotBlank() }
}
