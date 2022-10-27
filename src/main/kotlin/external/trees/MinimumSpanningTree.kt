package external.trees

import external.graphs.UndirectedWeightedGraph

interface MinimumSpanningTree {

    fun edges(): Iterable<UndirectedWeightedGraph.Edge>

    fun weight(): Double

    fun print() {
        println("Edge\tWeight")
        for (e in edges()) {
            println("${e.v} - ${e.w}\t${e.weight}")
        }
        println("Weight: ${weight()}")
    }

}