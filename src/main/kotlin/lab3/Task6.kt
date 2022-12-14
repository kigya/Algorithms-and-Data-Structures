package lab3

import java.io.File
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JScrollPane
import javax.swing.JTable

class FordFulkerson() {
    private var top = -1
    var size = 10000
    var s = IntArray(size)

    class Graph(var applicants: Int, var jobs: Int) {
        var adjMatrix: Array<IntArray> = Array(applicants) { IntArray(jobs) }
        fun canDoJob(applicant: Int, job: Int) {
            adjMatrix[applicant][job] = 1
        }
    }

    private fun push(element: Int) {
        if (top != size - 1) s[++top] = element
    }

    fun maxMatching(graph: Graph): Int {
        val applicants = graph.applicants
        val jobs = graph.jobs
        val assign = IntArray(jobs + 2)
        var i = 0
        while (i < jobs) {
            assign[i] = -1
            i++
        }
        var jobCount = 0
        for (applicant in 0 until applicants) {
            val visited = BooleanArray(jobs)
            if (bfs(graph, applicant, visited, assign)) jobCount++
        }
        return jobCount
    }

    private fun bfs(graph: Graph, applicant: Int, visited: BooleanArray, assign: IntArray): Boolean {
        (0 until graph.jobs).forEach { job ->
            if (graph.adjMatrix[applicant][job] == 1 && !visited[job]) {
                visited[job] = true
                val assignedApplicant = assign[job]
                if (assignedApplicant < 0 || bfs(graph, assignedApplicant, visited, assign)) {
                    assign[job] = applicant
                    push(job)
                    return true
                }
            }
        }
        return false
    }
}

fun main() {
    val (names, companies) = getDataFromFile()
    val graph: FordFulkerson.Graph = FordFulkerson.Graph(names.size, companies.size)
    graph.generateRandomCanDoJob()
    val fordFulkerson = FordFulkerson()
    fordFulkerson.maxMatching(graph)
    val data = Array(companies.size) { arrayOfNulls<String>(companies.size) }
    companies.indices.forEach { i ->
        (0..1).forEach { j ->
            if (j == 0) data[i][j] = names[i] else data[i][j] = companies[fordFulkerson.s[i + 2]]
        }
    }
    setupFrame(data)
}

private fun setupFrame(data: Array<Array<String?>>) {
    val jFrame = JFrame("Ford-Fulkerson")
    val namelabel = JLabel("Best matches")
    namelabel.setBounds(220, 30, 400, 30)
    val column = arrayOf("Applicants", "Companies")
    val jTable = JTable(data, column)
    val jScrollPane = JScrollPane(jTable)
    jScrollPane.setBounds(30, 100, 500, 700)
    jFrame.add(jScrollPane)
    jFrame.add(namelabel)
    jFrame.setSize(600, 1000)
    jFrame.layout = null
    jFrame.isVisible = true
    jFrame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
}

private fun getDataFromFile(): Pair<List<String>, List<String>> {
    val names = parseFile("src/main/kotlin/lab3/names.txt").shuffled().take(1000)
    val companies = parseFile("src/main/kotlin/lab3/companies.txt").shuffled().take(1000)
    return Pair(names, companies)
}

fun parseFile(path: String): List<String> {
    val names = mutableListOf<String>()
    val file = File(path)
    file.forEachLine {
        names.add(it)
    }
    return names
}

fun FordFulkerson.Graph.generateRandomCanDoJob() {
    for (i in 0 until applicants) {
        for (j in 0 until jobs) {
            if (Math.random() < 0.2) {
                canDoJob(i, j)
            }
        }
    }
}