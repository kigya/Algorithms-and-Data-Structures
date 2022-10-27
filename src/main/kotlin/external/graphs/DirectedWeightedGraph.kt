package external.graphs

import external.datastructures.Queue
import java.util.*

class DirectedWeightedGraph(public override val verticies: Int): Graph {
    public override var edges: Int = 0
    private val adj: Array<Queue<Edge>> = Array(verticies) { Queue() }
    private val indegree: IntArray = IntArray(verticies)

    public class Edge(public val from: Int, public val to: Int, public val weight: Double)

    public fun addEdge(from: Int, to: Int, weight: Double) {
        val edge = Edge(from, to, weight)
        adj[from].add(edge)
        indegree[to]++
        edges++
    }

    public fun edges(): Collection<Edge> {
        val stack = Stack<Edge>()
        adj.flatMap { it }.forEach { stack.push(it) }
        return stack
    }

    public fun adjacentEdges(from: Int): Collection<Edge> {
        return adj[from]
    }

    public override fun adjacentVertices(from: Int): Collection<Int> {
        return adjacentEdges(from).map { it.to }
    }

    public fun outdegree(v: Int): Int {
        return adj[v].size
    }
}