fun main() {
    println("Part One Solution: ${partOne()}")
    println("Part Two Solution: ${partTwo()}")
}

private fun partOne(): Int {

    val fishes = readFishes()
        .toMutableList()

    for (day in 1..80) {
        val newFishes = mutableListOf<Fish>()
        fishes.forEach { fish ->
            fish.ageByOne()?.let {
                newFishes.add(it)
            }
        }
        fishes.addAll(newFishes)
    }
    return fishes.size
}

private fun partTwo(): Long {
    val fishes = readFishes()

    val fishTimers = LongArray(10) { 0 }
    fishes.forEach {
        fishTimers[it.timer] += 1L
    }

    for (day in 1..256) {
        for (i in fishTimers.indices) {
            if (i == 0) {
                // Adding to position 9 and 7 because they'll be processed in this loop and moved to the right positions
                fishTimers[9] += fishTimers[0]
                fishTimers[7] += fishTimers[0]
                fishTimers[0] = 0
            } else {
                fishTimers[i - 1] += fishTimers[i]
                fishTimers[i] = 0
            }
        }
    }
    return fishTimers.sum()
}

private fun readFishes() = readInputLines()[0]
    .split(",")
    .map {
        Fish(it.toInt())
    }

data class Fish(
    var timer: Int
) {
    fun ageByOne(): Fish? {
        return if (timer == 0) {
            timer = 6
            Fish(8)
        } else {
            timer -= 1
            null
        }
    }
}

private fun readInputLines(): List<String> {
    return {}::class.java.classLoader.getResource("input.txt")
        .readText()
        .split("\n")
        .filter { it.isNotBlank() }
}
