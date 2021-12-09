fun main() {
    println("Part One Solution: ${partOne()}")
    println("Part Two Solution: ${partTwo()}")
}

private fun partOne(): Int {

    val map = readMap()
    return map.keys
        .filter { it.isLowestOfSurroundingPoints(map) }
        .sumOf { map[it]!! + 1 }
}

private fun partTwo(): Long {

    val map = readMap()
    return map.keys.asSequence()
        .filter { it.isLowestOfSurroundingPoints(map) }
        .map { computeBasinSize(it, map, mutableSetOf()) }
        .sortedDescending()
        .take(3)
        .reduce { acc, it ->
            acc * it
        }
}

fun computeBasinSize(point: Point, map: Map<Point, Int>, visited: MutableSet<Point>): Long {
    if (!visited.add(point)) return 0
    return 1 + point.findPointsInBasin(map).sumOf {
        computeBasinSize(it, map, visited)
    }
}

private fun readMap(): Map<Point, Int> {

    return readInputLines()
        .filter { it.isNotBlank() }
        .flatMapIndexed { column, line ->
            line.split("")
                .filter { it.isNotBlank() }
                .mapIndexed { row, value ->
                    Pair(Point(row, column), value.toInt())
                }
        }
        .toMap()
}

data class Point(
    val x: Int,
    val y: Int,
) {
    fun findPointsInBasin(map: Map<Point, Int>): Set<Point> {
        val pointValue = map[this]!!
        return getValidSurroundingPoints(map)
            .filter {
                map[it]!! > pointValue && map[it]!! != 9
            }
            .toSet()
    }

    fun isLowestOfSurroundingPoints(map: Map<Point, Int>): Boolean {
        val pointValue = map[this]!!
        return getValidSurroundingPoints(map).all { map[it]!! > pointValue }
    }

    private fun getValidSurroundingPoints(map: Map<Point, Int>) = getSurroundingPoints()
        .filter { it.existsInMap(map) }
        .toSet()

    private fun existsInMap(map: Map<Point, Int>) = map[this] != null
    private fun getSurroundingPoints() = setOf(
        this.copy(x = x - 1),
        this.copy(x = x + 1),
        this.copy(y = y - 1),
        this.copy(y = y + 1),
    )
}
