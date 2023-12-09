package tasks

import base.Task
import utils.separateBy


class Day05 : Task {
    override val id: Int
        get() = 5

    override fun firstPart(lines: Sequence<String>): String {
        val lineList = lines.toList()
        val inputParts = lineList.separateBy("")

        val maps = inputParts.drop(1).associate { list -> list[0].trim() to list.drop(1) }

        val seeds = inputParts[0][0].substringAfter(':')
            .split("\\s+".toRegex())
            .asSequence()
            .filter { it.isNotBlank() }
            .map { it.toLong() }
            .toList()

        val rangeMaps = maps.entries.associate { (k, v) -> k to constructMap(v) }

        return seeds.minOfOrNull { seed -> seedToLocation(seed, rangeMaps) }.toString()
    }


    private fun seedToLocation(seed: Long, maps: Map<String, RangeMap>): Long {
        val soil = maps["seed-to-soil map:"]!!.getValue(seed)
        val fertilizer = maps["soil-to-fertilizer map:"]!!.getValue(soil)
        val water = maps["fertilizer-to-water map:"]!!.getValue(fertilizer)
        val light = maps["water-to-light map:"]!!.getValue(water)
        val temperature = maps["light-to-temperature map:"]!!.getValue(light)
        val humidity = maps["temperature-to-humidity map:"]!!.getValue(temperature)
        val location = maps["humidity-to-location map:"]!!.getValue(humidity)

        return location
    }

    private fun constructMap(rangeLines: List<String>): RangeMap {
        return RangeMap(rangeLines
            .map { mapLineToRange(it) })
    }

    private fun mapLineToRange(line: String): Range {
        val (k, v, l) = line.split(" ").filter { it.isNotBlank() }.map { it.trim().toLong() }
        return Range(v, k, l)
    }

    private class RangeMap(val ranges: List<Range>) {
        fun getValue(key: Long): Long {
            return ranges
                .firstOrNull { key in it.keyRange }
                ?.valueCalc?.let { it(key) }
                ?: key
        }
    }

    private class Range(keyStart: Long, valueStart: Long, rangeLen: Long) {
        val keyRange: LongRange
        val valueCalc: (Long) -> Long

        init {
            keyRange = (keyStart..(keyStart + rangeLen))
            valueCalc = { key: Long -> (valueStart - keyStart) + key }
        }
    }


    // unused stuff for part two:

//    private fun locationToSeed(location:Long, reverseMaps: Map<String,RangeMap>): Long {
//        val humidity = reverseMaps["humidity-to-location map:"]!!.getValue(location)
//        val temperature = reverseMaps["temperature-to-humidity map:"]!!.getValue(humidity)
//        val light = reverseMaps["light-to-temperature map:"]!!.getValue(temperature)
//        val water = reverseMaps["water-to-light map:"]!!.getValue(light)
//        val fertilizer = reverseMaps["fertilizer-to-water map:"]!!.getValue(water)
//        val soil = reverseMaps["soil-to-fertilizer map:"]!!.getValue(fertilizer)
//        val seed = reverseMaps["seed-to-soil map:"]!!.getValue(soil)
//
//        return seed
//    }
//
//    private fun constructMapReversed(rangeLines: List<String>): RangeMap {
//        return RangeMap(rangeLines
//            .map { mapLineToRangeReversed(it) })
//    }
//
//
//    private fun mapLineToRangeReversed(line: String): Range {
//        val (k, v, l) = line.split(" ").filter { it.isNotBlank() }.map { it.trim().toLong() }
//        return Range(k, v, l)
//    }
//
//    fun seedLineToRanges(line: String):List<LongRange> {
//        return line.substringAfter(':')
//            .split("\\s+".toRegex())
//            .filter { it.isNotBlank() }
//            .map { it.toLong() }
//            .chunked(2)
//            .map { list -> (list[0]..list[0]+list[1]) }
//    }


}
