package lab2

fun main() {
    val a: Array<String> = initAlphabetArray()
    val htc = HashTableChain<String, Int>(a.size)
    putValues(a, htc)
    htc.print()
    printCollisions(htc)
    isomorphicTest()
    partitionTest()
}

private fun partitionTest() {
    printArrayArray(partition3(intArrayOf(1, 2, 3, 4, 5)))
}

private fun isomorphicTest() {
    println("Check<->Coach: " + isIsomorphic("check", "coach"))
    println("Check<->Spell: " + isIsomorphic("check", "spell"))
}

private fun printCollisions(htc: HashTableChain<String, Int>) {
    print("Collisions: ")
    htc.printCollisions()
}

private fun putValues(a: Array<String>, htc: HashTableChain<String, Int>) {
    a.indices.forEach { i -> htc.put(a[i], i) }
}

private fun initAlphabetArray() = Array(26) { i -> (i + 97).toChar().toString() }

fun isIsomorphic(s: String, t: String): Boolean {
    if (s.length != t.length) return false
    val htc = HashTableChain<Char, Char>(s.length)
    s.indices.forEach { i: Int ->
        if (htc.get(s[i]) == null) htc.put(s[i], t[i])
        else if (htc.get(s[i]) != t[i]) return false
    }
    return true
}

fun partition3(a: IntArray): Array<Array<Int>> {
    val sum = a.sum()
    if (sum % 3 != 0) return emptyArray()
    val htc = HashTableChain<Int, Int>(a.size)
    a.forEach { htc.put(it, 1) }
    val subsets = mutableListOf<Array<Int>>()
    val subset = mutableListOf<Int>()
    fun partition3Rec(sum: Int, i: Int) {
        if (sum == 0) {
            subsets.add(subset.toTypedArray())
            return
        }
        if (sum < 0) return
        for (j in i until a.size) {
            if (htc.get(a[j]) == 1) {
                htc.put(a[j], 0)
                subset.add(a[j])
                partition3Rec(sum - a[j], j + 1)
                subset.removeAt(subset.size - 1)
                htc.put(a[j], 1)
            }
        }
    }
    partition3Rec(sum / 3, 0)
    return subsets.toTypedArray()
}

fun printArrayArray(a: Array<Array<Int>>) {
    a.forEach { println(it.contentToString()) }
}

// worst: O(n)
// average: O(n)
// best: O(n)
class HashTableChain<K, V>(private val size: Int = 100) {
    private val array: Array<MutableList<Pair<K, V>>> = Array(size) { mutableListOf() }

    fun put(key: K, value: V) {
        val index = getIndex(key)
        val list = array[index]
        val pair = list.find { it.first == key }
        if (pair != null) {
            list.remove(pair)
        }
        list.add(key to value)
    }

    fun get(key: K): V? {
        val index = getIndex(key)
        val list = array[index]
        return list.find { it.first == key }?.second
    }

    fun remove(key: K) {
        val index = getIndex(key)
        val list = array[index]
        val pair = list.find { it.first == key }
        if (pair != null) {
            list.remove(pair)
        }
    }

    private fun getIndex(key: K): Int {
        val hash = key.hashCode()
        return (hash * 0.6180339887 % 1 * size).toInt()
    }

    fun print() {
        for (i in array.indices) {
            println("$i: ${array[i]}")
        }
    }

    fun printSize() {
        println(array.sumOf { it.size })
    }

    fun printCollisions() {
        println(array.count { it.size > 1 })
    }

    fun printMaxCollisions() {
        println(array.maxOf { it.size })
    }

    fun printAverageCollisions() {
        println(array.sumOf { it.size } / array.size.toDouble())
    }

    fun printLoadFactor() {
        println(array.sumOf { it.size } / array.size.toDouble())
    }

    fun printEmptyCells() {
        println(array.count { it.isEmpty() })
    }
}


