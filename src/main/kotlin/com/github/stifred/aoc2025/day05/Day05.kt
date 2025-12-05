package com.github.stifred.aoc2025.day05

import com.github.stifred.aoc2025.solutions.nonEmptyLineSequence
import com.github.stifred.aoc2025.solutions.parseInput
import com.github.stifred.aoc2025.solutions.solve


fun main() {
  val db = parseInput(day = 5) { it.asIngredientDatabase() }

  solve(part = 1) { db.freshIds.size }
  solve(part = 2) { db.totalFreshIdCount }
}

data class IngredientDatabase(
  val freshRanges: List<LongRange>,
  val allIds: Set<Long>,
) {
  val totalFreshIdCount: Long get() {
    val fixedRanges = mutableListOf<LongRange>()
    for (range in freshRanges.sortedWith(compareBy(LongRange::first, LongRange::last))) {
      if (fixedRanges.any { it.first <= range.first && it.last >= range.last }) continue

      val last = fixedRanges.lastOrNull() ?: 0L..0L
      if (last.last >= range.first) {
        fixedRanges -= last
        fixedRanges += minOf(range.first, last.first)..maxOf(range.last, last.last)
      } else {
        fixedRanges += range
      }
    }

    return fixedRanges.sumOf { it.last - it.first + 1 }
  }

  val freshIds get() = allIds.filter { id ->
    freshRanges.any { id in it }
  }
}

fun String.asIngredientDatabase(): IngredientDatabase {
  val (a, b) = split("\n\n")

  return IngredientDatabase(
    a.nonEmptyLineSequence().map { l -> l.split('-').map { it.toLong() }.let { (a, b) -> a..b } }.toList(),
    b.nonEmptyLineSequence().map { it.toLong() }.toSet(),
  )
}
