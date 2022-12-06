package nl.bldn.aoc2022.puzzle06

import mu.KLogging
import javax.ws.rs.Consumes
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@Path("/puzzle/06")
class Puzzle06Resource {
    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    fun submitInput(
        input: String,
    ): Int {
        val markerSize = 4

        return input
            .windowed(markerSize)
            .mapIndexedNotNull { index, tile ->
                if (tile.toSet().size == tile.length) index + markerSize
                else null
            }
            .first()
    }

    @POST
    @Path("/part2")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    fun submitInput2(
        input: String,
    ): Int {
        val markerSize = 14

        return input
            .windowed(markerSize)
            .mapIndexedNotNull { index, tile ->
                if (tile.toSet().size == tile.length) index + markerSize
                else null
            }
            .first()
    }

    companion object : KLogging() {
    }
}