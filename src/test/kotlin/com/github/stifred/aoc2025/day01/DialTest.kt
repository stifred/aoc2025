package com.github.stifred.aoc2025.day01

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class DialTest {
  @Test
  fun `test input 1`() {
    val input = """
      L68
      L30
      R48
      L5
      R60
      L55
      L1
      L99
      R14
      L82
    """.trimIndent()
    val rotations = input.asRotations()

    assertEquals(3, findPassword1(rotations))
    assertEquals(6, findPassword2(rotations))
  }
}
