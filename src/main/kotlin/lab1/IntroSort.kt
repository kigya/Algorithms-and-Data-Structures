package lab1

import kotlin.math.floor
import kotlin.math.ln

private const val MIN_ARRAY_SIZE = 100
private const val MIDDLE_ARRAY_SIZE = 10000
private const val MAX_ARRAY_SIZE = 1000000
private const val MAX_ELEMENT = 1000000

private class Introsort(
    private val array: IntArray,
    private val THRESHOLD: Int = 16
) {
    fun sortArray() {
        val depthLimit = findDepthLimit()
        hybridSort(0, array.size - 1, depthLimit)
    }

    fun printArray() = println(array.contentToString())

    /**
     * worst: O(nlogn)
     * average: O(nlogn)
     * best: O(n)
     * storage: nlogn
     */
    private fun hybridSort(begin: Int, end: Int, depth: Int) {
        var depthLimit = depth
        if (end - begin > THRESHOLD) {
            if (depthLimit == 0) {
                heapSort(begin, end)
                return
            }
            depthLimit -= 1
            val pivot = findPivot(begin, begin + (end - begin) / 2 + 1, end)
            swap(pivot, end)
            val p = partition(begin, end)
            hybridSort(begin, p - 1, depthLimit)
            hybridSort(p + 1, end, depthLimit)
        } else {
            insertionSort(begin, end)
        }
    }

    /**
     * worst: O(n^2)
     * average: O(n^2)
     * best: O(n)
     */
    private fun insertionSort(left: Int, right: Int) {
        for (i in left..right) {
            val key = array[i]
            var j = i
            while (j > left && array[j - 1] > key) {
                array[j] = array[j - 1]
                j--
            }
            array[j] = key
        }
    }

    /**
     * worst: O(nlogn)
     * average: O(nlogn)
     * best: O(n)
     */
    private fun heapSort(begin: Int, end: Int) {
        val heapN = end - begin
        heapify(begin, heapN)
        for (i in heapN downTo 1) {
            swap(begin, begin + i)
            maxHeap(1, i, begin)
        }
    }

    private fun maxHeap(i: Int, heapN: Int, begin: Int) {
        var iterator = i
        val temp = array[begin + iterator - 1]
        var child: Int
        while (iterator <= heapN / 2) {
            child = 2 * iterator
            if (child < heapN
                && array[begin + child - 1] < array[begin + child]
            ) child++
            if (temp >= array[begin + child - 1]) break
            array[begin + iterator - 1] = array[begin + child - 1]
            iterator = child
        }
        array[begin + iterator - 1] = temp
    }

    private fun heapify(begin: Int, heapN: Int) {
        (heapN / 2 downTo 1).forEach { i ->
            maxHeap(i, heapN, begin)
        }
    }

    private fun swap(i: Int, j: Int) {
        val temp = array[i]
        array[i] = array[j]
        array[j] = temp
    }

    private fun findPivot(a1: Int, b1: Int, c1: Int): Int {
        val max = array[a1].coerceAtLeast(array[b1]).coerceAtLeast(array[c1])
        val min = array[a1].coerceAtMost(array[b1]).coerceAtMost(array[c1])
        val median = max xor min xor array[a1] xor array[b1] xor array[c1]
        if (median == array[a1]) return a1
        return if (median == array[b1]) b1 else c1
    }

    private fun partition(low: Int, high: Int): Int {
        val pivot = array[high]
        var i = low - 1
        (low until high).forEach { j ->
            if (array[j] <= pivot) {
                i++
                swap(i, j)
            }
        }
        swap(i + 1, high)
        return i + 1
    }

    private fun findDepthLimit() = (2 * floor(
        ln(array.size.toDouble()) / ln(2.0)
    )).toInt()
}

fun main() {
    //codeInput()
    userInput()
    //findOptimalThresholdGeneral()
}

private fun userInput() {
    println("Enter arrays amount, arrays lenght and arrays max element:")
    val (arraysAmount, arraySize, maxElement) = readLine()!!.split(" ").map { it.toInt() }
    findOptimalThresholdByUserInput(arraysAmount, arraySize, maxElement)
}

private fun codeInput() {
    val array = IntArray(MAX_ARRAY_SIZE) { (0 until MAX_ELEMENT).random() }
    val introSort = Introsort(array)
    introSort.sortArray()
    introSort.printArray()
}

fun findOptimalThresholdByUserInput(arraysAmount: Int, arraysLenght: Int, maxElement: Int) {
    val timeValues: MutableList<Pair<Int, Int>> = mutableListOf()
    (8..64).forEach { timeValues.add(Pair(it, 0)) }
    repeat((0..arraysAmount).count()) {
        (8..64).forEach { threshold ->
            val array = IntArray(arraysLenght) { (0..maxElement).random() }
            val introsortArray = Introsort(array, threshold)

            val startArray = System.currentTimeMillis()
            introsortArray.sortArray()
            val endArray = System.currentTimeMillis()

            timeValues[threshold - 8] = Pair(
                timeValues[threshold - 8].first,
                timeValues[threshold - 8].second + (endArray - startArray).toInt()
            )
        }
    }
    println(timeValues.sortedBy { it.second }.take(5))
}

// [(21, 3389), (20, 3391), (22, 3397), (23, 3403), (17, 3405)]
fun findOptimalThresholdGeneral() {
    val timeValues: MutableList<Pair<Int, Int>> = mutableListOf()
    (8..64).forEach { timeValues.add(Pair(it, 0)) }
    repeat((0..32).count()) {
        (8..64).forEach { threshold ->
            val maxArray = IntArray(MAX_ARRAY_SIZE) { (0..MAX_ELEMENT).random() }
            val middleArray = IntArray(MIDDLE_ARRAY_SIZE) { (0..MAX_ELEMENT).random() }
            val minArray = IntArray(MIN_ARRAY_SIZE) { (0..MAX_ELEMENT).random() }

            val introsortMaxArray = Introsort(maxArray, threshold)
            val introsortMiddleArray = Introsort(middleArray, threshold)
            val introsortMinArray = Introsort(minArray, threshold)

            val startMaxArray = System.currentTimeMillis()
            introsortMaxArray.sortArray()
            val endMaxArray = System.currentTimeMillis()

            val startMiddleArray = System.currentTimeMillis()
            introsortMiddleArray.sortArray()
            val endMiddleArray = System.currentTimeMillis()

            val startMinArray = System.currentTimeMillis()
            introsortMinArray.sortArray()
            val endMinArray = System.currentTimeMillis()

            timeValues[threshold - 8] = Pair(
                timeValues[threshold - 8].first,
                timeValues[threshold - 8].second + (endMaxArray - startMaxArray).toInt()
            )

            timeValues[threshold - 8] = Pair(
                timeValues[threshold - 8].first,
                timeValues[threshold - 8].second + (endMiddleArray - startMiddleArray).toInt()
            )

            timeValues[threshold - 8] = Pair(
                timeValues[threshold - 8].first,
                timeValues[threshold - 8].second + (endMinArray - startMinArray).toInt()
            )
        }
    }
    println(timeValues.sortedBy { it.second }.take(5))
}
