package lab2

@Suppress("UNCHECKED_CAST")
sealed class RedBlackTree<E : Comparable<E>> {

    data class Tree<E : Comparable<E>>(
        val color: Color,
        val left: RedBlackTree<E>,
        val element: E,
        val right: RedBlackTree<E>
    ) : RedBlackTree<E>() {
        fun balance(): Tree<E> {
            fun buildBalancedTree(
                leftLeft: RedBlackTree<E>, leftElement: E, leftRight: RedBlackTree<E>,
                midElement: E,
                rightLeft: RedBlackTree<E>, rightElement: E, rightRight: RedBlackTree<E>
            ) = Tree(
                color = Color.R, left = Tree(Color.B, leftLeft, leftElement, leftRight),
                element = midElement,
                right = Tree(Color.B, rightLeft, rightElement, rightRight)
            )

            if (isBlack()) {
                if (left is Tree<E> && isRed(left)) {
                    if (left.left is Tree<E> && isRed(left.left)) {
                        return buildBalancedTree(
                            leftLeft = left.left.left, leftElement = left.left.element, leftRight = left.left.right,
                            midElement = left.element,
                            rightLeft = left.right, rightElement = this.element, rightRight = this.right
                        )
                    } else if (left.right is Tree<E> && isRed(left.right)) {
                        return buildBalancedTree(
                            leftLeft = left.left, leftElement = left.element, leftRight = left.right.left,
                            midElement = left.right.element,
                            rightLeft = left.right.right, rightElement = this.element, rightRight = this.right
                        )
                    }
                }

                if (right is Tree<E> && isRed(right)) {
                    if (right.left is Tree<E> && isRed(right.left)) {
                        return buildBalancedTree(
                            leftLeft = this.left, leftElement = this.element, leftRight = right.left.left,
                            midElement = right.left.element,
                            rightLeft = right.left.right, rightElement = right.element, rightRight = right.right
                        )
                    } else if (right.right is Tree<E> && isRed(right.right)) {
                        return buildBalancedTree(
                            leftLeft = this.left,
                            leftElement = this.element,
                            leftRight = right.left,
                            midElement = right.element,
                            rightLeft = right.right.left,
                            rightElement = right.right.element,
                            rightRight = right.right.right
                        )
                    }
                }
            }

            return this
        }

        private fun isRed(left: Tree<E>) = left.color == Color.R

        private fun isBlack() = color == Color.B
    }

    fun contains(element: E): Boolean = when (this) {
        Empty -> false
        is Tree -> when {
            element < this.element -> left.contains(element)
            element > this.element -> right.contains(element)
            else -> true
        }
    }

    fun insert(element: E): Tree<E> {
        fun insertInto(tree: RedBlackTree<E>): Tree<E> =
            when (tree) {
                Empty -> Tree(Color.R, tree, element, tree)
                is Tree -> when {
                    element < tree.element -> tree.copy(left = insertInto(tree.left)).balance()
                    element > tree.element -> tree.copy(right = insertInto(tree.right)).balance()
                    else -> tree
                }
            }

        return insertInto(this).copy(color = Color.B)
    }

    // remove element from tree
    fun remove(element: E): RedBlackTree<E> {
        fun removeElement(tree: RedBlackTree<E>): RedBlackTree<out E> {
            return when (tree) {
                Empty -> Empty
                is Tree -> when {
                    element < tree.element -> tree.copy(left = removeElement(tree.left) as RedBlackTree<E>)
                    element > tree.element -> tree.copy(right = removeElement(tree.right) as RedBlackTree<E>)
                    else -> {
                        fun removeRoot(tree: Tree<E>): RedBlackTree<out E> =
                            when {
                                tree.left is Empty && tree.right is Empty -> Empty
                                tree.left is Empty -> tree.right
                                tree.right is Empty -> tree.left
                                else -> {
                                    val (min, newRight) = removeMin(tree.right)
                                    tree.copy(element = min, right = newRight)
                                }
                            }

                        removeRoot(tree)
                    }
                }
            }
        }

        return removeElement(this) as RedBlackTree<E>
    }

    private fun removeMin(tree: RedBlackTree<E>): Pair<E, RedBlackTree<E>> {
        fun removeMinFromTree(tree: Tree<E>): Pair<E, RedBlackTree<E>> =
            when (tree.left) {
                Empty -> tree.element to tree.right
                is Tree -> {
                    val (min, newLeft) = removeMinFromTree(tree.left)
                    min to tree.copy(left = newLeft)
                }
            }

        return when (tree) {
            Empty -> throw IllegalArgumentException("Tree is empty")
            is Tree -> removeMinFromTree(tree)
        }
    }

    fun print() {
        fun printTree(tree: RedBlackTree<E>, level: Int = 0) {
            if (tree is Tree) {
                printTree(tree.right, level + 1)
                println(" ".repeat(level * 4) + tree.color + " " + tree.element)
                printTree(tree.left, level + 1)
            }
        }

        printTree(this)
    }

    enum class Color { R, B }

    object Empty : RedBlackTree<Nothing>()

    companion object {
        fun <T : Comparable<T>> emptyTree(): RedBlackTree<T> = Empty as RedBlackTree<T>
    }

}

fun main() {
    val tree = RedBlackTree.emptyTree<Int>()
        .insert(3)
        .insert(2)
        .insert(5)
        .insert(6)
        .insert(9)
        .insert(4)
        .insert(7)
        .insert(8)
    tree.print()
    println("Is contains 9: ${tree.contains(9)}")

    println()
    println("After removing:")
    tree.remove(9).remove(7).remove(6).print()

    println()
    println()
    println("Building string tree: ")
    stringTree()
}

/*
        B 9
            R 8
    R 7
        B 6
B 5
        R 4
    B 3
        R 2

After removing:
    R 8
B 5
        R 4
    B 3
        R 2

 */

fun stringTree() {
    val tree = RedBlackTree.emptyTree<String>()
        .insert("Java")
        .insert("Kotlin")
        .insert("Python")
        .insert("Scala")
        .insert("JavaScript")
        .insert("C++")
        .insert("Groovy")
        .insert("Ruby")
        .insert("Swift")
    tree.print()
    println("Is contains 'Kotlin': ${tree.contains("Kotlin")}")

    println()
    println("After removing:")
    tree.remove("JavaScript").remove("Python").remove("C++").print()
}

/*
Building string tree:
            R Swift
        B Scala
    R Ruby
        B Python
B Kotlin
            R JavaScript
        B Java
    R Groovy
        B C++
Is contains 'Kotlin': true

After removing:
            R Swift
        B Scala
    R Ruby
B Kotlin
        B Java
    R Groovy

 */