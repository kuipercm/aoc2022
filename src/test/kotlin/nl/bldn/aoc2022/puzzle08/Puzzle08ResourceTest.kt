package nl.bldn.aoc2022.puzzle08

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class Puzzle08ResourceTest {
    @Test
    fun `Should find visible tree count`() {
        val input = """
        |30373
        |25512
        |65332
        |33549
        |35390
        """.trimMargin()

        val resource = Puzzle08Resource()

        val count = resource.submitInput(input)

        assertThat(count).isEqualTo(21)
    }

    @Test
    fun `Should find max scenic score`() {
        val input = """
        |30373
        |25512
        |65332
        |33549
        |35390
        """.trimMargin()

        val resource = Puzzle08Resource()

        val score = resource.submitInput2(input)

        assertThat(score).isEqualTo(8)
    }
}