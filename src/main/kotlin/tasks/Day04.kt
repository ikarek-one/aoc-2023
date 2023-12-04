package tasks

import base.Task


class Day04 : Task {
    override val id: Int
        get() = 4

    override fun firstPart(lines: Sequence<String>): String {
        return lines
            .map { parseLine(it) }
            .map { it.points() }
            .sum()
            .toString()
    }


    private fun parseLine(line: String): Game {
        val id = line.substringBefore(':').replace("\\D+".toRegex(), "").toInt()
        val numberPart = line.substringAfter(':')
        val winningNums =
            numberPart.substringBefore('|')
                .split("\\s+".toRegex())
                .filter { it.isNotBlank() }
                .map { it.toLong() }
                .toList()

        val yourNums =
            numberPart.substringAfter('|')
                .split("\\s+".toRegex())
                .filter { it.isNotBlank() }
                .map { it.toLong() }
                .toList()

        return Game(id, yourNums, winningNums)
    }

    private data class Game(val id: Int, val yourNums: List<Long>, val winningNums: List<Long>) {
        fun winningCount() =
            yourNums.count { it in winningNums }.toLong()

        fun points(): Long {
            val n = winningCount()
            return if (n < 1) 0 else pow(2, n - 1)
        }

        fun pow(base: Long, exp: Long): Long {
            return when (exp) {
                0L -> 1
                1L -> base
                else -> base * pow(base, exp - 1)
            }
        }
    }


}