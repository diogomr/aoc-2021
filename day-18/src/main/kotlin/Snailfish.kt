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

    var result: SnailfishNumber? = null
    readInputLines().forEach {
        val number = SnailfishNumber()
        readNumber(it.substring(1), number)
        result = if (result == null) number else sum(result!!, number)
    }
    return result!!.getMagnitude()
}

private fun partTwo(): Int {
    var magnitude = Int.MIN_VALUE
    val lines = readInputLines()
    for (i in lines.indices) {
        for (j in lines.indices) {
            if (i == j) {
                continue
            }
            val first = SnailfishNumber()
            readNumber(lines[i].substring(1), first)

            val second = SnailfishNumber()
            readNumber(lines[j].substring(1), second)

            val result = sum(first, second)
            if (result.getMagnitude() > magnitude) {
                magnitude = result.getMagnitude()
            }
        }
    }
    return magnitude
}

data class SnailfishNumber(
    var leftValue: Int? = null,
    var rightValue: Int? = null,
    var leftPair: SnailfishNumber? = null,
    var rightPair: SnailfishNumber? = null,
) {
    override fun toString(): String {
        val left = if (leftValue != null) leftValue.toString() else leftPair.toString()
        val right = if (rightValue != null) rightValue.toString() else rightPair.toString()
        return "[$left,$right]"
    }

    fun getMagnitude(): Int {
        val left = if (leftValue != null) leftValue!! else leftPair!!.getMagnitude()
        val right = if (rightValue != null) rightValue!! else rightPair!!.getMagnitude()
        return (left * 3) + (right * 2)
    }
}

private fun sum(left: SnailfishNumber, right: SnailfishNumber): SnailfishNumber {
    val sum = SnailfishNumber(null, null, left, right)
    reduce(sum)
    return sum
}

private fun reduce(number: SnailfishNumber) {
    if (explode(number, number, 0)) {
        reduce(number)
    } else if (split(number, number)) {
        reduce(number)
    }
}

private fun split(number: SnailfishNumber, root: SnailfishNumber): Boolean {
    if (number.leftPair != null && split(number.leftPair!!, root)) return true
    if (number.leftValue != null && number.leftValue!! >= 10) {
        number.leftPair = splitInteger(number.leftValue!!)
        number.leftValue = null
        return true
    }
    if (number.rightPair != null && split(number.rightPair!!, root)) return true
    if (number.rightValue != null && number.rightValue!! >= 10) {
        number.rightPair = splitInteger(number.rightValue!!)
        number.rightValue = null
        return true
    }
    return false
}

private fun splitInteger(number: Int): SnailfishNumber {
    val result = number / 2
    return if (number % 2 == 0) SnailfishNumber(result, result) else SnailfishNumber(result, result + 1)
}

private fun explode(root: SnailfishNumber, number: SnailfishNumber, level: Int): Boolean {
    if (level > 3 && number.leftPair == null && number.rightPair == null) {
        explodeNumber(root, number)
        return true
    } else {
        if (number.leftValue != null && number.rightValue != null) return false
        if (number.leftValue == null && explode(root, number.leftPair!!, level + 1)) return true
        if (number.rightValue == null && explode(root, number.rightPair!!, level + 1)) return true
        return false
    }
}

