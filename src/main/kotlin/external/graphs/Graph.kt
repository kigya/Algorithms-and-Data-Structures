package external.graphs

interface Graph {
    val verticies: Int
    var edges: Int
    fun adjacentVertices(from: Int): Collection<Int>

    fun vertices(): IntRange = 0 until verticies
}
