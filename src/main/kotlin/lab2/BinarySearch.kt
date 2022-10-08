package lab2

private const val ARRAY_SIZE = 10
private const val MAX_ELEMENT = 100

private class BinarySearch(
    private val list: List<Int>,
    private val key: Int
) {

    fun search(): Int {
        var left = 0
        var right = list.size - 1
        var compareOperations = 0
        while (left <= right) {
            compareOperations++
            val middle = (left + right) / 2
            if (list[middle] == key) {
                compareOperations++
                println("Number of compare operations: $compareOperations")
                return middle
            } else if (list[middle] < key) {
                compareOperations++
                left = middle + 1
            } else {
                compareOperations++
                right = middle - 1
            }
        }
        println("Number of compare operations: $compareOperations")
        return -1
    }

    fun recursiveSearch(left: Int = 0, right: Int = list.size - 1): Int {
        if (left > right) {
            return -1
        }
        val middle = (left + right) / 2
        return when {
            list[middle] == key -> middle
            list[middle] < key -> recursiveSearch(middle + 1, right)
            else -> recursiveSearch(left, middle - 1)
        }
    }

    fun searchNative(): Int {
        return list.binarySearch(key)
    }
}

/*
Random array: 6, 29, 29, 31, 46, 51, 53, 54, 63, 79
Key: 29
Number of compare operations: 4
Custom: 1
Native: 1
Recursive: 1
 */
fun main() {
    val randomArray: List<Int> = IntArray(ARRAY_SIZE) { (0..MAX_ELEMENT).random() }.sorted()
    val key = randomArray.random()
    println("Random array: ${randomArray.joinToString()}")
    println("Key: $key")

    val binarySearch = BinarySearch(randomArray, key)
    println("Custom: " + binarySearch.search())
    println("Native: " + binarySearch.searchNative())
    println("Recursive: " + binarySearch.recursiveSearch())
}

// binary tree class


