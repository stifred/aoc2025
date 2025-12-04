package com.github.stifred.aoc2025.day02

import com.github.stifred.aoc2025.numbers.asDigits
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class IdTest {
  @Test
  fun `invalid IDs`() {
    assertTrue { Id(55.asDigits()).isInvalid1() }
    assertTrue { Id(7575.asDigits()).isInvalid1() }
    assertFalse { Id(999888.asDigits()).isInvalid1() }
    assertFalse { Id(969696.asDigits()).isInvalid1() }
    assertFalse { Id(12123.asDigits()).isInvalid1() }
  }

  @Test
  fun `invalider IDs`() {
    assertTrue { Id(55.asDigits()).isInvalid2() }
    assertTrue { Id(7575.asDigits()).isInvalid2() }
    assertFalse { Id(999888.asDigits()).isInvalid2() }
    assertTrue { Id(969696.asDigits()).isInvalid2() }
    assertFalse { Id(12123.asDigits()).isInvalid2() }
  }
}
