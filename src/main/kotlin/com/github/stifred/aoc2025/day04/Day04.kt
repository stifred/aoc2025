package com.github.stifred.aoc2025.day04

import com.github.stifred.aoc2025.grids.Direction
import com.github.stifred.aoc2025.grids.Grid2D.Companion.asGrid
import com.github.stifred.aoc2025.grids.Vector2
import com.github.stifred.aoc2025.solutions.parseInput
import com.github.stifred.aoc2025.solutions.solve

fun main() {
  val pd = parseInput(day = 4) { it.asPrintingDepartment() }

  solve(part = 1, benchmark = true) { pd.accessibleByForklift.size }
  solve(part = 2, benchmark = true) { pd.totalAccessibleByForkliftCount }
}

val Set<Vector2>.accessibleByForklift get() = asSequence().filter { pos ->
  Direction.all.asSequence()
    .map { pos.move(it) }
    .filter { it in this }
    .take(4)
    .count() < 4
}.toSet()

val Set<Vector2>.totalAccessibleByForkliftCount: Int get() = sequence {
  val grid = toMutableSet()

  while (true) {
    val accessible = grid.accessibleByForklift.takeIf { it.isNotEmpty() } ?: break
    grid -= accessible

    yield(accessible.size)
  }
}.sum()

fun String.asPrintingDepartment() = asGrid(mapOf('@' to true)).usedPositions
