package nl.bldn.aoc2022.puzzle04

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class Puzzle04ResourceTest {

    @Test
    fun `Should calculate overlap`() {
        val input = """
            2-4,6-8
            2-3,4-5
            5-7,7-9
            2-8,3-7
            6-6,4-6
            2-6,4-8
        """.trimIndent()

        val sut = Puzzle04Resource()

        val result = sut.submitInput(input)

        assertThat(result).isEqualTo(4)
    }
}