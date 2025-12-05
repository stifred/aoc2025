package com.github.stifred.aoc2025.day05

import com.github.stifred.aoc2025.solutions.asLongRange
import com.github.stifred.aoc2025.solutions.nonEmptyLineSequence
import com.github.stifred.aoc2025.solutions.parseInput
import com.github.stifred.aoc2025.solutions.solve

fun main() {
  val db = parseInput(day = 5) { it.asIngredientDatabase() }

  solve(part = 1) { db.freshIdCount }
  solve(part = 2) { db.totalFreshIdCount }
}

data class IngredientDatabase(val freshRanges: List<FreshRange>, val allIds: Set<Long>) {
  val freshIdCount get() = allIds.count(::isFresh)
  val totalFreshIdCount: Long get() = freshRanges.asSequence()
    .sortedWith(compareBy(FreshRange::min, FreshRange::max))
    .fold(listOf()) { fixed: List<FreshRange>, range: FreshRange ->
      val last = fixed.lastOrNull()

      if (last != null && last touches range) fixed - last + (last combinedWith range) else fixed + range
    }.sumOf { it.size }

  private fun isFresh(id: Long) = freshRanges.any { id in it }
}

data class FreshRange(val min: Long, val max: Long) {
  val size get() = 1 + max - min

  operator fun contains(id: Long) = id in min..max
  infix fun touches(other: FreshRange) = max >= other.min

  infix fun combinedWith(other: FreshRange) = copy(min = minOf(min, other.min), max = maxOf(max, other.max))
}

fun String.asIngredientDatabase(): IngredientDatabase {
  val (a, b) = split("\n\n")

  return IngredientDatabase(
    freshRanges = a.nonEmptyLineSequence().map { it.asLongRange() }.map { FreshRange(it.first, it.last) }.toList(),
    allIds = b.nonEmptyLineSequence().map { it.toLong() }.toSet(),
  )
}
