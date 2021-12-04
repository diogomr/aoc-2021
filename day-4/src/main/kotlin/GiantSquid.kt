fun main() {
    println("Part One Solution: ${partOne()}")
    println("Part Two Solution: ${partTwo()}")
}

private fun partOne(): Int {

    val numbersToDraw = readNumberToDraw()
    val bingoCards = readBingoCards()

    numbersToDraw.forEach { number ->
        bingoCards.forEach { card ->
            card.mark(number)
            if (card.isBingo()) {
                return card.getWinningScore(number)
            }
        }
    }

    return 0
}

private fun partTwo(): Int {
    val numbersToDraw = readNumberToDraw()
    val bingoCards = readBingoCards().toMutableList()

    var lastWinningCardScore = 0
    numbersToDraw.forEach { number ->
        bingoCards.forEach { card ->
            if (!card.hasWon) { // Do not mark cards that already won or else their score will be changed
                card.mark(number)
                if (card.isBingo()) {
                    lastWinningCardScore = card.getWinningScore(number)
                }
            }
        }
    }

    return lastWinningCardScore
}

private fun readBingoCards(): List<BingoCard> {
    return readLines()
        .drop(1)
        .filter { it.isNotBlank() }
        .windowed(5, 5, false) {

            val card: Array<IntArray> = Array(5) {
                IntArray(5)
            }
            it.forEachIndexed { index, line ->
                card[index] = line.split(" ")
                    .filter { el -> el.isNotBlank() } // handle double spaces of single digit numbers
                    .map { number -> number.toInt() }
                    .toIntArray()
            }
            BingoCard(card)
        }
}

private fun readNumberToDraw(): List<Int> {
    return readLines()
        .first()
        .split(",")
        .map { it.toInt() }
}

data class BingoCard(
    val card: Array<IntArray>,
    var hasWon: Boolean = false,
) {
    companion object {
        private const val MARK = -1
    }

    fun mark(draw: Int) {
        for (row in card.indices) {
            for (column in card[0].indices) {
                if (card[row][column] == draw) {
                    card[row][column] = MARK
                    return
                }
            }
        }
    }

    fun isBingo(): Boolean {
        for (row in card.indices) {
            if (card[row].all { it == MARK }) {
                this.hasWon = true
                return true
            }

            val columnArray = mutableListOf<Int>()
            for (column in card[0].indices) {
                columnArray.add(card[column][row])
            }
            if (columnArray.all { it == MARK }) {
                this.hasWon = true
                return true
            }
        }
        return false
    }

    fun getWinningScore(winningDrawNumber: Int): Int {
        val sumOfNonMarked = card.sumOf { row ->
            row.filter { it != MARK }.sum()
        }
        return sumOfNonMarked * winningDrawNumber
    }
}

private fun readLines(): List<String> {
    return {}::class.java.classLoader.getResource("input.txt")
        .readText()
        .split("\n")
}
