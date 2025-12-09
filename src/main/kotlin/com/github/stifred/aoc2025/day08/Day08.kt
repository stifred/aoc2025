package com.github.stifred.aoc2025.day08

import com.github.stifred.aoc2025.numbers.product
import com.github.stifred.aoc2025.searching.firstOf
import com.github.stifred.aoc2025.solutions.nonEmptyLineSequence
import com.github.stifred.aoc2025.solutions.parseInput
import com.github.stifred.aoc2025.solutions.solve

fun main() {
  val boxList = parseInput(day = 8) { it.asJunctionBoxes() }

  solve(benchmark = true) {
    val results = boxList.buildCircuits(originalLimit = 1000).toList()
    "A=${results.firstOf<SizeProduct>().product}; B=${results.firstOf<XProduct>().product}"
  }
}

fun List<JunctionBox>.buildCircuits(originalLimit: Int) = sequence {
  val circuits = mutableListOf<MutableSet<JunctionBox>>()
  var countDown = originalLimit

  asSequence()
    .flatMapIndexed { i, l -> asSequence().drop(i + 1).map { l to it } }
    .sortedBy { (a, b) -> a.squaredDistanceTo(b) }
    .forEach { (a, b) ->
      val aIndex = circuits.indexOfFirst { a in it }
      val bIndex = circuits.indexOfFirst { b in it }
      if (aIndex >= 0) {
        if (bIndex >= 0 && bIndex != aIndex) {
          val from = maxOf(aIndex, bIndex)
          val to = minOf(aIndex, bIndex)

          circuits[to] += circuits[from] + setOf(a, b)
          circuits.removeAt(from)
        } else {
          circuits[aIndex] += b
        }
      } else if (bIndex >= 0) {
        circuits[bIndex] += a
      } else {
        circuits += mutableSetOf(a, b)
      }

      countDown--
      if (countDown == 0) {
        yield(
          circuits.asSequence()
            .map { it.size }
            .sortedDescending()
            .take(3)
            .product()
            .let { SizeProduct(it) },
        )
      }

      if (circuits.size == 1 && circuits.first().size == size) {
        yield(XProduct(a.x * b.x))
      }
    }
}.take(2)

data class JunctionBox(val x: Long, val y: Long, val z: Long) {
  fun squaredDistanceTo(other: JunctionBox) =
    (x - other.x).squared() + (y - other.y).squared() + (z - other.z).squared()

  private fun Long.squared() = this * this
}

fun String.asJunctionBoxes() = nonEmptyLineSequence()
  .map { it.split(',') }
  .map { (x, y, z) -> JunctionBox(x.toLong(), y.toLong(), z.toLong()) }
  .toList()

sealed class CircuitResult
data class SizeProduct(val product: Int) : CircuitResult()
data class XProduct(val product: Long) : CircuitResult()
