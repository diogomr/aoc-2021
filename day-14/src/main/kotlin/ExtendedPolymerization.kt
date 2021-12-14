fun main() {
    println("Part One Solution: ${partOne()}")
    println("Part Two Solution: ${partTwo()}")
    println("Part Two Solution Non Recursive: ${partTwoNonRecursive()}")
}

private fun partOne(): Int {
    var template = readInputLines()[0].toCharArray().toList()
    val rules = readRules()

    for (step in 1..10) {
        val result = mutableListOf<Char>()
        result.add(template[0])
        for (i in template.indices) {
            if (i + 1 !in template.indices) {
                break
            }
            val pair = "${template[i]}${template[i + 1]}"
            result.add(rules[pair]!![0])
            result.add(template[i + 1])
        }
        template = result
    }

    val charCount = template.groupingBy { it }.eachCount()
    return charCount.maxOf { it.value } - charCount.minOf { it.value }
}

private fun partTwo(): Long {
    val template = readInputLines()[0].toCharArray().toList()
    val rules = readRules()

    var letterCounter = mapOf(template[0] to 1L)
    val cache = mutableMapOf<Pair<Int, Pair<Char, Char>>, Map<Char, Long>>()
    template.indices.take(template.size - 1)
        .forEach {
            val result = stepThrough(Pair(template[it], template[it + 1]), rules, 40, cache)
            letterCounter = merge(letterCounter, result)
        }

    return letterCounter.maxOf { it.value } - letterCounter.minOf { it.value }
}

private fun partTwoNonRecursive(): Long {
    val template = readInputLines()[0].toCharArray().toList()
    val rules = readRules()

    var pairCounter = mutableMapOf<Pair<Char, Char>, Long>()
    template.indices.take(template.size - 1)
        .forEach {
            val pair = Pair(template[it], template[it + 1])
            pairCounter.merge(pair, 1L) { old, new -> old + new }
        }

    for (i in 1..40) {
        val newCounter = mutableMapOf<Pair<Char, Char>, Long>()
        pairCounter.forEach {
            val (left, right) = it.key
            val middle = rules["$left$right"]
            newCounter.merge(Pair(left, middle!![0]), it.value) { old, new -> old + new }
            newCounter.merge(Pair(middle[0], right), it.value) { old, new -> old + new }
        }
        pairCounter = newCounter
    }

    val charCount = mutableMapOf<Char, Long>()
    pairCounter.forEach {
        charCount.merge(it.key.first, it.value) { old, new -> old + new }
    }

    // First key is always chosen to handle overlaps so the last element of the template is missing in the final pair
    charCount.merge(template[template.size - 1], 1L) { old, new -> old + new }

    return charCount.maxOf { it.value } - charCount.minOf { it.value }
}

private fun stepThrough(
    pair: Pair<Char, Char>,
    rules: Map<String, String>,
    iter: Int,
    cache: MutableMap<Pair<Int, Pair<Char, Char>>, Map<Char, Long>>
): Map<Char, Long> {

    cache[Pair(iter, pair)]?.let {
        return it
    }

    val (left, right) = pair

    val middle = rules["$left$right"]!![0]
    if (iter == 1) {
        return if (middle == right) mapOf(middle to 2L) else mapOf(middle to 1L, right to 1L)
    }

    val newIteration = iter - 1

    val leftMidPair = Pair(left, middle)
    val leftMid = stepThrough(leftMidPair, rules, newIteration, cache).apply {
        cache[Pair(newIteration, leftMidPair)] = this
    }

    val midRightPair = Pair(middle, right)
    val midRight = stepThrough(midRightPair, rules, newIteration, cache).apply {
        cache[Pair(newIteration, Pair(middle, right))] = this
    }

    return merge(leftMid, midRight)
}

fun merge(first: Map<Char, Long>, second: Map<Char, Long>): Map<Char, Long> {
    val result = first.toMutableMap()
    second.forEach { entry ->
        result.merge(entry.key, entry.value) { old, new ->
            old + new
        }
    }
    return result
}

private fun readRules(): Map<String, String> {
    val pairToMidValue = mutableMapOf<String, String>()

    readInputLines().drop(1)
        .forEach {
            val (pair, value) = it.split(" -> ")
            pairToMidValue[pair] = value
        }
    return pairToMidValue
}

private fun readInputLines(): List<String> {
    return {}::class.java.classLoader.getResource("input.txt")!!
        .readText()
        .split("\n")
        .filter { it.isNotBlank() }
}
