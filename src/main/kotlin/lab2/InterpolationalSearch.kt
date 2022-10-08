package lab2

private const val ARRAY_SIZE = 10
private const val MAX_ELEMENT = 100

private class InterpolationalSearch(
    private val list: List<Int>,
    private val key: Int
) {

    fun search(): Int {
        var left = 0
        var right = list.size - 1
        var compareOperations = 0
        while (left <= right) {
            compareOperations++
            val middle = left + (key - list[left]) * (right - left) / (list[right] - list[left])
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
}

/*
Random array: 3, 11, 20, 20, 53, 53, 66, 71, 73, 88
Key: 53
Number of compare operations: 2
Index: 5
 */
fun main() {
    val randomArray: List<Int> = IntArray(ARRAY_SIZE) { (0..MAX_ELEMENT).random() }.sorted()
    val key = randomArray.random()
    println("Random array: ${randomArray.joinToString()}")
    println("Key: $key")

    val interpolationalSearch = InterpolationalSearch(randomArray, key)
    println("Index: " + interpolationalSearch.search())
}

