package utils

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

