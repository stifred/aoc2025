package com.github.stifred.aoc2025.day04

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class PrintingDepartmentTest {
  @Test
  fun `test input`() {
    val input = """
      ..@@.@@@@.
      @@@.@.@.@@
      @@@@@.@.@@
      @.@@@@..@.
      @@.@@@@.@@
      .@@@@@@@.@
      .@.@.@.@@@
      @.@@@.@@@@
      .@@@@@@@@.
      @.@.@@@.@.
    """.trimIndent()
    val pd = input.asPrintingDepartment()

    assertEquals(13, pd.accessibleByForklift.size)
    assertEquals(43, pd.totalAccessibleByForkliftCount)
  }
}
