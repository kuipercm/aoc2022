package nl.bldn.aoc2022.puzzle05

import mu.KLogging
import nl.bldn.aoc2022.puzzle01.slices
import javax.ws.rs.Consumes
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@Path("/puzzle/05")
class Puzzle05Resource {
    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    fun submitInput(
        input: String,
    ) = operateCrane(CraneModel._9000, input)

    @POST()
    @Path("/part2")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    fun submitPart2(
        input: String,
    ) = operateCrane(CraneModel._9001, input)

    private fun operateCrane(craneModel: CraneModel, input: String): String {
        val (crateStacks, craneOperations) = input.convertToPuzzleInput()
        val afterOperations = craneModel.operateCrane(craneOperations, crateStacks.associateBy { it.id })
        return afterOperations.map { it.value.boxes.first() }.joinToString("")
    }

    private fun String.convertToPuzzleInput(): Pair<List<CrateStack>, List<CraneOperation>> {
        val sections = this.lines().slices { it.isBlank() }
        val crateStacks = sections[0].toCrateStacks()
        val operations = sections[1].toCraneOperations()

        return Pair(crateStacks, operations)
    }

    private fun List<String>.toCrateStacks(): List<CrateStack> {
        val rows = size
        val columns = maxOf { it.length }

        val field = map {
            val original = it.toList().toMutableList()
            while (original.size < columns) {
                original.add(' ')
            }
            original.toTypedArray()
        }.toTypedArray()

        return (1 until columns).map { c ->
            (0 until rows-1).mapNotNull {
                val value = field[it][c].toString()
                if (value.isBlank() || value == "[" || value == "]") null else value
            }
        }.filter { it.isNotEmpty() }.mapIndexed{ index, boxes -> CrateStack(index+1, boxes) }
    }

    private fun List<String>.toCraneOperations(): List<CraneOperation> =
        map {
            val groupValues = operationPattern.matchEntire(it)!!.groupValues
            CraneOperation(
                amountToMove = groupValues[1].toInt(),
                fromStackId = groupValues[2].toInt(),
                toStackId = groupValues[3].toInt(),
            )
        }

    private fun CraneModel.operateCrane(craneOperations: List<CraneOperation>, input: Map<Int, CrateStack>): Map<Int, CrateStack> =
        if (craneOperations.isEmpty()) input
        else {
            val currentOperation = craneOperations.first()
            operateCrane(craneOperations.drop(1), currentOperation.execute(input, this))
        }

    companion object : KLogging() {
        private val operationPattern = Regex("move (\\d+) from (\\d) to (\\d)")
    }
}

private data class CrateStack(
    val id: Int,
    val boxes: List<String>
)

private data class CraneOperation(
    val amountToMove: Int,
    val fromStackId: Int,
    val toStackId: Int
) {
    fun execute(stacks: Map<Int, CrateStack>, craneModel: CraneModel): Map<Int, CrateStack> {
        val fromStackBoxes = stacks.getValue(fromStackId).boxes
        val updatedToBoxes = craneModel.moveBoxes(fromStackBoxes.subList(0, amountToMove)) + stacks.getValue(toStackId).boxes
        val updatedFromBoxes = fromStackBoxes.subList(amountToMove, fromStackBoxes.size)

        val result = stacks.toMutableMap()
        result[toStackId] = CrateStack(toStackId, updatedToBoxes)
        result[fromStackId] = CrateStack(fromStackId, updatedFromBoxes)
        return result
    }

    companion object : KLogging()
}

private enum class CraneModel {
    _9000 {
        override fun moveBoxes(boxes: List<String>) =
            boxes.reversed()
    },
    _9001 {
        override fun moveBoxes(boxes: List<String>) = boxes
    },
    ;

    abstract fun moveBoxes(boxes: List<String>): List<String>
}