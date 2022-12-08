package nl.bldn.aoc2022.puzzle08

import mu.KLogging
import nl.bldn.aoc2022.puzzle08.Location.*
import javax.ws.rs.Consumes
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@Path("/puzzle/08")
class Puzzle08Resource {
    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    fun submitInput(
        input: String,
    ): Int {
        val treeGrid = input.parseTreeGrid()

        return treeGrid.allTrees.values.count { it.isVisible(treeGrid.allTrees ) }
    }

    @POST
    @Path("/part2")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    fun submitInput2(
        input: String,
    ): Int {
        val treeGrid = input.parseTreeGrid()

        return treeGrid.allTrees.values.maxOf { it.scenicScore(treeGrid.allTrees) }
    }


    private fun String.parseTreeGrid(): TreeGrid {
        val trees = mutableMapOf<String, Tree>()
        val array = this.lines().map { it.toCharArray().toTypedArray() }.toTypedArray()

        for (i: Int in array.indices) {
            for (j: Int in array[i].indices) {
                val currentTreeId = createTreeId(j,i)
                val topTreeId = if (i-1 in array.indices) createTreeId(j, i-1) else null
                val bottomTreeId = if (i+1 in array.indices) createTreeId(j, i+1) else null
                val leftTreeId = if (j-1 in array[i].indices) createTreeId(j-1, i) else null
                val rightTreeId = if (j+1 in array[i].indices) createTreeId(j+1,i) else null
                val allNeighborIds = mapOf(
                    TOP to topTreeId,
                    BOTTOM to bottomTreeId,
                    LEFT to leftTreeId,
                    RIGHT to rightTreeId,
                )

                trees.computeIfAbsent(currentTreeId) {
                    Tree (
                        id = it,
                        xPosition = j,
                        yPosition = i,
                        height = array[i][j].digitToInt(),
                        neighbors = allNeighborIds
                    )
                }
            }
        }

        return TreeGrid(trees)
    }

    private fun createTreeId(x:Int, y:Int) = "x${x}y$y"

    companion object : KLogging() {
    }
}

private data class TreeGrid(
    val allTrees: Map<String, Tree>
)

private data class Tree(
    val id: String,
    val xPosition: Int,
    val yPosition: Int,
    val height: Int,
    val neighbors: Map<Location, String?>,
) {
    fun isVisible(allTrees: Map<String, Tree>): Boolean {
        val isVisibleFromLeft = allTrees.gatherAllTreesToThe(LEFT, neighbors.getValue(LEFT), listOf(this)).map { it.height }.isDecending()
        val isVisibleFromTop = allTrees.gatherAllTreesToThe(TOP, neighbors.getValue(TOP), listOf(this)).map { it.height }.isDecending()
        val isVisibleFromBottom = allTrees.gatherAllTreesToThe(BOTTOM, neighbors.getValue(BOTTOM), listOf(this)).map { it.height }.isDecending()
        val isVisibleFromRight = allTrees.gatherAllTreesToThe(RIGHT, neighbors.getValue(RIGHT), listOf(this)).map { it.height }.isDecending()

        return isVisibleFromLeft || isVisibleFromTop || isVisibleFromBottom || isVisibleFromRight
    }

    fun scenicScore(allTrees: Map<String, Tree>): Int {
        val treesToTheLeft = allTrees.gatherAllTreesToThe(LEFT, neighbors.getValue(LEFT), emptyList()).lineOfSight(height)
        val treesToTheTop = allTrees.gatherAllTreesToThe(TOP, neighbors.getValue(TOP), emptyList()).lineOfSight(height)
        val treesToTheBottom = allTrees.gatherAllTreesToThe(BOTTOM, neighbors.getValue(BOTTOM), emptyList()).lineOfSight(height)
        val treesToTheRight = allTrees.gatherAllTreesToThe(RIGHT, neighbors.getValue(RIGHT), emptyList()).lineOfSight(height)

        val scenicscore = treesToTheLeft * treesToTheTop * treesToTheBottom * treesToTheRight
        println("$id; left $treesToTheLeft; top $treesToTheTop; bottom $treesToTheBottom; right $treesToTheRight; score: $scenicscore")

        return scenicscore
    }

    private fun Map<String, Tree>.gatherAllTreesToThe(location: Location, currentId: String?, found: List<Tree>): List<Tree> =
        if (currentId == null) found
        else {
            val neighbor = this.getValue(currentId)
            this.gatherAllTreesToThe(location, neighbor.neighbors.getValue(location), found + neighbor)
        }

    private fun List<Int>.isDecending(): Boolean =
        if (size == 1) true
        else first() > subList(1, size).maxOf { it }

    private fun List<Tree>.lineOfSight(height: Int): Int =
        withIndex().firstOrNull { it.value.height >= height }?.index?.plus(1) ?: size

}

private enum class Location {
    LEFT,
    RIGHT,
    TOP,
    BOTTOM,
}