private fun explodeNumber(root: SnailfishNumber, number: SnailfishNumber) {
    val parent = findParent(root, number)
    if (parent!!.leftPair !== number) { // Pair is rightPair of parent

        parent!!.rightPair = null
        parent.rightValue = 0

        if (parent.leftValue != null) {
            parent.leftValue = parent.leftValue!! + number.leftValue!!
        } else {
            val rightmostLeft = findRightmostLeft(parent)
            rightmostLeft.rightValue = rightmostLeft.rightValue!! + number.leftValue!!
        }

        var grandparent = findParent(root, parent)
        var parentCopy = parent
        while (true) {
            // Find leftmost right
            if (grandparent?.rightPair === parentCopy) {
                parentCopy = grandparent
                grandparent = findParent(root, grandparent!!)
                if (grandparent == null) return else continue
            } else {
                if (grandparent!!.rightValue != null) {
                    grandparent.rightValue = grandparent.rightValue!! + number.rightValue!!
                } else {
                    val leftmostRight = findLeftmostRight(grandparent)
                    leftmostRight.leftValue = leftmostRight.leftValue!! + number.rightValue!!
                }
                return
            }
        }
    } else { // Pair is leftPair of parent

        parent!!.leftPair = null
        parent.leftValue = 0

        if (parent.rightValue != null) {
            parent.rightValue = parent.rightValue!! + number.rightValue!!
        } else {
            val leftmostRight = findLeftmostRight(parent)
            leftmostRight.leftValue = leftmostRight.leftValue!! + number.rightValue!!
        }

        var grandparent = findParent(root, parent)
        var parentCopy = parent
        while (true) {
            // Find rightmost left
            if (grandparent?.leftPair === parentCopy) {
                parentCopy = grandparent
                grandparent = findParent(root, grandparent!!)
                if (grandparent == null) return else continue
            } else {
                if (grandparent!!.leftValue != null) {
                    grandparent.leftValue = grandparent.leftValue!! + number.leftValue!!
                } else {
                    val rightmostLeft = findRightmostLeft(grandparent)
                    rightmostLeft.rightValue = rightmostLeft.rightValue!! + number.leftValue!!
                }
                return
            }
        }
    }
}

private fun findRightmostLeft(number: SnailfishNumber): SnailfishNumber {
    if (number.leftValue != null) return number
    return findDeepRight(number.leftPair!!)
}

private fun findLeftmostRight(number: SnailfishNumber): SnailfishNumber {
    if (number.rightValue != null) return number
    return findDeepLeft(number.rightPair!!)
}

private fun findDeepLeft(number: SnailfishNumber): SnailfishNumber {
    if (number.leftValue != null) return number
    return findDeepLeft(number.leftPair!!)
}

private fun findDeepRight(number: SnailfishNumber): SnailfishNumber {
    if (number.rightValue != null) return number
    return findDeepRight(number.rightPair!!)
}

private fun findParent(root: SnailfishNumber, child: SnailfishNumber): SnailfishNumber? {
    if (root === child) return null
    if (root.leftValue != null && root.rightValue != null) return null
    if (root.leftPair === child) return root
    if (root.rightPair === child) return root
    root.leftPair?.let {
        findParent(it, child)?.let { p -> return p }
    }
    root.rightPair?.let {
        findParent(it, child)?.let { p -> return p }
    }
    return null
}

const val OPEN = '['
const val CLOSE = ']'
const val COMMA = ','
private fun readNumber(input: String, number: SnailfishNumber): String {

    var inputCopy = input
    while (true) {
        val i = inputCopy[0]
        if (i == COMMA) {
            inputCopy = inputCopy.substring(1)
            continue
        } else if (i == OPEN) {
            val child = SnailfishNumber(null, null, null, null)
            inputCopy = readNumber(inputCopy.substring(1), child)
            if (number.leftPair == null && number.leftValue == null) {
                number.leftPair = child
            } else if (number.rightPair == null && number.rightValue == null) {
                number.rightPair = child
            } else {
                throw RuntimeException("Impossible state")
            }
        } else if (i == CLOSE) {
            return inputCopy.substring(1)
        } else {
            if (number.leftPair == null && number.leftValue == null) {
                number.leftValue = i.digitToInt()
            } else {
                number.rightValue = i.digitToInt()
            }
            inputCopy = inputCopy.substring(1)
        }
    }
}

private fun readInputLines(): List<String> {
    return {}::class.java.classLoader.getResource("input.txt")!!
        .readText()
        .split("\n")
        .filter { it.isNotBlank() }
}
