package com.github.stifred.aoc2025.day03

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class BatteryBankTest {
  @Test
  fun `test input`() {
    val input = """
      987654321111111
      811111111111119
      234234234234278
      818181911112111
    """.trimIndent()
    val banks = input.asBanks()

    assertEquals(98, banks[0].joltage(with = 2))
    assertEquals(89, banks[1].joltage(with = 2))
    assertEquals(78, banks[2].joltage(with = 2))
    assertEquals(92, banks[3].joltage(with = 2))
    assertEquals(357, banks.totalJoltage(with = 2))

    assertEquals(987654321111, banks[0].joltage(with = 12))
    assertEquals(811111111119, banks[1].joltage(with = 12))
    assertEquals(434234234278, banks[2].joltage(with = 12))
    assertEquals(888911112111, banks[3].joltage(with = 12))
    assertEquals(3121910778619, banks.totalJoltage(with = 12))
  }
}
