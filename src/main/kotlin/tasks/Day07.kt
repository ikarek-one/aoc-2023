package tasks

import base.Task



class Day07 : Task {
    override val id: Int
        get() = 7

    private val cardComparator = CardComparator()
    private val handComparator = HandComparator()


    override fun firstPart(lines: Sequence<String>): String {
        val hands = lines
            .map { parseInput(it) }
            .sortedWith(handComparator.reversed())
            .toList()

        val size = hands.size

        return hands.withIndex()
            .sumOf { (size - it.index) * it.value.bid }
            .toString()
    }

    private fun parseInput(line: String):Hand {
        val (cards, bid) = line.split("\\s+".toRegex())
        return Hand(cards, bid.toLong())
    }

    private enum class HandType : Comparable<HandType> {
        FIVE,
        FOUR,
        FULL_HOUSE,
        THREE,
        TWO_PAIR,
        ONE_PAIR,
        HIGH_CARD
    }

    private inner class HandComparator: Comparator<Hand> {
        override fun compare(o1: Hand?, o2: Hand?): Int {
            return cardComparator.compare(o1!!.cards, o2!!.cards)
        }
    }

    private inner class CardComparator : Comparator<String> {
        val ordering = listOf('A', 'K', 'Q', 'J', 'T', '9', '8', '7', '6', '5', '4', '3', '2')

        override fun compare(o1: String?, o2: String?): Int {
            if (o1 == null || o2 == null) throw NullPointerException()

            return if (findHandType(o1).ordinal > findHandType(o2).ordinal) {
                -1
            } else if (findHandType(o1).ordinal < findHandType(o2).ordinal) {
                1
            } else {
                val one = o1.map { ordering.indexOf(it) }.onEach { if (it < 0) throw Exception("wrong char!") }
                val two = o2.map { ordering.indexOf(it) }.onEach { if (it < 0) throw Exception("wrong char!") }

                require(one.size == two.size)

                for (i in one.indices) {
                    if (one[i] > two[i]) return -1
                    else if (one[i] < two[i]) return 1
                }

                return 0
            }
        }
    }


    private inner class Hand(val cards: String, val bid: Long)


    private fun findHandType(card: String): HandType {
        val handSize = 5
        require(card.length == handSize) { "card.length (${card.length}) != handSize ($handSize)" }

        val occurrences =
            card.toCharArray().toList().groupingBy { it }.eachCount().toList().sortedByDescending { it.second }
        val first = occurrences.getOrNull(0)?.second
        val second = occurrences.getOrNull(1)?.second

        return when {
            (first == 5) -> HandType.FIVE
            (first == 4) -> HandType.FOUR
            (first == 3 && second == 2) -> HandType.FULL_HOUSE
            (first == 3) -> HandType.THREE
            (first == 2 && second == 2) -> HandType.TWO_PAIR
            (first == 2) -> HandType.ONE_PAIR
            (occurrences.map { it.first }.distinct().size == occurrences.size) -> HandType.HIGH_CARD
            else ->
                throw Exception("Wrong card scheme! card = '$card'; occurrences = $occurrences")
        }
    }

}