package tasks

import base.Task


class Day03 : Task {
    override val id: Int
        get() = 3

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


    fun fillAcceptations(characters: List<List<Char>>): Array<Array<Boolean>> {
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

        assert(lineChars.size == accepts.size)
        require(lineChars.size == accepts.size) { -> "lineChars.size != accepts.size" }

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

}