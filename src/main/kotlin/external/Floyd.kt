package external

private const val inf = 999999

fun main() {
    greeting()
    val adjacencyMatrix = formattedArray(readLinesToList())
    val floyd = Floyd(adjacencyMatrix)
    floyd.findShortestPath()
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

    fun findShortestPath() {
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
}

fun readLinesToList(): Array<String> {
    val list = mutableListOf<String>()
    val first = readLine()?.trim()
    list.add(first.toString())
    val remainingLines = first?.split()?.size
    (0 until remainingLines!! - 1).forEach { _ -> list.add(readLine().toString()) }
    return list.toTypedArray()
}

fun String.split(): List<String> {
    return this.split(Regex("\\s+"))
}

fun Array<String>.print() {
    this.forEach { println(it) }
}

fun formattedArray(array: Array<String>): Array<IntArray> {
    val formattedArray = Array(array.size) { IntArray(array.size) }
    val splitted: Array<List<String>> = array.split()
    for (i in array.indices) {
        for (j in array.indices) {
            if (!splitted[i][j].isNumber()) formattedArray[i][j] = inf else
                formattedArray[i][j] = splitted[i][j].toInt()
        }
    }
    return formattedArray
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

fun greeting() {
    println("-----Floyd Algorithm-----")
    println("Enter the matrix line by line, leaving spaces between the elements\n" +
            "To enter infinity, enter any non-numeric character, for example \"i\"\n" +
            "Hint: the dimension of the matrix will be dynamically calculated after entering the first line")
    println()
}

