package lab3

import java.util.*
import java.util.Arrays.fill
import kotlin.system.exitProcess

class HungarianAlgorithm(matrix: Array<IntArray>) {
    private var matrix: Array<IntArray>
    private var rowMarkedZeros: IntArray
    private var columnMarkedZeros: IntArray
    private var rowIsCovered: IntArray
    private var columnIsCovered: IntArray
    private var storageZeros: IntArray

    init {
        if (matrix.size != matrix[0].size) {
            try {
                throw IllegalAccessException("The matrix is not square!")
            } catch (ex: IllegalAccessException) {
                System.err.println(ex)
                exitProcess(1)
            }
        }
        this.matrix = matrix
        rowMarkedZeros = IntArray(matrix.size)
        columnMarkedZeros = IntArray(matrix[0].size)
        rowIsCovered = IntArray(matrix.size)
        columnIsCovered = IntArray(matrix[0].size)
        storageZeros = IntArray(matrix.size)
        fill(storageZeros, -1)
        fill(rowMarkedZeros, -1)
        fill(columnMarkedZeros, -1)
    }

    fun findOptimalAssignment(): Array<IntArray?> {
        reduceMatrix()
        markIndependentZeros()
        coverColumnsWhichContainMarkedZero()
        while (!this.allColumnsAreCovered()) {
            var mainZero = markZeros()
            while (mainZero == null) {  
                step7()
                mainZero = markZeros()
            }
            if (rowMarkedZeros[mainZero[0]] == -1) {
                createChainOfAlternatingSquares(mainZero)
                coverColumnsWhichContainMarkedZero()
            } else {
                rowIsCovered[mainZero[0]] = 1 
                columnIsCovered[rowMarkedZeros[mainZero[0]]] = 0 
                step7()
            }
        }
        val optimalAssignment = arrayOfNulls<IntArray>(matrix.size)
        for (i in columnMarkedZeros.indices) {
            optimalAssignment[i] = intArrayOf(i, columnMarkedZeros[i])
        }
        return optimalAssignment
    }

    private fun allColumnsAreCovered(): Boolean = !columnIsCovered.contains(0)

    private fun reduceMatrix() {
        matrix.indices.forEach { i ->
            var currentRowMin = Int.MAX_VALUE
            matrix[i].indices
                .asSequence()
                .filter { matrix[i][it] < currentRowMin }
                .forEach { currentRowMin = matrix[i][it] }
            matrix[i].indices.forEach { k ->
                matrix[i][k] -= currentRowMin
            }
        }

        matrix[0].indices.forEach { i ->
            var currentColMin = Int.MAX_VALUE
            matrix.indices
                .asSequence().filterNot { matrix[it][i] >= currentColMin }
                .forEach { currentColMin = matrix[it][i] }
            matrix.indices.forEach { k ->
                matrix[k][i] -= currentColMin
            }
        }
    }

    private fun markIndependentZeros() {
        val rowHasSquare = IntArray(matrix.size)
        val colHasSquare = IntArray(matrix[0].size)
        matrix.indices.forEach { i ->
            matrix.indices.forEach { j: Int ->
                if (matrix[i][j] == 0 && rowHasSquare[i] == 0 && colHasSquare[j] == 0) {
                    rowHasSquare[i] = 1
                    colHasSquare[j] = 1
                    rowMarkedZeros[i] = j
                    columnMarkedZeros[j] = i
                    return
                }
            }
        }
    }

    private fun coverColumnsWhichContainMarkedZero() = columnMarkedZeros.indices.forEach { i ->
        columnIsCovered[i] = if (columnMarkedZeros[i] != -1) 1 else 0
    }

    private fun step7() {
        var minUncoveredValue = Int.MAX_VALUE
        matrix.indices.forEach { i ->
            if (rowIsCovered[i] == 1) {
                return@forEach
            }
            matrix[0].indices
                .asSequence()
                .filter { columnIsCovered[it] == 0 && matrix[i][it] < minUncoveredValue }
                .forEach { minUncoveredValue = matrix[i][it] }
        }
        if (minUncoveredValue > 0) {
            matrix.indices.forEach { i ->
                matrix[0].indices.forEach { j ->
                    if (rowIsCovered[i] == 1 && columnIsCovered[j] == 1)
                        matrix[i][j] += minUncoveredValue
                    else if (rowIsCovered[i] == 0 && columnIsCovered[j] == 0) matrix[i][j] -= minUncoveredValue
                }
            }
        }
    }

    private fun markZeros(): IntArray? {
        matrix.indices.forEach { i ->
            if (rowIsCovered[i] == 0) {
                matrix[i].indices.forEach { j ->
                    if (matrix[i][j] == 0 && columnIsCovered[j] == 0) {
                        storageZeros[i] = j
                        return intArrayOf(i, j)
                    }
                }
            }
        }
        return null
    }

    private fun createChainOfAlternatingSquares(mainZero: IntArray) {
        var i = mainZero[0]
        var j = mainZero[1]
        val K: MutableSet<IntArray> = LinkedHashSet()
        K.add(mainZero)
        var found = false
        do {
            found = if (columnMarkedZeros[j] != -1) {
                K.add(intArrayOf(columnMarkedZeros[j], j))
                true
            } else {
                false
            }
            if (!found) {
                break
            }
            i = columnMarkedZeros[j]
            j = storageZeros[i]
            (if (j != -1) {
                K.add(intArrayOf(i, j))
                true
            } else {
                false
            }).also { found = it }
        } while (found)

        K.forEach { zero ->
            if (columnMarkedZeros[zero[1]] == zero[0]) {
                columnMarkedZeros[zero[1]] = -1
                rowMarkedZeros[zero[0]] = -1
            }

            if (storageZeros[zero[0]] == zero[1]) {
                rowMarkedZeros[zero[0]] = zero[1]
                columnMarkedZeros[zero[1]] = zero[0]
            }
        }
        fill(storageZeros, -1)
        fill(rowIsCovered, 0)
        fill(columnIsCovered, 0)
    }
}

fun main() {
    println("Row - employee; column - efficency")
    val matrix = arrayOf(
        intArrayOf(5, 9, 1, 10),
        intArrayOf(10, 3, 2, 7),
        intArrayOf(8, 7, 4, 6),
        intArrayOf(4, 6, 8, 3)
    )
    matrix.forEach { println(it.contentToString()) }
    val hungarianAlgorithm = HungarianAlgorithmAdvanced(matrix)
    val optimal = hungarianAlgorithm.findOptimalAssignment()
    optimal.print()
}

fun Array<IntArray?>.print() {
    for (i in this.indices) {
        for (j in this[i]!!.indices) {
            print("${this[i]!![j]} ")
        }
        println()
    }
}

