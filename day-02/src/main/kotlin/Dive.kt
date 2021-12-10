fun main() {

    println("Part One Solution: ${partOne()}")
    println("Part Two Solution: ${partTwo()}")
}

private fun partOne(): Int {
    var horizontal = 0
    var depth = 0

    readInput()
        .forEach {
            when (it.first) {
                Command.FORWARD -> horizontal += it.second
                Command.DOWN -> depth += it.second
                Command.UP -> depth -= it.second
            }
        }

    return horizontal * depth
}

private fun partTwo(): Int {
    var aim = 0
    var horizontal = 0
    var depth = 0

    readInput()
        .forEach {
            when (it.first) {
                Command.FORWARD -> {
                    horizontal += it.second
                    depth += aim * it.second
                }
                Command.DOWN -> aim += it.second
                Command.UP -> aim -= it.second
            }
        }

    return horizontal * depth
}

private fun readInput(): List<Pair<Command, Int>> {
    return {}::class.java.classLoader.getResource("input.txt")
        .readText()
        .split("\n")
        .filter { it.isNotBlank() }
        .map {
            val split = it.split(" ")
            val command = Command.valueOf(split[0].uppercase())
            val value = split[1].toInt()
            Pair(command, value)
        }
}

enum class Command {
    FORWARD,
    DOWN,
    UP,
}
