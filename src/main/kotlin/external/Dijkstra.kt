package external

import external.datastructures.IndexedPriorityQueue
import external.datastructures.Stack
import external.exceptions.NoSuchPathException
import external.graphs.DirectedWeightedGraph

class Dijkstra(graph: DirectedWeightedGraph, private val from: Int) {

    private val distTo: DoubleArray = DoubleArray(graph.verticies) { if (it == from) 0.0 else Double.POSITIVE_INFINITY }

    private val edgeTo: Array<DirectedWeightedGraph.Edge?> = arrayOfNulls(graph.verticies)

    private val pq: IndexedPriorityQueue<Double> = IndexedPriorityQueue(graph.verticies)

    init {

        if (graph.edges().any { it.weight < 0 }) {
            throw IllegalArgumentException("there is a negative weight edge")
        }

        pq.insert(from, distTo[from])
        while (!pq.isEmpty()) {
            val v = pq.poll().first
            for (e in graph.adjacentEdges(v)) {
                relax(e)
            }
        }
    }

    private fun relax(e: DirectedWeightedGraph.Edge) {
        val v = e.from
        val w = e.to
        if (distTo[w] > distTo[v] + e.weight) {
            distTo[w] = distTo[v] + e.weight
            edgeTo[w] = e
            if (pq.contains(w)) {
                pq.decreaseKey(w, distTo[w])
            } else {
                pq.insert(w, distTo[w])
            }
        }
    }

    fun distTo(v: Int): Double {
        return distTo[v]
    }

    private fun hasPathTo(v: Int): Boolean {
        return distTo[v] < java.lang.Double.POSITIVE_INFINITY
    }

    fun pathTo(v: Int): Iterable<DirectedWeightedGraph.Edge> {
        if (!hasPathTo(v)) throw NoSuchPathException("There is no path from [$from] to [$v]")
        val path = Stack<DirectedWeightedGraph.Edge>()
        var e = edgeTo[v]
        while (e != null) {
            path.push(e)
            e = edgeTo[e.from]
        }
        return path
    }

    fun show() {
        for (v in edgeTo.indices) {
            if (edgeTo[v] != null) {
                val e = edgeTo[v]
                println("$v: ${e!!.from} -> $v (${e.weight})")
            }
        }
    }

    fun showDistTo() {
        for (v in distTo.indices) println("$v: ${distTo[v]}")
    }

}

fun main() {
    val graph = DirectedWeightedGraph(8)
    graph.addEdge(0, 1, -5.0)
    graph.addEdge(0, 4, 9.0)
    graph.addEdge(0, 7, 8.0)
    graph.addEdge(1, 2, 12.0)
    graph.addEdge(1, 3, 15.0)
    graph.addEdge(1, 7, 4.0)
    graph.addEdge(2, 3, 3.0)
    graph.addEdge(2, 6, 11.0)
    graph.addEdge(3, 6, 9.0)
    graph.addEdge(4, 5, -4.0)
    graph.addEdge(4, 6, 20.0)
    graph.addEdge(4, 7, 5.0)
    graph.addEdge(5, 2, -1.0)
    graph.addEdge(5, 6, 13.0)
    graph.addEdge(7, 5, -6.0)
    graph.addEdge(7, 2, 7.0)

    val dijkstra = Dijkstra(graph, 0)
    println(dijkstra.distTo(6))
    println(dijkstra.pathTo(6).joinToString(" -> ") { "(${it.from}, ${it.to})" })

    println("Steps: ")
    dijkstra.show()

    println("Distance to every vertex: ")
    dijkstra.showDistTo()
}