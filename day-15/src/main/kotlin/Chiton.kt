import java.util.LinkedList
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

private fun partOne(): Int {

    val map: Array<IntArray> = readInputLines()
        .map { it.toCharArray().map { c -> c.digitToInt() }.toIntArray() }
        .toTypedArray()

    val distanceMap = dijkstraShortestDistance(Point(0, 0), map)
    return distanceMap[map.size - 1][map.size - 1].first
}

fun dijkstraShortestDistance(start: Point, map: Array<IntArray>): Array<Array<Pair<Int, Boolean>>> {

    val distance = Array(map.size) { Array(map.size) { Pair(Int.MAX_VALUE, false) } }
    distance[start.first][start.second] = Pair(0, true)

    val unvisited = LinkedList<Point>()
    for (i in map.indices) {
        for (j in map.indices) {
            unvisited.add(Point(i, j))
        }
    }

    var cur = start
    while (unvisited.isNotEmpty()) {
        unvisited.remove(cur)
        cur.let { (x, y) ->
            distance[x][y] = distance[x][y].copy(second = true)
        }

        val adj = findAdjacent(cur, map)
        adj.forEach { (x, y) ->
            val newDistance = map[x][y] + distance[cur.first][cur.second].first
            if (distance[x][y].first > newDistance) distance[x][y] = distance[x][y].copy(first = newDistance)
        }

        var value = Int.MAX_VALUE
        for (i in distance.indices) {
            for (j in distance.indices) {
                if (!distance[i][j].second && distance[i][j].first < value) {
                    value = distance[i][j].first
                    cur = Point(i, j)
                }
            }
        }
    }

    return distance
}

fun findAdjacent(point: Point, map: Array<IntArray>): List<Point> {

    val range = map.indices
    val (x, y) = point

    return listOf(
        Point(x, y - 1),
        Point(x, y + 1),
        Point(x - 1, y),
        Point(x + 1, y),
    )
        .filter { (itx, ity) -> itx in range && ity in range }
}

private fun partTwo(): Int {

    val map: Array<IntArray> = readInputLines()
        .map { it.toCharArray().map { c -> c.digitToInt() }.toIntArray() }
        .toTypedArray()

    val mapExpanded = expandMap(map)

    val distanceMap = dijkstraShortestDistance(Point(0, 0), mapExpanded)
    return distanceMap[mapExpanded.size - 1][mapExpanded.size - 1].first
}

fun expandMap(map: Array<IntArray>): Array<IntArray> {
    val size = map.size
    val expandedMap = Array(size * 5) { IntArray(size * 5) }
    for (i in 0..4) {
        for (j in 0..4) {
            addAndCopy(i * size, j * size, i + j, map, expandedMap)
        }
    }
    return expandedMap
}

fun addAndCopy(x: Int, y: Int, add: Int, from: Array<IntArray>, to: Array<IntArray>) {
    for (i in from.indices) {
        for (j in from.indices) {
            val new = from[i][j] + add
            to[x + i][y + j] = if (new > 9) new - 9 else new
        }
    }
}

private fun readInputLines(): List<String> {
    return {}::class.java.classLoader.getResource("input.txt")!!
        .readText()
        .split("\n")
        .filter { it.isNotBlank() }
}
