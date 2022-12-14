package nl.bldn.aoc2022.puzzle09

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class Puzzle09ResourceTest {
    @Test
    fun `Should process input correctly`() {
        val input = """
            R 4
            U 4
            L 3
            D 1
            R 4
            D 1
            L 5
            R 2
        """.trimIndent()

        val resource = Puzzle09Resource()

        val result = resource.submitInput(input)

        assertThat(result).isEqualTo(13)
    }

    @Test
    fun `Should process input correctly 2`() {
        val input = """
            R 4
            U 4
            L 3
            D 1
            R 4
            D 1
            L 5
            R 2
        """.trimIndent()

        val resource = Puzzle09Resource()

        val result = resource.submitInput2(input)

        assertThat(result).isEqualTo(1)
    }

    @Test
    fun `Should process input correctly 2 - 2`() {
        val input = """
            R 5
            U 8
            L 8
            D 3
            R 17
            D 10
            L 25
            U 20
        """.trimIndent()

        val resource = Puzzle09Resource()

        val result = resource.submitInput2(input)

        assertThat(result).isEqualTo(36)
    }
}