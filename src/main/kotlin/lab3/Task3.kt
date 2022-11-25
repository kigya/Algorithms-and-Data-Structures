package lab3

import external.datastructures.DisjointSet
import external.datastructures.PriorityQueue
import external.datastructures.Queue
import external.graphs.UndirectedWeightedGraph
import external.trees.MinimumSpanningTree

class Kruskal(G: UndirectedWeightedGraph): MinimumSpanningTree {
    private var weight: Double = 0.0
    private var edges: Queue<UndirectedWeightedGraph.Edge> = Queue()

    init {
        val pq = PriorityQueue<UndirectedWeightedGraph.Edge>(G.verticies, compareBy { it.weight })
        for (v in G.vertices()) {
            for (e in G.adjacentEdges(v)) {
                pq.add(e)
            }
        }

        val set = DisjointSet(G.verticies)
        while (!pq.isEmpty()) {
            val edge = pq.poll()
            if (!set.connected(edge.v, edge.w)) {
                edges.add(edge)
                set.union(edge.v, edge.w)
                weight += edge.weight
            }
        }
    }

    override fun edges(): Iterable<UndirectedWeightedGraph.Edge> = edges

    override fun weight(): Double = weight
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

    val mst = Kruskal(G)
    mst.print()
}