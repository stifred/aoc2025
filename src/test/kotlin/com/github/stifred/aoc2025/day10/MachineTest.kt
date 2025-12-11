package com.github.stifred.aoc2025.day10

import org.junit.jupiter.api.Assertions.*
import kotlin.test.Test

class MachineTest {
  @Test
  fun `test input`() {
    val input = """
      [.##.] (3) (1,3) (2) (2,3) (0,2) (0,1) {3,5,4,7}
      [...#.] (0,2,3,4) (2,3) (0,4) (0,1,2) (1,2,3,4) {7,5,12,7,2}
      [.###.#] (0,1,2,3,4) (0,3,4) (0,1,2,4,5) (1,2) {10,11,11,5,10,5}
    """.trimIndent()
    val diagrams = input.asLightDiagrams()

    assertEquals(2, diagrams[0].fewestPressesRequired)
    assertEquals(3, diagrams[1].fewestPressesRequired)
    assertEquals(2, diagrams[2].fewestPressesRequired)
    assertEquals(7, diagrams.totalFewestPressesRequired)

    assertEquals(10, diagrams[0].fewestPressesRequiredForJoltages)
    assertEquals(12, diagrams[1].fewestPressesRequiredForJoltages)
    assertEquals(11, diagrams[2].fewestPressesRequiredForJoltages)
    assertEquals(33, diagrams.totalFewestPressesRequiredForJoltages)
  }
}
