package com.github.stifred.aoc2025.day08

import com.github.stifred.aoc2025.numbers.product
import com.github.stifred.aoc2025.solutions.nonEmptyLineSequence
import com.github.stifred.aoc2025.solutions.parseInput
import com.github.stifred.aoc2025.solutions.solve

fun main() {
  val boxList = parseInput(day = 8) { it.asJunctionBoxes() }

  solve(part = 1) { boxList.buildCircuits(1000).value() }
  solve(part = 2) {
    val e = runCatching { boxList.buildCircuits() }.exceptionOrNull() as? SecondAnswer ?: error("Error")
    e.value
  }
}

fun List<JunctionBox>.buildCircuits(limit: Int = Int.MAX_VALUE): List<Circuit> {
  val circuits = mutableListOf<Circuit>()
  var countDown = limit

  val pairs = asSequence()
    .flatMap { l -> map { setOf(l, it) } }
    .filter { it.size == 2 }
    .distinct()
    .map { it.first() to it.last() }
    .sortedBy { (a, b) -> a.squaredDistanceTo(b) }
    .toList()

  for ((a, b) in pairs) {
    val circuitA = circuits.firstOrNull { a in it.boxes }
    val circuitB = circuits.firstOrNull { b in it.boxes }

    when {
      circuitA != null -> when {
        circuitB != null -> {
          circuits -= circuitA
          circuits -= circuitB
          circuits += Circuit(circuitA.boxes + circuitB.boxes + a + b)
        }
        else -> {
          circuits -= circuitA
          circuits += Circuit(circuitA.boxes + b)
        }
      }
      circuitB != null -> {
        circuits -= circuitB
        circuits += Circuit(circuitB.boxes + a)
      }
      else -> {
        circuits += Circuit(setOf(a, b))
      }
    }

    countDown--
    if (countDown == 0) break

    if (circuits.size == 1 && circuits.first().boxes.size == size) {
      throw SecondAnswer(a.x * b.x)
    }
  }

  return circuits
}

data class SecondAnswer(val value: Long) : RuntimeException("$value")

fun List<Circuit>.value() = asSequence()
  .map { it.boxes.size }
  .sortedDescending()
  .take(3)
  .product()

data class Circuit(val boxes: Set<JunctionBox>)

data class JunctionBox(val x: Long, val y: Long, val z: Long) {
  fun squaredDistanceTo(other: JunctionBox) =
    (x - other.x).squared() + (y - other.y).squared() + (z - other.z).squared()

  private fun Long.squared() = this * this

  fun closestIn(others: Collection<JunctionBox>) = others.asSequence()
    .filter { it != this }
    .minBy { squaredDistanceTo(it) }
}

fun String.asJunctionBoxes() = nonEmptyLineSequence()
  .map { it.split(',') }
  .map { (x, y, z) -> JunctionBox(x.toLong(), y.toLong(), z.toLong()) }
  .toList()
