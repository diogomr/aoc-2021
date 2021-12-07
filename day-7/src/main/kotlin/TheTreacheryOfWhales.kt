import kotlin.math.abs

fun main() {
    println("Part One Solution: ${partOne()}")
    println("Part Two Solution: ${partTwo()}")
}

private fun partOne(): Int {

    val positions = readInputLines()[0].split(",")
        .map { it.toInt() }

    var minFuel = Int.MAX_VALUE
    for (p in positions) {
        var fuelForPosition = 0
        for (m in positions) {
            fuelForPosition += abs(p - m)
        }
        if (fuelForPosition < minFuel) minFuel = fuelForPosition
    }
    return minFuel
}

private fun partTwo(): Int {

    val positions = readInputLines()[0].split(",")
        .map { it.toInt() }

    var minFuel = Int.MAX_VALUE
    for (p in positions) {
        var fuelForPosition = 0
        for (m in positions) {
            fuelForPosition += IntRange(0, abs(p - m)).sum()
        }
        if (fuelForPosition < minFuel) minFuel = fuelForPosition
    }
    return minFuel
}

private fun readInputLines(): List<String> {
    return {}::class.java.classLoader.getResource("input.txt")!!
        .readText()
        .split("\n")
        .filter { it.isNotBlank() }
}
