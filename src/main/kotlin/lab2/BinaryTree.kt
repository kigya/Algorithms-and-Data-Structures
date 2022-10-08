package lab2

private const val TREE_SIZE = 20
private const val MAX_ELEMENT = 100

private class Node(
    private var value: Int,
    private var left: Node? = null,
    private var right: Node? = null
) {
    fun value() = value

    fun changeValue(value: Int) {
        this.value = value
    }

    fun changeLeft(left: Node?) {
        this.left = left
    }

    fun changeRight(right: Node?) {
        this.right = right
    }

    fun leftNode() = left
    fun rightNode() = right
}

private class BinaryTree {

    private var root: Node? = null

    fun addAscending(value: Int) {
        fun addRec(current: Node?, value: Int): Node {
            if (current == null) return Node(value)
            if (value < current.value())
                current.changeLeft(addRec(current.leftNode(), value))
            else if (value > current.value())
                current.changeRight(addRec(current.rightNode(), value))
            return current
        }
        root = addRec(root, value)
    }

    fun addDescending(value: Int) {
        fun addRec(current: Node?, value: Int): Node {
            if (current == null) return Node(value)
            if (value > current.value())
                current.changeLeft(addRec(current.leftNode(), value))
            else if (value < current.value())
                current.changeRight(addRec(current.rightNode(), value))
            return current
        }
        root = addRec(root, value)
    }

    fun isEmpty() = root == null

    fun remove(value: Int) {

        fun smallestValue(root: Node): Int {
            return if (root.leftNode() == null) root.value() else smallestValue(root.leftNode()!!)
        }

        fun removeRec(current: Node?, value: Int): Node? {
            if (current == null) return null

            if (value == current.value()) {
                if (current.leftNode() == null && current.rightNode() == null) return null
                if (current.leftNode() == null) return current.rightNode()
                if (current.rightNode() == null) return current.leftNode()

                val smallestValue = smallestValue(current.rightNode()!!)
                current.changeValue(smallestValue)
                current.changeRight(removeRec(current.rightNode(), smallestValue))
                return current
            }

            if (value < current.value()) current.changeLeft(removeRec(current.leftNode(), value))
            else current.changeRight(removeRec(current.rightNode(), value))

            return current
        }

        root = removeRec(root, value)
    }

    fun contains(value: Int): Boolean {
        fun containsRec(current: Node?, value: Int): Boolean {
            if (current == null) return false
            if (value == current.value()) return true
            return if (value < current.value()) containsRec(current.leftNode(), value)
            else containsRec(current.rightNode(), value)
        }
        return containsRec(root, value)
    }

    fun find(value: Int): Node? {
        fun findRec(current: Node?, value: Int): Node? {
            if (current == null) return null
            if (value == current.value()) return current
            return if (value < current.value()) findRec(current.leftNode(), value)
            else findRec(current.rightNode(), value)
        }
        return findRec(root, value)
    }

    fun traverseInOrder(): List<Int> {
        fun traverseInOrderRec(node: Node?, nodes: MutableList<Int>) {
            if (node != null) {
                traverseInOrderRec(node.leftNode(), nodes)
                nodes.add(node.value())
                traverseInOrderRec(node.rightNode(), nodes)
            }
        }

        return mutableListOf<Int>().apply { traverseInOrderRec(root, this) }
    }

    fun traversePreOrder(): List<Int> {
        fun traversePreOrderRec(node: Node?, nodes: MutableList<Int>) {
            if (node != null) {
                nodes.add(node.value())
                traversePreOrderRec(node.leftNode(), nodes)
                traversePreOrderRec(node.rightNode(), nodes)
            }
        }

        return mutableListOf<Int>().apply { traversePreOrderRec(root, this) }
    }

    fun traversePostOrder(): List<Int> {
        fun traversePostOrderRec(node: Node?, nodes: MutableList<Int>) {
            if (node != null) {
                traversePostOrderRec(node.leftNode(), nodes)
                traversePostOrderRec(node.rightNode(), nodes)
                nodes.add(node.value())
            }
        }

        return mutableListOf<Int>().apply { traversePostOrderRec(root, this) }
    }

