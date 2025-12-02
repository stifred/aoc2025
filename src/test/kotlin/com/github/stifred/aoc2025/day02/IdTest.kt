package com.github.stifred.aoc2025.day02

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class IdTest {
  @Test
  fun `invalid IDs`() {
    assertTrue { Id("55").isInvalid1() }
    assertTrue { Id("7575").isInvalid1() }
    assertFalse { Id("999888").isInvalid1() }
    assertFalse { Id("12123").isInvalid1() }
  }
}
