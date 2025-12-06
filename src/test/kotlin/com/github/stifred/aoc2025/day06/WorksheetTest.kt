package com.github.stifred.aoc2025.day06

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class WorksheetTest {
  @Test
  fun `test input`() {
    val input = """
      123 328  51 64 
       45 64  387 23 
        6 98  215 314
      *   +   *   +
    """.trimIndent()
    val worksheet = input.asWorksheet()

    assertEquals(4277556L, worksheet.kiddieSum)
    assertEquals(3263827L, worksheet.grownupSum)
  }
}