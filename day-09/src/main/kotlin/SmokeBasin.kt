fun main() {
    println("Part One Solution: ${partOne()}")
    println("Part Two Solution: ${partTwo()}")
}

private fun partOne(): Int {

    val heightMap = readHeightMap()

    val lowPoints = findLowPoints(heightMap)

    return lowPoints.sumOf { heightMap[it.first][it.second] + 1 }
}

private fun partTwo(): Long {
    val heightMap = readHeightMap()

    val lowPoints = findLowPoints(heightMap)

    var result = 1L

    lowPoints.map {
        findBasinSize(it, heightMap, 1, mutableListOf())
    }.sortedDescending()
        .take(3)
        .forEach {
            result *= it
        }
    return result
}

private fun readHeightMap(): List<MutableList<Int>> {
    val heightMap = mutableListOf<MutableList<Int>>()

    readInputLines()
        .forEach {
            val row = mutableListOf<Int>()
            it.split("")
                .filter { it.isNotBlank() }
                .map { it.toInt() }
                .map { row.add(it) }
            heightMap.add(row)
        }
    return heightMap
}

private fun findLowPoints(heightMap: List<MutableList<Int>>): List<Pair<Int, Int>> {
    val lowPoints = mutableListOf<Pair<Int, Int>>()
    for (row in heightMap.indices) {
        for (column in heightMap[0].indices) {
            var isLowPoint = true
            if (row - 1 in heightMap.indices) {
                if (heightMap[row - 1][column] <= heightMap[row][column]) isLowPoint = false
            }
            if (row + 1 in heightMap.indices) {
                if (heightMap[row + 1][column] <= heightMap[row][column]) isLowPoint = false
            }
            if (column - 1 in heightMap[0].indices) {
                if (heightMap[row][column - 1] <= heightMap[row][column]) isLowPoint = false
            }
            if (column + 1 in heightMap[0].indices) {
                if (heightMap[row][column + 1] <= heightMap[row][column]) isLowPoint = false
            }
            if (isLowPoint) {
                lowPoints.add(Pair(row, column))
            }
        }
    }
    return lowPoints
}

fun findBasinSize(
    point: Pair<Int, Int>,
    map: List<List<Int>>,
    initialSize: Int,
    visited: MutableList<Pair<Int, Int>>
): Int {
    visited.add(point)
    var count = initialSize
    val previousRow = Pair(point.first - 1, point.second)
    if (previousRow.exists(map) &&
        previousRow !in visited &&
        previousRow.isNotNine(map) &&
        previousRow.isHigher(point, map)
    ) {
        count += findBasinSize(previousRow, map, 1, visited)
    }
    val nextRow = Pair(point.first + 1, point.second)
    if (nextRow.exists(map) &&
        nextRow !in visited &&
        nextRow.isNotNine(map) &&
        nextRow.isHigher(point, map)
    ) {
        count += findBasinSize(nextRow, map, 1, visited)
    }
    val previousColumn = Pair(point.first, point.second - 1)
    if (previousColumn.exists(map) &&
        previousColumn !in visited &&
        previousColumn.isNotNine(map) &&
        previousColumn.isHigher(point, map)
    ) {
        count += findBasinSize(previousColumn, map, 1, visited)
    }
    val nextColumn = Pair(point.first, point.second + 1)
    if (nextColumn.exists(map) &&
        nextColumn !in visited &&
        nextColumn.isNotNine(map) &&
        nextColumn.isHigher(point, map)
    ) {
        count += findBasinSize(nextColumn, map, 1, visited)
    }
    return count
}

fun Pair<Int, Int>.isHigher(other: Pair<Int, Int>, map: List<List<Int>>) =
    map[this.first][this.second] > map[other.first][other.second]

fun Pair<Int, Int>.isNotNine(map: List<List<Int>>) = map[this.first][this.second] != 9

fun Pair<Int, Int>.exists(map: List<List<Int>>) = this.first in map.indices && this.second in map[0].indices

fun readInputLines(): List<String> {
    return {}::class.java.classLoader.getResource("input.txt")!!
        .readText()
        .split("\n")
        .filter { it.isNotBlank() }
}
