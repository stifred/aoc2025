package com.github.stifred.aoc2025.searching

import org.junit.jupiter.api.Assertions.*
import kotlin.test.Test

class CombinatorTest {
  @Test
  fun `combinator makes any combination`() {
    val list = listOf('A', 'B')

    val combinator = Combinator(candidates = list, combinationSize = 3, unique = false)
    val expected = listOf(
      listOf('A', 'A', 'A'),
      listOf('A', 'A', 'B'),
      listOf('A', 'B', 'A'),
      listOf('A', 'B', 'B'),
      listOf('B', 'A', 'A'),
      listOf('B', 'A', 'B'),
      listOf('B', 'B', 'A'),
      listOf('B', 'B', 'B'),
    )
    val actual = buildList { combinator.forEachRemaining { add(it) } }
    assertEquals(expected, actual)
  }

  @Test
  fun `combinator can also make only unique combinations`() {
    val list = listOf('A', 'B')

    val combinator = Combinator(candidates = list, combinationSize = 3, unique = true)
    val expected = listOf(
      listOf('A', 'A', 'A'),
      listOf('A', 'A', 'B'),
      listOf('A', 'B', 'B'),
      listOf('B', 'B', 'B'),
    )
    val actual = buildList { combinator.forEachRemaining { add(it) } }
    assertEquals(expected, actual)
  }

}