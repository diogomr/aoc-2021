import java.util.LinkedList
import java.util.Stack

fun main() {
    println("Part One Solution: ${partOne()}")
    println("Part Two Solution: ${partTwo()}")
}

val closingMatch = mapOf(
    ')' to '(',
    ']' to '[',
    '}' to '{',
    '>' to '<',
)

val openingMatch = mapOf(
    '(' to ')',
    '[' to ']',
    '{' to '}',
    '<' to '>',
)

val openingDelimiters = openingMatch.keys

val illegalCharScore = mapOf(
    ')' to 3,
    ']' to 57,
    '}' to 1197,
    '>' to 25137,
)

val completingCharScore = mapOf(
    ')' to 1,
    ']' to 2,
    '}' to 3,
    '>' to 4,
)

private fun partOne(): Int {
    return readInputLines()
        .map {
            val stack = Stack<Char>()
            for (curChar in it.toCharArray()) {
                if (curChar in openingDelimiters) {
                    stack.push(curChar)
                } else {
                    if (stack.pop() != closingMatch[curChar]!!) {
                        return@map curChar
                    }
                }
            }
        }
        .sumOf {
            illegalCharScore[it] ?: 0
        }
}

private fun partTwo(): Long {
    val sortedScore = readInputLines()
        .asSequence()
        .map {
            val stack = Stack<Char>()
            for (c in it.toCharArray()) {
                if (c in openingDelimiters) {
                    stack.push(c)
                } else {
                    if (stack.pop() != closingMatch[c]!!) {
                        return@map Stack<Char>()
                    }
                }
            }
            stack
        }
        .map {
            val toComplete = LinkedList<Char>()
            while (!it.empty()) {
                toComplete.add(openingMatch[it.pop()]!!)
            }
            toComplete
        }.map {
            var score = 0L
            while (!it.isEmpty()) {
                score = (score * 5) + completingCharScore[it.pop()!!]!!
            }
            score
        }
        .filter { it != 0L }
        .sorted()
        .toList()


    return sortedScore[sortedScore.size / 2]
}

private fun readInputLines(): List<String> {
    return {}::class.java.classLoader.getResource("input.txt")!!
        .readText()
        .split("\n")
        .filter { it.isNotBlank() }
}
