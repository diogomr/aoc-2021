fun main() {

    partOne().apply {
        println("Part One Solution: $this")
    }

    partTwo().apply {
        println("Part Two Solution: $this")
    }
}

private fun partOne(): Int {
    val lines = readLines()
    val columns = readColumns(lines)

    val mostCommonBits = mutableListOf<Char>()
    columns.forEach {
        mostCommonBits.add(findMostCommonBit(it))
    }

    val mostCommonBitsString = String(mostCommonBits.toCharArray())
    val leastCommonBitsString = invertBits(mostCommonBitsString)

    val gammaRate = readBinaryNumber(mostCommonBitsString)
    val epsilonRate = readBinaryNumber(leastCommonBitsString)

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
    return readBinaryNumber(lines[0])
}

private fun readBinaryNumber(mostCommonBitsString: String) = Integer.parseUnsignedInt(mostCommonBitsString, 2)

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

private fun findMostCommonBit(column: Collection<Char>) =
    if (column.count { it == '1' } >= column.count { it == '0' }) '1' else '0'

private fun invertBits(bitString: String) = String(bitString.map { invertBit(it) }.toCharArray())
private fun invertBit(bit: Char) = if (bit == '1') '0' else '1'

private fun readLines(): List<String> {
    return {}::class.java.classLoader.getResource("input.txt")
        .readText()
        .split("\n")
        .filter { it.isNotBlank() }
}
