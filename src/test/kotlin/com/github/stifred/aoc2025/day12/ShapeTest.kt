package com.github.stifred.aoc2025.day12

import com.github.stifred.aoc2025.grids.Vector2.Companion.xy
import org.junit.jupiter.api.Assertions.*
import kotlin.test.Test

class ShapeTest {
  @Test
  fun `flips and rotations`() {
    val shape = Shape(original = setOf(0 xy 0, 1 xy 0, 2 xy 0, 0 xy 1, 1 xy 1, 0 xy 2, 1 xy 2))

    assertEquals(setOf(0 xy 0, 1 xy 0, 2 xy 0, 1 xy 1, 2 xy 1, 1 xy 2, 2 xy 2), shape.flipped)
    assertEquals(setOf(2 xy 0, 2 xy 1, 2 xy 2, 0 xy 0, 0 xy 1, 1 xy 0, 1 xy 1), shape.rotations[0])
    assertEquals(setOf(2 xy 2, 1 xy 2, 0 xy 2, 1 xy 1, 2 xy 1, 1 xy 0, 2 xy 0), shape.rotations[1])
    assertEquals(setOf(0 xy 0, 0 xy 1, 0 xy 2, 1 xy 1, 1 xy 2, 2 xy 1, 2 xy 2), shape.rotations[2])
  }
}