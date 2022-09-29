package lab1

import java.lang.Math.*
import kotlin.math.ln

fun generateRandomArrayUniform(size: Int, seed: Int): IntArray {
    val array = IntArray(size)
    for (i in 0 until size) array[i] = (random() * seed).toInt()
    return array
}

fun boxMullerRandomArray(size: Int, seed: Int): IntArray {
    val array = IntArray(size)
    for (i in 0 until size) {
        val u1 = random()
        val u2 = random()
        val z0 = kotlin.math.sqrt(-2 * ln(u1)) * kotlin.math.cos(2 * PI * u2)
        array[i] = (z0 * seed).toInt()
    }
    return array
}

fun main() {
    val size = 20
    val seed = 100
    val uniformArray = generateRandomArrayUniform(size, seed)
    val gaussArray = boxMullerRandomArray(size, seed)
    println("Middle key uniform array: " + quickSortMiddleKey(uniformArray).contentToString())
    println("Middle key gauss distribution array: " + quickSortMiddleKey(gaussArray).contentToString())
    println("Second key uniform array: " + quickSortSecondKey(uniformArray).contentToString())
    println("Second key gauss distribution array: " + quickSortSecondKey(gaussArray).contentToString())
    println("Median of three key uniform array: " + quickSortMedianOfThree(uniformArray).contentToString())
    println("Median of three key gauss distribution array: " + quickSortMedianOfThree(gaussArray).contentToString())
    println("Random key uniform array: " + quickSortRandomKey(uniformArray).contentToString())
    println("Random key gauss distribution array: " + quickSortRandomKey(gaussArray).contentToString())
    println("Hoare uniform array: " + quickSortHoare(uniformArray).contentToString())
    println("Hoare gauss distribution array: " + quickSortHoare(gaussArray).contentToString())
    println("Lomuto uniform array: " + quickSortHoare(uniformArray).contentToString())
    println("Lomuto gauss distribution array: " + quickSortHoare(gaussArray).contentToString())
    println("Upper median uniform array: " + quickSortHoare(uniformArray).contentToString())
    println("Upper median gauss distribution array: " + quickSortHoare(gaussArray).contentToString())

    printTime()
}

fun quickSortMiddleKey(array: IntArray): IntArray {
    if (array.size < 2) return array
    val key = array[array.size / 2]
    val less = array.filter { it < key }.toIntArray()
    val equal = array.filter { it == key }.toIntArray()
    val greater = array.filter { it > key }.toIntArray()
    return quickSortMiddleKey(less) + equal + quickSortMiddleKey(greater)
}

fun quickSortSecondKey(array: IntArray): IntArray {
    if (array.size < 2) return array
    val key = array[1]
    val less = array.filter { it < key }.toIntArray()
    val equal = array.filter { it == key }.toIntArray()
    val greater = array.filter { it > key }.toIntArray()
    return quickSortSecondKey(less) + equal + quickSortSecondKey(greater)
}

fun quickSortMedianOfThree(array: IntArray): IntArray {
    if (array.size < 2) return array
    val key = medianOfThree(array[0], array[array.size / 2], array[array.size - 1])
    val less = array.filter { it < key }.toIntArray()
    val equal = array.filter { it == key }.toIntArray()
    val greater = array.filter { it > key }.toIntArray()
    return quickSortMedianOfThree(less) + equal + quickSortMedianOfThree(greater)
}

fun medianOfThree(a: Int, b: Int, c: Int): Int {
    return when {
        b in (a + 1) until c -> b
        b in (c + 1) until a -> b
        c in (a + 1) until b -> c
        c in (b + 1) until a -> c
        a in (b + 1) until c -> a
        a in (c + 1) until b -> a
        else -> a
    }
}

fun quickSortRandomKey(array: IntArray): IntArray {
    if (array.size < 2) return array
    val key = array[(random() * array.size).toInt()]
    val less = array.filter { it < key }.toIntArray()
    val equal = array.filter { it == key }.toIntArray()
    val greater = array.filter { it > key }.toIntArray()
    return quickSortRandomKey(less) + equal + quickSortRandomKey(greater)
}

fun quickSortHoare(array: IntArray): IntArray {
    if (array.size < 2) return array
    val key = array[0]
    var i = -1
    var j = array.size
    while (true) {
        do i++
        while (array[i] < key)
        do j--
        while (array[j] > key)
        if (i >= j) return array
        val temp = array[i]
        array[i] = array[j]
        array[j] = temp
    }
}

fun quickSortLomuto(array: IntArray): IntArray {
    if (array.size < 2) return array
    val key = array[array.size - 1]
    var i = 0
    (0 until array.size - 1).forEach { j ->
        if (array[j] <= key) {
            val temp = array[i]
            array[i] = array[j]
            array[j] = temp
            i++
        }
    }
    val temp = array[i]
    array[i] = key
    array[array.size - 1] = temp
    return array
}

fun quickSortUpperMedian(array: IntArray): IntArray {
    if (array.size < 2) return array
    val key: Int = if (array.size % 2 == 0 && array.size > 2)
        upperMedianFour(array[0], array[array.size / 2 + 1], array[array.size / 2], array[array.size - 1])
    else medianOfThree(array[0], array[array.size / 2], array[array.size - 1])

    val less = array.filter { it < key }.toIntArray()
    val equal = array.filter { it == key }.toIntArray()
    val greater = array.filter { it > key }.toIntArray()
    return quickSortUpperMedian(less) + equal + quickSortUpperMedian(greater)
}

fun upperMedianFour(i: Int, i1: Int, i2: Int, i3: Int): Int {
    val array = arrayOf(i, i1, i2, i3)
    val sorted = array.sorted()
    return sorted[2]
}


fun printTime() {
    val size = 1000000
    val seed = 10000
    val gaussArray = boxMullerRandomArray(size, seed)

    val start = System.currentTimeMillis()
    quickSortMiddleKey(gaussArray)
    val end = System.currentTimeMillis()
    println("Middle key sort time: ${end - start} ms")

    val start2 = System.currentTimeMillis()
    quickSortSecondKey(gaussArray)
    val end2 = System.currentTimeMillis()
    println("Second key sort time: ${end2 - start2} ms")

    val start4 = System.currentTimeMillis()
    quickSortMedianOfThree(gaussArray)
    val end4 = System.currentTimeMillis()
    println("Median of three sort time: ${end4 - start4} ms")

    val start6 = System.currentTimeMillis()
    quickSortRandomKey(gaussArray)
    val end6 = System.currentTimeMillis()
    println("Random key sort time: ${end6 - start6} ms")

    val start12 = System.currentTimeMillis()
    quickSortUpperMedian(gaussArray)
    val end12 = System.currentTimeMillis()
    println("Upper median sort time: ${end12 - start12} ms")

    val start8 = System.currentTimeMillis()
    quickSortHoare(gaussArray)
    val end8 = System.currentTimeMillis()
    println("Hoare sort time: ${end8 - start8} ms")

    val start10 = System.currentTimeMillis()
    quickSortLomuto(gaussArray)
    val end10 = System.currentTimeMillis()
    println("Lomuto array sort time: ${end10 - start10} ms")
}




