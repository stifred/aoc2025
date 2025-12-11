package com.github.stifred.aoc2025.searching

import com.github.stifred.aoc2025.searching.MapCombinator.Companion.combinations
import org.junit.jupiter.api.Assertions.*
import kotlin.test.Test

class MapCombinatorTest {
  @Test
  fun `combines map`() {
    val map = mapOf(
      'A' to listOf('a', 'b', 'c'),
      'B' to listOf('d', 'e'),
    )

    val expected = listOf(
      listOf('A' to 'a', 'B' to 'd'),
      listOf('A' to 'a', 'B' to 'e'),
      listOf('A' to 'b', 'B' to 'd'),
      listOf('A' to 'b', 'B' to 'e'),
      listOf('A' to 'c', 'B' to 'd'),
      listOf('A' to 'c', 'B' to 'e'),
    )
    val actual = buildList { map.combinations().forEachRemaining { add(it) } }
    assertEquals(expected, actual)
  }
}