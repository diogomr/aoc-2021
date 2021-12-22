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

    val player1 = Player(lines[0].split(":")[1].strip().toInt(), 0, 1000)
    val player2 = Player(lines[1].split(":")[1].strip().toInt(), 0, 1000)

    val die = Die()
    while (!player1.hasWon() && !player2.hasWon()) {
        player1.move(die.roll().value + die.roll().value + die.roll().value)
        if (player1.hasWon()) {
            break
        }
        player2.move(die.roll().value + die.roll().value + die.roll().value)
    }
    val losingScore = if (player1.score > player2.score) player2.score else player1.score
    return die.rolls * losingScore
}

private fun partTwo(): Long {
    val lines = readInputLines()

    val player1 = Player(lines[0].split(":")[1].strip().toInt(), 0, 21)
    val player2 = Player(lines[1].split(":")[1].strip().toInt(), 0, 21)

    val cache = mutableMapOf<Universe, List<Universe>>()
    val universe = Universe(player1, player2)
    fillCache(universe, cache)

    val result = getResult(cache)
    return result[universe]!!.maxOf { it.value }
}

private fun getResult(cache: MutableMap<Universe, List<Universe>>): Map<Universe, Map<String, Long>> {

    val results = mutableMapOf<Universe, Map<String, Long>>()
    cache.entries
        .filter { (k, _) -> k.hasWon() }
        .forEach { (k, _) ->
            results[k] = mutableMapOf(k.whoWon() to 1)
        }

    while (results.size != cache.size) {
        cache.entries
            .filter { entry -> entry.key !in results && entry.value.all { it in results } }
            .forEach { (k, v) ->
                val universeResult = mutableMapOf<String, Long>()
                for (subUniverse in v) {
                    results[subUniverse]!!.forEach {
                        universeResult.merge(it.key, it.value) { old, new -> old + new }
                    }
                }
                results[k] = universeResult
            }
    }

    return results
}

private fun fillCache(universe: Universe, cache: MutableMap<Universe, List<Universe>>) {
    if (universe in cache) return
    if (universe.hasWon()) {
        cache[universe] = emptyList()
        return
    }

    val roll = universe.roll()
    cache[universe] = roll

    roll.forEach { fillCache(it, cache) }
}

data class Universe(
    val player1: Player,
    val player2: Player,
) {
    fun hasWon() = player1.hasWon() || player2.hasWon()
    fun whoWon() = if (player1.hasWon()) "player1" else "player2"

    fun roll(): List<Universe> {
        val player1Universes = mutableListOf<Universe>()
        for (x in 1..3) {
            for (y in 1..3) {
                for (z in 1..3) {
                    player1Universes.add(Universe(player1.copy().apply { move(x + y + z) }, player2))
                }
            }
        }
        val universes = mutableListOf<Universe>()
        for (u in player1Universes) {
            if (u.hasWon()) {
                universes.add(u)
                continue
            }
            for (x in 1..3) {
                for (y in 1..3) {
                    for (z in 1..3) {
                        universes.add(u.copy(player2 = u.player2.copy().apply { move(x + y + z) }))
                    }
                }
            }
        }
        return universes
    }
}

data class Die(
    var value: Int = 100,
    var rolls: Int = 0,
) {
    fun roll(): Die {
        if (value == 100) {
            value = 1
        } else {
            value++
        }
        rolls++
        return this
    }
}

data class Player(
    var position: Int,
    var score: Int,
    val winningScore: Int,
) {
    fun move(value: Int) {
        val toAdd = value % 10
        position += toAdd
        if (position > 10) position -= 10
        score += position
    }

    fun hasWon() = score >= winningScore
}

private fun readInputLines(): List<String> {
    return {}::class.java.classLoader.getResource("input.txt")!!
        .readText()
        .split("\n")
        .filter { it.isNotBlank() }
}
