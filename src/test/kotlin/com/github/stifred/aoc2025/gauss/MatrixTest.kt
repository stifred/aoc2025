package com.github.stifred.aoc2025.gauss

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class MatrixTest {
  @Test
  fun `test matrix example`() {
    val matrix = Matrix(width = 7, height = 4).apply {
      putValue(0, 6, 3)
      putValue(1, 6, 5)
      putValue(2, 6, 4)
      putValue(3, 6, 7)

      putValue(0, 4, 1)
      putValue(0, 5, 1)
      putValue(1, 1, 1)
      putValue(1, 5, 1)
      putValue(2, 2, 1)
      putValue(2, 3, 1)
      putValue(2, 4, 1)
      putValue(3, 0, 1)
      putValue(3, 1, 1)
      putValue(3, 3, 1)
    }

    val expectedRowsBefore = listOf(
      listOf(0, 0, 0, 0, 1, 1, 3).map(Int::toDouble),
      listOf(0, 1, 0, 0, 0, 1, 5).map(Int::toDouble),
      listOf(0, 0, 1, 1, 1, 0, 4).map(Int::toDouble),
      listOf(1, 1, 0, 1, 0, 0, 7).map(Int::toDouble),
    )
    val actualRowsBefore = (0..3).map { matrix.dataRow(it) }
    assertEquals(expectedRowsBefore, actualRowsBefore)

    matrix.runGaussJordanElimination()

    val expectedRows = listOf(
      listOf(1, 0, 0, 1, 0, -1, 2).map(Int::toDouble),
      listOf(0, 1, 0, 0, 0,  1, 5).map(Int::toDouble),
      listOf(0, 0, 1, 1, 0, -1, 1).map(Int::toDouble),
      listOf(0, 0, 0, 0, 1,  1, 3).map(Int::toDouble),
    )
    val actualRows = (0..3).map { matrix.dataRow(it) }
    assertEquals(expectedRows, actualRows)
  }
}