package external

import external.datastructures.IndexedPriorityQueue
import external.datastructures.Queue
import external.graphs.UndirectedWeightedGraph
import external.trees.MinimumSpanningTree

class Prim(G: UndirectedWeightedGraph): MinimumSpanningTree {
    private var weight: Double = 0.0
    private var edges: Queue<UndirectedWeightedGraph.Edge> = Queue()

    private val distTo: DoubleArray = DoubleArray(G.verticies) { Double.POSITIVE_INFINITY }

    private val edgeTo: Array<UndirectedWeightedGraph.Edge?> = arrayOfNulls(G.verticies)

    private val pq: IndexedPriorityQueue<Double> = IndexedPriorityQueue(G.verticies)

    private val visited = Array(G.verticies) { false }

    init {
        for (s in G.vertices()) {
            if (!visited[s]) {
                distTo[s] = 0.0
                pq.insert(s, 0.0)
                while (!pq.isEmpty()) {
                    val v = pq.poll().first
                    visited[v] = true
                    for (e in G.adjacentEdges(v)) {
                        scan(e, v)
                    }
                }
            }
        }

        for (v in edgeTo.indices) {
            val e = edgeTo[v]
            if (e != null) {
                edges.add(e)
                weight += e.weight
            }
        }
    }

    private fun scan(e: UndirectedWeightedGraph.Edge, v: Int) {
        val w = e.other(v)
        if (!visited[w]) {
            if (e.weight < distTo[w]) {
                distTo[w] = e.weight
                edgeTo[w] = e
                if (pq.contains(w)) {
                    pq.decreaseKey(w, distTo[w])
                } else {
                    pq.insert(w, distTo[w])
                }
            }
        }
    }

    override fun edges(): Iterable<UndirectedWeightedGraph.Edge> {
        return edges
    }

    override fun weight(): Double {
        return weight
    }

}

fun main() {
    val G = UndirectedWeightedGraph(8)
    G.addEdge(4, 5, 0.35)
    G.addEdge(4, 7, 0.37)
    G.addEdge(5, 7, 0.28)
    G.addEdge(0, 7, 0.16)
    G.addEdge(1, 5, 0.32)
    G.addEdge(0, 4, 0.38)
    G.addEdge(2, 3, 0.17)
    G.addEdge(1, 7, 0.19)
    G.addEdge(0, 2, 0.26)
    G.addEdge(1, 2, 0.36)
    G.addEdge(1, 3, 0.29)
    G.addEdge(2, 7, 0.34)
    G.addEdge(6, 2, 0.40)
    G.addEdge(3, 6, 0.52)
    G.addEdge(6, 0, 0.58)
    G.addEdge(6, 4, 0.93)
    G.print()
    println()

    val mst = Prim(G)
    mst.print()
}