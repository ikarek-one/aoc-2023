package tasks

import base.Task

class Day01 : Task {
    override val id: Int
        get() = 1

    override fun firstPart(lines: Sequence<String>): String {
        return lines
            .map { it.toCharArray() }
            .map { "" + it.first { ch -> ch in '0'..'9' } + it.last { ch -> ch in '0'..'9' } }
            .map { it.toLong() }
            .sum()
            .toString()
    }


    // for the second part:
    override fun secondPart(lines: Sequence<String>): String {
        return lines
            .map { solveLinePartTwo(it) }
            .sum()
            .toString()
    }

    private fun solveLinePartTwo(line: String): Long {
        val digitWords = mapOf(
            "one" to '1',
            "two" to '2',
            "three" to '3',
            "four" to '4',
            "five" to '5',
            "six" to '6',
            "seven" to '7',
            "eight" to '8',
            "nine" to '9'
        )

        val positions = sequence {
            for ((word, digit) in digitWords) {
                val firstAsWord = word.toRegex().findAll(line).toList().firstOrNull()?.range?.first
                val firstAsDigit = digit.toString().toRegex().findAll(line).toList().firstOrNull()?.range?.first

                val lastAsWord = word.toRegex().findAll(line).toList().lastOrNull()?.range?.last
                val lastAsDigit = digit.toString().toRegex().findAll(line).toList().lastOrNull()?.range?.last

                yield(
                    DigitPosition(
                        digit,
                        listOfNotNull(firstAsWord, firstAsDigit).minOrNull(),
                        listOfNotNull(lastAsWord, lastAsDigit).maxOrNull()
                    )
                )
            }
        }.toList()

        val firstDigit = positions.filter { it.firstOccurrence != null }.minBy { it.firstOccurrence!! }.digit
        val lastDigit = positions.filter { it.lastOccurrence != null }.maxBy { it.lastOccurrence!! }.digit
        val numberAsString = "" + firstDigit + lastDigit

        return numberAsString.toLong()
    }


    private data class DigitPosition(val digit: Char, val firstOccurrence: Int?, val lastOccurrence: Int?)
}
