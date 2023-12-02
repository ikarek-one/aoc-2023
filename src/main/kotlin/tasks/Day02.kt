package tasks

import base.Task


class Day02 : Task {
    override val id: Int
        get() = 2

    override fun firstPart(lines: Sequence<String>): String {
        return lines.map { parseLine(it) }
            .filter { game ->
                val ms = subsetOfMaxes(game.subsets)
                ms.red <= 12 && ms.green <= 13 && ms.blue <= 14
            }
            .sumOf { it.id }
            .toString()
    }

    override fun secondPart(lines: Sequence<String>): String {
        return lines.map { parseLine(it) }
            .map { subsetOfMaxes(it.subsets) }
            .map { it.red * it.green * it.blue }
            .sum()
            .toString()
    }

    private fun subsetOfMaxes(subsets: List<Subset>): Subset {
        val maxRed = subsets.maxOf { it.red }
        val maxGreen = subsets.maxOf { it.green }
        val maxBlue = subsets.maxOf { it.blue }

        return Subset(maxRed, maxGreen, maxBlue)
    }

    private fun parseLine(line: String): Game {
        val id = line.substringBefore(':').replace("\\D+".toRegex(), "").toLong()

        val subsetsStrings = line
            .substringAfter(':')
            .split("; ")

        val subsets = sequence {
            for (subsetString in subsetsStrings) {
                val colors = subsetString.split(", ")
                    .asSequence()
                    .map { it.trim() }
                    .map { it.split(" ") }
                    .associate { l -> l[1].trim() to l[0].trim().toLong() }

                val subset = Subset(colors["red"] ?: 0, colors["green"] ?: 0, colors["blue"] ?: 0)
                yield(subset)
            }
        }.toList()

        return Game(id, subsets)
    }

    private data class Game(val id: Long, val subsets: List<Subset>)
    private data class Subset(val red: Long, val green: Long, val blue: Long)
}
