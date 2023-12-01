package base

interface Task {
    val id: Int

    fun firstPart(lines: Sequence<String>): String? = null
    fun secondPart(lines: Sequence<String>): String? = null
}

