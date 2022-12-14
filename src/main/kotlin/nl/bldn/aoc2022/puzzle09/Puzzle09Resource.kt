package nl.bldn.aoc2022.puzzle09

import mu.KLogging
import nl.bldn.aoc2022.puzzle09.Coordinate.Companion.ORIGIN
import nl.bldn.aoc2022.puzzle09.Direction.*
import javax.ws.rs.Consumes
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType
import kotlin.math.abs

@Path("/puzzle/09")
class Puzzle09Resource {
    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    fun submitInput(
        input: String,
    ): Int {
        val motions = input.parseMotions()

        return motions.countAllTailPositions()
    }

    @POST
    @Path("/part2")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    fun submitInput2(
        input: String,
    ): Int {
        val motions = input.parseMotions()
        val rope = Rope(listOf(ORIGIN, ORIGIN, ORIGIN, ORIGIN, ORIGIN, ORIGIN, ORIGIN, ORIGIN, ORIGIN, ORIGIN), mutableSetOf())

        return motions.countAllTailPositionsPart2(rope)
    }

    private fun String.parseMotions(): List<Motion> {
        return this.lines().map {
            val parts = it.split(" ")
            Motion(direction =
            Direction.fromInput(parts[0]), parts[1].toInt())
        }
    }

    private fun List<Motion>.countAllTailPositions(): Int {
        return computeAllHeadPositions().computeAllTailPositions().toSet().size
    }

    private fun List<Motion>.countAllTailPositionsPart2(rope: Rope): Int {
        var currentRope = rope
        for (m in this) {
            println(currentRope)
            currentRope = currentRope.apply(m)
        }

        return currentRope.tailPositions.size
    }

    private fun List<Motion>.computeAllHeadPositions(): List<Coordinate> {
        return runningFold(listOf(ORIGIN)) { prev, motion ->
            (1..motion.amount).map {
                val last = prev.last()
                when(motion.direction) {
                    LEFT -> Coordinate(last.x - it, last.y)
                    RIGHT -> Coordinate(last.x + it, last.y)
                    UP -> Coordinate(last.x, last.y - it)
                    DOWN -> Coordinate(last.x, last.y + it)
                }
            }
        }.flatten()
    }

    private fun List<Coordinate>.computeAllTailPositions(start: Coordinate = ORIGIN) =
        zipWithNext().runningFold(start) { prev, (first, second) ->
            if (second.isAdjacent(prev)) prev
            else first
        }

    private fun List<Coordinate>.computeAllTailPositionsLongerTail() =
        emptyList<Coordinate>()

    companion object : KLogging() {
    }
}

private data class Coordinate(
    val x: Int,
    val y: Int
) {
    fun isAdjacent(other: Coordinate) =
        abs(other.x-x) <= 1 && abs(other.y-y) <= 1

    operator fun plus(other: Coordinate): Coordinate {
        return Coordinate(x + other.x, y + other.y)
    }

    override fun toString() = "[$x,$y]"

    companion object {
        val ORIGIN = Coordinate(0,0)
    }
}

private data class Motion(
    val direction: Direction,
    val amount: Int
)

private enum class  Direction (private val s: String) {
    LEFT ("L"),
    RIGHT ("R"),
    UP ("U"),
    DOWN ("D"),
    ;

    companion object {
        fun fromInput(s: String) = values().singleOrNull { it.s == s } ?: throw IllegalStateException("Cannot find direction for $s")
    }
}

private data class Rope(
    val positions: List<Coordinate>,
    val tailPositions: MutableSet<Coordinate>,
) {
    fun apply(motion: Motion): Rope {
        val head = positions[0]
        val headPositions = (1..motion.amount).map {
            when(motion.direction) {
                LEFT -> Coordinate(head.x - it, head.y)
                RIGHT -> Coordinate(head.x + it, head.y)
                UP -> Coordinate(head.x, head.y - it)
                DOWN -> Coordinate(head.x, head.y + it)
            }
        }

        var currentPositions = positions
        for (headPosition in headPositions) {
            tailPositions.add(currentPositions.last())
            val temp = listOf(headPosition) + currentPositions.takeLast(9)
            currentPositions = temp.zipWithNext().runningFold(headPosition) { previous, (_, me) ->
                followPrevious(previous, me)
            }
        }

        tailPositions.add(currentPositions.last())

        return Rope(currentPositions, tailPositions)
    }

    private fun followPrevious(newHead: Coordinate, me: Coordinate) =
        if (me.isAdjacent(newHead)) me
        else findAdjacent(newHead, me)

    private fun findAdjacent(newHead: Coordinate, me: Coordinate): Coordinate {
        val xDiff = newHead.x - me.x
        val yDiff = newHead.y - me.y

        return when {
            xDiff > 0 && yDiff > 0 -> me + Coordinate(1,1)
            xDiff < 0 && yDiff > 0 -> me + Coordinate(-1, 1)
            xDiff > 0 && yDiff < 0 -> me + Coordinate(1, -1)
            xDiff < 0 && yDiff < 0 -> me + Coordinate(-1, -1)
            xDiff == 0 && yDiff > 0 -> me + Coordinate(0,1)
            xDiff == 0 && yDiff < 0 -> me + Coordinate(0, -1)
            xDiff > 0 && yDiff == 0 -> me + Coordinate(1, 0)
            xDiff < 0 && yDiff == 0 -> me + Coordinate(-1, 0)
            else -> throw IllegalArgumentException("Should not get here")
        }
    }

}