fun main() {
    partOne().apply {
        println("Part One Solution: $this")
    }
    partTwo().apply {
        println("Part Two Solution: $this")
    }

    partOneFunctional().apply {
        println("Part One Functional Solution: $this")
    }
    partTwoWindowed().apply {
        println("Part Two Windowed Solution: $this")
    }
}

fun partOne(): Int {

    val lines = readInput()

    var count = 0
    for (i in lines.indices) {
        if (i == 0) {
            continue
        }
        if (lines[i] > lines[i - 1]) {
            count++
        }
    }

    return count
}

fun partTwo(): Int {

    val lines = readInput()

    var count = 0
    var previous: ThreeMeasureSlide? = null
    for (i in lines.indices) {
        if ((i + 2) < lines.size) {
            val current = ThreeMeasureSlide(lines[i], lines[i + 1], lines[i + 2])
            if ((previous != null) && (current.sum() > previous.sum())) {
                count++
            }
            previous = current
        }
    }
    return count
}

data class ThreeMeasureSlide(
    val first: Int,
    val second: Int,
    val third: Int,
) {
    fun sum() = first.plus(second).plus(third)
}

fun partOneFunctional(): Int {

    val lines = readInput()
    return lines.drop(1)
        .filterIndexed { index, element ->
            element > lines[index]
        }
        .count()
}

fun partTwoWindowed(): Int {

    val lines = readInput()
    val slides = lines.windowed(3, 1, false) {
        it[0] + it[1] + it[2]
    }

    return slides.drop(1)
        .filterIndexed { index, element ->
            element > slides[index]
        }
        .count()
}

private fun readInput(): List<Int> {
    return {}::class.java.classLoader.getResource("input.txt")
        .readText()
        .split("\n")
        .filter { it.isNotBlank() }
        .map { Integer.parseInt(it) }
}
