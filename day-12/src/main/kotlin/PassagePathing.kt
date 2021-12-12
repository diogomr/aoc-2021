fun main() {
    println("Part One Solution: ${partOne()}")
    println("Part Two Solution: ${partTwo()}")
}

private fun partOne(): Int {
    val map = readMap()

    return getPaths(map, "start", mutableListOf(), false)
        .count()
}

private fun partTwo(): Int {
    val map = readMap()

    return getPaths(map, "start", mutableListOf(), true)
        .count()
}

private fun getPaths(
    map: Map<String, List<String>>,
    current: String,
    visited: List<String>,
    allowSingleSmallCaveTwice: Boolean
): List<List<String>> {

    if (current == "end") {
        return listOf(listOf(current))
    }

    val visitedNew = visited.toMutableList()
    if (current.lowercase() == current) {
        visitedNew.add(current)
    }

    val directions = map.entries
        .filter { current in it.value }
        .map { it.key }
        .plus(map[current] ?: emptyList())
        .filter { it != "start" }

    val allowAlreadyVisited = allowSingleSmallCaveTwice && current !in visited
    val result = directions
        .filter { it !in visited || allowAlreadyVisited }
        .flatMap {
            getPaths(map, it, visitedNew, allowAlreadyVisited)
        }

    return result.map {
        mutableListOf(current) + it
    }
}

private fun readMap(): Map<String, List<String>> {
    val map: MutableMap<String, MutableList<String>> = mutableMapOf()

    readInputLines()
        .forEach {
            val split = it.split("-")
            if (map[split[0]] != null) {
                map[split[0]]!!.add(split[1])
            } else {
                map[split[0]] = mutableListOf(split[1])
            }
        }

    return map
}

private fun readInputLines(): List<String> {
    return {}::class.java.classLoader.getResource("input.txt")!!
        .readText()
        .split("\n")
        .filter { it.isNotBlank() }
}
