package tasks

import base.Task


class Day09 : Task {
    override val id: Int
        get() = 9

    override fun firstPart(lines: Sequence<String>): String {
        return lines
            .map { line -> line.split("\\s+".toRegex()).map { it.toLong() } }
            .map { longs -> extrapolate(longs) }
            .sum()
            .toString()
    }

    private fun extrapolate(seq: List<Long>): Long {
        val differences = mutableListOf(seq.toMutableList())

        while (differences.last().any { it != 0L }) {
            val list = differences.last()

            differences.add(
                (0 until list.size - 1)
                    .map { list[it + 1] - list[it] }
                    .toMutableList()
            )
        }

        for (i in ((differences.size - 1) downTo 1)) {
            differences[i - 1] += differences[i - 1].last() + differences[i].last()
        }

        return differences[0].last()
    }


}