package com.github.stifred.aoc2025.grids

import com.github.stifred.aoc2025.grids.Vector2.Companion.xy
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class Vector2Test {
  @Test
  fun `moving in all directions`() {
    val before = 5 xy 5

    assertEquals(4 xy 5, before.move(Direction.Left))
    assertEquals(4 xy 4, before.move(Direction.LeftUp))
    assertEquals(5 xy 4, before.move(Direction.Up))
    assertEquals(6 xy 4, before.move(Direction.UpRight))
    assertEquals(6 xy 5, before.move(Direction.Right))
    assertEquals(6 xy 6, before.move(Direction.RightDown))
    assertEquals(5 xy 6, before.move(Direction.Down))
    assertEquals(4 xy 6, before.move(Direction.DownLeft))
  }
}