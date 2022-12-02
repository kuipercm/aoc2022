package nl.bldn.aoc2022.puzzle02

import nl.bldn.aoc2022.puzzle02.GameOutcome.*
import javax.ws.rs.*
import javax.ws.rs.core.MediaType.APPLICATION_JSON
import javax.ws.rs.core.MediaType.TEXT_PLAIN

@Path("/puzzle/02")
class Puzzle02Resource {
    @POST
    @Consumes(TEXT_PLAIN)
    @Produces(APPLICATION_JSON)
    fun submitInput(
        input: String,
    ): Puzzle02Output {
        val moves = input.convertToPuzzleInput()

        return Puzzle02Output(moves.sumOf { it.handValue.toLong() })
    }

    private fun String.convertToPuzzleInput(): List<RpsMove> {
        return this.lines().map {
            val (first, second) = it.split(" ")
            RpsMove(RpsSign.fromString(first), RpsSign.fromString(second))
        }
    }
}

private data class RpsMove(
    val opponent: RpsSign,
    val myHand: RpsSign,
) {
    val handValue: Int = myHand.points + myHand.playAgainst(opponent).score
}

private enum class RpsSign(val representation1: String, val representation2: String, val points: Int) {
    ROCK ("A", "X", 1) {
        override fun playAgainst(other: RpsSign) =
            when (other) {
                ROCK -> DRAW
                SCISSORS -> WIN
                PAPER -> LOSS
            }
    },
    PAPER ("B", "Y", 2) {
        override fun playAgainst(other: RpsSign) =
            when (other) {
                ROCK -> WIN
                SCISSORS -> LOSS
                PAPER -> DRAW
            }
    },
    SCISSORS ("C", "Z", 3) {
        override fun playAgainst(other: RpsSign) =
            when (other) {
                ROCK -> LOSS
                SCISSORS -> DRAW
                PAPER -> WIN
            }
    }
    ;

    abstract fun playAgainst(other: RpsSign): GameOutcome

    companion object {
        fun fromString(input: String) =
            RpsSign.values().singleOrNull { it.representation1 == input || it.representation2 == input }
                ?: throw IllegalArgumentException("Unknown input for sign: $input")
    }
}

private enum class GameOutcome(val score: Int) {
    WIN (6),
    DRAW (3),
    LOSS (0)
}

data class Puzzle02Output(val score: Long)