package lab3

import java.util.*

fun main() {
    val verticies = List(100) { it + 1 }
    val graph = Graph(verticies)
    initRandomEdges(verticies, graph)
    graph.printGraph()
    println("Verticies amount: " + graph.getNumberOfVertices())
    println("Edges amount: " + graph.getNumberOfEdges())
    println("Vertices: " + graph.getVertices())
    println("Edges: " + graph.getEdges())
    println("Connectivity components: " + graph.getConnectivityComponents())
    println("Is Eulerian: " + graph.isEulerian())
    if (graph.isEulerian()) {
        println("Eulerian cycle: " + graph.findEulerianCycle())
    }
    println("Is Bipartite: " + graph.isBipartite())
    if (graph.isBipartite()) {
        println("Fractions: " + graph.findFractions())
    }

    println()
    eulerCycleGuaranteed()
}

private fun eulerCycleGuaranteed() {
    val verticies2 = List(5) { it + 1 }
    val graph2 = Graph(verticies2)
    graph2.addEdge(1, 2)
    graph2.addEdge(2, 3)
    graph2.addEdge(3, 4)
    graph2.addEdge(4, 5)
    graph2.addEdge(5, 1)
    graph2.addEdge(1, 3)
    graph2.addEdge(3, 5)
    graph2.addEdge(5, 2)
    graph2.addEdge(2, 4)
    graph2.addEdge(4, 1)
    graph2.printGraph()
    println("Is Eulerian: " + graph2.isEulerian())
    if (graph2.isEulerian()) {
        println("Eulerian cycle: " + graph2.findEulerianCycle())
    }
}

private fun initRandomEdges(randomVerticies: List<Int>, graph: Graph<Int>) {
    for (i in 0..100) {
        val vertex1 = randomVerticies.random()
        val vertex2 = randomVerticies.random()
        graph.addEdge(vertex1, vertex2)
    }
}

class Graph<T>(private val vertices: List<T>) {
    private val adjacencyList = vertices.associateWith { mutableListOf<T>() }.toMutableMap()
    private val edges = mutableListOf<Pair<T, T>>()

    fun addEdge(from: T, to: T) {
        if (from !in adjacencyList || to !in adjacencyList) {
            throw IllegalArgumentException("Vertex not found")
        }
        adjacencyList[from]!!.add(to)
        edges.add(from to to)
    }

    fun removeEdge(from: T, to: T) {
        if (from !in adjacencyList || to !in adjacencyList) {
            throw IllegalArgumentException("Vertex not found")
        }
        adjacencyList[from]!!.remove(to)
        edges.remove(from to to)
    }

    fun printGraph() {
        for (vertex in adjacencyList.keys) {
            print("$vertex -> ")
            for (adjacentVertex in adjacencyList[vertex]!!) {
                print("$adjacentVertex ")
            }
            println()
        }
    }

    fun getVertices() = vertices

    fun getEdges() = edges

    private fun getAdjacentVertices(vertex: T) = adjacencyList[vertex]!!

    private fun getInboundVertices(vertex: T) = adjacencyList.filter { it.value.contains(vertex) }.keys.toList()

    private fun getOutboundVertices(vertex: T) = adjacencyList[vertex]!!

    fun getNumberOfVertices() = vertices.size

    fun getNumberOfEdges() = edges.size

    private fun getNumberOfInboundEdges(vertex: T) = getInboundVertices(vertex).size

    private fun getNumberOfOutboundEdges(vertex: T) = getOutboundVertices(vertex).size

    private fun getNumberOfAdjacentEdges(vertex: T) = getAdjacentVertices(vertex).size

    fun getVerticesByDegree(): List<T> {
        return adjacencyList.keys.sortedByDescending { getNumberOfInboundEdges(it) + getNumberOfOutboundEdges(it) }
    }

    fun getVerticesByInboundDegree(): List<T> {
        return adjacencyList.keys.sortedByDescending { getNumberOfInboundEdges(it) }
    }

    fun getVerticesByOutboundDegree(): List<T> {
        return adjacencyList.keys.sortedByDescending { getNumberOfOutboundEdges(it) }
    }

    fun getVerticesByAdjacentDegree(): List<T> {
        return adjacencyList.keys.sortedByDescending { getNumberOfAdjacentEdges(it) }
    }

    fun getConnectivityComponents(): List<List<T>> {
        val components = mutableListOf<List<T>>()
        val visited = mutableListOf<T>()
        for (vertex in adjacencyList.keys) {
            if (vertex !in visited) {
                components.add(getConnectedComponent(vertex, visited))
            }
        }
        return components
    }

    private fun getConnectedComponent(vertex: T, visited: MutableList<T>): List<T> {
        visited.add(vertex)
        val connectedComponent = mutableListOf(vertex)
        for (adjacentVertex in getAdjacentVertices(vertex)) {
            if (adjacentVertex !in visited) {
                connectedComponent.addAll(getConnectedComponent(adjacentVertex, visited))
            }
        }
        return connectedComponent
    }

    fun isEulerian(): Boolean {
        return getConnectivityComponents().size == 1 && getVertices().all {
            getNumberOfInboundEdges(it) == getNumberOfOutboundEdges(
                it
            )
        }
    }

    fun findEulerianCycle(): List<T> {
        val cycle = mutableListOf<T>()
        val edges = edges.toMutableList()
        var vertex = vertices.random()
        cycle.add(vertex)
        while (edges.isNotEmpty()) {
            val nextVertex = edges.find { it.first == vertex }?.second
            if (nextVertex != null) {
                cycle.add(nextVertex)
                edges.remove(vertex to nextVertex)
                vertex = nextVertex
            } else {
                val prevVertex = edges.find { it.second == vertex }?.first
                if (prevVertex != null) {
                    cycle.add(prevVertex)
                    edges.remove(prevVertex to vertex)
                    vertex = prevVertex
                }
            }
        }
        return cycle
    }

    fun isBipartite(): Boolean {
        val visited = mutableMapOf<T, Boolean>()
        val queue = LinkedList<T>()
        for (vertex in adjacencyList.keys) if (vertex !in visited) {
            queue.add(vertex)
            visited[vertex] = true
            while (queue.isNotEmpty()) {
                val currentVertex = queue.poll()
                getAdjacentVertices(currentVertex).forEach { adjacentVertex ->
                    if (adjacentVertex !in visited) {
                        queue.add(adjacentVertex)
                        visited[adjacentVertex] = !visited[currentVertex]!!
                    } else if (visited[adjacentVertex] == visited[currentVertex]) {
                        return false
                    }
                }
            }
        }
        return true
    }

    fun findFractions(): List<List<T>> {
        val fractions = mutableListOf<List<T>>()
        val visited = mutableMapOf<T, Boolean>()
        val queue = LinkedList<T>()
        for (vertex in adjacencyList.keys) {
            if (vertex !in visited) {
                queue.add(vertex)
                visited[vertex] = true
                val fraction = mutableListOf(vertex)
                while (queue.isNotEmpty()) {
                    val currentVertex = queue.poll()
                    for (adjacentVertex in getAdjacentVertices(currentVertex)) {
                        if (adjacentVertex !in visited) {
                            queue.add(adjacentVertex)
                            visited[adjacentVertex] = !visited[currentVertex]!!
                            fraction.add(adjacentVertex)
                        }
                    }
                }
                fractions.add(fraction)
            }
        }
        return fractions
    }
}


