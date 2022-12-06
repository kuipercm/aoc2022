package nl.bldn.aoc2022.puzzle03

import javax.ws.rs.*
import javax.ws.rs.core.MediaType.APPLICATION_JSON
import javax.ws.rs.core.MediaType.TEXT_PLAIN

@Path("/puzzle/03")
class Puzzle03Resource {
    @POST
    @Consumes(TEXT_PLAIN)
    @Produces(APPLICATION_JSON)
    fun submitInput(
        input: String,
    ): Int {
        val rucksacks = input.convertToPuzzleInput()

        return rucksacks.map { it.findSingleCommon() }.sumOf { it.asPriority() }
        // Part 1: 8298
        
    }

    private fun String.convertToPuzzleInput(): List<RuckSack> {
        return this.lines().map {
            val first = it.take(it.length / 2)
            val second = it.takeLast(it.length / 2)
            RuckSack(first.toList(), second.toList())
        }
    }

    private fun String.asPriority() =
        if (lowercaseTest.matches(this)) alphabet.indexOf(this[0]) + 1
        else alphabet.uppercase().indexOf(this[0]) + 27


    companion object {
        private val lowercaseTest = Regex("[a-z]")
        private val alphabet = "abcdefghijklmnopqrstuvwxyz"
    }
}

private data class RuckSack(
    val front: List<Char>,
    val back: List<Char>,
) {
    fun findSingleCommon(): String {
        return front.intersect(back).single().toString()
    }
}