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

    val lines = readInputLines()
    val algorithm = lines[0]

    var map = readMap(lines)
    for (i in 1..2) {
        map = applyAlgorithm(map, algorithm, i)
    }

    map.forEach { println(it.joinToString("")) }

    return map.sumOf { line ->
        line.count { it == '#' }
    }
}

private fun partTwo(): Int {
    val lines = readInputLines()
    val algorithm = lines[0]

    var map = readMap(lines)
    for (i in 1..50) {
        map = applyAlgorithm(map, algorithm, i)
    }

    map.forEach { println(it.joinToString("")) }

    return map.sumOf { line ->
        line.count { it == '#' }
    }
}

/**
 * Since our algorithm's 0 index is #, outer layer will change between # and . on every iteration
 */
private fun applyAlgorithm(map: List<List<Char>>, algorithm: String, iteration: Int): List<List<Char>> {
    val char = if (algorithm[0] == '.') '.' else if (iteration % 2 == 0) '#' else '.'
    val mapCopy = copyWithOuterLayer(map, char)
    val result = getNewEmptyMap(mapCopy.size)
    val range = mapCopy.indices
    for (x in range) {
        for (y in range) {
            val decimal = getDecimal(mapCopy, x, y, char)
            result[x][y] = algorithm[decimal]
        }
    }
    return result
}

private fun getNewEmptyMap(size: Int, init: Char = '.'): List<MutableList<Char>> {
    return List(size) { MutableList(size) { init } }
}

private fun readMap(lines: List<String>): List<List<Char>> {
    val map = getNewEmptyMap(lines[1].length)
    lines.drop(1)
        .forEachIndexed { lineIdx, line ->
            line.forEachIndexed { charIdx, char ->
                if (char == '#') {
                    map[lineIdx][charIdx] = '#'
                }
            }
        }
    return map
}

fun copyWithOuterLayer(map: List<List<Char>>, init: Char): List<MutableList<Char>> {
    val empty = getNewEmptyMap(map.size + 2, init)
    for (x in map.indices) {
        for (y in map.indices) {
            empty[x + 1][y + 1] = map[x][y]
        }
    }
    return empty
}

fun getDecimal(map: List<List<Char>>, x: Int, y: Int, nonExistingChar: Char): Int {
    var binary = ""
    for (xv in -1..1) {
        for (yv in -1..1) {
            if (x + xv !in map.indices || y + yv !in map.indices) {
                binary += if (nonExistingChar == '.') 0 else 1
            } else {
                binary += if (map[x + xv][y + yv] == '.') 0 else 1
            }
        }
    }
    return binary.toInt(2)
}

private fun readInputLines(): List<String> {
    return {}::class.java.classLoader.getResource("input.txt")!!
        .readText()
        .split("\n")
        .filter { it.isNotBlank() }
}
