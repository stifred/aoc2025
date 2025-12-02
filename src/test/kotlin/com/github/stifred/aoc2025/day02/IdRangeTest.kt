package com.github.stifred.aoc2025.day02

import org.junit.jupiter.api.Assertions.*
import kotlin.test.Test

class IdRangeTest {
  @Test
  fun `test input 1`() {
    val ranges = """
      11-22,95-115,998-1012,1188511880-1188511890,222220-222224,
      1698522-1698528,446443-446449,38593856-38593862,565653-565659,
      824824821-824824827,2121212118-2121212124
    """.trimIndent().replace("\n", "").asIdRanges()
    assertEquals(1227775554, ranges.invalidIdSum1())
    assertEquals(4174379265, ranges.invalidIdSum2())
  }

  @Test
  fun `test ID range`() {
    assertEquals(listOf(Id("99")), IdRange(95, 115).invalidIds1())
  }

  @Test
  fun `test ID range of invalider Id`() {
    assertEquals(listOf(Id("99"), Id("111")), IdRange(95, 115).invalidIds2())
  }
}
