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

data class IngredientDatabase(val freshRanges: List<LongRange>, val allIds: Set<Long>) {
  val freshIdCount get() = allIds.count(::isFresh)
  val totalFreshIdCount: Long get() = freshRanges.asSequence()
    .sortedWith(compareBy(LongRange::first, LongRange::last))
    .fold(listOf()) { fixed: List<LongRange>, range: LongRange ->
      val last = fixed.lastOrNull()

      if (last != null && last.last >= range.first) {
        fixed - setOf(last) + setOf(minOf(range.first, last.first)..maxOf(range.last, last.last))
      } else {
        fixed + setOf(range)
      }
    }.sumOf { 1 + it.last - it.first  }

  private fun isFresh(id: Long) = freshRanges.any { id in it }
}

fun String.asIngredientDatabase(): IngredientDatabase {
  val (a, b) = split("\n\n")

  return IngredientDatabase(
    freshRanges = a.nonEmptyLineSequence().map { it.asLongRange() }.toList(),
    allIds = b.nonEmptyLineSequence().map { it.toLong() }.toSet(),
  )
}
