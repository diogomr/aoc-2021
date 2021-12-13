fun main() {
    println("Part One Solution: ${partOne()}")
    println("Part Two Solution: ${partTwo()}")
}

private fun partOne(): Int {
    val folds = mutableListOf<String>()
    val map = Array(1400) { Array(1400) { "." } }
    var readingFolds = false
    readInputLines().forEach {
        if (it.isBlank()) {
            readingFolds = true
            return@forEach
        }
        if (readingFolds) {
            folds.add(it)
        } else {
            val (x, y) = it.split(",")
            map[y.toInt()][x.toInt()] = "#"
        }
    }

    val (coordinate, value) = folds[0].split(" ")[2].split("=")
    val horizontal = coordinate == "y"
    fold(map, horizontal, value.toInt())

    return map.flatMap { it.asIterable() }
        .count { it == "#" }
}

private fun fold(map: Array<Array<String>>, horizontal: Boolean, value: Int) {
    if (horizontal) {
        for (i in 0 until value) {
            for (j in map[i].indices) {
                if (map[i][j] == "#" || map[value * 2 - i][j] == "#") {
                    map[i][j] = "#"
                    map[value * 2 - i][j] = "."
                }
            }
        }
    } else {
        for (j in 0 until value) {
            for (i in map.indices) {
                if (map[i][j] == "#" || map[i][value * 2 - j] == "#") {
                    map[i][j] = "#"
                    map[i][value * 2 - j] = "."
                }
            }
        }
    }
}

private fun partTwo(): Int {
    val folds = mutableListOf<String>()
    val map = Array(2000) { Array(2000) { "." } }
    var readingFolds = false
    readInputLines().forEach {
        if (it.isBlank()) {
            readingFolds = true
            return@forEach
        }
        if (readingFolds) {
            folds.add(it)
        } else {
            val (x, y) = it.split(",")
            map[y.toInt()][x.toInt()] = "#"
        }
    }

    folds.forEach {
        val (coordinate, value) = it.split(" ")[2].split("=")
        val horizontal = coordinate == "y"
        fold(map, horizontal, value.toInt())
    }

    map.forEach {
        if (it.contains("#")) {
            println(it.joinToString(" "))
        }
    }

    return -1
}

private fun readInputLines(): List<String> {
    return {}::class.java.classLoader.getResource("input.txt")!!
        .readText()
        .split("\n")
}
