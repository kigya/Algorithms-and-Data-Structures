package lab1

private const val MIN_ARRAY_SIZE = 100
private const val MIDDLE_ARRAY_SIZE = 10000
private const val MAX_ARRAY_SIZE = 1000000
private const val MAX_ELEMENT = 1000000

class MergeInsertionSort(
    private val array: IntArray,
    private val THRESHOLD: Int = 16
) {

    fun printArray() = println(array.contentToString())

    /**
     * worst: O(nk + nlog(n/k))
     * average: O(n + nlog(n/k))
     * best: O(n + nlog(n/k))
     * storage: O(1)
     */
    fun sortArray(p: Int, r: Int) {
        if (r - p > THRESHOLD) {
            val q = (p + r) / 2
            sortArray(p, q)
            sortArray(q + 1, r)
            merge(p, q, r)
        } else {
            insertionSort(p, r)
        }
    }

    /**
     * worst: O(n^2)
     * average: O(n^2)
     * best: O(n)
     * storage: O(1)
     */
    private fun insertionSort(left: Int, right: Int) {
        (left + 1..right).forEach { i ->
            val key = array[i]
            var j = i - 1
            while (j >= left && array[j] > key) {
                array[j + 1] = array[j]
                j--
            }
            array[j + 1] = key
        }
    }

    /**
     * worst: O(nlogn)
     * average: O(nlogn)
     * best: O(nlogn)
     * storage: O(1)
     */
    private fun merge(left: Int, middle: Int, right: Int) {
        val leftArray = array.copyOfRange(left, middle + 1)
        val rightArray = array.copyOfRange(middle + 1, right + 1)
        var i = 0
        var j = 0
        var k = left
        while (i < leftArray.size && j < rightArray.size) {
            if (leftArray[i] <= rightArray[j]) {
                array[k] = leftArray[i]
                i++
            } else {
                array[k] = rightArray[j]
                j++
            }
            k++
        }
        while (i < leftArray.size) {
            array[k] = leftArray[i]
            i++
            k++
        }
        while (j < rightArray.size) {
            array[k] = rightArray[j]
            j++
            k++
        }
    }

}

fun main() {
    //codeInput()
    //userInput()
    findOptimalThresholdGeneral()
}

private fun userInput() {
    println("Enter arrays amount, arrays lenght and arrays max element:")
    val (arraysAmount, arraySize, maxElement) = readLine()!!.split(" ").map { it.toInt() }
    findOptimalThresholdByUserInput(arraysAmount, arraySize, maxElement)
}

private fun codeInput() {
    val array = IntArray(MAX_ARRAY_SIZE) { (0 until MAX_ELEMENT).random() }
    val introSort = MergeInsertionSort(array)
    introSort.sortArray(0, array.lastIndex)
    introSort.printArray()
}

fun findOptimalThresholdByUser(arraysAmount: Int, arraysLenght: Int, maxElement: Int) {
    val timeValues: MutableList<Pair<Int, Int>> = mutableListOf()
    (8..64).forEach { timeValues.add(Pair(it, 0)) }
    repeat((0..arraysAmount).count()) {
        (8..64).forEach { threshold ->
            val array = IntArray(arraysLenght) { (0..maxElement).random() }
            val mergeInsertionSort = MergeInsertionSort(array, threshold)

            val startArray = System.currentTimeMillis()
            mergeInsertionSort.sortArray(0, array.size - 1)
            val endArray = System.currentTimeMillis()

            timeValues[threshold - 8] = Pair(
                timeValues[threshold - 8].first,
                timeValues[threshold - 8].second + (endArray - startArray).toInt()
            )
        }
    }
    println(timeValues.sortedBy { it.second }.take(5))
}

//[(26, 2628), (23, 2636), (28, 2637), (24, 2638), (21, 2640)]
fun findOptimalThreshold() {
    val timeValues: MutableList<Pair<Int, Int>> = mutableListOf()
    (8..64).forEach { timeValues.add(Pair(it, 0)) }
    repeat((0..32).count()) {
        (8..64).forEach { threshold ->
            val maxArray = IntArray(MAX_ARRAY_SIZE) { (0..MAX_ELEMENT).random() }
            val middleArray = IntArray(MIDDLE_ARRAY_SIZE) { (0..MAX_ELEMENT).random() }
            val minArray = IntArray(MIN_ARRAY_SIZE) { (0..MAX_ELEMENT).random() }

            val mergeInsertionMaxArray = MergeInsertionSort(maxArray, threshold)
            val mergeInsertionMiddleArray = MergeInsertionSort(middleArray, threshold)
            val introsortMinArray = MergeInsertionSort(minArray, threshold)

            val startMaxArray = System.currentTimeMillis()
            mergeInsertionMaxArray.sortArray(0, maxArray.size - 1)
            val endMaxArray = System.currentTimeMillis()

            val startMiddleArray = System.currentTimeMillis()
            mergeInsertionMiddleArray.sortArray(0, middleArray.size - 1)
            val endMiddleArray = System.currentTimeMillis()

            val startMinArray = System.currentTimeMillis()
            introsortMinArray.sortArray(0, minArray.size - 1)
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