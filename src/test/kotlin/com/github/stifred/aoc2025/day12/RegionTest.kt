package com.github.stifred.aoc2025.day12

import org.junit.jupiter.api.Assertions.*
import kotlin.test.Test

class RegionTest {
  @Test
  fun `test input`() {
    val input = """
      0:
      ###
      ##.
      ##.

      1:
      ###
      ##.
      .##

      2:
      .##
      ###
      ##.

      3:
      ##.
      ###
      ##.

      4:
      ###
      #..
      ###

      5:
      ###
      .#.
      ###

      4x4: 0 0 0 0 2 0
      12x5: 1 0 1 0 2 2
      12x5: 1 0 1 0 3 2
    """.trimIndent()
    val (shapes, regions) = input.asShapesAndRegions()

    assertEquals(true, regions[0].canBeFilledBy(shapes))
    assertEquals(true, regions[1].canBeFilledBy(shapes))
    assertEquals(false, regions[2].canBeFilledBy(shapes))
  }
}
