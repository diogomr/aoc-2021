import java.util.LinkedList
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

private fun partOne(): Long {

    val instructions = readInstructions()

    val range = (99 downTo 91) +
        (89 downTo 81) +
        (79 downTo 71) +
        (69 downTo 61) +
        (59 downTo 51) +
        (49 downTo 41) +
        (39 downTo 31) +
        (29 downTo 21) +
        (19 downTo 11)

    for (a in range) {
        for (b in range) {
            c@ for (c in range) {
                d@ for (d in range) {
                    e@ for (e in range) {
                        f@ for (f in range) {
                            for (g in range) {
                                val number = "$a$b$c$d$e$f$g"
                                val stack = LinkedList(number.toCharArray().map { it.digitToInt() })
                                val result = runMonad(stack, instructions)
                                if (result == 0L) return number.toLong()
                                if (result > 200000000) {
                                    continue@c
                                }
                                if (result > 10000000) {
                                    continue@d
                                }
                                if (result > 1000000) {
                                    continue@e
                                }
                                if (result > 10000) {
                                    continue@f
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    return -1
}

private fun partTwo(): Long {

    val instructions = readInstructions()

    val range = (11..19) +
        (21..29) +
        (31..39) +
        (41..49) +
        (51..59) +
        (61..69) +
        (71..79) +
        (81..89) +
        (91..99)

    for (a in range) {
        for (b in range) {
            c@ for (c in range) {
                d@ for (d in range) {
                    e@ for (e in range) {
                        f@ for (f in range) {
                            for (g in range) {
                                val number = "$a$b$c$d$e$f$g"
                                val stack = LinkedList(number.toCharArray().map { it.digitToInt() })
                                val result = runMonad(stack, instructions)
                                if (result == 0L) return number.toLong()
                                if (result > 100000000) {
                                    continue@c
                                }
                                if (result > 10000000) {
                                    continue@d
                                }
                                if (result > 1000000) {
                                    continue@e
                                }
                                if (result > 10000) {
                                    continue@f
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    return -1
}

private fun runMonad(input: LinkedList<Int>, instructions: List<Instruction>): Long {

    val memory = mutableMapOf(
        "w" to 0L,
        "x" to 0L,
        "y" to 0L,
        "z" to 0L,
    )

    instructions.forEach {
        when (it.operator) {
            Operator.INP -> {
                memory[it.left] = input.pop()!!.toLong()
            }
            Operator.MUL -> {
                val variable = it.left
                if (it.right!!.toIntOrNull() != null) {
                    memory[variable] = memory[variable]!! * it.right.toInt()
                } else {
                    memory[variable] = memory[variable]!! * memory[it.right]!!
                }
            }
            Operator.ADD -> {
                val variable = it.left
                if (it.right!!.toIntOrNull() != null) {
                    memory[variable] = memory[variable]!! + it.right.toInt()
                } else {
                    memory[variable] = memory[variable]!! + memory[it.right]!!
                }
            }
            Operator.MOD -> {
                val variable = it.left
                if (it.right!!.toIntOrNull() != null) {
                    memory[variable] = memory[variable]!! % it.right.toInt()
                } else {
                    memory[variable] = memory[variable]!! % memory[it.right]!!
                }
            }
            Operator.DIV -> {
                val variable = it.left
                if (it.right!!.toIntOrNull() != null) {
                    memory[variable] = memory[variable]!! / it.right.toInt()
                } else {
                    memory[variable] = memory[variable]!! / memory[it.right]!!
                }
            }
            Operator.EQL -> {
                val variable = it.left
                if (it.right!!.toIntOrNull() != null) {
                    memory[variable] = if (memory[variable]!! == it.right.toLong()) 1 else 0
                } else {
                    memory[variable] = if (memory[variable]!! == memory[it.right]!!) 1 else 0
                }
            }
        }
    }
    return memory["z"]!!
}

private fun readInstructions(): List<Instruction> {
    val regex = Regex("([a-z]{3})\\s([a-z])\\s?([a-z0-9-]*)")
    return readInputLines().map {
        val result = regex.matchEntire(it)!!
        Instruction(
            operator = Operator.valueOf(result.groups[1]!!.value.uppercase()),
            left = result.groups[2]!!.value,
            right = result.groups[3]?.value,
        )
    }
}

data class Instruction(
    val operator: Operator,
    val left: String,
    val right: String?,
)

enum class Operator {
    INP,
    MUL,
    ADD,
    MOD,
    DIV,
    EQL,
}

private fun readInputLines(): List<String> {
    return {}::class.java.classLoader.getResource("input.txt")!!
        .readText()
        .split("\n")
        .filter { it.isNotBlank() }
}
