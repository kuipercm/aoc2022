package nl.bldn.aoc2022.puzzle05

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class Puzzle05ResourceTest {

    @Test
    fun `Should create correct stacks`() {
        val input = """
        |    [D]    
        |[N] [C]    
        |[Z] [M] [P]
        | 1   2   3 
        |
        |move 1 from 2 to 1
        |move 3 from 1 to 3
        |move 2 from 2 to 1
        |move 1 from 1 to 2
        """.trimMargin()

        val resource = Puzzle05Resource()
        val result = resource.submitInput(input)

        assertThat(result).isEqualTo("CMZ")
    }

    @Test
    fun `Should create correct stacks - crane model 9001`() {
        val input = """
        |    [D]    
        |[N] [C]    
        |[Z] [M] [P]
        | 1   2   3 
        |
        |move 1 from 2 to 1
        |move 3 from 1 to 3
        |move 2 from 2 to 1
        |move 1 from 1 to 2
        """.trimMargin()

        val resource = Puzzle05Resource()
        val result = resource.submitPart2(input)

        assertThat(result).isEqualTo("MCD")
    }
}