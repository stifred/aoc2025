package com.github.stifred.aoc2025.day05

import kotlin.test.Test
import kotlin.test.assertEquals

class IngredientDatabaseTest {
  @Test
  fun `test input`() {
    val db = """
      3-5
      10-14
      16-20
      12-18

      1
      5
      8
      11
      17
      32
    """.trimIndent().asIngredientDatabase()

    assertEquals(3, db.freshIds.size)
    assertEquals(14, db.totalFreshIdCount)
  }
}