    fun traverseLevelOrder(): List<Int> {
        val root = this.root ?: return listOf()

        val queue = java.util.LinkedList<Node>()
        queue.add(root)

        val items = mutableListOf<Int>()

        while (queue.isNotEmpty()) {
            val node = queue.remove()
            items.add(node.value())

            node.leftNode()?.let(queue::add)
            node.rightNode()?.let(queue::add)
        }

        return items
    }

    fun balance() {
        val list = this.traverseInOrder()
        this.root = null
        this.balanceRec(list)
    }

    private fun balanceRec(list: List<Int>) {
        if (list.isEmpty()) return
        val middle = list.size / 2
        this.addAscending(list[middle])
        this.balanceRec(list.subList(0, middle))
        this.balanceRec(list.subList(middle + 1, list.size))
    }

    // find k minumum key in tree
    fun findKMin(k: Int): Int {
        val list = this.traverseInOrder()
        return list[k - 1]
    }

    // check if binary search tree
    fun isBST(): Boolean {
        fun isBSTRec(node: Node?, min: Int, max: Int): Boolean {
            if (node == null) return true
            if (node.value() < min || node.value() > max) return false
            return isBSTRec(node.leftNode(), min, node.value() - 1) && isBSTRec(node.rightNode(), node.value() + 1, max)
        }
        return isBSTRec(root, Int.MIN_VALUE, Int.MAX_VALUE)
    }


    fun print() {
        fun printRec(current: Node?, prefix: String, isLeft: Boolean) {
            if (current != null) {
                printRec(current.rightNode(), "$prefix${if (isLeft) "│   " else "    "}", false)
                println("$prefix${if (isLeft) "└── " else "┌── "}${current.value()}")
                printRec(current.leftNode(), "$prefix${if (isLeft) "    " else "│   "}", true)
            }
        }
        printRec(root, "", true)
    }
}

fun main() {
    val tree = BinaryTree()
    repeat((0 until TREE_SIZE).count()) { tree.addAscending((0..MAX_ELEMENT).random()) }
    tree.print()
    println()
    println("Traverse in order: " + tree.traverseInOrder())
    println("Traverse pre order: " + tree.traversePreOrder())
    println("Traverse post order: " + tree.traversePostOrder())
    println("Traverse level order: " + tree.traverseLevelOrder())
    println("Second min key: " + tree.findKMin(2))
    val valueToSearch = tree.findKMin(4).also { println("Value to search: $it") }
    tree.find(valueToSearch)?.let { println(it.value()) }
    println("Is BST: " + tree.isBST())
    println()
    println("After balancing:")
    tree.balance()
    tree.print()
}

/*
│       ┌── 100
│   ┌── 99
│   │   │   ┌── 97
│   │   └── 83
└── 78
    └── 63
        └── 59
            │           ┌── 52
            │           │   └── 44
            │       ┌── 42
            │       │   └── 40
            │       │       └── 35
            │   ┌── 30
            │   │   └── 24
            └── 12
                │       ┌── 11
                │   ┌── 9
                └── 0

Traverse in order: [0, 9, 11, 12, 24, 30, 35, 40, 42, 44, 52, 59, 63, 78, 83, 97, 99, 100]
Traverse pre order: [78, 63, 59, 12, 0, 9, 11, 30, 24, 42, 40, 35, 52, 44, 99, 83, 97, 100]
Traverse post order: [11, 9, 0, 24, 35, 40, 44, 52, 42, 30, 12, 59, 63, 97, 83, 100, 99, 78]
Traverse level order: [78, 63, 99, 59, 83, 100, 12, 97, 0, 30, 9, 24, 42, 11, 40, 52, 35, 44]
Second min key: 9
Value to search: 12
12
Is BST: true

After balancing:
│           ┌── 100
│       ┌── 99
│       │   └── 97
│   ┌── 83
│   │   │   ┌── 78
│   │   └── 63
│   │       └── 59
│   │           └── 52
└── 44
    │       ┌── 42
    │   ┌── 40
    │   │   └── 35
    │   │       └── 30
    └── 24
        │   ┌── 12
        └── 11
            └── 9
                └── 0
 */