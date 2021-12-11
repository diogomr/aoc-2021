fun main() {
    println("Part One Solution: ${partOne()}")
    println("Part Two Solution: ${partTwo()}")
}

private fun partOne(): Int {

    val matrix = readInputLines()
        .map { line -> line.toCharArray().map { it.digitToInt() }.toIntArray() }
        .toTypedArray()

    var count = 0
    for (i in 1..100) {
        increaseAll(matrix, 1)
        count += flash(matrix)
    }

    return count
}

fun flash(matrix: Array<IntArray>, flashed: MutableList<Pair<Int, Int>> = mutableListOf()): Int {
    val original = flashed.size
    for (i in matrix.indices) {
        for (j in matrix[0].indices) {
            if (matrix[i][j] > 9) {
                matrix[i][j] = 0
                flashed.add(Pair(i, j))
                increaseAdjacent(matrix = matrix, i = i, j = j, flashed = flashed)
            }
        }
    }
    if (original == flashed.size) return original
    return flash(matrix = matrix, flashed = flashed)
}

fun increaseAdjacent(matrix: Array<IntArray>, i: Int, j: Int, flashed: MutableList<Pair<Int, Int>>) {
    increase(matrix, i = i - 1, j = j - 1, flashed)
    increase(matrix, i = i - 1, j = j, flashed)
    increase(matrix, i = i - 1, j = j + 1, flashed)
    increase(matrix, i = i, j = j - 1, flashed)
    increase(matrix, i = i, j = j + 1, flashed)
    increase(matrix, i = i + 1, j = j - 1, flashed)
    increase(matrix, i = i + 1, j = j, flashed)
    increase(matrix, i = i + 1, j = j + 1, flashed)
}

fun increase(matrix: Array<IntArray>, i: Int, j: Int, flashed: MutableList<Pair<Int, Int>>) {
    if (i in matrix.indices && j in matrix[0].indices && Pair(i, j) !in flashed) {
        matrix[i][j]++
    }
}

fun increaseAll(matrix: Array<IntArray>, by: Int) {
    for (i in matrix.indices) {
        for (j in matrix[0].indices) {
            matrix[i][j] += by
        }
    }
}

private fun partTwo(): Int {
    val matrix = readInputLines()
        .map { line -> line.toCharArray().map { it.digitToInt() }.toIntArray() }
        .toTypedArray()

    for (i in generateSequence(1) { it + 1 }) {
        increaseAll(matrix, 1)
        flash(matrix)
        if (isAllZeroes(matrix)) {
            return i
        }
    }
    return -1
}

private fun isAllZeroes(matrix: Array<IntArray>): Boolean {
    for (x in matrix.indices) {
        for (y in matrix[0].indices) {
            if (matrix[x][y] != 0) {
                return false
            }
        }
    }
    return true
}

private fun readInputLines(): List<String> {
    return {}::class.java.classLoader.getResource("input.txt")!!
        .readText()
        .split("\n")
        .filter { it.isNotBlank() }
}
