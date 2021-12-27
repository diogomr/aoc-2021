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

private val EAST = ">"
private val SOUTH = "v"
private val EMPTY = "."
private fun partOne(): Int {

    var lines = readInputLines()
        .map { line -> line.toCharArray().map { it.toString() } }

    var count = 1
    while (true) {
        var moved = false
        val toChange = lines.toMutableList().map { it.toMutableList() }
        for (x in lines.indices) {
            for (y in lines[0].indices) {
                if (lines[x][y] == EAST) {
                    if ((y + 1) in lines[0].indices) {
                        if (lines[x][y + 1] == EMPTY) {
                            moved = true
                            toChange[x][y + 1] = EAST
                            toChange[x][y] = EMPTY
                        }
                    } else if (lines[x][0] == EMPTY) {
                        moved = true
                        toChange[x][0] = EAST
                        toChange[x][y] = EMPTY
                    }
                }
            }
        }

        lines = toChange.toMutableList().map { it.toMutableList() }
        for (x in lines.indices) {
            for (y in lines[0].indices) {
                if (lines[x][y] == SOUTH) {
                    if ((x + 1) in lines.indices) {
                        if (lines[x + 1][y] == EMPTY) {
                            moved = true
                            toChange[x + 1][y] = SOUTH
                            toChange[x][y] = EMPTY
                        }
                    } else if (lines[0][y] == EMPTY) {
                        moved = true
                        toChange[0][y] = SOUTH
                        toChange[x][y] = EMPTY
                    }
                }
            }
        }
        lines = toChange.toMutableList().map { it.toMutableList() }

        if (moved) count++ else break
    }

    return count
}

private fun partTwo(): Long {

    return -1
}

private fun readInputLines(): List<String> {
    return {}::class.java.classLoader.getResource("input.txt")!!
        .readText()
        .split("\n")
        .filter { it.isNotBlank() }
}
