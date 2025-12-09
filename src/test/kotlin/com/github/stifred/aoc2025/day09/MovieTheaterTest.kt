package com.github.stifred.aoc2025.day09

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class MovieTheaterTest {
  @Test
  fun `test input`() {
    val input = """
      7,1
      11,1
      11,7
      9,7
      9,5
      2,5
      2,3
      7,3
    """.trimIndent()
    val theater = input.asMovieTheater()

    assertEquals(50, theater.findBiggestRectangles().first().area)
    assertEquals(24, theater.findBiggestRedGreenRectangle().area)
  }
}
