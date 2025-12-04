package com.github.stifred.aoc2025.day04

import com.github.stifred.aoc2025.grids.Direction
import com.github.stifred.aoc2025.grids.Grid2D.Companion.asGrid
import com.github.stifred.aoc2025.grids.Vector2
import com.github.stifred.aoc2025.solutions.parseInput
import com.github.stifred.aoc2025.solutions.solve

fun main() {
  val pd = parseInput(day = 4) { it.asPrintingDepartment() }

  solve(part = 1, benchmark = false) { pd.accessibleByForklift.size }
  solve(part = 2, benchmark = false) { pd.totalAccessibleByForkliftCount }
}

val Set<Vector2>.accessibleByForklift get() = asSequence().filter { pos ->
  Direction.all.asSequence().map(pos::move).filter(this::contains).take(4).count() < 4
}.toSet()

val Set<Vector2>.totalAccessibleByForkliftCount: Int get() {
  val copy = toMutableSet()
  while (copy.removeAll(copy.accessibleByForklift)) { /* And another one */ }
  return size - copy.size
}

fun String.asPrintingDepartment() = asGrid(mapOf('@' to true)).usedPositions
