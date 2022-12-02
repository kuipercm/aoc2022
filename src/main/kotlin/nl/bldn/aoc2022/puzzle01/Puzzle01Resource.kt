package nl.bldn.aoc2022.puzzle01

import javax.ws.rs.*
import javax.ws.rs.core.MediaType.APPLICATION_JSON
import javax.ws.rs.core.MediaType.TEXT_PLAIN

@Path("/puzzle/01")
class Puzzle01Resource {
    @POST
    @Consumes(TEXT_PLAIN)
    @Produces(APPLICATION_JSON)
    fun submitInput(
        input: String,
        @QueryParam("numberOfElves") @DefaultValue("1") numberOfElvesToInclude: Int = 1
    ): Puzzle01Output {
        val elves = input.convertToPuzzleInput()

        return Puzzle01Output(elves
            .sortedByDescending { it.totalCalories }
            .take(numberOfElvesToInclude)
            .sumOf { it.totalCalories }
        )
    }

    private fun String.convertToPuzzleInput(): List<ElfCalories> {
        return this.lines().slices { it.isBlank() }
            .map { ElfCalories(it.map { s -> s.toInt() }) }
    }

    private fun List<String>.slices(slicesSeparator: (String) -> Boolean):List<List<String>> {
        val result = mutableListOf<List<String>>()
        var current = mutableListOf<String>()
        for (s in this) {
            if (slicesSeparator(s)) {
                result.add(current)
                current = mutableListOf()
                continue
            }

            current.add(s)
        }
        return result
    }
}

private data class ElfCalories(
    val calories: List<Int>,
    val totalCalories: Long = calories.sumOf { it.toLong() },
) {
}

data class Puzzle01Output(val elfCalories: Long)