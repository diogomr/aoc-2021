fun main() {
    println("Part One Solution: ${partOne()}")
    println("Part Two Solution: ${partTwo()}")
}

private fun partOne(): Int {
    val lines = readLines()
    val columns = readColumns(lines)

    val mostCommonBits = columns.map {
        findMostCommonBit(it)
    }.toCharArray().concatToString()

    val gammaRate = mostCommonBits.toInt(2)
    val epsilonRate = invertBits(mostCommonBits).toInt(2)

    return gammaRate * epsilonRate
}

private fun partTwo(): Int {
    val lines = readLines()

    val o2GeneratorRating = getO2GeneratorRating(lines.toMutableList())
    val cO2ScrubberRating = getCO2ScrubberRating(lines.toMutableList())
    return o2GeneratorRating * cO2ScrubberRating
}

private fun getO2GeneratorRating(lines: MutableList<String>): Int {
    return findRating(lines) {
        findMostCommonBit(it)
    }
}

private fun getCO2ScrubberRating(lines: MutableList<String>): Int {
    return findRating(lines) {
        invertBit(findMostCommonBit(it))
    }
}

private fun findRating(
    lines: MutableList<String>,
    bitCriteria: (List<Char>) -> Char
): Int {
    var index = 0
    while (lines.size > 1) {
        val columns = readColumns(lines)
        val bit = bitCriteria.invoke(columns[index])
        lines.removeIf {
            it[index] != bit
        }
        index++
    }
    return lines[0].toInt(2)
}

private fun readColumns(lines: List<String>): List<MutableList<Char>> {
    val columns = List(lines[0].length) {
        mutableListOf<Char>()
    }

    lines.forEach {
        it.forEachIndexed { charIndex, char ->
            columns[charIndex].add(char)
        }
    }
    return columns
}

private fun findMostCommonBit(column: Collection<Char>): Char {
    val ones = column.count { it == '1' }
    return if (ones >= column.size - ones) '1' else '0'
}

private fun invertBits(bits: String) = bits.map { invertBit(it) }.toCharArray().concatToString()
private fun invertBit(bit: Char) = if (bit == '1') '0' else '1'

private fun readLines(): List<String> {
    return {}::class.java.classLoader.getResource("input.txt")
        .readText()
        .split("\n")
        .filter { it.isNotBlank() }
}
