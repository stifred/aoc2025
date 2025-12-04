package com.github.stifred.aoc2025.numbers

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class SplittingTest {
  @Test
  fun `asDigits splits up correctly`() {
    assertEquals(listOf(2.toUByte(), 0.toUByte(), 0.toUByte(), 5.toUByte()), 2005.asDigits())
    assertEquals(listOf(2.toUByte(), 0.toUByte(), 0.toUByte(), 5.toUByte()), 2005L.asDigits())
  }

  @Test
  fun `and they can be connected again`() {
    assertEquals(2005, listOf(2.toUByte(), 0.toUByte(), 0.toUByte(), 5.toUByte()).toInt())
    assertEquals(2005L, listOf(2.toUByte(), 0.toUByte(), 0.toUByte(), 5.toUByte()).toLong())
  }
}
