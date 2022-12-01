package external

import kotlin.math.min

private const val inf = -999999

fun main(args: Array<String>) {
    val adjacencyMatrix = readArgs(args)
    val floyd = Floyd(adjacencyMatrix)
    floyd.findShortestPath()
}

class FloydBottleneck(
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

    fun findShortestPath() {
        println()
        println("L0:")
        printMatrix(adjacencyMatrix)
        println("S0:")
        printMatrix(referenceMatrix)
        for (k in adjacencyMatrix.indices) {
            println()
            println("----")
            for (i in adjacencyMatrix.indices) {
                for (j in adjacencyMatrix.indices) {
                    if (adjacencyMatrix[i][j] < min(adjacencyMatrix[i][k], adjacencyMatrix[k][j])) {
                        adjacencyMatrix[i][j] = min(adjacencyMatrix[i][k], adjacencyMatrix[k][j])
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
}


