fun main() {
    println("Part One Solution: ${partOne()}")
    println("Part Two Solution: ${partTwo()}")
}

private const val DELIMITER = " | "
private const val SPACE = " "

private fun partOne(): Int {

    return readInputLines()
        .flatMap {
            it.split(DELIMITER)[1].split(SPACE)
        }
        .count {
            val length = it.length
            length == 2 || length == 3 || length == 4 || length == 7
        }
}

private fun partTwo(): Int {

    return readInputLines().sumOf {
        val (left, right) = it.split(DELIMITER)
        val patterns = left.split(SPACE)

        val one = patterns.find { p -> p.length == 2 }!!
        val six = patterns.find { p -> p.length == 6 && !getIntersection(p, one).equalsIgnoreOrder(one) }!!

        val rightBottomSegment = getIntersection(one, six)
        val three = patterns.find { p -> p.length == 5 && getIntersection(p, one).equalsIgnoreOrder(one) }!!
        val five = patterns.find { p ->
            p.length == 5 && p != three && getIntersection(p, rightBottomSegment) == rightBottomSegment
        }!!
        val two = patterns.find { p -> p.length == 5 && p != three && p != five }!!

        val rightTopSegment = getIntersection(two, one)

        val eight = patterns.find { p -> p.length == 7 }!!
        val nine = patterns.find { p -> p.length == 6 && getIntersection(p, three).equalsIgnoreOrder(three) }!!
        val bottomLeftSegment = getNonIntersecting(eight, nine)

        val displays = right.split(SPACE)
        displays.map { display ->
            getDigit(
                display = display,
                rightTop = rightTopSegment,
                rightBottom = rightBottomSegment,
                bottomLeft = bottomLeftSegment
            )
        }.joinToString("").toInt()
    }
}

fun getDigit(display: String, rightTop: String, rightBottom: String, bottomLeft: String): Int {
    return when (display.length) {
        2 -> 1
        3 -> 7
        4 -> 4
        5 -> if (!display.contains(rightBottom)) 2 else if (display.contains(rightTop)) 3 else 5
        6 -> if (!display.contains(rightTop)) 6 else if (display.contains(bottomLeft)) 0 else 9
        7 -> 8
        else -> throw RuntimeException("Impossible state")
    }
}

fun String.equalsIgnoreOrder(other: String): Boolean {
    return this.toSortedSet() == other.toSortedSet()
}

fun getIntersection(first: String, second: String): String {
    return first.toSortedSet()
        .intersect(second.toSortedSet())
        .joinToString("")
}

fun getNonIntersecting(first: String, second: String): String {
    val intersection = getIntersection(first, second)
    val toReturn = (first + second).toSortedSet().toMutableList()
    toReturn.removeAll(intersection.toSortedSet())
    return toReturn.joinToString("")
}

private fun readInputLines(): List<String> {
    return {}::class.java.classLoader.getResource("input.txt")!!
        .readText()
        .split("\n")
        .filter { it.isNotBlank() }
}
