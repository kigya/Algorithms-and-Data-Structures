package lab3

private const val inf = 999999

fun main() {
    val floyd = Floyd(generateRandomAdjacencyMatrix(10))
    floyd.findShortestPaths()
    println("Fire station: ${floyd.findVertexWithShortestPath()}")
}

class Floyd(
    private var adjacencyMatrix: Array<IntArray>,
) {
    private var referenceMatrix: Array<IntArray> = Array(adjacencyMatrix.size) { IntArray(adjacencyMatrix.size) }

    init {
        for (i in adjacencyMatrix.indices) {
            for (j in adjacencyMatrix.indices) {
                referenceMatrix[i][j] = j + 1
            }
        }
    }

    fun findShortestPaths() {
        println()
        println("L0:")
        printMatrix(adjacencyMatrix)
        println("S0:")
        printMatrix(referenceMatrix)
        for (k in adjacencyMatrix.indices) {
            println()
            print("----")
            for (i in adjacencyMatrix.indices) {
                for (j in adjacencyMatrix.indices) {
                    if (adjacencyMatrix[i][j] > adjacencyMatrix[i][k] + adjacencyMatrix[k][j]
                        && adjacencyMatrix[i][k] != inf && adjacencyMatrix[k][j] != inf
                    ) {
                        adjacencyMatrix[i][j] = adjacencyMatrix[i][k] + adjacencyMatrix[k][j]
                        referenceMatrix[i][j] = referenceMatrix[i][k]
                    }
                }
            }
            println("L${k + 1}:")
            printMatrix(adjacencyMatrix)
            println("S${k + 1}")
            printMatrix(referenceMatrix)
        }
    }

    private fun printMatrix(matrix: Array<IntArray>) {
        for (i in matrix.indices) {
            print("[ ")
            for (j in matrix.indices) {
                if (matrix[i][j] == inf) {
                    print("inf")
                } else {
                    print(String.format("%2d", matrix[i][j]))
                }
                if (j != matrix.size - 1) {
                    print(", ")
                }
            }
            println(" ]")
        }
        println()
    }

    fun findVertexWithShortestPath(): Int {
        var min = inf
        var vertex = 0
        adjacencyMatrix.indices.forEach { i ->
            val sum = adjacencyMatrix.indices.sumOf { adjacencyMatrix[i][it] }
            if (sum < min) {
                min = sum
                vertex = i
            }
        }
        return vertex
    }
}

fun String.split(): List<String> {
    return this.split(Regex("\\s+"))
}

fun Array<String>.print() {
    this.forEach { println(it) }
}

fun Array<String>.split(): Array<List<String>> {
    val spliited = Array(this.size) { listOf<String>() }
    for (i in this.indices) {
        spliited[i] = this[i].split()
    }
    return spliited
}

fun String.isNumber(): Boolean {
    return this.matches("-?\\d+(\\.\\d+)?".toRegex())
}

private fun generateRandomAdjacencyMatrix(size: Int): Array<IntArray> {
    val matrix = Array(size) { IntArray(size) }
    for (i in matrix.indices) {
        for (j in matrix.indices) {
            if (i == j) {
                matrix[i][j] = 0
            } else {
                matrix[i][j] = (1..50).random()
            }
        }
    }
    return matrix
}