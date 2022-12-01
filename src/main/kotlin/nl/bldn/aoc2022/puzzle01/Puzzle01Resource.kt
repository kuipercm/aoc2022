package nl.bldn.aoc2022.puzzle01

import javax.ws.rs.Consumes
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType.APPLICATION_JSON
import javax.ws.rs.core.MediaType.TEXT_PLAIN

@Path("/puzzle/01")
class Puzzle01Resource {
    @POST
    @Consumes(TEXT_PLAIN)
    @Produces(APPLICATION_JSON)
    fun submitInput(input: String): Puzzle01Output {
        val puzzleInput01 = input.convertToPuzzleInput()

        return Puzzle01Output(puzzleInput01.elves.sortedByDescending { it.totalCalories }.take(3).sumOf { it.totalCalories })
    }

    private fun String.convertToPuzzleInput(): Puzzle01Input {
        val result = mutableListOf<ElfCalories>()
        var current = mutableListOf<Int>()
        for (s in lines()) {
            if (s.isBlank()) {
                result.add(ElfCalories(current))
                current = mutableListOf()
                continue
            }

            current.add(s.toInt())
        }

        return Puzzle01Input(result)
    }
}

data class Puzzle01Input (val elves: List<ElfCalories>)
data class ElfCalories(
    val calories: List<Int>,
    val totalCalories: Long = calories.map { it.toLong() }.sum(),
) {
}

data class Puzzle01Output(val elfCalories: Long)