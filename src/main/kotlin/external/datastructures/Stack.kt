package external.datastructures

import java.util.*


class Stack<T> : Collection<T> {
    private var head: Node<T>? = null
    public override var size = 0
        private set

    private class Node<T>(var value: T) {
        var next: Node<T>? = null
    }

    fun push(item: T) {
        val new = Node(item)
        new.next = head
        head = new
        size++
    }

    fun peek(): T {
        if (isEmpty()) throw NoSuchElementException()
        return head!!.value
    }

    fun poll(): T {
        if (isEmpty()) throw NoSuchElementException()
        val old = head!!
        head = old.next
        size--
        return old.value
    }

    override fun isEmpty(): Boolean {
        return isEmpty()
    }

    override fun contains(element: T): Boolean {
        for (obj in this) if (obj == element) return true
        return false
    }

    override fun containsAll(elements: Collection<T>): Boolean {
        for (element in elements) if (!contains(element)) return false
        return true
    }

    override fun iterator(): Iterator<T> = object : Iterator<T> {
        var node = head

        override fun hasNext(): Boolean = node != null

        override fun next(): T {
            if (!hasNext()) throw NoSuchElementException()
            val current = node!!
            node = current.next
            return current.value
        }
    }
}