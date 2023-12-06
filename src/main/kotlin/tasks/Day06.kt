package tasks

import base.Task


class Day06 : Task {
    override val id: Int
        get() = 6

    override fun firstPart(lines: Sequence<String>): String {
        val races = parseInput(lines.toList())

        return races.map { winningTimesSimple(it) }
            .fold(1L) { a, b -> a * b }.toString()
    }

    override fun secondPart(lines: Sequence<String>): String {
        return winningTimesSimple(parseInputPartTwo(lines.toList())).toString()
    }


    private fun winningTimesSimple(race: Race): Long {
        val (t, record) = race
        val a = 1

        return (0..t)
            .map { w -> (w * a) * (t - w) }.count { it > record }.toLong()
    }

    private fun parseInput(lines: List<String>): List<Race> {
        val (times, distances) = listOf(lines[0], lines[1])
            .map { str ->
                str.substringAfter(':')
                    .split("\\s+".toRegex()).filter { s -> s.isNotBlank() }.map { s -> s.toLong() }
            }

        require(times.size == distances.size) { "times.size != distances.size" }

        return times.indices
            .map { Race(times[it], distances[it]) }
    }

    private fun parseInputPartTwo(lines: List<String>): Race {
        val (time, dist) = lines.map { it.replace("\\D+".toRegex(), "").toLong() }
        return Race(time, dist)
    }

    private data class Race(val time: Long, val recordDistance: Long)

}
