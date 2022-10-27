package external

import external.graphs.DirectedUnweightedGraph

fun main() {
    val graph = DirectedUnweightedGraph(8)
    graph.addEdge(0, 1)
    graph.addEdge(1, 2)
    graph.addEdge(2, 3)
    graph.addEdge(3, 0)
    graph.addEdge(3, 4)
    graph.addEdge(4, 5)
    graph.addEdge(5, 6)
    graph.addEdge(6, 7)
    graph.addEdge(7, 4)
    graph.addEdge(7, 3)

    graph.print()
    println("Eulerian cycle: ")
    graph.printEulerianCycle()
}