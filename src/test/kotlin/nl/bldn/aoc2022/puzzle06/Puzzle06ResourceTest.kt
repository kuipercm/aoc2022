package nl.bldn.aoc2022.puzzle06

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class Puzzle06ResourceTest {
    @Test
    fun `Verify input1`() {
        val input = "mjqjpqmgbljsphdztnvjfqwrcgsmlb"

        val resource = Puzzle06Resource()

        val result = resource.submitInput(input)
        assertThat(result).isEqualTo(7)
    }

    @Test
    fun `Verify input2`() {
        val input = "bvwbjplbgvbhsrlpgdmjqwftvncz"

        val resource = Puzzle06Resource()

        val result = resource.submitInput(input)
        assertThat(result).isEqualTo(5)
    }

    @Test
    fun `Verify input3`() {
        val input = "nppdvjthqldpwncqszvftbrmjlhg"

        val resource = Puzzle06Resource()

        val result = resource.submitInput(input)
        assertThat(result).isEqualTo(6)
    }

    @Test
    fun `Verify input1 - part2`() {
        val input = "mjqjpqmgbljsphdztnvjfqwrcgsmlb"

        val resource = Puzzle06Resource()

        val result = resource.submitInput2(input)
        assertThat(result).isEqualTo(19)
    }
}