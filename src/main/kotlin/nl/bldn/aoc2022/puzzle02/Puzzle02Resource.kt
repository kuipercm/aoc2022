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
//        val moves = input.convertToPuzzleInput()
        val expectedOutcome = input.convertToPuzzleInputPart2()

        return Puzzle02Output(expectedOutcome.sumOf { it.handValue.toLong() })
        // Output part 1: Puzzle02Output(score=14069)
        // Output part 2: Puzzle02Output(score=12411)
    }

    private fun String.convertToPuzzleInput(): List<RpsMove> {
        return this.lines().map {
            val (first, second) = it.split(" ")
            RpsMove(RpsSign.fromString(first), RpsSign.fromString(second))
        }
    }

    private fun String.convertToPuzzleInputPart2(): List<RpsExpectedOutcome> {
        return this.lines().map {
            val (first, second) = it.split(" ")
            RpsExpectedOutcome(RpsSign.fromString(first), GameOutcome.fromString(second))
        }
    }
}

private data class RpsMove(
    val opponent: RpsSign,
    val myHand: RpsSign,
) {
    val handValue: Int = myHand.points + myHand.playAgainst(opponent).score
}

private data class RpsExpectedOutcome(
    val opponent: RpsSign,
    val result: GameOutcome
) {
    val handValue = result.score + opponent.toAchieve(result).points
}

private enum class RpsSign(val representation: String, val points: Int) {
    ROCK ("A", 1) {
        override fun playAgainst(other: RpsSign) =
            when (other) {
                ROCK -> DRAW
                SCISSORS -> WIN
                PAPER -> LOSS
            }

        override fun toAchieve(outcome: GameOutcome) =
            when (outcome) {
                DRAW -> ROCK
                WIN -> PAPER
                LOSS -> SCISSORS
            }
    },
    PAPER ("B", 2) {
        override fun playAgainst(other: RpsSign) =
            when (other) {
                ROCK -> WIN
                SCISSORS -> LOSS
                PAPER -> DRAW
            }

        override fun toAchieve(outcome: GameOutcome) =
            when (outcome) {
                DRAW -> PAPER
                WIN -> SCISSORS
                LOSS -> ROCK
            }
    },
    SCISSORS ("C", 3) {
        override fun playAgainst(other: RpsSign) =
            when (other) {
                ROCK -> LOSS
                SCISSORS -> DRAW
                PAPER -> WIN
            }

        override fun toAchieve(outcome: GameOutcome) =
            when (outcome) {
                DRAW -> SCISSORS
                WIN -> ROCK
                LOSS -> PAPER
            }
    }
    ;

    abstract fun playAgainst(other: RpsSign): GameOutcome
    abstract fun toAchieve(outcome: GameOutcome): RpsSign

    companion object {
        fun fromString(input: String) =
            RpsSign.values().singleOrNull { it.representation == input }
                ?: throw IllegalArgumentException("Unknown input for sign: $input")
    }
}

private enum class GameOutcome(val representation: String, val score: Int) {
    WIN ("Z", 6),
    DRAW ("Y", 3),
    LOSS ("X", 0),
    ;

    companion object {
        fun fromString(input: String) =
            GameOutcome.values().singleOrNull { it.representation == input }
                ?: throw IllegalArgumentException("Unknown input for outcome: $input")
    }
}

data class Puzzle02Output(val score: Long)