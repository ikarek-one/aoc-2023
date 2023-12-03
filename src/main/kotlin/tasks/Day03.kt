package tasks

import base.Task


class Day03 : Task {
    override val id: Int
        get() = 3


    // part one:
    override fun firstPart(lines: Sequence<String>): String {
        val characters = lines.map { it.toList() }.toList()

        val acceptations = fillAcceptations(characters)

        return characters.indices
            .asSequence()
            .map { row -> partNumbersInLine(characters[row], acceptations[row]) }
            .flatten()
            .map { it.toLong() }
            .sum()
            .toString()
    }

    private fun fillAcceptations(characters: List<List<Char>>): Array<Array<Boolean>> {
        val acceptations = characters
            .map { lineList -> Array(lineList.size) { false } }
            .toTypedArray()

        for (row in characters.indices) {
            for (col in characters[row].indices) {
                val ch = characters[row][col]
                if (charType(ch) == CharType.SYMBOL) {
                    val moves = listOf(
                        -1 to -1,
                        -1 to 0,
                        -1 to 1,
                        0 to -1,
                        0 to 1,
                        1 to -1,
                        1 to 0,
                        1 to 1
                    )

                    moves
                        .map { it.first + row to it.second + col }
                        .forEach { acceptations.getOrNull(it.first)?.setIfAble(it.second, true) }
                }
            }
        }

        return acceptations
    }

    private fun partNumbersInLine(lineChars: List<Char>, accepts: Array<Boolean>): List<String> {
        var buffer = ""
        val acceptsBuffer = mutableListOf<Boolean>()
        val numbers = mutableListOf<String>()

        require(lineChars.size == accepts.size) { "lineChars.size != accepts.size" }

        for (i in lineChars.indices) {
            if (charType(lineChars[i]) == CharType.DIGIT) {
                buffer += lineChars[i]
                acceptsBuffer += accepts[i]
            } else {
                if (buffer.isNotEmpty()) {
                    if (acceptsBuffer.any { it }) {
                        numbers += buffer
                    }
                    buffer = ""
                    acceptsBuffer.clear()
                }
            }
        }

        if (buffer.isNotEmpty() && acceptsBuffer.any { it }) {
            numbers += buffer
        }

        return numbers.toList()
    }


    private enum class CharType {
        DIGIT,
        SYMBOL,
        EMPTY,
        DOT;
    }

    private fun charType(ch: Char?): CharType {
        return when (ch) {
            null -> CharType.EMPTY
            in '0'..'9' -> CharType.DIGIT
            '.' -> CharType.DOT
            else -> CharType.SYMBOL
        }
    }

    private fun <T> Array<T>.setIfAble(idx: Int, elem: T): Boolean {
        return if (idx in this.indices) {
            this[idx] = elem
            true
        } else false
    }


    // part two:
    override fun secondPart(lines: Sequence<String>): String {
        val linesList = lines.toList()
        val asteriskPositions = linesList
            .asSequence()
            .map { line -> line.toList().withIndex().filter { it.value == '*' } }
            .withIndex()
            .flatMap { line -> line.value.asSequence().map { symbol -> line.index to symbol.index } }

        return sequence {
            for (asterisk in asteriskPositions) {
                val (row, col) = asterisk
                val numbers = listOf(row - 1, row, row + 1)
                    .mapNotNull { linesList.getOrNull(it) }
                    .map { numbersInLine(it) }
                    .flatten()
                    .filter { col in (it.location.first - 1)..(it.location.last + 1) }

                yield(numbers)
            }
        }.filter { it.size == 2 }
            .map { it[0].num * it[1].num }
            .sum()
            .toString()
    }

    private data class PartNumber(val num: Long, val location: IntRange)

    private fun numbersInLine(line: String): List<PartNumber> {
        val numberRegex = "\\d+".toRegex()
        return numberRegex.findAll(line)
            .map { matchResult ->
                PartNumber(matchResult.value.toLong(), matchResult.range)
            }.toList()
    }
}
