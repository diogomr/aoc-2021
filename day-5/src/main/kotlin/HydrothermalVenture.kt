fun main() {
    println("Part One Solution: ${partOne()}")
    println("Part Two Solution: ${partTwo()}")
}

private fun partOne(): Int {

    val field = Array(1000) {
        IntArray(1000)
    }

    readLines().forEach {
        val linePoints = it.getLinePoints(diagonals = false)
        linePoints.forEach { p ->
            field[p.x][p.y] = ++field[p.x][p.y]
        }
    }

    return countOverlappedPoints(field)
}

private fun partTwo(): Int {

    val field = Array(1000) {
        IntArray(1000)
    }

    readLines().forEach {
        val linePoints = it.getLinePoints(diagonals = true)
        linePoints.forEach { p ->
            field[p.x][p.y] = ++field[p.x][p.y]
        }
    }

    return countOverlappedPoints(field)
}

private fun countOverlappedPoints(field: Array<IntArray>) =
    field.iterator().asSequence().flatMap { it.asIterable() }.count { it > 1 }

private fun readLines(): List<Line> {

    return readInputLines()
        .map {
            val coordinates = it.split(" -> ")

            val from = coordinates[0].split(",")
            val to = coordinates[1].split(",")

            Line(
                from = Point(from[0].toInt(), from[1].toInt()),
                to = Point(to[0].toInt(), to[1].toInt())
            )
        }
}

data class Point(val x: Int, val y: Int)
data class Line(val from: Point, val to: Point) {
    fun getLinePoints(diagonals: Boolean = false): Collection<Point> {
        return if (to.x == from.x) {
            val start = if (to.y > from.y) from.y else to.y
            val end = if (to.y > from.y) to.y else from.y
            IntRange(start, end)
                .map { Point(from.x, it) }
        } else if (to.y == from.y) {
            val start = if (to.x > from.x) from.x else to.x
            val end = if (to.x > from.x) to.x else from.x
            IntRange(start, end)
                .map { Point(it, from.y) }
        } else {
            if (!diagonals) {
                return emptyList()
            }

            val xStep = if (to.x > from.x) 1 else -1
            val yStep = if (to.y > from.y) 1 else -1

            val diagonalPoints = mutableListOf<Point>()
            diagonalPoints.add(from)
            do {
                val last = diagonalPoints.last()
                val currentPoint = last.copy(x = last.x + xStep, y = last.y + yStep)
                diagonalPoints.add(currentPoint)
            } while (currentPoint != to)
            diagonalPoints
        }
    }
}

private fun readInputLines(): List<String> {
    return {}::class.java.classLoader.getResource("input.txt")
        .readText()
        .split("\n")
        .filter { it.isNotBlank() }
}
