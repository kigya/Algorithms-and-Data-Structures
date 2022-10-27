package external.graphs

import external.datastructures.Queue
import java.util.*

@Suppress("UNUSED_MEMBER")
class DirectedWeightedGraph(override val verticies: Int) : Graph {
    override var edges: Int = 0
    private val adj: Array<Queue<Edge>> = Array(verticies) { Queue() }
    private val indegree: IntArray = IntArray(verticies)

    class Edge(val from: Int, val to: Int, val weight: Double)

    fun addEdge(from: Int, to: Int, weight: Double) {
        val edge = Edge(from, to, weight)
        adj[from].add(edge)
        indegree[to]++
        edges++
    }

    fun edges(): Collection<Edge> {
        val stack = Stack<Edge>()
        adj.flatMap { it }.forEach { stack.push(it) }
        return stack
    }

    fun adjacentEdges(from: Int): Collection<Edge> {
        return adj[from]
    }

    override fun adjacentVertices(from: Int): Collection<Int> {
        return adjacentEdges(from).map { it.to }
    }

    fun outdegree(v: Int): Int {
        return adj[v].size
    }

    fun print() {
        println("$verticies vertices, $edges edges")
        for (v in 0 until verticies) {
            print("$v: ")
            for (e in adj[v]) {
                print("${e.to}(${e.weight}) ")
            }
            println()
        }
    }

    fun hasNegativeCycle(): Boolean {
        val distTo = DoubleArray(verticies) { if (it == 0) 0.0 else Double.POSITIVE_INFINITY }
        val edgeTo = arrayOfNulls<Edge>(verticies)
        val onQueue = BooleanArray(verticies)
        val queue = Queue<Int>()
        queue.add(0)
        onQueue[0] = true
        while (!queue.isEmpty()) {
            val v = queue.poll()
            onQueue[v] = false
            for (e in adj[v]) {
                val w = e.to
                if (distTo[w] > distTo[v] + e.weight) {
                    distTo[w] = distTo[v] + e.weight
                    edgeTo[w] = e
                    if (!onQueue[w]) {
                        queue.add(w)
                        onQueue[w] = true
                    }
                }
            }
        }
        for (v in 0 until verticies) {
            for (e in adj[v]) {
                val w = e.to
                if (distTo[w] > distTo[v] + e.weight) {
                    return true
                }
            }
        }
        return false
    }
}