package external.datastructures

import java.util.NoSuchElementException

@Suppress("UNCHECKED_CAST")
class IndexedPriorityQueue<T>(size: Int, private val comparator: Comparator<T>? = null) : Collection<T> {

    private val maxN: Int = size

    public override var size: Int = 0
        private set

    private val binaryHeap: IntArray = IntArray(size + 1)

    private val inversedBinaryHeap: IntArray = IntArray(size + 1) { -1 }

    private val keys: Array<T?> = Array<Comparable<T>?>(size + 1) { null } as Array<T?>

    fun insert(i: Int, key: T) {
        if (i < 0 || i >= maxN) throw IndexOutOfBoundsException()
        if (contains(i)) throw IllegalArgumentException("index is already in the priority queue")
        size++
        inversedBinaryHeap[i] = size
        binaryHeap[size] = i
        keys[i] = key
        swim(size)
    }

    fun decreaseKey(i: Int, key: T) {
        if (i < 0 || i >= maxN) throw IndexOutOfBoundsException()
        if (!contains(i)) throw NoSuchElementException("index is not in the priority queue")
        if (!greater(keys[i]!!, key)) throw IllegalArgumentException(
            "Calling decreaseKey()" + "with given argument would not strictly decrease the key"
        )
        keys[i] = key
        swim(inversedBinaryHeap[i])
    }

    fun increaseKey(i: Int, key: T) {
        if (i < 0 || i >= maxN) throw IndexOutOfBoundsException()
        if (!contains(i)) throw NoSuchElementException("index is not in the priority queue")
        if (!less(keys[i]!!, key)) throw IllegalArgumentException(
            "Calling increaseKey()" + "with given argument would not strictly increase the key"
        )
        keys[i] = key
        sink(inversedBinaryHeap[i])
    }

    fun peek(): Pair<Int, T> {
        if (isEmpty()) throw NoSuchElementException("Priority queue underflow")
        return Pair(binaryHeap[1], keys[binaryHeap[1]]!!)
    }

    fun poll(): Pair<Int, T> {
        if (isEmpty()) throw NoSuchElementException("Priority queue underflow")
        val min = binaryHeap[1]
        val element = keys[min]
        exch(1, size--)
        sink(1)
        assert(min == binaryHeap[size + 1])
        inversedBinaryHeap[min] = -1
        keys[min] = null
        binaryHeap[size + 1] = -1
        return Pair(min, element!!)
    }

    private fun less(x: T, y: T): Boolean {
        return if (comparator != null) comparator.compare(x, y) < 0 else {
            val left = x as Comparable<T>
            left < y
        }
    }

    private fun greater(x: T, y: T): Boolean {
        return if (comparator != null) comparator.compare(x, y) > 0 else {
            val left = x as Comparable<T>
            left > y
        }
    }

    private fun greater(i: Int, j: Int): Boolean {
        return greater(keys[binaryHeap[i]]!!, keys[binaryHeap[j]]!!)
    }

    private fun exch(i: Int, j: Int) {
        val swap = binaryHeap[i]
        binaryHeap[i] = binaryHeap[j]
        binaryHeap[j] = swap
        inversedBinaryHeap[binaryHeap[i]] = i
        inversedBinaryHeap[binaryHeap[j]] = j
    }

    private fun swim(n: Int) {
        var k = n
        while (k > 1 && greater(k / 2, k)) {
            exch(k, k / 2)
            k /= 2
        }
    }

    private fun sink(n: Int) {
        var k = n
        while (2 * k <= size) {
            var j = 2 * k
            if (j < size && greater(j, j + 1)) j++
            if (!greater(k, j)) break
            exch(k, j)
            k = j
        }
    }

    fun contains(i: Int): Boolean {
        if (i < 0 || i >= maxN) throw IndexOutOfBoundsException()
        return inversedBinaryHeap[i] != -1
    }

    override fun isEmpty(): Boolean = size == 0

    override fun contains(element: T): Boolean {
        for (obj in this) {
            if (obj == element) return true
        }
        return false
    }

    override fun containsAll(elements: Collection<T>): Boolean {
        for (element in elements) if (!contains(element)) return false
        return true
    }

    override fun iterator(): Iterator<T> = keys.copyOfRange(1, size + 1).map { it!! }.iterator()
}
