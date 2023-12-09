package tasks

import base.Task
import tasks.Day05.Companion.separateBy


class Day08 : Task {
    override val id: Int
        get() = 8


    override fun firstPart(lines: Sequence<String>): String {
        val (turnsLine, nodeStrings) = lines.toList().separateBy("")
        val turnsIterator = generateSequence(0, Int::inc)
            .map { turnsLine[0][(it % turnsLine[0].length)] }
            .iterator()
        val nodes = nodeStrings.map { parseNode(it) }.associateBy { it.current }

        val start = "AAA"
        val end = "ZZZ"

        var currentNode = nodes[start]!!
        var counter = 0L

        while (currentNode.current != end) {
            when (val ch = turnsIterator.next()) {
                'L' -> currentNode = nodes[currentNode.left]!!
                'R' -> currentNode = nodes[currentNode.right]!!
                else -> throw Exception("Unexpected char: '$ch'!")
            }
            counter += 1
        }

        return counter.toString()
    }

    private fun parseNode(line: String): Node {
        val regex = "([A-Z]+) = \\(([A-Z]+), ([A-Z]+)\\)".toRegex()
//        println(">line '$line', findAll = ${regex.findAll(line).toList().map { it.value }  }")
        val (current, left, right) = regex.find(line)!!.destructured
        return Node(current, left, right)
    }

    private data class Node(val current: String, val left: String, val right: String)
}