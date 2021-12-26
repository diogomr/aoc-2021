import kotlin.math.abs
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
        .mapIndexed { lineIdx, line ->
            line.toCharArray()
                .mapIndexed { charIdx, char ->
                    Cell(lineIdx, charIdx, char.toString())
                }
        }

    var arrangements = setOf(Arrangement(lines, 2..3))
    while (true) {
        arrangements = arrangements.flatMap {
            it.getNextPossible()
        }.toSet()

        if (arrangements.all { it.isFinished() }) {
            break
        }
    }

    val result = arrangements.minByOrNull { it.totalEnergy }
    println(result)
    return result!!.totalEnergy
}

private fun partTwo(): Int {

    val lines = readInputLines2()
        .mapIndexed { lineIdx, line ->
            line.toCharArray()
                .mapIndexed { charIdx, char ->
                    Cell(lineIdx, charIdx, char.toString())
                }
        }

    var arrangements = setOf(Arrangement(lines, 2..5))
    while (true) {
        arrangements = arrangements.flatMap {
            it.getNextPossible()
        }.toSet()

        if (arrangements.all { it.isFinished() }) {
            break
        }
    }

    val result = arrangements.minByOrNull { it.totalEnergy }
    println(result)
    return result!!.totalEnergy
}

data class Cell(
    val x: Int,
    val y: Int,
    var value: String,
)

data class Arrangement(
    val lines: List<List<Cell>>,
    val xRange: IntRange,
    val totalEnergy: Int = 0,
) {
    companion object {
        private val ENERGY_MAP = mapOf(
            "A" to 1,
            "B" to 10,
            "C" to 100,
            "D" to 1000,
        )

        private val ROOMS = mapOf(
            "A" to 3,
            "B" to 5,
            "C" to 7,
            "D" to 9,
        )
    }

    fun isFinished() = ROOMS.keys.all { isRoomDone(it) }

    fun getNextPossible(): Set<Arrangement> {
        if (isFinished()) {
            return setOf(this)
        }

        val possibilities = mutableSetOf<Arrangement>()
        getMovableAmphipods().forEach { c ->
            findPossiblePositions(c).forEach { (x, y) ->
                val copy = lines.map {
                    it.map { c ->
                        c.copy()
                    }.toMutableList()
                }
                copy[x][y].value = c.value
                copy[c.x][c.y].value = "."

                val energy = (abs(x - c.x) + abs(y - c.y)) * ENERGY_MAP[c.value]!!
                possibilities.add(Arrangement(copy, xRange, totalEnergy + energy))
            }
        }

        return possibilities
    }

    private fun getMovableAmphipods(): Set<Cell> {
        val hallwayAmphipods = getHallway().filter { it.value != "." }
        val roomAmphipods = mutableSetOf<Cell>()
        ROOMS.keys.forEach {
            if (!isRoomDone(it) && !isRoomReady(it)) {
                getFirstAmphipod(it)?.let { (x, y) ->
                    roomAmphipods.add(lines[x][y])
                }
            }
        }
        return roomAmphipods.apply { addAll(hallwayAmphipods) }
    }

    private fun findPossiblePositions(cell: Cell): Set<Pair<Int, Int>> {
        return if (cell.x in xRange) {
            getHallwayPositions(cell)
        } else {
            getRoomPositions(cell)
        }
    }

    private fun getRoomPositions(cell: Cell): Set<Pair<Int, Int>> {
        if (isRoomDone(cell.value)) return emptySet()
        if (!isRoomReady(cell.value)) return emptySet()
        val destination = getDeepestAvailablePosition(cell.value)
        val yRange =
            if (cell.y > destination.second) destination.second until cell.y else cell.y + 1..destination.second
        return if (isHallwayEmpty(yRange)) setOf(destination) else emptySet()
    }

    private fun getHallwayPositions(cell: Cell): Set<Pair<Int, Int>> = setOf(
        Pair(1, 1),
        Pair(1, 2),
        Pair(1, 4),
        Pair(1, 6),
        Pair(1, 8),
        Pair(1, 10),
        Pair(1, 11),
    ).filter {
        val (x, y) = it
        if (lines[x][y].value != ".") {
            false
        } else if (isHallwayEmpty()) {
            true
        } else {
            if (y < cell.y) {
                getHallway()
                    .filter { pos -> pos.value != "." }
                    .none { c -> c.y < cell.y && c.y > y }
            } else {
                getHallway()
                    .filter { pos -> pos.value != "." }
                    .none { c -> c.y > cell.y && c.y < y }
            }
        }
    }.toSet()

    private fun isRoomDone(room: String): Boolean {
        val roomNumber = ROOMS[room]!!
        return xRange.all { lines[it][roomNumber].value == room }
    }

    private fun isRoomReady(room: String): Boolean {
        val roomNumber = ROOMS[room]!!
        return xRange.all { lines[it][roomNumber].value == room || lines[it][roomNumber].value == "." }
    }

    private fun getDeepestAvailablePosition(room: String): Pair<Int, Int> {
        val roomNumber = ROOMS[room]!!
        val result = xRange.last { lines[it][roomNumber].value == "." }
        return Pair(result, roomNumber)
    }

    private fun getFirstAmphipod(room: String): Pair<Int, Int>? {
        val roomNumber = ROOMS[room]!!
        val result = xRange.firstOrNull { lines[it][roomNumber].value != "." }
        return result?.let {
            Pair(it, roomNumber)
        }
    }

    private fun isHallwayEmpty() = getHallway().all { it.value == "." }
    private fun isHallwayEmpty(range: IntRange) = getHallway().all { if (it.y !in range) true else it.value == "." }
    private fun getHallway() = lines[1].subList(1, 12)

    override fun toString(): String {
        return "totalEnergy: $totalEnergy\n" +
            lines.joinToString("\n") { line -> line.joinToString("") { it.value } }
    }
}

private fun readInputLines(): List<String> {
    return {}::class.java.classLoader.getResource("input.txt")!!
        .readText()
        .split("\n")
        .filter { it.isNotBlank() }
}

private fun readInputLines2(): List<String> {
    return {}::class.java.classLoader.getResource("input2.txt")!!
        .readText()
        .split("\n")
        .filter { it.isNotBlank() }
}
