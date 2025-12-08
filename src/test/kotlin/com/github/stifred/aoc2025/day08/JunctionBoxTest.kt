package com.github.stifred.aoc2025.day08

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test

class JunctionBoxTest {
  @Test
  fun `test input`() {
    val input = """
      162,817,812
      57,618,57
      906,360,560
      592,479,940
      352,342,300
      466,668,158
      542,29,236
      431,825,988
      739,650,466
      52,470,668
      216,146,977
      819,987,18
      117,168,530
      805,96,715
      346,949,466
      970,615,88
      941,993,340
      862,61,35
      984,92,344
      425,690,689
    """.trimIndent()
    val list = input.asJunctionBoxes()

    assertEquals(JunctionBox(425, 690, 689), list.first().closestIn(list))
    assertEquals(40, list.buildCircuits(limit = 10).value())

    val e = assertThrows<SecondAnswer> { list.buildCircuits() }
    assertEquals(25272, e.value)
  }
}
