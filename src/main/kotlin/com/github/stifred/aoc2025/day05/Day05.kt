package com.github.stifred.aoc2025.day05

import com.github.stifred.aoc2025.solutions.nonEmptyLineSequence
import com.github.stifred.aoc2025.solutions.parseInput
import com.github.stifred.aoc2025.solutions.solve


fun main() {
  val db = parseInput(day = 5) { it.asIngredientDatabase() }

  solve(part = 1) { db.freshIds.size }
}

data class IngredientDatabase(
  val freshRanges: List<LongRange>,
  val allIds: List<Long>,
) {
  val freshIds get() = allIds.filter { id -> freshRanges.any { id in it } }
}

fun String.asIngredientDatabase(): IngredientDatabase {
  val (a, b) = split("\n\n")

  return IngredientDatabase(
    a.nonEmptyLineSequence().map { l -> l.split('-').map { it.toLong() }.let { (a, b) -> a..b } }.toList(),
    b.nonEmptyLineSequence().map { it.toLong() }.toList(),
  )
}
