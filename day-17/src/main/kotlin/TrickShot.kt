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

typealias Point = Pair<Int, Int>
typealias Velocity = Pair<Int, Int>

private fun partOne(): Int {

    val (xRange, yRange) = readRanges()

    var highest = Point(Int.MIN_VALUE, Int.MIN_VALUE)
    val start = Point(0, 0)
    for (x in 0..xRange.last) {
        for (y in yRange.first..200) {
            val initial = Velocity(x, y)
            findHighestPointOfHittingTrajectory(start, initial, xRange, yRange, highest)?.let {
                if (it.second > highest.second) highest = it
            }
        }
    }
    return highest.second
}

private fun partTwo(): Int {

    val (xRange, yRange) = readRanges()

    var count = 0
    val start = Point(0, 0)
    for (x in 0..xRange.last) {
        for (y in yRange.first..200) {
            val initial = Velocity(x, y)
            if (isHittingTrajectory(start, initial, xRange, yRange)) {
                count++
            }
        }
    }
    return count
}

private tailrec fun findHighestPointOfHittingTrajectory(
    point: Point,
    velocity: Velocity,
    xRange: IntRange,
    yRange: IntRange,
    high: Point
): Point? {
    val highest = if (point.second > high.second) point else high
    if (point.first in xRange && point.second in yRange) {
        return highest
    }

    // Out of horizontal range: will never be able to get back in it since horizontal velocity is always >= 0
    if (point.first > xRange.last) {
        return null
    }

    // Stopped on horizontal axis and out of range
    if (velocity.first == 0 && point.first !in xRange) {
        return null
    }

    // Out of vertical range and with velocity in opposite direction of range
    if (point.second < yRange.first && velocity.second < 1) {
        return null
    }

    val nextPoint = Point(point.first + velocity.first, point.second + velocity.second)
    val nextVelocity = Velocity(if (velocity.first == 0) 0 else velocity.first - 1, velocity.second - 1)

    return findHighestPointOfHittingTrajectory(nextPoint, nextVelocity, xRange, yRange, highest)
}

private fun isHittingTrajectory(point: Point, velocity: Velocity, xRange: IntRange, yRange: IntRange): Boolean {
    return findHighestPointOfHittingTrajectory(point, velocity, xRange, yRange, point) != null
}

private fun readRanges(): Pair<IntRange, IntRange> {

    val regex = Regex(".*(x=[0-9/.]+).*(y=[0-9/.-]+)")

    val input = readInputLines()[0]

    val groups = regex.matchEntire(input)!!.groups
    val (x0, x1) = groups[1]!!.value.substring(2).split("..").map { it.toInt() }
    val (y0, y1) = groups[2]!!.value.substring(2).split("..").map { it.toInt() }

    val xRange = x0..x1
    val yRange = if (y0 > y1) y1..y0 else y0..y1

    return Pair(xRange, yRange)
}

private fun readInputLines(): List<String> {
    return {}::class.java.classLoader.getResource("input.txt")!!
        .readText()
        .split("\n")
        .filter { it.isNotBlank() }
}
