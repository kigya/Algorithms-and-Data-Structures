package external.graphs

import external.datastructures.Queue
import java.util.*


class DirectedUnweightedGraph(override val verticies: Int): Graph {
    override var edges: Int = 0
    private val adj: Array<Queue<Int>> = Array(verticies) { Queue() }
    private val indegree: IntArray = IntArray(verticies)

    fun addEdge(from: Int, to: Int) {
        adj[from].add(to)
        indegree[to]++
        edges++
    }

    override fun adjacentVertices(from: Int): Collection<Int> {
        return adj[from]
    }

    fun outdegree(from: Int): Int {
        return adj[from].size
    }

    fun indegree(v: Int): Int {
        return indegree[v]
    }

    private fun findEulerianCycle(): List<Int> {
        val stack = Stack<Int>()
        val cycle = mutableListOf<Int>()
        stack.push(0)
        while (!stack.isEmpty()) {
            var v = stack.pop()
            while (!adj[v].isEmpty()) {
                stack.push(v)
                v = adj[v].poll()
            }
            cycle.add(v)
        }
        return cycle
    }

    fun printEulerianCycle() {
        val cycle = findEulerianCycle()
        for (i in 0 until cycle.size - 1) {
            print("${cycle[i]}-${cycle[i + 1]} ")
        }
        println()
    }

    fun print() {
        println("digraph {")
        for (v in 0 until verticies) {
            for (w in adj[v]) {
                println("  $v -> $w;")
            }
        }
        println("}")
    }
}
