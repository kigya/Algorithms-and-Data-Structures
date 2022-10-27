package external.graphs

import external.datastructures.Queue

class UndirectedWeightedGraph(override val verticies: Int): Graph {
    override var edges: Int = 0
    private val adj: Array<Queue<Edge>> = Array(verticies) { Queue() }

    class Edge(val v: Int, val w: Int, val weight: Double): Comparable<Edge> {
        override fun compareTo(other: Edge): Int {
            return this.weight.compareTo(other.weight)
        }

        fun other(s: Int): Int {
            if (s == v) return w
            if (s == w) return v
            throw IllegalArgumentException()
        }
    }

    fun addEdge(v: Int, w: Int, weight: Double) {
        val edge = Edge(v, w, weight)
        adj[v].add(edge)
        adj[w].add(edge)
        edges++
    }

    fun adjacentEdges(v: Int): Collection<Edge> {
        return adj[v]
    }

    override fun adjacentVertices(v: Int): Collection<Int> {
        return adjacentEdges(v).map { it.other(v) }
    }

    fun degree(v: Int): Int {
        return adj[v].size
    }

    fun edges(): Collection<Edge> {
        return adj.flatMap { it }
    }

    fun print() {
        println("$verticies vertices, $edges edges")
        for (v in 0 until verticies) {
            print("$v: ")
            for (e in adj[v]) {
                print("${e.other(v)}(${e.weight}) ")
            }
            println()
        }
    }
}
