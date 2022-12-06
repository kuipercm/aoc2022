package nl.bldn.aoc2022.puzzle04

import mu.KLogging
import javax.ws.rs.Consumes
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@Path("/puzzle/04")
class Puzzle04Resource {
    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    fun submitInput(
        input: String,
    ): Int {
        val elfPairs = input.convertToPuzzleInput()
        elfPairs.forEach { logger.info { "$it - overlaps: ${it.overlap()} - ${it.first.toList()}" } }

//        return elfPairs.count { it.fullContainment() }
        // Part 1: 453
        return elfPairs.count { it.overlap() }
        //Part 2: 966
    }

    private fun String.convertToPuzzleInput(): List<ElfPair> {
        return this.lines().map {
            val split = it.split(",")
            val first = split[0].toIntRange()
            val second = split[1].toIntRange()
            ElfPair(first, second)
        }
    }

    private fun String.toIntRange(): IntRange {
        val bounds = this.split("-")
        return IntRange(bounds[0].toInt(), bounds[1].toInt())
    }

    companion object : KLogging()
}

private data class ElfPair(
    val first: IntRange,
    val second: IntRange,
) {
    fun fullContainment(): Boolean =
        first.subtract(second).isEmpty() || second.subtract(first).isEmpty()

    fun overlap(): Boolean =
        first.intersect(second).isNotEmpty()
}