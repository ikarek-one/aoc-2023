package tasks

import base.Task
import base.fromConsolePartOne

fun main() {
    fromConsolePartOne(Day05())
}

class Day05 : Task {
    override val id: Int
        get() = 5

    override fun firstPart(lines: Sequence<String>): String? {
        val lineList = lines.toList()
        val inputParts = lineList.separateBy("")

        inputParts.forEach { println(it) }

        val maps = inputParts.drop(1).associate { list -> list[0].trim() to list.drop(1) }

        val seeds = inputParts[0][0].substringAfter(':')
            .split("\\s+".toRegex())
            .asSequence()
            .filter { it.isNotBlank() }
            .map { it.toLong() }
            .toList()

        val seedsToSoil = constructMap(maps["seed-to-soil map:"]!!)

        val rangeMaps = maps.entries.associate { (k, v) -> k to constructMap(v) }

        (0L..100L).forEach { s -> println("seed: $s || soil: ${seedsToSoil.getValue(s)}") }



        return seeds.map { seed -> seedToLocation(seed, rangeMaps) }.min().toString()
    }

    /*
    seeds: 79 14 55 13
seed-to-soil map:
soil-to-fertilizer map:
fertilizer-to-water map:
water-to-light map:
light-to-temperature map:
temperature-to-humidity map:
humidity-to-location map:
     */

    fun seedToLocation(seed:Long, maps: Map<String,RangeMap>): Long {
        val soil = maps["seed-to-soil map:"]!!.getValue(seed)
        val fertilizer = maps["soil-to-fertilizer map:"]!!.getValue(soil)
        val water = maps["fertilizer-to-water map:"]!!.getValue(fertilizer)
        val light = maps["water-to-light map:"]!!.getValue(water)
        val temperature = maps["light-to-temperature map:"]!!.getValue(light)
        val humidity = maps["temperature-to-humidity map:"]!!.getValue(temperature)
        val location = maps["humidity-to-location map:"]!!.getValue(humidity)

        return location
    }

    fun <E> List<E>.separateBy(separator: E): List<List<E>> {
        val sublists = mutableListOf<List<E>>()
        val currentList = mutableListOf<E>()

        this.forEach { elem ->
            println("elem: $elem; chars: ${(elem as String).toCharArray().map { it.code }}")
            if (elem == separator) {
                if (currentList.isNotEmpty()) {
                    sublists.add(currentList.toList())
                    currentList.clear()
                }
            } else {
                currentList.add(elem)
            }
        }
        sublists.add(currentList)
        return sublists.toList()
    }

    fun mapLineToRange(line: String): Range {
        val (k, v, l) = line.split(" ").filter { it.isNotBlank() }.map { it.trim().toLong() }
//        return Range(k, v, l)
        return Range(v, k, l)
    }


    fun constructMap(rangeLines: List<String>): RangeMap {
        return RangeMap(rangeLines
            .map { mapLineToRange(it) })
    }

    class RangeMap(val ranges: List<Range>) {
        fun getValue(key: Long): Long {
            return ranges
                .firstOrNull { key in it.keyRange }
                ?.valueCalc?.let { it(key) }
                ?: key
        }

    }

    class Range(keyStart: Long, valueStart: Long, rangeLen: Long) {
        val keyRange: LongRange
        val valueCalc: (Long) -> Long

        init {
            keyRange = (keyStart..(keyStart + rangeLen))
            valueCalc = { key: Long -> (valueStart - keyStart) + key }
        }
    }


    /*
    * seeds: 79 14 55 13

seed-to-soil map:
50 98 2
52 50 48

soil-to-fertilizer map:
0 15 37
37 52 2
39 0 15

fertilizer-to-water map:
49 53 8
0 11 42
42 0 7
57 7 4

water-to-light map:
88 18 7
18 25 70

light-to-temperature map:
45 77 23
81 45 19
68 64 13

temperature-to-humidity map:
0 69 1
1 0 69

humidity-to-location map:
60 56 37
56 93 4
    * */

}