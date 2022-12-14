package nl.bldn.aoc2022.puzzle10

import mu.KLogging
import nl.bldn.aoc2022.puzzle10.InstructionType.*
import nl.bldn.aoc2022.puzzle10.InstructionType.Companion.fromValue
import javax.ws.rs.Consumes
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@Path("/puzzle/10")
class Puzzle10Resource {
    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    fun submitInput(
        input: String,
    ): Int {
        val instructions = input.parseInstructions()
        val ticker = Ticker()
        val cycles = ticker.tickingFor(instructions)

        val start = 20
        val interval = 40

        return (start until cycles.size step interval).map {
            it * cycles[it].startX
        }.sum()
    }

    @POST
    @Path("/part2")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    fun submitInput2(
        input: String,
    ): Int {
        val instructions = input.parseInstructions()
        val ticker = Ticker()
        val cycles = ticker.tickingFor(instructions)

        cycles.forEachIndexed { index, cycle ->
            val crt = index % 40
            if (crt == 0) {
                println()
            }

            val currentSprite = setOf(cycle.startX - 1, cycle.startX, cycle.startX + 1)
//            println("$index; $crt; $currentSprite;")

            if (currentSprite.indexOf(crt) >= 0) {
                print("X")
            } else {
                print(".")
            }

        }

        return 0;
    }

    private fun String.parseInstructions(): List<Instruction> =
        this.lines().map {
            if (it.contains(" ")) {
                val parts = it.split(" ")
                Instruction(fromValue(parts[0]), parts[1].toInt())
            } else {
                Instruction(NOOP)
            }
        }

    companion object : KLogging() {
    }
}

private data class Instruction(
    val type: InstructionType,
    val value: Int = 0
)

private enum class InstructionType(val representation: String){
    NOOP ("noop"),
    ADDX ("addx"),
    ;

    companion object {
        fun fromValue(s: String) = values().singleOrNull { it.representation == s} ?: throw IllegalArgumentException("unknown type for $s")
    }
}

private data class Cycle(
    val startX: Int,
    val endX: Int,
)

private class Ticker {
    private val tickDurations = mapOf(
        ADDX to 2,
        NOOP to 1
    )

    fun tickingFor(instructions: List<Instruction>): List<Cycle> {
        val start = Cycle(1,1)
        return instructions
            .runningFold(emptyList<Cycle>()) { prev, instruction ->
                instruction.toCycles(prev.lastOrNull() ?: Cycle(1,1))
            }.flatten()
    }

    private fun Instruction.toCycles(prevCycle: Cycle): List<Cycle> =
        (1..tickDurations.getValue(type)).map {
            if (it == 1) Cycle(prevCycle.endX, prevCycle.endX)
            else Cycle(prevCycle.endX, prevCycle.endX + value)
        }
}